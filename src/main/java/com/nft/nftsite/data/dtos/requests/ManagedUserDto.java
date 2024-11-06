package com.nft.nftsite.data.dtos.requests;

import com.nft.nftsite.data.dtos.responses.ThirdPartyUserDetails;
import com.nft.nftsite.data.models.enumerations.ThirdPartySignInType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ManagedUserDto {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private boolean activated;

    private boolean thirdPartySignIn;

    private ThirdPartySignInType thirdPartySignInType;

    public ManagedUserDto(SignupRequestDto requestDto) {
        String signUpEmail = requestDto.getUsername();
        this.username = signUpEmail;
        this.email = signUpEmail;
        this.firstName = signUpEmail.substring(0, signUpEmail.indexOf("@"));
        this.password = requestDto.getPassword();
    }

    public ManagedUserDto(ThirdPartyUserDetails thirdPartyUserDetails, ThirdPartySignInType type) {
        this.username = thirdPartyUserDetails.getId();
        this.password = RandomStringUtils.randomAlphanumeric(36, 50);
        this.firstName = thirdPartyUserDetails.getFirstName();
        this.lastName = thirdPartyUserDetails.getLastName();
        this.email = thirdPartyUserDetails.getEmail();
        this.activated = true;
        this.thirdPartySignIn = true;
        this.thirdPartySignInType = type;
    }
}
