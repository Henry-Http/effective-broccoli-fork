package com.nft.nftsite.data.repository;

import com.nft.nftsite.data.models.Transaction;
import com.nft.nftsite.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByUser(User user);

}
