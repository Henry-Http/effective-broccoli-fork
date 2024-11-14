package com.nft.nftsite.services.users;


import com.nft.nftsite.data.models.enumerations.EmailConfirmType;
import com.nft.nftsite.data.models.EmailConfirm;
import com.nft.nftsite.data.models.User;

public interface EmailConfirmService {

    void sendConfirmation(User user, EmailConfirmType type);

    void sendAdminInvite(User user, String inviterName);

    EmailConfirm retrieveByToken(String token);

    void revokeToken(String token);

    void resendOtp(User user);

}
