package com.nft.nftsite.exceptions;

import org.springframework.http.HttpStatus;

public class ServiceNotImplementedException extends NFTSiteException {

    public ServiceNotImplementedException() {
        this("Service not implemented");
    }

    public ServiceNotImplementedException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
