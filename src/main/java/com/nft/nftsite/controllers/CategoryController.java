package com.nft.nftsite.controllers;

import com.nft.nftsite.data.dtos.requests.CreateCategoryRequest;
import com.nft.nftsite.data.dtos.responses.CategoryResponse;
import com.nft.nftsite.services.nft.NftService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@AllArgsConstructor
@Slf4j
public class CategoryController {

    private final NftService nftService;


    @PostMapping("/new")
    @Operation(summary = "Create new category")
    public ResponseEntity<CategoryResponse> createNewCategory(@Valid @RequestBody CreateCategoryRequest categoryRequest) {
        return new ResponseEntity<>(nftService.createNewCategory(categoryRequest), HttpStatus.OK);
    }


    @DeleteMapping("/delete-by-name/{categoryName}")
    @Operation(summary = "Delete an existing category by name")
    public ResponseEntity<String> deleteCategoryByName(@PathVariable String categoryName) {
        nftService.deleteCategoryByName(categoryName);
        return new ResponseEntity<>("Category Deleted Successfully!", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{categoryId}")
    @Operation(summary = "Delete an existing category by id")
    public ResponseEntity<String> deleteCategoryById(@PathVariable Long categoryId) {
        nftService.deleteCategoryById(categoryId);
        return new ResponseEntity<>("Category Deleted Successfully!", HttpStatus.OK);
    }


}
