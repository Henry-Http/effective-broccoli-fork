package com.nft.nftsite.data.dtos.requests;

import com.nft.nftsite.utils.RegexPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SetupAccountRequestDto {

    @NotBlank(message = "Invalid setup token")
    private String token;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = RegexPattern.PASSWORD, message = "Password is weak")
    private String password;

}
