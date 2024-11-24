package com.nft.nftsite.services.users;

import com.nft.nftsite.data.dtos.requests.UpdateUserDetailsDto;
import com.nft.nftsite.data.dtos.responses.UserDetailsDto;
import com.nft.nftsite.data.dtos.responses.payment.DepositResponse;
import com.nft.nftsite.data.models.Image;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.models.UserDetails;
import com.nft.nftsite.data.repository.UserDetailsRepository;
import com.nft.nftsite.exceptions.FileTypeNotAcceptableException;
import com.nft.nftsite.exceptions.UsernameAlreadyUsedException;
import com.nft.nftsite.services.cloudinaryImage.ImageService;
import com.nft.nftsite.services.email.EmailService;
import com.nft.nftsite.utils.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@AllArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final SpringTemplateEngine templateEngine;
    private final ImageService imageService;
    private final ModelMapper modelMapper;

    @Override
    public UserDetailsDto getUserDetails() {
        User authUser = userService.getAuthenticatedUser();
        UserDetails userDetails = authUser.getUserDetails();

        if (userDetails == null) {
            return UserDetailsDto.builder().build();
        } else {
            UserDetailsDto userDetailsDto = modelMapper.map(userDetails, UserDetailsDto.class);
            userDetailsDto.setThirdPartySignIn(authUser.isThirdPartySignIn());
            userDetailsDto.setThirdPartySignInType(authUser.getThirdPartySignInType());
            return userDetailsDto;
        }
    }

    @Override
    public UserDetailsDto getUserDetailsDto(User user) {
        return UserDetailsDto.builder()
                .id(user.getId())
                .firstName(user.getUserDetails().getFirstName())
                .lastName(user.getUserDetails().getLastName())
                .tag(user.getUserDetails().getTag())
                .build();
    }

    @Override
    public UserDetailsDto updateUserDetails(UpdateUserDetailsDto updateUserDetailsDto) {
        User authUser = userService.getAuthenticatedUser();
        UserDetails userDetails = authUser.getUserDetails();

        String initialFirstName = userDetails.getFirstName();

        if (userDetailsRepository.existsByTagIgnoreCaseAndIdNot(updateUserDetailsDto.getTag(), userDetails.getId()))
            throw new UsernameAlreadyUsedException("Tag not available");

        userDetails.setFirstName(updateUserDetailsDto.getFirstName());
        userDetails.setLastName(updateUserDetailsDto.getLastName());
        userDetails.setTag(updateUserDetailsDto.getTag());
        userDetails.setGender(updateUserDetailsDto.getGender());
        userDetails.setEmailAddress(authUser.getUsername());

        UserDetails savedUserDetails = userDetailsRepository.save(userDetails);
        if (!StringUtils.hasText(initialFirstName)) {
            sendWelcomeEmail(savedUserDetails);
        }
        return modelMapper.map(savedUserDetails, UserDetailsDto.class);
    }

    @Override
    public UserDetailsDto uploadDisplayPicture(MultipartFile picture) {
        log.info("Uploading display picture: {}", picture.getContentType());

        if (!ValidationUtil.isValidImage(picture)) throw new FileTypeNotAcceptableException();

        User authUser = userService.getAuthenticatedUser();
        UserDetails userDetails = authUser.getUserDetails();
        Image image = imageService.uploadNewImage(picture);
        userDetails.setDisplayPicture(image);
        return modelMapper.map(userDetailsRepository.save(userDetails), UserDetailsDto.class);
    }

    @Override
    public DepositResponse getUserBalance() {
        User authUser = userService.getAuthenticatedUser();
        UserDetails userDetails = authUser.getUserDetails();
        return DepositResponse.builder()
                .amount(userDetails.getBalance())
                .status(null)
                .build();
    }

    @Override
    public void deductBalance(double amount) {
        User authUser = userService.getAuthenticatedUser();
        UserDetails userDetails = authUser.getUserDetails();
        userDetails.setBalance(userDetails.getBalance() - amount);
        userDetailsRepository.save(userDetails);
    }

    private void sendWelcomeEmail(UserDetails userDetails) {
        Context context = new Context();
        context.setVariable("firstName", userDetails.getFirstName());

        String htmlContent = templateEngine.process("welcome", context);
        log.info("Email content ready for sending to {}", userDetails.getEmailAddress());
        emailService.sendEmail(userDetails.getEmailAddress(), "Welcome to NFTSite", htmlContent);
    }

}
