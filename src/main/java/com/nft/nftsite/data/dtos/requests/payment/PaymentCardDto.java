package com.nft.nftsite.data.dtos.requests.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCardDto {

    private int totalTransactions;

    private Double pendingPayments;

    private Double approvedPayments;

}
