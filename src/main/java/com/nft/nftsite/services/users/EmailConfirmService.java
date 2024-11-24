package com.nft.nftsite.services.users;


import com.nft.nftsite.data.dtos.responses.PaymentDetails;
import com.nft.nftsite.data.models.enumerations.EmailConfirmType;
import com.nft.nftsite.data.models.EmailConfirm;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.models.enumerations.PaymentType;

public interface EmailConfirmService {

    void sendConfirmation(User user, EmailConfirmType type);

    void sendAdminInvite(User user, String inviterName);

    void sendPaymentEmail(String email, String amount, PaymentType paymentType, PaymentDetails paymentDetails);

    EmailConfirm retrieveByToken(String token);

    void revokeToken(String token);

    void resendOtp(User user);

    void sendPaymentRequestEmail(String amount);
}
