package com.nft.nftsite.data.dtos.responses.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto {

    private Long id;
    private boolean activated;
    private String createdAt;
    private String role;
    private String profilePicture;
    private String firstName;
    private String lastName;

}
