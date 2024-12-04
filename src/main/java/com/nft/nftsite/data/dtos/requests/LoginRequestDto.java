package com.nft.nftsite.data.dtos.requests;

import com.nft.nftsite.utils.RegexPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestDto {

    @Pattern(regexp = RegexPattern.EMAIL, message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = RegexPattern.PASSWORD, message = "Invalid Password")
    private String password;

}
