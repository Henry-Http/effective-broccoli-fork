package com.nft.nftsite.data.dtos.requests;

import com.nft.nftsite.utils.RegexPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class InviteUserRequestDto {

    @NotBlank(message = "Email is required")
    @Pattern(regexp = RegexPattern.EMAIL, message = "Invalid email address")
    private String email;

    @NotEmpty(message = "Choose at least a role for user")
    private List<String> roles;

}
