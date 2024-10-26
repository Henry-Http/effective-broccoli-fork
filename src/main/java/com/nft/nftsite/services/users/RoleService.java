package com.nft.nftsite.services.users;


import com.nft.nftsite.data.models.Role;

import java.util.List;

public interface RoleService {

    List<String> findAllRoles();

    List<Role> findAllRolesInList(List<String> roles);

}
