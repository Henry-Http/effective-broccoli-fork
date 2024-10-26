package com.nft.nftsite.exceptions;

import org.springframework.http.HttpStatus;

public class FileTypeNotAcceptableException extends NFTSiteException {

    public FileTypeNotAcceptableException() {
        this("File type not acceptable");
    }

    public FileTypeNotAcceptableException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
