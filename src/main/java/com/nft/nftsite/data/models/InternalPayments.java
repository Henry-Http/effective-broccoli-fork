package com.nft.nftsite.data.models;


import com.nft.nftsite.data.models.enumerations.InternalPaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "internal_payments")
public class InternalPayments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private User user;

    private InternalPaymentStatus status;

    private String description;

    private final LocalDateTime createdAt = LocalDateTime.now();

}
