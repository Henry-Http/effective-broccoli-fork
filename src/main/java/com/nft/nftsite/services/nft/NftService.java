package com.nft.nftsite.services.nft;

import com.nft.nftsite.data.dtos.requests.CreateCategoryRequest;
import com.nft.nftsite.data.dtos.requests.CreateNftRequest;
import com.nft.nftsite.data.dtos.responses.CategoryResponse;
import com.nft.nftsite.data.dtos.responses.NftResponse;
import jakarta.validation.Valid;

public interface NftService {

    NftResponse createNewNft(@Valid CreateNftRequest nftRequest);

    CategoryResponse createNewCategory(@Valid CreateCategoryRequest categoryRequest);

    void deleteCategoryByName(String categoryName);

    void deleteCategoryById(Long categoryId);

}
