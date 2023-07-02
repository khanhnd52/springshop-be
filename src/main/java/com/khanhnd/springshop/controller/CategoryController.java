package com.khanhnd.springshop.controller;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.khanhnd.springshop.domain.Category;
import com.khanhnd.springshop.dto.CategoryDto;
import com.khanhnd.springshop.service.CategoryService;
import com.khanhnd.springshop.service.MapValidationErrorService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @Autowired
    MapValidationErrorService mapValidationErrorService;

    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDto dto, BindingResult result) {
        ResponseEntity<?> responseEntity = mapValidationErrorService.mapValidationFields(result);

        if (responseEntity != null) {
            return responseEntity;
        }

        Category entity = new Category();
        BeanUtils.copyProperties(dto, entity);

        entity = categoryService.save(entity);

        dto.setId(entity.getId());


        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id, @RequestBody CategoryDto dto) {
        Category entity = new Category();
        BeanUtils.copyProperties(dto, entity);

        entity = categoryService.update(id, entity);

        dto.setId(entity.getId());


        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<?> getCategories(){
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/page")
    public ResponseEntity<?> getCategories(@PageableDefault(size = 5, sort = "name", direction = Sort.Direction.ASC) Pageable pageable){
        return new ResponseEntity<>(categoryService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}/get")
    public ResponseEntity<?> getCategories(@PathVariable("id") Long id) {
        return new ResponseEntity<>(categoryService.findById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteById(id);

        return new ResponseEntity<>("Category with Id: " + id + " was deleted", HttpStatus.OK);
    }
}
