package com.nft.nftsite.data.repository;

import com.nft.nftsite.data.models.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    Boolean existsByTagIgnoreCaseAndIdNot(String tag, Long id);

    Boolean existsByTag(String tag);

}
