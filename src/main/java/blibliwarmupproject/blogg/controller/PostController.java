package blibliwarmupproject.blogg.controller;

import blibliwarmupproject.blogg.entity.Post;
import blibliwarmupproject.blogg.model.request.CreatePostRequest;
import blibliwarmupproject.blogg.model.request.UpdatePostRequest;
import blibliwarmupproject.blogg.model.response.BaseResponse;
import blibliwarmupproject.blogg.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/posts", produces = MediaType.APPLICATION_JSON_VALUE)
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping
    public Mono<BaseResponse<String>> create(@RequestBody CreatePostRequest request) {
        return postService.create(request).flatMap(data -> BaseResponse.ok(Mono.just(data)));
    }

    @GetMapping
    public Mono<BaseResponse<List<Post>>> get() {
        return postService.get().flatMap(data -> BaseResponse.ok(Mono.just(data)));
    }

    @PutMapping("/{id}")
    public Mono<BaseResponse<String>> update(
            @PathVariable("id") String id,
            @RequestBody UpdatePostRequest request) {
        return postService.update(id, request).flatMap(data -> BaseResponse.ok(Mono.just(data)));
    }

    @DeleteMapping("/{id}")
    public Mono<BaseResponse<String>> delete(@PathVariable("id") String id) {
        return postService.delete(id).flatMap(data -> BaseResponse.ok(Mono.just(data)));
    }
}
