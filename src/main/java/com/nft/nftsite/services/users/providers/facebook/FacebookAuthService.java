package com.nft.nftsite.services.users.providers.facebook;

import com.nft.nftsite.data.models.enumerations.ThirdPartySignInType;
import com.nft.nftsite.services.users.factories.ThirdPartyAuthService;
import com.nft.nftsite.data.dtos.responses.ThirdPartyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class FacebookAuthService implements ThirdPartyAuthService {

    private final RestClient restClient;

    @Override
    public ThirdPartySignInType supportedType() {
        return ThirdPartySignInType.FACEBOOK;
    }

    @Override
    public ThirdPartyUserDetails verify(String token) {
        FacebookUserDto response = restClient
                .get()
                .uri("https://graph.facebook.com/me?fields=id,first_name,last_name,email")
                .header("Authorization", String.format("Bearer %s", token))
                .retrieve()
                .body(FacebookUserDto.class);

        assert response != null;

        return ThirdPartyUserDetails.builder()
                .id(response.getId())
                .email(response.getEmail())
                .firstName(response.getFirstName())
                .lastName(response.getLastName())
                .build();
    }
}
