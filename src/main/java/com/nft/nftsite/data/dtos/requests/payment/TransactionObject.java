package com.nft.nftsite.data.dtos.requests.payment;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionObject {

    private String id;

    private String paylinkId;

    private String createdAt;

    private TransactionMetaObject meta;

}
