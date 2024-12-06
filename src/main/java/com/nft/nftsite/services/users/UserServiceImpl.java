package com.nft.nftsite.services.users;

import com.nft.nftsite.data.models.*;
import com.nft.nftsite.data.models.enumerations.EmailConfirmType;
import com.nft.nftsite.data.models.enumerations.InvitationStatus;
import com.nft.nftsite.data.repository.AdminInvitationRepository;
import com.nft.nftsite.data.repository.UserRepository;
import com.nft.nftsite.exceptions.*;
import com.nft.nftsite.security.AuthenticatedUser;
import com.nft.nftsite.security.JwtGenerator;
import com.nft.nftsite.services.users.factories.ThirdPartyAuthFactory;
import com.nft.nftsite.services.users.factories.ThirdPartyAuthService;
import com.nft.nftsite.utils.PageDto;
import com.nft.nftsite.utils.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.nft.nftsite.data.dtos.requests.*;
import com.nft.nftsite.data.dtos.responses.*;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final ThirdPartyAuthFactory thirdPartyAuthFactory;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailConfirmService emailConfirmService;
    private final JwtGenerator jwtGenerator;
    private  final AuthenticationManager authenticationManager;
    private final ModelMapper mapper;
    private final UserRoleService userRoleService;
    private final AdminInvitationRepository adminInvitationRepository;
    private final RoleService roleService;


