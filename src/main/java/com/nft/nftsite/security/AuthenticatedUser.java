package com.nft.nftsite.security;

import com.nft.nftsite.data.models.Role;
import com.nft.nftsite.data.models.User;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class AuthenticatedUser implements UserDetails {

    private User user;

    private List<Role> roles;

    public String getUsername() {
        return user.getUsername();
    }

    public String getPassword() {
        return user.getPassword();
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return mapRolesToAuthorities();
    }

    public boolean isAccountNonExpired() {
        return true;
    }

    public boolean isAccountNonLocked() {
        return user.isActivated();
    }

    public boolean isCredentialsNonExpired() {
        return true;
    }

    public boolean isEnabled() {
        return true;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities() {
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

}
