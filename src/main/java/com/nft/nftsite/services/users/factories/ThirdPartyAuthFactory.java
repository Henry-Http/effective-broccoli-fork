package com.nft.nftsite.services.users.factories;

import com.nft.nftsite.data.models.enumerations.ThirdPartySignInType;
import com.nft.nftsite.exceptions.ServiceNotImplementedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThirdPartyAuthFactory {

    private final List<ThirdPartyAuthService> thirdPartyAuthServices;

    public ThirdPartyAuthService findByType(ThirdPartySignInType type) {
        return thirdPartyAuthServices
                .stream()
                .filter(service -> service.supportedType().equals(type))
                .findFirst()
                .orElseThrow(ServiceNotImplementedException::new);
    }
}
