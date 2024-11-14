package com.nft.nftsite.data.models;

import com.nft.nftsite.data.models.enumerations.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "payments")
public class Payments {

    @Id
    private String id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private User payer;

    private Double amountPaid;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String payLinkId;

    private String createdAt;

    private String transactionSignature;

    private Double itemPrice;

    private String paymentEmail;

    private String paymentFullName;

    private String deliveryAddress;

    private String additionalJson;

    private Long nftId;

}
