package com.nft.nftsite.data.repository;

import com.nft.nftsite.data.models.Image;
import com.nft.nftsite.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> findByIdAndUploadedBy(Long id, User uploadedBy);

}
