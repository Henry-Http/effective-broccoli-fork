package com.nft.nftsite.services.users.providers.google;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleUserDto {

    private String id;

    private String name;

    private String email;

    private String picture;

    @JsonProperty("verified_email")
    private boolean verifiedEmail;

    @JsonProperty("family_name")
    private String familyName;

    @JsonProperty("given_name")
    private String givenName;

}
