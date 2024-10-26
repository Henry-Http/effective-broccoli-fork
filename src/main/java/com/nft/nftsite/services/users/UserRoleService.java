package com.nft.nftsite.services.users;

import com.nft.nftsite.data.models.Role;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.models.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface UserRoleService {

    List<Role> getUserRoles(User user);

    UserRole save(User user, Role role);

    void assignRolesToUser(List<String> roles, User user);

    Page<UserRole> getAllByRoles(Set<String> roles, Pageable pageable);
}
