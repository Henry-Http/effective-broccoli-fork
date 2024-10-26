package com.nft.nftsite.data.repository;

import com.nft.nftsite.data.models.EmailConfirm;
import com.nft.nftsite.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmailConfirmRepository extends JpaRepository<EmailConfirm, Long> {
    Optional<EmailConfirm> findByToken(String token);

    List<EmailConfirm> findAllByUser(User user);

    List<EmailConfirm> findAllByUserAndExpiredAndCreatedAtIsAfter(User user, boolean expired, LocalDateTime createdAt);

}
