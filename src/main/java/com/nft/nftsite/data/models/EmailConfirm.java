package com.nft.nftsite.data.models;

import com.nft.nftsite.data.models.enumerations.EmailConfirmType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "email_confirm")
public class EmailConfirm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private String email;

    private boolean expired;

    @Enumerated(EnumType.STRING)
    private EmailConfirmType type;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    private final LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

}
