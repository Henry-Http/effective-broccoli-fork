package com.nft.nftsite.services.nft;

import com.nft.nftsite.data.dtos.requests.CreateCategoryRequest;
import com.nft.nftsite.data.dtos.requests.CreateNftRequest;
import com.nft.nftsite.data.dtos.requests.NftFilterDto;
import com.nft.nftsite.data.dtos.responses.CategoryResponse;
import com.nft.nftsite.data.dtos.responses.NftResponse;
import com.nft.nftsite.data.models.Category;
import com.nft.nftsite.utils.PageDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NftService {

    // NFTS
    NftResponse createNewNft(@Valid CreateNftRequest nftRequest);

    PageDto<NftResponse> getAllNfts(Pageable pageable, NftFilterDto filterDto);

    NftResponse findById(Long nftId);


    // CATEGORY
    CategoryResponse createNewCategory(@Valid CreateCategoryRequest categoryRequest);

    Category findCategoryById(Long categoryId);

//    void deleteCategoryByName(String categoryName);

    void deleteCategoryById(Long categoryId);

    List<CategoryResponse> findAllCategories();

    List<CategoryResponse> findAllCategoryForAdmin();

    void restoreCategoryById(Long categoryId);

    PageDto<NftResponse> getOneUsersCollection(Pageable pageable);

    PageDto<NftResponse> getOneUsersCreations(Pageable pageable);

    void updateNftOwner(Long nftId);
}
