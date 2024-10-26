package com.nft.nftsite.data.dtos.requests;

import com.nft.nftsite.utils.RegexPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPasswordRequestDto {

    @NotBlank(message = "Invalid token")
    private String token;

    @NotBlank(message = "New password is required")
    @Pattern(regexp = RegexPattern.PASSWORD, message = "Weak password")
    private String newPassword;
}
