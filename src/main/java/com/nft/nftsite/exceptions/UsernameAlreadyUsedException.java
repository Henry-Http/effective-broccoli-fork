package com.nft.nftsite.exceptions;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyUsedException extends NFTSiteException {

    public UsernameAlreadyUsedException() {
        this("Email already used");
    }

    public UsernameAlreadyUsedException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
