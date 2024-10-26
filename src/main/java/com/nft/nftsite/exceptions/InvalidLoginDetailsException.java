package com.nft.nftsite.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidLoginDetailsException extends NFTSiteException {

    public InvalidLoginDetailsException() {
        this("Invalid login details");
    }

    public InvalidLoginDetailsException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}
