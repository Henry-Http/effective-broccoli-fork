package com.nft.nftsite.controllers;


import com.nft.nftsite.data.dtos.requests.payment.CreateDeposit;
import com.nft.nftsite.data.dtos.requests.payment.PaymentCardDto;
import com.nft.nftsite.data.dtos.requests.payment.PaymentRequestDto;
import com.nft.nftsite.data.dtos.responses.payment.DepositResponse;
import com.nft.nftsite.data.dtos.responses.payment.UserTransaction;
import com.nft.nftsite.services.payment.InternalPaymentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@AllArgsConstructor
@Slf4j
public class PaymentController {

    private final InternalPaymentService paymentService;

    @PostMapping(value = "/deposit")
    @Operation(summary = "Deposit to Wallet", description = "Endpoint to call when user clicks Ive made the payment")
    public ResponseEntity<DepositResponse> receiveWebhook(@RequestBody CreateDeposit requestDto) {
        return new ResponseEntity<>(paymentService.deposit(requestDto), HttpStatus.OK);
    }

    @GetMapping("/approved")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Get all approved payments", description = "Get all payments that have been approved")
    public ResponseEntity<List<PaymentRequestDto>> getAllApprovedPayments() {
        return ResponseEntity.ok(paymentService.getAllApprovedPayments());
    }

    @GetMapping("/pending")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Get all pending payments", description = "Get all payments awaiting approval")
    public ResponseEntity<List<PaymentRequestDto>> getAllPendingPayments() {
        return ResponseEntity.ok(paymentService.getAllPendingPayments());
    }

    @GetMapping("/declined")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Get all declined payments", description = "Get all declined payments")
    public ResponseEntity<List<PaymentRequestDto>> getAllFailedPayments() {
        return ResponseEntity.ok(paymentService.getAllFailedPayments());
    }

    @GetMapping("/cards-data")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Get payments data insight", description = "Get cards data")
    public ResponseEntity<PaymentCardDto> getPaymentCards() {
        return ResponseEntity.ok(paymentService.getPaymentCards());
    }

    @GetMapping("/user-transaction-list")
    @Operation(summary = "Get list of transactions for a user")
    public ResponseEntity<List<UserTransaction>> getUserTransactionList() {
        return ResponseEntity.ok(paymentService.getUserTransactionList());
    }

    @PostMapping(value = "/approve/{paymentId}")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Approve payment request")
    public ResponseEntity<DepositResponse> approvePayment(@PathVariable Long paymentId) {
        return new ResponseEntity<>(paymentService.approvePayment(paymentId), HttpStatus.OK);
    }

    @PostMapping(value = "/decline/{paymentId}")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Decline payment request")
    public ResponseEntity<DepositResponse> declinePayment(@PathVariable Long paymentId) {
        return new ResponseEntity<>(paymentService.declinePayment(paymentId), HttpStatus.OK);
    }

}
