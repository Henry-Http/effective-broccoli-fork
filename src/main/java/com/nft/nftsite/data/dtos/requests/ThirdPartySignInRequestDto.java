package com.nft.nftsite.data.dtos.requests;

import com.nft.nftsite.data.models.enumerations.ThirdPartySignInType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ThirdPartySignInRequestDto {

    private String accessToken;

    private ThirdPartySignInType type;

}
