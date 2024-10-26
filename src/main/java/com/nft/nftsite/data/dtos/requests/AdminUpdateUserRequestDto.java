package com.nft.nftsite.data.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUpdateUserRequestDto {

    @NotNull(message = "User ID is required")
    private Long id;

    @NotNull(message = "Roles are required")
    private List<String> roles;

    private boolean activated;

}
