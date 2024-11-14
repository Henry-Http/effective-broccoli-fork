package com.nft.nftsite.data.dtos.requests;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminTokenResponseDto {

    private String token;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;
    private String profilePicture;

}
