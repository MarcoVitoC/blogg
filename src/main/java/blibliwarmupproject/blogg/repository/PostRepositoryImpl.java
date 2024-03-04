package blibliwarmupproject.blogg.repository;

import blibliwarmupproject.blogg.entity.Post;
import lombok.RequiredArgsConstructor;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepository {

    private final static String KEY = "POSTS";

    private final ReactiveRedisOperations<String, Post> redisOperations;
    private final ReactiveHashOperations<String, String, Post> hashOperations;

    @Autowired
    public PostRepositoryImpl(ReactiveRedisOperations<String, Post> redisOperations) {
        this.redisOperations = redisOperations;
        this.hashOperations = redisOperations.opsForHash();
    }


    @Override
    public <S extends Post> Mono<S> save(S entity) {
        return hashOperations.put(KEY, entity.getId(), entity).thenReturn(entity);
    }

    @Override
    public <S extends Post> Flux<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public <S extends Post> Flux<S> saveAll(Publisher<S> entityStream) {
        return null;
    }

    @Override
    public Mono<Post> findById(String s) {
        return hashOperations.get(KEY, s);
    }

    @Override
    public Mono<Post> findById(Publisher<String> id) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(String s) {
        return null;
    }

    @Override
    public Mono<Boolean> existsById(Publisher<String> id) {
        return null;
    }

    @Override
    public Flux<Post> findAll() {
        return hashOperations.values(KEY);
    }

    @Override
    public Flux<Post> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public Flux<Post> findAllById(Publisher<String> idStream) {
        return null;
    }

    @Override
    public Mono<Long> count() {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String s) {
        return hashOperations.remove(KEY, s).then();
    }

    @Override
    public Mono<Void> deleteById(Publisher<String> id) {
        return null;
    }

    @Override
    public Mono<Void> delete(Post entity) {
        return null;
    }

    @Override
    public Mono<Void> deleteAllById(Iterable<? extends String> strings) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Iterable<? extends Post> entities) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll(Publisher<? extends Post> entityStream) {
        return null;
    }

    @Override
    public Mono<Void> deleteAll() {
        return null;
    }
}
