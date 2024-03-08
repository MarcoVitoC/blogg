package blibliwarmupproject.blogg.repository;

import blibliwarmupproject.blogg.entity.Category;
import blibliwarmupproject.blogg.entity.Post;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CategoryRepository extends ReactiveCrudRepository<Category, Long> {
}
