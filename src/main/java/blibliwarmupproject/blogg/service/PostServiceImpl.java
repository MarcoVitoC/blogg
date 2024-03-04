package blibliwarmupproject.blogg.service;

import blibliwarmupproject.blogg.exception.InvalidRequestException;
import blibliwarmupproject.blogg.exception.NotFoundException;
import blibliwarmupproject.blogg.entity.Post;
import blibliwarmupproject.blogg.model.request.CreatePostRequest;
import blibliwarmupproject.blogg.model.request.UpdatePostRequest;
import blibliwarmupproject.blogg.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepository postRepository;

    @Override
    public Mono<String> create(CreatePostRequest request) {
        String id = UUID.randomUUID().toString();

        if (request.getTitle().isEmpty() || request.getBody().isEmpty()) {
            return Mono.error(new InvalidRequestException("All field is required"));
        }

        return postRepository.save(Post.builder()
            .id(id)
            .title(request.getTitle())
            .body(request.getBody())
            .build()
        ).thenReturn("Post created successfully!");
    }

    @Override
    public Mono<List<Post>> get() {
        return postRepository.findAll()
            .switchIfEmpty(Mono.error(new NotFoundException("Post is empty!")))
            .collectList();
    }

    @Override
    public Mono<String> update(String id, UpdatePostRequest request) {
        return postRepository.findById(id)
            .flatMap(post -> {
                post.setTitle(request.getTitle());
                post.setBody(request.getBody());

               return postRepository.save(post).thenReturn("Post updated successfully!");
            })
            .switchIfEmpty(Mono.error(new NotFoundException("Post not found")));
    }

    @Override
    public Mono<String> delete(String id) {
        return postRepository.findById(id)
            .flatMap(post -> postRepository.deleteById(id).thenReturn("Post deleted successfully!"))
            .switchIfEmpty(Mono.error(new NotFoundException("Post not found")));
    }
}
