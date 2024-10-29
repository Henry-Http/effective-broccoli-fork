package com.nft.nftsite.exceptions;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends NFTSiteException {

    public CategoryNotFoundException() {
        this("Category Not Found!");
    }

    public CategoryNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

}
