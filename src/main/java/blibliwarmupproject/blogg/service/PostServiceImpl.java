package blibliwarmupproject.blogg.service;

import blibliwarmupproject.blogg.exception.InvalidRequestException;
import blibliwarmupproject.blogg.exception.NotFoundException;
import blibliwarmupproject.blogg.entity.Post;
import blibliwarmupproject.blogg.model.request.CreatePostRequest;
import blibliwarmupproject.blogg.model.request.UpdatePostRequest;
import blibliwarmupproject.blogg.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReactiveRedisTemplate<String, Post> reactiveRedisTemplate;

    @Override
    public Mono<String> create(CreatePostRequest request) {
        if (request.getTitle().isEmpty() || request.getBody().isEmpty()) {
            return Mono.error(new InvalidRequestException("All field is required"));
        }

        return postRepository.save(Post.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .isNewPost(true)
                .build())
            .thenReturn("Post created successfully!");
    }

    @Override
    public Mono<List<Post>> get() {
        return reactiveRedisTemplate.keys("*")
            .collectList()
            .flatMap(cache -> cache.isEmpty() ? fetchAllPost() : tryFetchNewPost(cache));
    }

    @Override
    public Mono<String> update(String id, UpdatePostRequest request) {
        if (request.getTitle().isEmpty() || request.getBody().isEmpty()) {
            return Mono.error(new InvalidRequestException("All field is required"));
        }

        return postRepository.findById(id)
            .flatMap(post -> {
                post.setNewPost(false);
                post.setTitle(request.getTitle());
                post.setBody(request.getBody());

                return postRepository.save(post)
                    .flatMap(updatedPost -> reactiveRedisTemplate.getExpire(updatedPost.getId())
                        .flatMap(oldExpireTime -> reactiveRedisTemplate.opsForValue().set(updatedPost.getId(), updatedPost)
                            .then(reactiveRedisTemplate.expire(updatedPost.getId(), oldExpireTime))
                            .thenReturn("Post updated successfully!")));
            })
            .switchIfEmpty(Mono.error(new NotFoundException("Post not found")));
    }

    @Override
    public Mono<String> delete(String id) {
        return postRepository.findById(id)
            .flatMap(post -> reactiveRedisTemplate.delete(post.getId())
                .then(postRepository.deleteById(id).thenReturn("Post deleted successfully!")))
            .switchIfEmpty(Mono.error(new NotFoundException("Post not found")));
    }

    private Mono<List<Post>> fetchAllPost() {
        return postRepository.findAll()
            .flatMap(post -> reactiveRedisTemplate.opsForValue().set(post.getId(), post)
                .then(reactiveRedisTemplate.expire(post.getId(), Duration.ofMinutes(5))))
            .thenMany(reactiveRedisTemplate.keys("*")
                .flatMap(key -> reactiveRedisTemplate.opsForValue().get(key)))
            .collectList();
    }

    private Mono<List<Post>> tryFetchNewPost(List<String> cache) {
        return postRepository.findAll()
            .collectList()
            .flatMap(db -> cache.size() == db.size() ? getCache() : fetchNewPost(cache, db));
    }

    private Mono<List<Post>> getCache() {
        return reactiveRedisTemplate.keys("*")
            .flatMap(key -> reactiveRedisTemplate.opsForValue().get(key))
            .collectList();
    }

    private Mono<List<Post>> fetchNewPost(List<String> cache, List<Post> db) {
        List<String> cacheKeys = cache.stream().map(Object::toString).toList();
        List<Post> newPosts = db.stream()
            .filter(newPost -> !cacheKeys.contains(newPost.getId()))
            .collect(Collectors.toList());

        return Flux.fromIterable(newPosts)
            .flatMap(post -> reactiveRedisTemplate.opsForValue().set(post.getId(), post)
                .then(reactiveRedisTemplate.expire(post.getId(), Duration.ofMinutes(5))))
            .thenMany(reactiveRedisTemplate.keys("*")
                .flatMap(key -> reactiveRedisTemplate.opsForValue().get(key)))
            .collectList();
    }
}
