package com.nft.nftsite.controllers;


import com.nft.nftsite.data.dtos.requests.WithdrawalRequest;
import com.nft.nftsite.data.dtos.requests.payment.CreateDeposit;
import com.nft.nftsite.data.dtos.requests.payment.PaymentCardDto;
import com.nft.nftsite.data.dtos.requests.payment.PaymentRequestDto;
import com.nft.nftsite.data.dtos.responses.WithdrawalDto;
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
    public ResponseEntity<DepositResponse> depositFunds(@RequestBody CreateDeposit requestDto) {
        return new ResponseEntity<>(paymentService.deposit(requestDto), HttpStatus.OK);
    }

    @PostMapping(value = "/withdraw")
    @Operation(summary = "Withdraw from Wallet", description = "Endpoint to call when user enters amount, network and wallet address")
    public ResponseEntity<WithdrawalDto> withdrawFunds(@RequestBody WithdrawalRequest requestDto) {
        return new ResponseEntity<>(paymentService.withdraw(requestDto), HttpStatus.OK);
    }

    @GetMapping("/approved")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Get all approved payments", description = "Get all payments that have been approved")
    public ResponseEntity<List<PaymentRequestDto>> getAllApprovedPayments() {
        return ResponseEntity.ok(paymentService.getAllApprovedPayments());
    }

    @GetMapping("/withdrawal-requests")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Get all pending withdrawals", description = "Get all withdrawals awaiting completion")
    public ResponseEntity<List<WithdrawalDto>> getAllPendingWithdrawals() {
        return ResponseEntity.ok(paymentService.getAllPendingWithdrawals());
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

    @PostMapping(value = "/complete-withdrawal/{withdrawalId}")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Approve payment request")
    public ResponseEntity<WithdrawalDto> approveWithdrawal(@PathVariable Long withdrawalId) {
        return new ResponseEntity<>(paymentService.approveWithdrawal(withdrawalId), HttpStatus.OK);
    }

    @PostMapping(value = "/decline/{paymentId}")
    @Secured("ROLE_ADMIN")
    @Operation(summary = "Decline payment request")
    public ResponseEntity<DepositResponse> declinePayment(@PathVariable Long paymentId) {
        return new ResponseEntity<>(paymentService.declinePayment(paymentId), HttpStatus.OK);
    }

}
