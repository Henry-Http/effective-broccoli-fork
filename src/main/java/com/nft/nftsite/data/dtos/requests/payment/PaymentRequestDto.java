package com.nft.nftsite.data.dtos.requests.payment;

import com.nft.nftsite.data.models.enumerations.InternalPaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDto {

    private Long id;

    private Double amount;

    private LocalDateTime createdAt;

    private InternalPaymentStatus status;

    private String description;

}
