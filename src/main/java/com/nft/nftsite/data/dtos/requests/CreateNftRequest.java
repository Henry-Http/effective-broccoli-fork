package com.nft.nftsite.data.dtos.requests;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
    private String description;

    @NotNull(message = "Please select category")
    @Positive(message = "Invalid category provided")
    private Long categoryId;

    @NotNull(message = "Starting price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Starting price must be greater than 0")
    private Double startingPrice;

    @NotNull(message = "Please upload an image")
    private MultipartFile image;

}
