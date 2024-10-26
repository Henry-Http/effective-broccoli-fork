package com.nft.nftsite.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedRequestException extends NFTSiteException {

    public UnauthorizedRequestException() {
        this("Unauthorized request");
    }

    public UnauthorizedRequestException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

}
