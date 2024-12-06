package com.nft.nftsite.data.repository;

import com.nft.nftsite.data.dtos.responses.UsersOverviewDto;
import com.nft.nftsite.data.models.Role;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.models.enumerations.ThirdPartySignInType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsernameEqualsIgnoreCase(String username);

    Optional<User> findByUsernameAndThirdPartySignInType(String username, ThirdPartySignInType thirdPartySignInType);

    Boolean existsByUsername(String username);

    @Query("""
            SELECT new com.nft.nftsite.data.dtos.responses.UsersOverviewDto(
                (SELECT COUNT(u.id) FROM User u),
                (SELECT COUNT(u.id) FROM User u WHERE u.activated = true),
                (SELECT COUNT(u.id) FROM User u WHERE u.activated = false)
            )
            """)
    UsersOverviewDto getOverview();

    List<User> findAllByRolesContaining(Role role);

    List<User> findAllByUserDetails_Verified(boolean verified);

}
