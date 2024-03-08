package blibliwarmupproject.blogg.repository;

import blibliwarmupproject.blogg.entity.Post;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PostRepository extends ReactiveCrudRepository<Post, String> {
    Mono<Post> findByTitle(String title);
}
