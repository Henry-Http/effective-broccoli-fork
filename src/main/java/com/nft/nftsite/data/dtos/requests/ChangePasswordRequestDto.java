package com.nft.nftsite.data.dtos.requests;

import com.nft.nftsite.utils.RegexPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequestDto {

    @NotBlank(message = "Old password is required")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Pattern(regexp = RegexPattern.PASSWORD, message = "Weak password")
    private String newPassword;
}
