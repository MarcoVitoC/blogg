package blibliwarmupproject.blogg.controller;

import blibliwarmupproject.blogg.entity.Category;
import blibliwarmupproject.blogg.model.request.category.CreateCategoryRequest;
import blibliwarmupproject.blogg.model.request.category.UpdateCategoryRequest;
import blibliwarmupproject.blogg.model.response.BaseResponse;
import blibliwarmupproject.blogg.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/categories", produces = MediaType.APPLICATION_JSON_VALUE)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public Mono<BaseResponse<String>> create(@RequestBody CreateCategoryRequest request) {
        return categoryService.create(request).flatMap(data -> BaseResponse.ok(Mono.just(data)));
    }

    @GetMapping
    public Mono<BaseResponse<List<Category>>> get() {
        return categoryService.get().flatMap(data -> BaseResponse.ok(Mono.just(data)));
    }

    @PutMapping("/{id}")
    public Mono<BaseResponse<String>> update(@PathVariable("id") Long id, @RequestBody UpdateCategoryRequest request) {
        return categoryService.update(id, request).flatMap(data -> BaseResponse.ok(Mono.just(data)));
    }

    @DeleteMapping
    public Mono<BaseResponse<String>> delete(@PathVariable("id") Long id) {
        return categoryService.delete(id).flatMap(data -> BaseResponse.ok(Mono.just(data)));
    }
}
