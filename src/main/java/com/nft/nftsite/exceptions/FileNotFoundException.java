package com.nft.nftsite.exceptions;

import org.springframework.http.HttpStatus;

public class FileNotFoundException extends NFTSiteException {

    public FileNotFoundException() {
        this("File not found");
    }

    public FileNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
