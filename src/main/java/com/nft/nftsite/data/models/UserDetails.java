package com.nft.nftsite.data.models;

import com.nft.nftsite.data.models.enumerations.Gender;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
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

}
