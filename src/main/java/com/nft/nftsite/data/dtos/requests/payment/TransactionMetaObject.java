package com.nft.nftsite.data.dtos.requests.payment;

import com.nft.nftsite.data.models.enumerations.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionMetaObject {

    private String transactionSignature;

    private PaymentStatus transactionStatus;

    private String amount;

    private TransactionCustomerDetails customerDetails;

    private String totalAmount;

}
