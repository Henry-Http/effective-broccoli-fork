package com.nft.nftsite.data.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NftFilterDto {

    private String search;
    private Long categoryId;
    private Double minPrice;
    private Double maxPrice;
    private Double currentBid;

}
