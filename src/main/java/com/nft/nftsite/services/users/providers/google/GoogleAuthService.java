package com.nft.nftsite.services.users.providers.google;

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
public class GoogleAuthService implements ThirdPartyAuthService {

    private final RestClient restClient;

    @Override
    public ThirdPartySignInType supportedType() {
        return ThirdPartySignInType.GOOGLE;
    }

    @Override
    public ThirdPartyUserDetails verify(String token) {
        GoogleUserDto response = restClient
                .get()
                .uri("https://www.googleapis.com/userinfo/v2/me")
                .header("Authorization", String.format("Bearer %s", token))
                .retrieve()
                .body(GoogleUserDto.class);

        assert response != null;

        return ThirdPartyUserDetails.builder()
                .id(response.getId())
                .email(response.getEmail())
                .firstName(response.getGivenName())
                .lastName(response.getFamilyName())
                .build();
    }
}
