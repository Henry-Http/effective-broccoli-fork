package com.nft.nftsite.services.users.factories;


import com.nft.nftsite.data.models.enumerations.ThirdPartySignInType;
import com.nft.nftsite.data.dtos.responses.ThirdPartyUserDetails;

public interface ThirdPartyAuthService {

    ThirdPartySignInType supportedType();

    ThirdPartyUserDetails verify(String token);

}
