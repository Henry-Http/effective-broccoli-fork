package com.nft.nftsite.data.dtos.responses;


import com.nft.nftsite.data.models.enumerations.NftStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyNftResponse {

    private Long nftId;

    private String nftName;

    private Double nftPrice;

    private NftStatus nftStatus;

}
