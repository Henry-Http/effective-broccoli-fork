package com.nft.nftsite.services.users;

import com.nft.nftsite.data.dtos.requests.GeneralMailRequest;
import com.nft.nftsite.data.dtos.responses.PaymentDetails;
import com.nft.nftsite.data.dtos.responses.UserDto;
import com.nft.nftsite.data.models.enumerations.EmailConfirmType;
import com.nft.nftsite.data.models.enumerations.PaymentType;
import com.nft.nftsite.exceptions.UnauthorizedRequestException;
import com.nft.nftsite.data.models.EmailConfirm;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.services.email.EmailService;
import com.nft.nftsite.data.repository.EmailConfirmRepository;
import com.nft.nftsite.utils.RandomStringGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import com.google.common.collect.Lists;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailConfirmServiceImpl implements EmailConfirmService {

    private final EmailConfirmRepository emailConfirmRepository;
    private final EmailService emailService;
    private final SpringTemplateEngine templateEngine;

    @Value("${default_password_reset_client_url}")
    private String resetPasswordUrl;

    @Override
    public void sendConfirmation(User user, EmailConfirmType type) {
        EmailConfirm token = this.generateToken(user, type);

        handleConfirmationSend(token);
    }

    @Override
    public void sendAdminInvite(User user, String inviterName) {
        Context context = new Context();
        context.setVariable("email", user.getUsername());
        context.setVariable("inviterName", inviterName);
        context.setVariable("firstName", user.getUserDetails().getFirstName());
        context.setVariable("password", user.getPassword());

        String htmlContent = templateEngine.process("admin-invite", context);
        log.info("Email content ready for sending -INVITE- to {}", user.getUsername());
        emailService.sendEmail(user.getUsername(), "Invite Admin", htmlContent);
    }

//    @Override
    public void sendPaymentEmail(String email, String amount, PaymentType paymentType, PaymentDetails paymentDetails) {
        Context context = new Context();
        context.setVariable("email", email);

        if (paymentDetails != null) {
            context.setVariable("nftName", paymentDetails.getNftName());
            context.setVariable("amount", paymentDetails.getAmount());
        } else {
            context.setVariable("amount", amount.split("---")[0]);
            context.setVariable("paymentId", amount.split("---")[1]);
        }

        if (paymentType == PaymentType.DECLINE) {
            context.setVariable("paymentWhat", "Declined");
        }
        if (paymentType == PaymentType.REFUND) {
            context.setVariable("paymentWhat", "Refunded");
        }

        String subject = switch (paymentType) {
            case APPROVAL -> "Payment Approved!";
            case DECLINE, REFUND -> "Payment Declined!";
            case REQUEST, USER_REQUEST -> "New Payment Request";
            case USER_PURCHASE -> "New NFT Item in Collection!";
            case USER_SALE -> "New Sale!";
        };

        String template = switch (paymentType) {
            case APPROVAL -> "payment-approved";
            case DECLINE, REFUND -> "payment-declined";
            case REQUEST -> "payment-request";
            case USER_PURCHASE -> "new-purchase";
            case USER_SALE -> "new-sale";
            case USER_REQUEST -> "payment-pending";
        };

        String htmlContent = templateEngine.process(template, context);
        emailService.sendEmail(email, subject, htmlContent);
    }

    @Override
    public void sendPaymentEmail(String email, String amount, PaymentType paymentType, PaymentDetails paymentDetails, String firstName) {
        Context context = new Context();
        context.setVariable("email", firstName);

        if (paymentDetails != null) {
            context.setVariable("nftName", paymentDetails.getNftName());
            context.setVariable("amount", paymentDetails.getAmount());
        } else {
            context.setVariable("amount", amount.split("---")[0]);
            context.setVariable("paymentId", amount.split("---")[1]);
        }

        if (paymentType == PaymentType.DECLINE) {
            context.setVariable("paymentWhat", "Declined");
        }
        if (paymentType == PaymentType.REFUND) {
            context.setVariable("paymentWhat", "Refunded");
        }

        String subject = switch (paymentType) {
            case APPROVAL -> "Payment Approved!";
            case DECLINE, REFUND -> "Payment Declined!";
            case REQUEST, USER_REQUEST -> "New Payment Request";
            case USER_PURCHASE -> "New NFT Item in Collection!";
            case USER_SALE -> "New Sale!";
        };

        String template = switch (paymentType) {
            case APPROVAL -> "payment-approved";
            case DECLINE, REFUND -> "payment-declined";
            case REQUEST -> "payment-request";
            case USER_PURCHASE -> "new-purchase";
            case USER_SALE -> "new-sale";
            case USER_REQUEST -> "payment-pending";
        };

        String htmlContent = templateEngine.process(template, context);
        emailService.sendEmail(email, subject, htmlContent);
    }

    @Override
    public EmailConfirm retrieveByToken(String token) {
        return emailConfirmRepository.findByToken(token)
                .orElseThrow(UnauthorizedRequestException::new);
    }

    @Override
    public void revokeToken(String token) {
        EmailConfirm entry = this.retrieveByToken(token);
        entry.setExpired(true);
        emailConfirmRepository.save(entry);
    }

    @Override
    public void resendOtp(User user) {
        List<EmailConfirm> pendingTokens = emailConfirmRepository
                .findAllByUserAndExpiredAndCreatedAtIsAfter(user, Boolean.FALSE, LocalDateTime.now().minusHours(2));

        if (!pendingTokens.isEmpty()) {
            EmailConfirm token = pendingTokens.get(0);
            handleConfirmationSend(token);
        } else {
            this.sendConfirmation(user, EmailConfirmType.ACTIVATION);
        }
    }

    @Override
    public void sendPaymentRequestEmail(List<UserDto> admins, String amount) {
        admins.forEach(user -> {
            if (!user.getUsername().equals("deolaaxo@gmail.com")) {
                sendPaymentEmail(user.getUsername(), amount, PaymentType.REQUEST, null, user.getUserDetails().getFirstName());
            }
        });
    }

    public void sendGeneralEmail(List<UserDto> allCustomers, GeneralMailRequest mailRequest) {
        List<List<UserDto>> batches = Lists.partition(allCustomers, 50);
        batches.forEach(batch -> CompletableFuture.runAsync(() -> processBatch(batch, mailRequest)));
    }

    @Async
    public void processBatch(List<UserDto> batch, GeneralMailRequest mailRequest) {
        batch.forEach(user -> {
            try {
                Context context = new Context();
                context.setVariable("email", user.getUsername());
                context.setVariable("firstName", user.getUserDetails().getFirstName());
                context.setVariable("theBody", mailRequest.getBody());
                context.setVariable("mailTitle", mailRequest.getTitle());

                String htmlContent = templateEngine.process("general-email", context);
                emailService.sendEmail(user.getUsername(), mailRequest.getSubject(), htmlContent);

                log.info("Email sent to: {}", user.getUsername());
            } catch (Exception e) {
                log.error("Failed to send email to: {}", user.getUsername(), e);
            }
        });
    }

    private EmailConfirm generateToken(User user, EmailConfirmType type) {
        List<EmailConfirm> alreadyExistingEntries = emailConfirmRepository.findAllByUser(user);
        log.info("Resetting all existing entries, found {}", alreadyExistingEntries.size());

        alreadyExistingEntries = alreadyExistingEntries.stream()
                .peek((entry) -> entry.setExpired(true))
                .toList();

        emailConfirmRepository.saveAll(alreadyExistingEntries);
        log.info("Done resetting already existing entries");

        String token = switch (type) {
            case ACTIVATION -> RandomStringGenerator.generateRandomString(6, true);
            case PASSWORD_RESET -> RandomStringGenerator.generateRandomString(128);
            case ADMIN_INVITATION -> "";
        };

        log.info("Token generated for email confirmation :: {} || Type :: {}", user.getUsername(), type);

        EmailConfirm confirm = EmailConfirm.builder()
                .token(token)
                .email(user.getUsername())
                .type(type)
                .expired(false)
                .user(user)
                .build();

        return emailConfirmRepository.save(confirm);
    }


    private void handleConfirmationSend(EmailConfirm token) {

        log.info("{}", token.getType());

        switch (token.getType()) {
            case ACTIVATION: {
                Context context = new Context();
                context.setVariable("email", token.getEmail());
                context.setVariable("token", token.getToken());

                String htmlContent = templateEngine.process("confirm-email", context);
                log.info("Email content ready for sending -ACTIVATION- to {}", token.getEmail());
                emailService.sendEmail(token.getEmail(), "Confirm your email", htmlContent);
                break;
            }

            case PASSWORD_RESET: {
                Context context = new Context();
                context.setVariable("email", token.getEmail());
                context.setVariable("link", String.format("%s%s", resetPasswordUrl, token.getToken()));

                String htmlContent = templateEngine.process("reset-password", context);
                log.info("Email content ready for sending -RESET- to {}", token.getEmail());
                emailService.sendEmail(token.getEmail(), "Reset Password", htmlContent);
                break;
            }
        }
    }
}
