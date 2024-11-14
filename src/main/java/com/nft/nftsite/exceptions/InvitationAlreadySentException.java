package com.nft.nftsite.exceptions;

import org.springframework.http.HttpStatus;

public class InvitationAlreadySentException extends NFTSiteException {

    public InvitationAlreadySentException() {
        this("Invitation Already Sent to this user!");
    }

    public InvitationAlreadySentException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
