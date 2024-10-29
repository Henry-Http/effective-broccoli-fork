package com.nft.nftsite.data.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCategoryRequest {

    @NotNull(message = "Category name is required")
    @NotBlank(message = "Category name is required")
    private String categoryName;

    private String categoryDescription;

}
