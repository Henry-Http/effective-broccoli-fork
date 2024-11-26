package com.nft.nftsite.services.nft;


import com.nft.nftsite.data.dtos.requests.CreateCategoryRequest;
import com.nft.nftsite.data.dtos.requests.CreateNftRequest;
import com.nft.nftsite.data.dtos.requests.NftFilterDto;
import com.nft.nftsite.data.dtos.requests.UpdateNftRequest;
import com.nft.nftsite.data.dtos.responses.CategoryResponse;
import com.nft.nftsite.data.dtos.responses.NftResponse;
import com.nft.nftsite.data.models.*;
import com.nft.nftsite.data.models.enumerations.NftStatus;
import com.nft.nftsite.data.models.enumerations.TransactionType;
import com.nft.nftsite.data.repository.CategoryRepository;
import com.nft.nftsite.data.repository.NftItemRepository;
import com.nft.nftsite.data.repository.TransactionRepository;
import com.nft.nftsite.exceptions.CategoryNameAlreadyExistsException;
import com.nft.nftsite.exceptions.CategoryNotFoundException;
import com.nft.nftsite.exceptions.NFTSiteException;
import com.nft.nftsite.services.cloudinaryImage.ImageService;
import com.nft.nftsite.services.users.UserDetailsService;
import com.nft.nftsite.services.users.UserService;
import com.nft.nftsite.utils.PageDto;
import com.nft.nftsite.utils.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private final UserDetailsService userDetailsService;
    private final TransactionRepository transactionRepository;

    @Value("${PALLETTEX_GAS_FEE}")
    private String gasFee;

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
        UserDetails userDetails = dbUser.getUserDetails();
        if (userDetails.getBalance() < Double.parseDouble(gasFee)) {
            throw new NFTSiteException("Insufficient balance", HttpStatus.BAD_REQUEST);
        }
        Image picture = imageService.uploadNewImage(nftRequest.getImage());
        NftItem nftItem = NftItem.builder()
                .name(nftRequest.getName())
                .description(nftRequest.getDescription())
                .startingPrice(nftRequest.getStartingPrice())
                .slug(generateSlug())
                .category(foundCategory)
                .owner(dbUser)
                .picture(picture)
                .nftStatus(NftStatus.FOR_SALE)
                .build();
        userDetailsService.deductBalance(Double.parseDouble(gasFee));
        Transaction transaction = new Transaction();
        transaction.setAmount(Double.parseDouble(gasFee) * (double) -1);
        transaction.setTransactionType(TransactionType.GAS_FEE_REMOVAL);
        transaction.setDebitOrCreditStatus(TransactionType.DEBIT);
        transaction.setUser(dbUser);
        transactionRepository.save(transaction);
        nftItem = nftRepository.save(nftItem);
        return modelMapper.map(nftItem, NftResponse.class);
    }

    @Override
    public NftResponse updateNft(UpdateNftRequest nftRequest) {
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
        Image picture = imageService.uploadNewImage(nftRequest.getImage());
        NftItem foundNft = nftRepository.findById(nftRequest.getId()).orElseThrow(() -> new NFTSiteException("NFT not found", HttpStatus.NOT_FOUND));
        foundNft.setCategory(foundCategory);
        foundNft.setName(nftRequest.getName());
        foundNft.setDescription(nftRequest.getDescription());
        foundNft.setStartingPrice(nftRequest.getStartingPrice());
        foundNft.setPicture(picture);
        nftRepository.save(foundNft);
        return modelMapper.map(foundNft, NftResponse.class);
    }

    @Override
    public PageDto<NftResponse> getAllNfts(Pageable pageable, NftFilterDto filterDto) {
        String search = ("%" + (filterDto.getSearch() == null ? "" : filterDto.getSearch().trim()) + "%").toLowerCase();
        Page<NftItem> nftPage = nftRepository.findAllNftsWithFilters(
                search,
                filterDto.getCategoryId(),
                NftStatus.FOR_SALE,
                pageable
        );
        Type pageDtoType = new TypeToken<PageDto<NftResponse>>() {
        }.getType();
        return modelMapper.map(nftPage, pageDtoType);
    }

    @Override
    public NftResponse findById(Long nftId) {
        NftItem item = nftRepository.findById(nftId).orElseThrow(() -> new NFTSiteException("NFT not found", HttpStatus.NOT_FOUND));
        return modelMapper.map(item, NftResponse.class);
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
    public void deleteCategoryById(Long categoryId) {
        Category category = findCategoryById(categoryId);
        category.setIsVisible(false);
        categoryRepository.save(category);
    }

    @Override
    public List<CategoryResponse> findAllCategories() {
        List<Category> categories = categoryRepository.findAllByIsVisible(true);
        if (!categories.isEmpty()) {
            Type listType = new TypeToken<List<CategoryResponse>>() {
            }.getType();
            return modelMapper.map(categories, listType);
        }
        return List.of();
    }

    @Override
    public List<CategoryResponse> findAllCategoryForAdmin() {
        List<Category> categories = categoryRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        if (!categories.isEmpty()) {
            Type listType = new TypeToken<List<CategoryResponse>>() {
            }.getType();
            List<CategoryResponse> responses = modelMapper.map(categories, listType);
            for (CategoryResponse categoryResponse: responses) {
                categoryResponse.setNftCount(nftRepository.countAllByCategory_Id(categoryResponse.getId()));
            }
            return responses;
        }
        return List.of();
    }

    @Override
    public void restoreCategoryById(Long categoryId) {
        Category category = findCategoryById(categoryId);
        category.setIsVisible(true);
        categoryRepository.save(category);
    }

    @Override
    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public PageDto<NftResponse> getOneUsersCollection(Pageable pageable) {
        User currentUser = userService.getAuthenticatedUser(true);
        User dbUser = userService.getUserById(currentUser.getId());
        Page<NftItem> nftPage = nftRepository.findByOwnerAndNftStatus(dbUser, NftStatus.HAS_BEEN_BOUGHT, pageable);
        Type pageDtoType = new TypeToken<PageDto<NftResponse>>() {
        }.getType();
        return modelMapper.map(nftPage, pageDtoType);
    }

    @Override
    public PageDto<NftResponse> getOneUsersCreations(Pageable pageable) {
        User currentUser = userService.getAuthenticatedUser(true);
        User dbUser = userService.getUserById(currentUser.getId());
        Page<NftItem> nftPage = nftRepository.findByOwnerAndNftStatus(dbUser, NftStatus.FOR_SALE, pageable);
        Type pageDtoType = new TypeToken<PageDto<NftResponse>>() {
        }.getType();
        return modelMapper.map(nftPage, pageDtoType);
    }


    @Override
    public void updateNftOwner(Long nftId) {
        NftItem nftItem = nftRepository.findById(nftId).orElseThrow(() -> new NFTSiteException("NFT not found", HttpStatus.NOT_FOUND));
        nftItem.setNftStatus(NftStatus.HAS_BEEN_BOUGHT);
        User currentUser = userService.getAuthenticatedUser(true);
        User dbUser = userService.getUserById(currentUser.getId());
        nftItem.setOwner(dbUser);
        nftRepository.save(nftItem);
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
