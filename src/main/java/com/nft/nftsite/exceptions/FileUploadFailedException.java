package com.nft.nftsite.exceptions;

import org.springframework.http.HttpStatus;

public class FileUploadFailedException extends NFTSiteException {

    public FileUploadFailedException() {
        this("File upload failed");
    }

    public FileUploadFailedException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
