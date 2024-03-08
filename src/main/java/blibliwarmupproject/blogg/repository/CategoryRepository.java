package blibliwarmupproject.blogg.repository;

import blibliwarmupproject.blogg.entity.Category;
import blibliwarmupproject.blogg.entity.Post;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {
    Mono<Category> findByName(String name);
}