//    @PostConstruct
//    void setUpAdmin() {
//        UserDetails userDetails = UserDetails.builder()
//                .firstName("Pallettex")
//                .lastName("Admin")
//                .emailAddress("adeola@pallettex.com")
//                .createdAt(LocalDateTime.now())
//                .tag(RandomStringGenerator.generateRandomString(20))
//                .build();
//        String password = "Password@123";
//        User user = User.builder()
//                .username("adeola@pallettex.com")
//                .password(passwordEncoder.encode(password))
//                .activated(true)
//                .userDetails(userDetails)
//                .build();
//        User savedUser = this.save(user);
//        List<String> roles = List.of("ROLE_ADMIN");
//        userRoleService.assignRolesToUser(roles, savedUser);
//    }


    @Override
    @Transactional
    public TokenResponseDto signUp(SignupRequestDto requestDto) {
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new UsernameAlreadyUsedException();
        }

        User savedUser = createUser(new ManagedUserDto(requestDto));
        log.info("User saved, attempting to send confirmation mail to {}", savedUser.getUsername());
        emailConfirmService.sendConfirmation(savedUser, EmailConfirmType.ACTIVATION);

        String token = jwtGenerator.generateToken(savedUser);
        return TokenResponseDto.builder().token("Bearer " + token).build();
    }

    private User createUser(ManagedUserDto managedUserDto) {
        UserDetails userDetails = UserDetails.builder()
                .emailAddress(managedUserDto.getEmail())
                .tag(RandomStringGenerator.generateRandomString(10))
                .firstName(managedUserDto.getFirstName())
                .lastName(managedUserDto.getLastName())
                .createdAt(LocalDateTime.now())
                .balance(0.0)
                .verified(false)
                .build();

        User user = User.builder()
                .username(managedUserDto.getUsername())
                .password(passwordEncoder.encode(managedUserDto.getPassword()))
                .activated(managedUserDto.isActivated())
                .thirdPartySignIn(managedUserDto.isThirdPartySignIn())
                .thirdPartySignInType(managedUserDto.getThirdPartySignInType())
                .userDetails(userDetails)
                .build();

        User savedUser = userRepository.save(user);
        userRoleService.assignRolesToUser(List.of("ROLE_CUSTOMER", "ROLE_USER"), savedUser);
        return savedUser;
    }

    @Override
    public ThirdPartySignInResponseDto thirdPartyLogin(ThirdPartySignInRequestDto thirdPartySignInDto) {
        ThirdPartyAuthService thirdPartyAuthService = thirdPartyAuthFactory.findByType(thirdPartySignInDto.getType());
        ThirdPartyUserDetails thirdPartyUserDetails = thirdPartyAuthService.verify(thirdPartySignInDto.getAccessToken());

        Optional<User> userOptional = userRepository
                .findByUsernameAndThirdPartySignInType(thirdPartyUserDetails.getId(), thirdPartySignInDto.getType());

        User user = userOptional.orElseGet(() -> this.createUser(
                new ManagedUserDto(thirdPartyUserDetails, thirdPartySignInDto.getType())
        ));

        UserDetailsDto userDetailsDto = mapper.map(user.getUserDetails(), UserDetailsDto.class);
        userDetailsDto.setThirdPartySignIn(user.isThirdPartySignIn());
        userDetailsDto.setThirdPartySignInType(user.getThirdPartySignInType());

        return ThirdPartySignInResponseDto.builder()
                .token(jwtGenerator.generateToken(user))
                .user(userDetailsDto)
                .build();
    }

    @Override
    public TokenResponseDto confirmRegistration(ConfirmRegistrationRequestDto requestDto) {
        EmailConfirm emailConfirm = emailConfirmService.retrieveByToken(requestDto.getCode());

        if (emailConfirm.getCreatedAt().plusHours(2).isBefore(LocalDateTime.now())
                || emailConfirm.isExpired()
        ) {
            // Token already expired or doesn't belong to user
            log.info("Token expired or already used");
            throw new UnauthorizedRequestException();
        }

        User user = emailConfirm.getUser();

        user.setActivated(true);
        User savedUser = userRepository.save(user);
        emailConfirmService.revokeToken(emailConfirm.getToken());

        String token = jwtGenerator.generateToken(savedUser);
        return TokenResponseDto.builder().token("Bearer " + token).build();
    }

    @Override
    public TokenResponseDto login(LoginRequestDto request) {
        try {
            log.info("LoginRequestDto username {}", request.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword())
            );

            // Check if the user has the ROLE_ADMIN authority
            boolean isAdmin = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(role -> role.equals("ROLE_ADMIN"));

            if (isAdmin) {
                throw new UnauthorizedRequestException("Admin users are not allowed to log in via this endpoint.");
            }

            String token = jwtGenerator.generateToken(authentication);
            return TokenResponseDto.builder().token("Bearer " + token).build();
        } catch (Exception e) {
            if (Objects.equals(e.getClass().getName(), "org.springframework.security.authentication.LockedException")) {
                this.resendOtp(request.getUsername());
                throw new UnauthorizedRequestException("VERIFICATION_REQUIRED");
            }
            throw new InvalidLoginDetailsException();
        }
    }


    @Override
    public User getAuthenticatedUser() {
        try {
            AuthenticatedUser authUser = (AuthenticatedUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (authUser == null) {
                throw new UnauthorizedRequestException();
            }
            return authUser.getUser();
        } catch (Exception e) {
            throw new UnauthorizedRequestException();
        }
    }

    @Override
    public User getAuthenticatedUser(boolean nullable) {
        try {
            return this.getAuthenticatedUser();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsernameEqualsIgnoreCase(username.trim());
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void changePassword(ChangePasswordRequestDto requestDto) {
        User user = this.getAuthenticatedUser();
        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword()))
            throw new UnauthorizedRequestException("Incorrect password");

        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void forgotPasswordInit(String emailAddress) {
        Optional<User> user = this.getUserByUsername(emailAddress);
        user.ifPresent(value ->
                emailConfirmService.sendConfirmation(value, EmailConfirmType.PASSWORD_RESET)
        );
    }

    @Override
    public void forgotPasswordComplete(ResetPasswordRequestDto requestDto) {
        log.info("Completing password reset");
        EmailConfirm initEntry = emailConfirmService.retrieveByToken(requestDto.getToken());

        if (initEntry.getCreatedAt().plusHours(2).isBefore(LocalDateTime.now()) || initEntry.isExpired()) {
            // Token already expired
            log.info("Token expired or already used!");
            throw new UnauthorizedRequestException();
        }

        User user = initEntry.getUser();
        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(user);
        emailConfirmService.revokeToken(requestDto.getToken());
        // TODO: send password change notification to email
    }

    @Override
    public void resendOtp(String email) {
        User user = this.getUserByUsername(email).orElseThrow(UnauthorizedRequestException::new);
        emailConfirmService.resendOtp(user);
    }

    @Override
    public PageDto<UserDto> getAllUsers(Pageable pageable) {
        return this.getAllUsers(null, pageable);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<Role> roles = roleService.findAllRolesInList(List.of("ROLE_CUSTOMER", "ROLE_USER"));
        List<User> allUsers = userRepository.findAllByRolesContaining(roles.get(0));
        List<User> allUsers2 = userRepository.findAllByRolesContaining(roles.get(1));
        Set<User> users = new HashSet<>();
        users.addAll(allUsers);
        users.addAll(allUsers2);
        Type pageDtoTypeToken = new TypeToken<List<UserDto>>() {
        }.getType();
        return mapper.map(users.stream().toList(), pageDtoTypeToken);
    }

    @Override
    public PageDto<UserDto> getAllUsers(UserSpecificationFields fields, Pageable pageable) {
        Specification<User> spec = Specification.where(null);

        if (fields != null) {
            if (fields.getFirstName() != null) {
                spec = spec.and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("userDetails").get("firstName")),
                        fields.getFirstName().toLowerCase())
                ));
            }

            if (fields.getLastName() != null) {
                spec = spec.and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("userDetails").get("lastName")),
                        fields.getLastName().toLowerCase())
                ));
            }

            if (fields.getTag() != null) {
                spec = spec.and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("userDetails").get("tag")),
                        fields.getTag().toLowerCase())
                ));
            }

            if (fields.getEmail() != null) {
                spec = spec.and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("userDetails").get("emailAddress")),
                        fields.getEmail().toLowerCase())
                ));
            }

            if (fields.getRole() != null) {
                spec = spec.and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                        root.join("roles").get("name"),
                        fields.getRole())
                ));
            }
        }

        Page<User> users = userRepository.findAll(spec, pageable);

        Type pageDtoTypeToken = new TypeToken<PageDto<UserDto>>() {
        }.getType();
        return mapper.map(users, pageDtoTypeToken);
    }

    @Override
    public UserDto getUser(Long id) {
        User user = this.getUserById(id);
        return mapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUser() {
        User user = this.getAuthenticatedUser();
        return mapper.map(this.getUser(user.getId()), UserDto.class);
    }

    @Override
    public UsersOverviewDto getUsersOverview() {
        return userRepository.getOverview();
    }

    @Override
    public boolean hasRole(String role) {
        return Optional.ofNullable(getAuthenticatedUser(false))
                .map(userRoleService::getUserRoles)
                .map(roles -> roles.stream()
                        .map(Role::getName)
                        .anyMatch(roleName -> roleName.equalsIgnoreCase(role)))
                .orElse(false);
    }

    @Override
    public boolean isControlCenterUser() {
        return hasRole("ROLE_CONTROL_CENTER");
    }

    @Override
    public AdminInvitationDto inviteAdmin(AdminInvitationDto requestDto) {
        Optional<User> existingUser = this.getUserByUsername(requestDto.getEmail());

        if (existingUser.isPresent()) throw new UsernameAlreadyUsedException();

        UserDetails userDetails = UserDetails.builder()
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .emailAddress(requestDto.getEmail())
                .createdAt(LocalDateTime.now())
                .tag(RandomStringGenerator.generateRandomString(20))
                .build();

        String password = RandomStringGenerator.generateRandomString(12);
        User user = User.builder()
                .username(requestDto.getEmail())
                .password(passwordEncoder.encode(password))
                .activated(true)
                .userDetails(userDetails)
                .build();

        User savedUser = this.save(user);
        List<String> roles = List.of("ROLE_ADMIN");

        userRoleService.assignRolesToUser(roles, savedUser);

        List<AdminInvitation> allInvites = adminInvitationRepository.findAllByEmail(requestDto.getEmail());
        for (AdminInvitation invite : allInvites) {
            if (invite.getStatus() == InvitationStatus.PENDING) {
                throw new InvitationAlreadySentException();
            } else if (invite.getStatus() == InvitationStatus.COMPLETED) {
                throw new InvitationAlreadySentException("User has already setup account");
            } else {
                adminInvitationRepository.delete(invite);
            }
        }

        String token = RandomStringGenerator.generateRandomString(128);
        AdminInvitation newInvitation = AdminInvitation.builder()
                .email(requestDto.getEmail())
                .roles("ROLE_ADMIN")
                .status(InvitationStatus.PENDING)
                .token(token)
                .invitedBy(this.getAuthenticatedUser())
                .updatedAt(LocalDateTime.now())
                .build();
        adminInvitationRepository.save(newInvitation);
        savedUser.setPassword(password);
        emailConfirmService.sendAdminInvite(savedUser, this.getAuthenticatedUser().getUserDetails().getFirstName());
        return mapper.map(newInvitation, AdminInvitationDto.class);
    }

    @Override
    public AdminTokenResponseDto loginAdmin(LoginRequestDto request) {
        try {
            log.info("AdminLoginRequestDto username is {}", request.getUsername());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword())
            );
            User foundUser = this.getUserByUsername(request.getUsername()).orElseThrow(() -> new InvalidLoginDetailsException("Invalid details provided"));
            if (foundUser.getRoles().stream().noneMatch(role -> role.getName().equals("ROLE_ADMIN"))) {
                throw new InvalidLoginDetailsException("Invalid details provided");
            }
            String token = jwtGenerator.generateToken(authentication);
            return AdminTokenResponseDto.builder()
                    .fullName(foundUser.getUserDetails().getFirstName() + "  " + foundUser.getUserDetails().getLastName())
                    .email(foundUser.getUsername())
                    .role(foundUser.getRoles().stream()
        .map(Role::getName)
        .collect(Collectors.joining(", ")))
                    .token(token).build();
        } catch (Exception e) {
            if (Objects.equals(e.getClass().getName(), "org.springframework.security.authentication.LockedException")) {
//                this.resendOtp(request.getUsername());
                throw new UnauthorizedRequestException("VERIFICATION_REQUIRED");
            }
            throw new InvalidLoginDetailsException();
        }
    }

    @Override
    public List<UserDto> getAllAdmins() {
        List<Role> roles = roleService.findAllRolesInList(List.of("ROLE_ADMIN"));
        List<User> allUsers = userRepository.findAllByRolesContaining(roles.get(0));
        Type pageDtoTypeToken = new TypeToken<List<UserDto>>() {
        }.getType();
        return mapper.map(allUsers, pageDtoTypeToken);
    }

    @Override
    public AdminInvitationDto resetAdminPassword(Long adminId) {
        User adminUser = getUserById(adminId);
        String password = RandomStringGenerator.generateRandomString(16);
        adminUser.setPassword(passwordEncoder.encode(password));
        this.save(adminUser);
        emailConfirmService.sendAdminInvite(adminUser, this.getAuthenticatedUser().getUserDetails().getFirstName());
        return new AdminInvitationDto();
    }

    @Override
    public GeneralMailResponse sendGeneralMail(GeneralMailRequest mailRequest) {
        List<UserDto> allCustomers = getAllUsers();
        emailConfirmService.sendGeneralEmail(allCustomers, mailRequest);
        return new GeneralMailResponse();
    }

    @Override
    public UserDetailsDto verifyUser(String email) {
        User user = getUserByUsername(email).orElseThrow(() -> new UserNotFoundException("User not found"));
        UserDetails userDetails = user.getUserDetails();
        userDetails.setVerified(true);
        user.setUserDetails(userDetails);
        userRepository.save(user);
        return mapper.map(userDetails, UserDetailsDto.class);
    }

}
