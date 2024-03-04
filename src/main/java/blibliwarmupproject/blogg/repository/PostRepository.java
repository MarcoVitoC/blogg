package blibliwarmupproject.blogg.repository;

import blibliwarmupproject.blogg.entity.Post;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends ReactiveCrudRepository<Post, String> {
}
