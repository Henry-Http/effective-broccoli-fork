package com.nft.nftsite.services.users;

import com.nft.nftsite.data.models.Role;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.models.UserRole;
import com.nft.nftsite.data.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {

    private UserRoleRepository userRoleRepository;
    private final RoleService roleService;

    @Override
    public List<Role> getUserRoles(User user) {
        return userRoleRepository.findAllByUser(user)
                .stream()
                .map(UserRole::getRole)
                .collect(Collectors.toList());
    }

    @Override
    public UserRole save(User user, Role role) {
        return userRoleRepository.save(
                UserRole.builder()
                        .user(user)
                        .role(role)
                        .build());
    }

    @Override
    public void assignRolesToUser(List<String> roles, User user) {
        List<Role> roleEntities = roleService.findAllRolesInList(roles);

        userRoleRepository.deleteAllByUser(user);

        Set<UserRole> userRoles = new HashSet<>();
        for (Role role : roleEntities) {
            userRoles.add(UserRole.builder()
                    .user(user)
                    .role(role)
                    .build());
        }

        userRoleRepository.saveAll(userRoles);
    }

    @Override
    public Page<UserRole> getAllByRoles(Set<String> roles, Pageable pageable) {
        return userRoleRepository.findAllByRole_NameIn(roles, pageable);
    }
}
