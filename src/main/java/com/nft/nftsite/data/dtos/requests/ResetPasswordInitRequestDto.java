package com.nft.nftsite.data.dtos.requests;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class ResetPasswordInitRequestDto {

    @Email(message = "Invalid email address")
    private String email;

}
