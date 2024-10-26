package com.nft.nftsite.data.repository;

import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.models.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    List<UserRole> findAllByUser(User user);

    void deleteAllByUser(User user);

    Page<UserRole> findAllByRole_NameIn(Set<String> role_names, Pageable pageable);

}
