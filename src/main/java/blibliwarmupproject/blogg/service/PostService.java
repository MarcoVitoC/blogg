package blibliwarmupproject.blogg.service;

import blibliwarmupproject.blogg.entity.Post;
import blibliwarmupproject.blogg.model.request.CreatePostRequest;
import blibliwarmupproject.blogg.model.request.UpdatePostRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface PostService {
    Mono<String> create(CreatePostRequest request);

    Mono<List<Post>> get();

    Mono<String> update(String id, UpdatePostRequest request);

    Mono<String> delete(String id);
}
