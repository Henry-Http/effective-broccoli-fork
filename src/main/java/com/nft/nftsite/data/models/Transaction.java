package com.nft.nftsite.data.models;


import com.nft.nftsite.data.models.enumerations.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private User user;

    private final LocalDateTime transactionTime = LocalDateTime.now();

    private Double amount;

    private TransactionType transactionType;

}
