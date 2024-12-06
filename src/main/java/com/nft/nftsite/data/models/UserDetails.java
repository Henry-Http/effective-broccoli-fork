package com.nft.nftsite.data.models;

import com.nft.nftsite.data.models.enumerations.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_details")
public class UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String emailAddress;

    @OneToOne
    @JoinColumn(name = "display_picture_id")
    private Image displayPicture;

    @Column(unique = true)
    private String tag;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private LocalDateTime createdAt;

    private Double balance = 0.0;

    @Column(nullable = true)
    private boolean verified = false;

}
