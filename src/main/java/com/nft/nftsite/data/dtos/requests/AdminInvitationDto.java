package com.nft.nftsite.data.dtos.requests;

import com.nft.nftsite.data.models.enumerations.InvitationStatus;
import com.nft.nftsite.utils.RegexPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminInvitationDto {

    private Long id;

    @NotBlank(message = "Email is required")
    @Pattern(regexp = RegexPattern.EMAIL, message = "Invalid email address")
    private String email;

    @NotBlank(message = "First name cannot be blank")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    private String lastName;

    private InvitationStatus status;

    private LocalDateTime createdAt;

}
