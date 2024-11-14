package com.nft.nftsite.data.repository;

import com.nft.nftsite.data.models.AdminInvitation;
import com.nft.nftsite.data.models.enumerations.InvitationStatus;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminInvitationRepository extends JpaRepository<AdminInvitation, Long> {

    List<AdminInvitation> findAllByEmail(String email);

    Optional<AdminInvitation> findByTokenAndStatus(String token, InvitationStatus status);

    @NonNull Page<AdminInvitation> findAll(@NonNull Pageable pageable);

    @NonNull Page<AdminInvitation> findAllByStatus(@NonNull Pageable pageable, InvitationStatus status);

}
