package com.nft.nftsite.data.dtos.requests.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HelioWebhookPayload {

    private String transaction;

    private HelioEvent event;

    private TransactionObject transactionObject;

}
