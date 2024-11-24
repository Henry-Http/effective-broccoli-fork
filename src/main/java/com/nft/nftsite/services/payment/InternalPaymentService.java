package com.nft.nftsite.services.payment;

import com.nft.nftsite.data.dtos.requests.payment.CreateDeposit;
import com.nft.nftsite.data.dtos.requests.payment.PaymentCardDto;
import com.nft.nftsite.data.dtos.requests.payment.PaymentRequestDto;
import com.nft.nftsite.data.dtos.responses.payment.DepositResponse;
import com.nft.nftsite.data.dtos.responses.payment.UserTransaction;
import java.util.List;

public interface InternalPaymentService {

    DepositResponse deposit(CreateDeposit request);

    List<PaymentRequestDto> getAllApprovedPayments();

    List<PaymentRequestDto> getAllPendingPayments();

    List<PaymentRequestDto> getAllFailedPayments();

    PaymentCardDto getPaymentCards();

    List<UserTransaction> getUserTransactionList();

    DepositResponse approvePayment(Long paymentId);

    DepositResponse declinePayment(Long paymentId);

    Double calculateTotal();

    long getPayments();

}
