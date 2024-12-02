package com.nft.nftsite.services.users;


import com.nft.nftsite.data.dtos.responses.*;
import com.nft.nftsite.data.dtos.requests.*;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.utils.PageDto;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

public interface UserService {

    TokenResponseDto signUp(SignupRequestDto requestDto);

    ThirdPartySignInResponseDto thirdPartyLogin(ThirdPartySignInRequestDto thirdPartySignInDto);

    TokenResponseDto confirmRegistration(ConfirmRegistrationRequestDto requestDto);

    TokenResponseDto login(LoginRequestDto request);

    User getAuthenticatedUser();

    User getAuthenticatedUser(boolean nullable);

    User getUserById(Long id);

    Optional<User> getUserByUsername(String username);

    User save(User user);

    void changePassword(ChangePasswordRequestDto requestDto);

    void forgotPasswordInit(String emailAddress);

    void forgotPasswordComplete(ResetPasswordRequestDto requestDto);

    void resendOtp(String email);

    PageDto<UserDto> getAllUsers(Pageable pageable);

    List<UserDto> getAllUsers();

    PageDto<UserDto> getAllUsers(UserSpecificationFields fields, Pageable pageable);

    UserDto getUser(Long id);

    UserDto getUser();

    UsersOverviewDto getUsersOverview();

    boolean hasRole(String role);

    boolean isControlCenterUser();

    AdminInvitationDto inviteAdmin(AdminInvitationDto requestDto);

    AdminTokenResponseDto loginAdmin(LoginRequestDto requestDto);

    List<UserDto> getAllAdmins();

    AdminInvitationDto resetAdminPassword(Long adminId);

    GeneralMailResponse sendGeneralMail(GeneralMailRequest mailRequest);
}
