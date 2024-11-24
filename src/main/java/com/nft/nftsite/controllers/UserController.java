package com.nft.nftsite.controllers;

import com.nft.nftsite.data.dtos.responses.payment.DepositResponse;
import com.nft.nftsite.services.users.UserDetailsService;
import com.nft.nftsite.services.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.nft.nftsite.data.dtos.requests.*;
import com.nft.nftsite.data.dtos.responses.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserDetailsService userDetailsService;


    @PostMapping("/signup")
    @Operation(summary = "Create new account")
    public ResponseEntity<TokenResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return new ResponseEntity<>(userService.signUp(requestDto), HttpStatus.OK);
    }

    @PostMapping("/third-party-signin")
    @Operation(summary = "Register/Login with third-party provider")
    public ResponseEntity<ThirdPartySignInResponseDto> thirdPartySignIn(@Valid @RequestBody ThirdPartySignInRequestDto requestDto) {
        return ResponseEntity.ok(userService.thirdPartyLogin(requestDto));
    }

    @PostMapping("/signup/complete")
    @Operation(summary = "Complete user signup")
    public ResponseEntity<TokenResponseDto> completeSignup(@Valid @RequestBody ConfirmRegistrationRequestDto requestDto) {
        log.info("Gotten request to confirm registration");
        return ResponseEntity.ok(userService.confirmRegistration(requestDto));
    }

    @PostMapping("/login")
    @Operation(summary = "Login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        return new ResponseEntity<>(userService.login(requestDto), HttpStatus.OK);
    }

    @GetMapping("/get-balance")
    @Operation(summary = "Get user balance")
    public ResponseEntity<DepositResponse> getUserBalance() {
        return new ResponseEntity<>(userDetailsService.getUserBalance(), HttpStatus.OK);
    }

    @GetMapping("/details")
    @Operation(summary = "Get user details")
    public ResponseEntity<UserDetailsDto> getUserDetails() {
        return new ResponseEntity<>(userDetailsService.getUserDetails(), HttpStatus.OK);
    }

    @PutMapping("/details")
    @Operation(summary = "Update user details")
    public ResponseEntity<UserDetailsDto> updateUserDetails(@RequestBody @Valid UpdateUserDetailsDto requestDto) {
        return ResponseEntity.ok(userDetailsService.updateUserDetails(requestDto));
    }

    @PostMapping("/details/display-picture")
    @Operation(summary = "Change display picture")
    public ResponseEntity<UserDetailsDto> changeDisplayPicture(@RequestParam("picture") MultipartFile picture) {
        return ResponseEntity.ok(userDetailsService.uploadDisplayPicture(picture));
    }

    @PostMapping("/change-password")
    @Operation(summary = "Change password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequestDto requestDto) {
        userService.changePassword(requestDto);
        return ResponseEntity.ok("Password changed successfully");
    }

    @PostMapping("/reset-password/init")
    @Operation(summary = "Reset password initialization")
    public ResponseEntity<String> resetPasswordInit(@RequestBody @Valid ResetPasswordInitRequestDto requestDto) {
        userService.forgotPasswordInit(requestDto.getEmail());
        return ResponseEntity.ok("Reset email sent");
    }

    @PostMapping("/reset-password/complete")
    @Operation(summary = "Reset password - new password")
    public ResponseEntity<String> resetPasswordComplete(@RequestBody @Valid ResetPasswordRequestDto requestDto) {
        userService.forgotPasswordComplete(requestDto);
        return ResponseEntity.ok("Password reset successfully");
    }

    @PostMapping("/otp/resend/{email}")
    @Operation(summary = "Resend OTP")
    public ResponseEntity<String> resendOtp(@PathVariable String email) {
        userService.resendOtp(email);
        return ResponseEntity.ok("Resent successfully");
    }

    @PostMapping("/admin/invite")
    @Operation(summary = "Invite new admin user")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<AdminInvitationDto> inviteNewUser(@RequestBody @Valid AdminInvitationDto requestDto) {
        return new ResponseEntity<>(userService.inviteAdmin(requestDto), HttpStatus.OK);
    }

    @PostMapping("/admin/login")
    @Operation(summary = "Login")
    public ResponseEntity<AdminTokenResponseDto> loginAdmin(@Valid @RequestBody LoginRequestDto requestDto) {
        return new ResponseEntity<>(userService.loginAdmin(requestDto), HttpStatus.OK);
    }

    @GetMapping("/admin/all")
    @Operation(summary = "Get all admins")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<List<UserDto>> getAllAdmins() {
        return ResponseEntity.ok(userService.getAllAdmins());
    }

}
