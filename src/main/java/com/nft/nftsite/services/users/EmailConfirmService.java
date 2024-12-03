package com.nft.nftsite.services.users;


import com.nft.nftsite.data.dtos.requests.GeneralMailRequest;
import com.nft.nftsite.data.dtos.responses.PaymentDetails;
import com.nft.nftsite.data.dtos.responses.UserDto;
import com.nft.nftsite.data.models.enumerations.EmailConfirmType;
import com.nft.nftsite.data.models.EmailConfirm;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.models.enumerations.PaymentType;

import java.util.List;

public interface EmailConfirmService {

    void sendConfirmation(User user, EmailConfirmType type);

    void sendAdminInvite(User user, String inviterName);

//    void sendPaymentEmail(String email, String amount, PaymentType paymentType, PaymentDetails paymentDetails);

    void sendPaymentEmail(String email, String amount, PaymentType paymentType, PaymentDetails paymentDetails, String firstName);

    EmailConfirm retrieveByToken(String token);

    void revokeToken(String token);

    void resendOtp(User user);

    void sendPaymentRequestEmail(List<UserDto> admins, String amount);

    void sendGeneralEmail(List<UserDto> allCustomers, GeneralMailRequest mailRequest);
}
