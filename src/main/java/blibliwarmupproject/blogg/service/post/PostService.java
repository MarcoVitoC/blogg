package blibliwarmupproject.blogg.service.post;

import blibliwarmupproject.blogg.entity.Post;
import blibliwarmupproject.blogg.model.request.post.CreatePostRequest;
import blibliwarmupproject.blogg.model.request.post.UpdatePostRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PostService {
    Mono<String> create(CreatePostRequest request);

    Mono<List<Post>> get();

    Mono<Post> getById(String id);

    Mono<String> update(String id, UpdatePostRequest request);

    Mono<String> delete(String id);
}
