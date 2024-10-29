package com.nft.nftsite.services.nft;


import com.nft.nftsite.data.dtos.requests.CreateCategoryRequest;
import com.nft.nftsite.data.dtos.requests.CreateNftRequest;
import com.nft.nftsite.data.dtos.requests.NftFilterDto;
import com.nft.nftsite.data.dtos.responses.CategoryResponse;
import com.nft.nftsite.data.dtos.responses.NftResponse;
import com.nft.nftsite.data.models.Category;
import com.nft.nftsite.data.models.NftItem;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.repository.CategoryRepository;
import com.nft.nftsite.data.repository.NftItemRepository;
import com.nft.nftsite.exceptions.CategoryNameAlreadyExistsException;
import com.nft.nftsite.exceptions.CategoryNotFoundException;
import com.nft.nftsite.services.users.UserService;
import com.nft.nftsite.utils.PageDto;
import com.nft.nftsite.utils.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;

@Service
@RequiredArgsConstructor
@Slf4j
public class NftServiceImpl implements NftService {

    private final NftItemRepository nftRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;


    @Override
    public NftResponse createNewNft(CreateNftRequest nftRequest) {
        User currentUser = userService.getAuthenticatedUser(true);
        return null;
    }

    @Override
    public PageDto<NftResponse> getAllNfts(Pageable pageable, NftFilterDto filterDto) {
        Page<NftItem> nftPage = nftRepository.findAllNftsWithFilters(
                filterDto.getSearch(),
                filterDto.getCategoryId(),
                filterDto.getMinPrice(),
                filterDto.getMaxPrice(),
                filterDto.getCurrentBid(),
                pageable
        );
        Type pageDtoType = new TypeToken<PageDto<NftResponse>>() {
        }.getType();
        return modelMapper.map(nftPage, pageDtoType);
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
        Category category = findCategoryById(categoryId);
        categoryRepository.delete(category);
    }

    @Override
    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    }

    private String generateSlug() {
        String slug = RandomStringGenerator.generateRandomString(16);
        if (nftRepository.existsBySlug(slug)) {
            return generateSlug();
        } else {
            return slug;
        }
    }

}
