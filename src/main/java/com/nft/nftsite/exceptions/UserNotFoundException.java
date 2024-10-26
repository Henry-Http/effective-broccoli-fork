package com.nft.nftsite.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends NFTSiteException {
    public UserNotFoundException() {
        this("User not found");
    }

    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
