package com.nft.nftsite.controllers;

import com.nft.nftsite.data.dtos.requests.payment.HelioWebhookPayload;
import com.nft.nftsite.data.dtos.responses.payment.BeginCheckoutResponse;
import com.nft.nftsite.data.dtos.responses.payment.WebhookResponse;
import com.nft.nftsite.services.payment.CheckoutService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/helio")
@AllArgsConstructor
@Slf4j
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping(value = "/webhook")
    @Operation(summary = "Helio Webhook")
    public ResponseEntity<WebhookResponse> receiveWebhook(@RequestBody HelioWebhookPayload webhookPayload) {
        return new ResponseEntity<>(checkoutService.receiveWebhook(webhookPayload), HttpStatus.OK);
    }

    @PostMapping(value = "/buy-now/{nftId}")
    @Operation(summary = "Buy an NFT")
    public ResponseEntity<BeginCheckoutResponse> receiveWebhook(@PathVariable Long nftId) {
        return new ResponseEntity<>(checkoutService.buyNftNow(nftId), HttpStatus.OK);
    }

    @GetMapping("/payments")
    @Operation(summary = "Get all Payments")
    public ResponseEntity<List<WebhookResponse>> getAllNfts() {
        return new ResponseEntity<>(checkoutService.getPayments(), HttpStatus.OK);
    }

}
