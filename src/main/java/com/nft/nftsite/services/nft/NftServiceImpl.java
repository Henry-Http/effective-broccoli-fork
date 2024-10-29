package com.nft.nftsite.services.nft;


import com.nft.nftsite.data.dtos.requests.CreateCategoryRequest;
import com.nft.nftsite.data.dtos.requests.CreateNftRequest;
import com.nft.nftsite.data.dtos.responses.CategoryResponse;
import com.nft.nftsite.data.dtos.responses.NftResponse;
import com.nft.nftsite.data.models.Category;
import com.nft.nftsite.data.repository.CategoryRepository;
import com.nft.nftsite.data.repository.NftItemRepository;
import com.nft.nftsite.exceptions.CategoryNameAlreadyExistsException;
import com.nft.nftsite.exceptions.CategoryNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NftServiceImpl implements NftService {

    private final NftItemRepository nftRepository;
    private final CategoryRepository categoryRepository;


    @Override
    public NftResponse createNewNft(CreateNftRequest nftRequest) {
        return null;
    }

    @Override
    public CategoryResponse createNewCategory(CreateCategoryRequest categoryRequest) {
        categoryRequest.setCategoryName(categoryRequest.getCategoryName().trim());
        if (categoryRepository.existsByNameEqualsIgnoreCase(categoryRequest.getCategoryName())) {
            throw new CategoryNameAlreadyExistsException();
        }
        Category category = Category.builder()
                .name(categoryRequest.getCategoryName())
                .description(categoryRequest.getCategoryDescription())
                .build();
        category = categoryRepository.save(category);
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    @Override
    public void deleteCategoryByName(String categoryName) {
        categoryName = categoryName.trim();
        Category category = categoryRepository.findByNameEqualsIgnoreCase(categoryName).orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(category);
    }

    @Override
    public void deleteCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        categoryRepository.delete(category);
    }
}
