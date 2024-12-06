package com.nft.nftsite.security;

import com.nft.nftsite.data.models.Role;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.repository.UserRepository;
import com.nft.nftsite.services.users.UserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user =
                userRepository.findByUsernameEqualsIgnoreCase(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        return AuthenticatedUser.builder()
                .user(user)
                .roles(userRoleService.getUserRoles(user))
                .build();
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
