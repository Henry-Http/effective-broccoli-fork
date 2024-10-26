package com.nft.nftsite.data.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserSpecificationFields {

    private String firstName, lastName, tag, email, role;

}
