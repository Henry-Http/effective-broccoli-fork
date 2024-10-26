package com.nft.nftsite.services.email;

public interface EmailService {

    void sendEmail(String to, String subject, String htmlContent);

}
