package com.nft.nftsite.data.dtos.requests;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateNftRequest {

    @NotNull(message = "NFT name is required")
    @NotBlank(message = "NFT name is required")
    private String name;

    @NotNull(message = "Description is required")
    @NotBlank(message = "Description is required")
    @Min(value = 5, message = "Description must be at least 5 characters long")
    @Max(value = 200, message = "Description must be at most 200 characters long")
    private String description;

    @NotNull(message = "Please select category")
    @Positive(message = "Invalid category provided")
    private Long categoryId;

    @NotNull(message = "Starting price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Starting price must be greater than 0")
    private Double startingPrice;

}
