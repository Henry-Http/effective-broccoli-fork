package com.nft.nftsite.controllers;

import com.nft.nftsite.data.dtos.responses.BuyNftResponse;
import com.nft.nftsite.services.payment.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/checkout")
@AllArgsConstructor
@Slf4j
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping(value = "/buy-now/{nftId}")
    @Operation(summary = "Buy an NFT")
    public ResponseEntity<BuyNftResponse> buyNft(@PathVariable Long nftId) {
//        return new ResponseEntity<>(checkoutService.buyNftNow(nftId), HttpStatus.OK);
        return null;
    }

}
