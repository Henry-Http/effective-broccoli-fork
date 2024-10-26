package com.nft.nftsite.services.users;

import com.nft.nftsite.data.models.Role;
import com.nft.nftsite.data.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;


    @Override
    public List<String> findAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toList());
    }

    @Override
    public List<Role> findAllRolesInList(List<String> roles) {
        return roleRepository.findAllByNameIn(roles);
    }
}
