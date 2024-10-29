package com.nft.nftsite.services.nft;


import com.nft.nftsite.data.dtos.requests.CreateCategoryRequest;
import com.nft.nftsite.data.dtos.requests.CreateNftRequest;
import com.nft.nftsite.data.dtos.requests.NftFilterDto;
import com.nft.nftsite.data.dtos.responses.CategoryResponse;
import com.nft.nftsite.data.dtos.responses.NftResponse;
import com.nft.nftsite.data.models.Category;
import com.nft.nftsite.data.models.Image;
import com.nft.nftsite.data.models.NftItem;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.repository.CategoryRepository;
import com.nft.nftsite.data.repository.NftItemRepository;
import com.nft.nftsite.exceptions.CategoryNameAlreadyExistsException;
import com.nft.nftsite.exceptions.CategoryNotFoundException;
import com.nft.nftsite.exceptions.NFTSiteException;
import com.nft.nftsite.services.cloudinaryImage.ImageService;
import com.nft.nftsite.services.users.UserService;
import com.nft.nftsite.utils.PageDto;
import com.nft.nftsite.utils.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NftServiceImpl implements NftService {

    private final NftItemRepository nftRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final ImageService imageService;

    @Override
    @Transactional
    public NftResponse createNewNft(CreateNftRequest nftRequest) {
        Category foundCategory;
        try {
            foundCategory = findCategoryById(nftRequest.getCategoryId());
        } catch (CategoryNotFoundException categoryNotFoundException) {
            throw new NFTSiteException("Category does not exist", HttpStatus.BAD_REQUEST);
        }
        nftRequest.setName(nftRequest.getName().trim());
        if (nftRepository.existsByNameEqualsIgnoreCase(nftRequest.getName())) {
            throw new NFTSiteException("NFT with this name already exists", HttpStatus.BAD_REQUEST);
        }
        User currentUser = userService.getAuthenticatedUser(true);
        User dbUser = userService.getUserById(currentUser.getId());
        List<Image> pictures = imageService.uploadNewImage(nftRequest.getImages().toArray(new MultipartFile[0]));
        NftItem nftItem = NftItem.builder()
                .name(nftRequest.getName())
                .description(nftRequest.getDescription())
                .startingPrice(nftRequest.getStartingPrice())
                .currentBid(0.00)
                .slug(generateSlug())
                .category(foundCategory)
                .owner(dbUser)
                .pictures(pictures)
                .build();
        nftItem = nftRepository.save(nftItem);
        return modelMapper.map(nftItem, NftResponse.class);
    }

    @Override
    public PageDto<NftResponse> getAllNfts(Pageable pageable, NftFilterDto filterDto) {
        String search = ("%" + (filterDto.getSearch() == null ? "" : filterDto.getSearch().trim()) + "%").toLowerCase();
        Page<NftItem> nftPage = nftRepository.findAllNftsWithFilters(
                search,
                filterDto.getCategoryId(),
//                filterDto.getMinPrice(),
//                filterDto.getMaxPrice(),
//                filterDto.getCurrentBid(),
                pageable
        );
        Type pageDtoType = new TypeToken<PageDto<NftResponse>>() {
        }.getType();
        return modelMapper.map(nftPage, pageDtoType);
    }

    @Override
    @Transactional
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
    public List<CategoryResponse> findAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if (!categories.isEmpty()) {
            Type listType = new TypeToken<List<CategoryResponse>>() {
            }.getType();
            return modelMapper.map(categories, listType);
        }
        return List.of();
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
