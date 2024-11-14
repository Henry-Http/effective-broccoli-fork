package com.nft.nftsite.data.dtos.responses.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalInfoJson {

    private String customerId;

    private String nftId;

}
