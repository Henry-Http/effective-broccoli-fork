package com.nft.nftsite.data.repository;

import com.nft.nftsite.data.models.Payments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payments, String> {

}
