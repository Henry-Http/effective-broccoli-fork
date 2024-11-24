package com.nft.nftsite.data.repository;

import com.nft.nftsite.data.models.InternalPayments;
import com.nft.nftsite.data.models.enumerations.InternalPaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InternalPaymentRepository extends JpaRepository<InternalPayments, Long> {

    List<InternalPayments> findAllByStatus(InternalPaymentStatus status);

}
