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
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/category")
@AllArgsConstructor
@Slf4j
public class CategoryController {

    private final NftService nftService;

    @PostMapping("/new")
    @Operation(summary = "Create new category")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<CategoryResponse> createNewCategory(@Valid @RequestBody CreateCategoryRequest categoryRequest) {
        return new ResponseEntity<>(nftService.createNewCategory(categoryRequest), HttpStatus.OK);
    }

    @GetMapping("/find")
    @Operation(summary = "Find all categories")
    public ResponseEntity<List<CategoryResponse>> findAllCategories() {
        return new ResponseEntity<>(nftService.findAllCategories(), HttpStatus.OK);
    }

    @GetMapping("/admin-find")
    @Operation(summary = "Admin Find all categories")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<CategoryResponse>> findAllCategoriesForAdmin() {
        return new ResponseEntity<>(nftService.findAllCategoryForAdmin(), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{categoryId}")
    @Operation(summary = "Delete an existing category by id")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Map<String, String>> deleteCategoryById(@PathVariable Long categoryId) {
        nftService.deleteCategoryById(categoryId);
        return new ResponseEntity<>(Map.of("message", "Category Deleted Successfully!"), HttpStatus.OK);
    }

    @PostMapping("/restore/{categoryId}")
    @Operation(summary = "Restore an existing category by id")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Map<String, String>> restoreCategoryById(@PathVariable Long categoryId) {
        nftService.restoreCategoryById(categoryId);
        return new ResponseEntity<>(Map.of("message", "Category Restored Successfully!"), HttpStatus.OK);
    }


}
