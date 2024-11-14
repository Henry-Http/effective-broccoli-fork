package com.nft.nftsite.data.dtos.responses.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebhookResponse {

    private String paymentId;

    private String payLinkId;

    private String createdAt;

    private Double itemPrice;

    private String paymentEmail;

    private String paymentFullName;

    private String deliveryAddress;

    private Long nftId;

    private Double amountPaid;

    private Long userId;

}
