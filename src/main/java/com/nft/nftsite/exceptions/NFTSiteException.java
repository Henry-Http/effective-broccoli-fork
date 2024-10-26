package com.nft.nftsite.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Getter
public class NFTSiteException extends RuntimeException {

    private final HttpStatus status;
    private final Map<String, String> data;

    public NFTSiteException() {
        this("An error occurred");
    }

    public NFTSiteException(String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public NFTSiteException(String message, HttpStatus status) {
        this(message, status, null);
    }

    public NFTSiteException(String message, HttpStatus status, Map<String, String> data) {
        super(message);
        this.status = status;
        this.data = data;
    }
}
