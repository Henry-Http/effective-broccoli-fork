package com.nft.nftsite.exceptions;

import org.springframework.http.HttpStatus;

import java.util.Map;

public class UnauthorizedRequestException extends NFTSiteException {

    public UnauthorizedRequestException() {
        this("Unauthorized request");
    }

    public UnauthorizedRequestException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedRequestException(String message, Map<String, String> token) {
        super(message, HttpStatus.UNAUTHORIZED, token);
    }

}
