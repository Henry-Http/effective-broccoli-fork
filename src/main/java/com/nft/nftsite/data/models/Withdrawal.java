package com.nft.nftsite.data.models;

import com.nft.nftsite.data.models.enumerations.WithdrawalStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "withdrawals")
public class Withdrawal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private User user;

    private final LocalDateTime createdAt = LocalDateTime.now();

    private String walletAddress;

    private String network;

    private LocalDateTime fulfilledAt;

    private WithdrawalStatus status;

}
