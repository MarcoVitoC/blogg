package blibliwarmupproject.blogg.service.category;

import blibliwarmupproject.blogg.entity.Category;
import blibliwarmupproject.blogg.model.request.category.CreateCategoryRequest;
import blibliwarmupproject.blogg.model.request.category.UpdateCategoryRequest;
import reactor.core.publisher.Mono;

import java.util.List;

public interface CategoryService {
    Mono<String> create(CreateCategoryRequest request);

    Mono<List<Category>> get();

    Mono<String> update(Long id, UpdateCategoryRequest request);

    Mono<String> delete(Long id);
}
