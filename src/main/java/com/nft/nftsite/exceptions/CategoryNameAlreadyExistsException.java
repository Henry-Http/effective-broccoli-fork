package com.nft.nftsite.exceptions;

import org.springframework.http.HttpStatus;

public class CategoryNameAlreadyExistsException extends NFTSiteException {

    public CategoryNameAlreadyExistsException() {
        this("A category with this name already exists");
    }

    public CategoryNameAlreadyExistsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

}
