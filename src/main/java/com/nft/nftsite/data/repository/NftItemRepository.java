package com.nft.nftsite.data.repository;

import com.nft.nftsite.data.models.NftItem;
import com.nft.nftsite.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NftItemRepository extends JpaRepository<NftItem, Long> {

    boolean existsByName(String name);

    Page<NftItem> findAllByOwner(User user, Pageable pageable);

}
