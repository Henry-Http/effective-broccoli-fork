package com.nft.nftsite.services.users;

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
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import java.time.LocalDateTime;
import java.util.List;

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

    @Override
    public void sendPaymentEmail(String email, String amount, PaymentType paymentType) {
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("amount", amount);

        String subject = switch (paymentType) {
            case APPROVAL -> "Payment Approved!";
            case DECLINE -> "Payment Declined!";
            case REQUEST -> "New Payment Request";
        };

        String template = switch (paymentType) {
            case APPROVAL -> "payment-approved";
            case DECLINE -> "payment-declined";
            case REQUEST -> "payment-request";
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
    public void sendPaymentRequestEmail(String amount) {
        List<String> emails = List.of(
                "patrick.okafor@pallettex.com"
        );
        for (String email : emails) {
            sendPaymentEmail(email, amount, PaymentType.REQUEST);
        }
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
