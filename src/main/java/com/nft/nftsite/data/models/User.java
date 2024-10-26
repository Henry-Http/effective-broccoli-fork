package com.nft.nftsite.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nft.nftsite.data.models.enumerations.ThirdPartySignInType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    @JsonIgnore
    @ToStringExclude
    private String password;

    @Column(name = "third_party_sign_in")
    private boolean thirdPartySignIn;

    @Enumerated(EnumType.STRING)
    @Column(name = "third_party_sign_in_type")
    private ThirdPartySignInType thirdPartySignInType;

    private boolean activated;

    private final LocalDateTime createdAt = LocalDateTime.now();

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserDetails userDetails;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User user) {
            return user.getId().equals(this.getId());
        }
        return false;
    }
}
