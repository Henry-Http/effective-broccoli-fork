package com.nft.nftsite.data.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmRegistrationRequestDto {

    @NotBlank(message = "Confirmation code is required")
    private String code;

}
