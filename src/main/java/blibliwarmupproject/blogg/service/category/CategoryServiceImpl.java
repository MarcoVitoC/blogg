package blibliwarmupproject.blogg.service.category;

import blibliwarmupproject.blogg.entity.Category;
import blibliwarmupproject.blogg.entity.Post;
import blibliwarmupproject.blogg.exception.InvalidRequestException;
import blibliwarmupproject.blogg.exception.NotFoundException;
import blibliwarmupproject.blogg.model.request.category.CreateCategoryRequest;
import blibliwarmupproject.blogg.model.request.category.UpdateCategoryRequest;
import blibliwarmupproject.blogg.repository.CategoryRepository;
import blibliwarmupproject.blogg.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReactiveRedisTemplate<String, Post> reactiveRedisTemplate;

    @Override
    public Mono<String> create(CreateCategoryRequest request) {
        if (request.getName().isEmpty()) {
            return Mono.error(new InvalidRequestException("All field is required"));
        }

        return categoryRepository.save(Category.builder()
                .name(request.getName())
                .build())
            .thenReturn("Category created successfully!");
    }

    @Override
    public Mono<List<Category>> get() {
        return categoryRepository.findAll().collectList();
    }

    @Override
    public Mono<String> update(Long id, UpdateCategoryRequest request) {
        if (request.getName().isEmpty()) {
            return Mono.error(new InvalidRequestException("All field is required"));
        }

        return categoryRepository.findById(id)
            .flatMap(category -> {
                category.setName(request.getName());

                return categoryRepository.save(category).thenReturn("Category updated successfully!");
            }).switchIfEmpty(Mono.error(new NotFoundException("Category not found")));
    }

    @Override
    public Mono<String> delete(Long id) {
        return reactiveRedisTemplate.keys("*")
            .flatMap(key -> reactiveRedisTemplate.opsForValue().get(key))
            .filter(post -> post.getCategoryId().equals(id))
            .flatMap(post -> postRepository.deleteById(post.getId())
                    .then(reactiveRedisTemplate.delete(post.getId())))
            .then(categoryRepository.deleteById(id))
            .thenReturn("Category deleted successfully!");
    }
}
