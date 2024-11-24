package com.nft.nftsite.data.dtos.responses.payment;


import com.nft.nftsite.data.models.enumerations.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserTransaction {

    private Double amount;

    private TransactionType transactionType;

    private LocalDateTime transactionTime;

}
