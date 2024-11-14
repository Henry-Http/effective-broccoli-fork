package com.nft.nftsite.data.models;

import com.nft.nftsite.data.models.enumerations.InvitationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "admin_invitations")
public class AdminInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String roles;

    private String token;

    private InvitationStatus status;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private User invitedBy;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    private User invitedUser;

    private final LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

}
