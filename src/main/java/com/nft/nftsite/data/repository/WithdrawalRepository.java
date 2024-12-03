package com.nft.nftsite.data.repository;

import com.nft.nftsite.data.models.Withdrawal;
import com.nft.nftsite.data.models.enumerations.WithdrawalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long>, JpaSpecificationExecutor<Withdrawal> {

    List<Withdrawal> findAllByStatus(WithdrawalStatus status);

}
