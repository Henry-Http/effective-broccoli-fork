package com.nft.nftsite.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@Builder
public class NFTSiteExceptionResponse {

    private String message;

    private HttpStatus status;

    private Map<String, String> data;

}
