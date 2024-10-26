package com.nft.nftsite.data.dtos.requests;

import com.nft.nftsite.data.models.enumerations.Gender;
import com.nft.nftsite.utils.RegexPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDetailsDto {
    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    private String lastName;

    @Pattern(regexp = RegexPattern.TAG, message = "Invalid tag format, please set a valid tag")
    @NotBlank(message = "Please set a valid tag")
    private String tag;

    private Gender gender;

}
