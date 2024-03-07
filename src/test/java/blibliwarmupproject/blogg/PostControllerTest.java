package blibliwarmupproject.blogg;

import blibliwarmupproject.blogg.controller.PostController;
import blibliwarmupproject.blogg.entity.Post;
import blibliwarmupproject.blogg.exception.InvalidRequestException;
import blibliwarmupproject.blogg.exception.NotFoundException;
import blibliwarmupproject.blogg.model.request.CreatePostRequest;
import blibliwarmupproject.blogg.model.request.UpdatePostRequest;
import blibliwarmupproject.blogg.repository.PostRepository;
import blibliwarmupproject.blogg.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;


@ExtendWith(SpringExtension.class)
@WebFluxTest(PostController.class)
public class PostControllerTest {

    @MockBean
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private ReactiveRedisTemplate<String, Post> reactiveRedisTemplate;

    @Autowired
    private WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(reactiveRedisTemplate.opsForValue()).thenReturn(Mockito.mock(ReactiveValueOperations.class));
    }

    @Test
    public void testCreatePostSuccess() {
        CreatePostRequest request = new CreatePostRequest("Test Blog Title", "Test Blog Body");

        when(postService.create(request)).thenReturn(Mono.just("Post created successfully!"));
        webTestClient.post().uri("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isOk()
            .expectBody().jsonPath("$.data").isEqualTo("Post created successfully!");
        verify(postService, times(1)).create(request);
    }

    @Test
    public void testCreatePostBadRequest() {
        CreatePostRequest request = new CreatePostRequest("", "Test Blog Body");

        when(postService.create(request)).thenReturn(Mono.error(new InvalidRequestException("All field is required")));
        webTestClient.post().uri("/api/posts")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody().jsonPath("$.error").isEqualTo("All field is required");
        verify(postService, times(1)).create(request);
    }

    @Test
    public void testGetAllPost() {
        List<Post> posts = buildMockPosts();
        postRepository.saveAll(posts);

        when(postService.get()).thenReturn(Mono.just(posts));
        webTestClient.get().uri("/api/posts")
            .exchange()
            .expectStatus().isOk();
//            .expectBodyList(Post.class).hasSize(posts.size());
        verify(postService, times(1)).get();
    }

    private static List<Post> buildMockPosts() {
        return IntStream.range(0, 2)
            .mapToObj(index -> Post.builder()
                .title("BloggTitle" + index + 1)
                .body("BloggBody" + index + 1)
                .isNewPost(true)
                .build())
            .toList();
    }

    @Test
    public void testGetPostById() {
        Post post = Post.builder().title("BloggTitle").body("BloggBody").isNewPost(true).build();
        postRepository.save(post);

        when(postService.getById(post.getId())).thenReturn(Mono.just(post));
        webTestClient.get().uri("/api/posts/{id}", post.getId())
                .exchange()
                .expectStatus().isOk();
        verify(postService, times(1)).getById(post.getId());
    }

    @Test
    public void testGetPostByIdNotFound() {
        Post post = Post.builder().title("BloggTitle").body("BloggBody").isNewPost(true).build();
        postRepository.save(post);

        when(postService.getById("abc")).thenReturn(Mono.error(new NotFoundException("Post not found")));
        webTestClient.get().uri("/api/posts/{id}", "abc")
                .exchange()
                .expectStatus().isNotFound();
        verify(postService, times(1)).getById("abc");
    }

    @Test
    public void testUpdatePost() {
        Post post = Post.builder().title("BloggTitle").body("BloggBody").isNewPost(true).build();
        postRepository.save(post);

        UpdatePostRequest request = new UpdatePostRequest("Test Update Title", "Test Update Body");

        when(postService.update(post.getId(), request)).thenReturn(Mono.just("Post updated successfully!"));
        webTestClient.put().uri("/api/posts/{id}", post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.data").isEqualTo("Post updated successfully!");
        verify(postService, times(1)).update(post.getId(), request);
    }

    @Test
    public void testUpdatePostBadRequest() {
        Post post = Post.builder().title("BloggTitle").body("BloggBody").isNewPost(true).build();
        postRepository.save(post);

        UpdatePostRequest request = new UpdatePostRequest("", "Test Update Body");

        when(postService.update(post.getId(), request)).thenReturn(Mono.error(new InvalidRequestException("All field is required")));
        webTestClient.put().uri("/api/posts/{id}", post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody().jsonPath("$.error").isEqualTo("All field is required");
    }

    @Test
    public void testUpdatePostNotFound() {
        Post post = Post.builder().title("BloggTitle").body("BloggBody").isNewPost(true).build();
        postRepository.save(post);

        UpdatePostRequest request = new UpdatePostRequest("Test Update Title", "Test Update Body");

        when(postService.update("abc", request)).thenReturn(Mono.error(new NotFoundException("Post not found")));
        webTestClient.put().uri("/api/posts/{id}", "abc")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(request))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("$.error").isEqualTo("Post not found");
    }

    @Test
    public void testDeletePost() {
        Post post = Post.builder().title("BloggTitle").body("BloggBody").isNewPost(true).build();
        postRepository.save(post);
        reactiveRedisTemplate.opsForValue().set(post.getId(), post);

        when(postService.delete(post.getId())).thenReturn(Mono.just("Post deleted successfully!"));
        webTestClient.delete().uri("/api/posts/{id}", post.getId())
            .exchange()
            .expectStatus().isOk()
            .expectBody().jsonPath("$.data").isEqualTo("Post deleted successfully!");
        verify(postService, times(1)).delete(post.getId());
    }

    @Test
    public void testDeletePostNotFound() {
        Post post = Post.builder().title("BloggTitle").body("BloggBody").isNewPost(true).build();
        postRepository.save(post);
        reactiveRedisTemplate.opsForValue().set(post.getId(), post);

        when(postService.delete("abc")).thenReturn(Mono.error(new NotFoundException("Post not found")));
        webTestClient.delete().uri("/api/posts/{id}", "abc")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("$.error").isEqualTo("Post not found");
        verify(postService, times(1)).delete("abc");
    }
}
