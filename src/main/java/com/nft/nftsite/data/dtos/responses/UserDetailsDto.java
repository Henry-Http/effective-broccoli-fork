package com.nft.nftsite.data.dtos.responses;

import com.nft.nftsite.data.models.enumerations.Gender;
import com.nft.nftsite.data.models.enumerations.ThirdPartySignInType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String emailAddress;

    private String tag;

    private Gender gender;

    private boolean thirdPartySignIn;

    private ImageDto displayPicture;

    private ThirdPartySignInType thirdPartySignInType;

    private LocalDateTime createdAt;

    private Double balance;

}
