package com.nft.nftsite.data.dtos.responses;

import com.nft.nftsite.data.models.Role;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDto {

    private Long id;

    private String username;

    private boolean activated;

    private LocalDateTime createdAt;

    private UserDetailsDto userDetails;

    private List<String> roles;

    public void setRoles(List<Role> roles) {
        this.roles = roles.stream().map(Role::getName).toList();
    }
}
