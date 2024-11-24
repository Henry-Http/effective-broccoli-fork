package com.nft.nftsite.data.dtos.responses.payment;


import com.nft.nftsite.data.models.enumerations.InternalPaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepositResponse {

    private Double amount;

    private InternalPaymentStatus status;

}
