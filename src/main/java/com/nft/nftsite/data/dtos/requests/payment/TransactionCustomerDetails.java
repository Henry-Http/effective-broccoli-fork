package com.nft.nftsite.data.dtos.requests.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionCustomerDetails {

    private String email;

    private String fullName;

    private String country;

    private String deliveryAddress;

    private String additionalJSON;

}
