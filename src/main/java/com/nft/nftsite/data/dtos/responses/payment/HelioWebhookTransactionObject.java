package com.nft.nftsite.data.dtos.responses.payment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HelioWebhookTransactionObject {

    private String id;

    private String paylinkId;

}
