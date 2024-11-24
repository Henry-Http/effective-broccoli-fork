package com.nft.nftsite.services.payment;

import com.nft.nftsite.data.dtos.requests.payment.CreateDeposit;
import com.nft.nftsite.data.dtos.requests.payment.PaymentCardDto;
import com.nft.nftsite.data.dtos.requests.payment.PaymentRequestDto;
import com.nft.nftsite.data.dtos.responses.payment.DepositResponse;
import com.nft.nftsite.data.dtos.responses.payment.UserTransaction;
import com.nft.nftsite.data.models.InternalPayments;
import com.nft.nftsite.data.models.Transaction;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.models.UserDetails;
import com.nft.nftsite.data.models.enumerations.InternalPaymentStatus;
import com.nft.nftsite.data.models.enumerations.PaymentType;
import com.nft.nftsite.data.repository.InternalPaymentRepository;
import com.nft.nftsite.data.repository.TransactionRepository;
import com.nft.nftsite.exceptions.NFTSiteException;
import com.nft.nftsite.services.users.EmailConfirmService;
import com.nft.nftsite.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements InternalPaymentService{

    private final InternalPaymentRepository paymentRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final TransactionRepository transactionRepository;
    private final EmailConfirmService emailConfirmService;

    @Override
    public DepositResponse deposit(CreateDeposit request) {
        User user = userService.getUserById(userService.getAuthenticatedUser().getId());
        InternalPayments payment = new InternalPayments();
        payment.setAmount(request.getAmount());
        payment.setUser(user);
        payment.setStatus(InternalPaymentStatus.PENDING);
        payment = paymentRepository.save(payment);
        emailConfirmService.sendPaymentRequestEmail(payment.getAmount().toString());
        return DepositResponse.builder().amount(payment.getAmount()).status(payment.getStatus()).build();
    }

    @Override
    public List<PaymentRequestDto> getAllApprovedPayments() {
        List<InternalPayments> pendingPayments = paymentRepository.findAllByStatus(InternalPaymentStatus.APPROVED);
        Type listType = new TypeToken<List<PaymentRequestDto>>() {
        }.getType();
        return modelMapper.map(pendingPayments, listType);
    }

    @Override
    public List<PaymentRequestDto> getAllPendingPayments() {
        List<InternalPayments> pendingPayments = paymentRepository.findAllByStatus(InternalPaymentStatus.PENDING);
        Type listType = new TypeToken<List<PaymentRequestDto>>() {
        }.getType();
        return modelMapper.map(pendingPayments, listType);
    }

    @Override
    public List<PaymentRequestDto> getAllFailedPayments() {
        List<InternalPayments> pendingPayments = paymentRepository.findAllByStatus(InternalPaymentStatus.DECLINED);
        Type listType = new TypeToken<List<PaymentRequestDto>>() {
        }.getType();
        return modelMapper.map(pendingPayments, listType);
    }

    @Override
    public PaymentCardDto getPaymentCards() {
        List<PaymentRequestDto> pending = getAllPendingPayments();
        List<PaymentRequestDto> approved = getAllPendingPayments();

        Double pendingTotal = pending.stream().mapToDouble(PaymentRequestDto::getAmount).sum();
        Double approvedTotal = approved.stream().mapToDouble(PaymentRequestDto::getAmount).sum();

        return PaymentCardDto.builder()
                .approvedPayments(approvedTotal)
                .pendingPayments(pendingTotal)
                .totalAmountPaid(approvedTotal + pendingTotal)
                .build();
    }

    @Override
    public List<UserTransaction> getUserTransactionList() {
        User user = userService.getUserById(userService.getAuthenticatedUser().getId());
        List<Transaction> userTransactions = transactionRepository.findAllByUser(user);
        Type listType = new TypeToken<List<UserTransaction>>() {
        }.getType();
        return modelMapper.map(userTransactions, listType);
    }

    @Override
    public DepositResponse approvePayment(Long paymentId) {
        InternalPayments payment = paymentRepository.findById(paymentId).orElseThrow(() -> new NFTSiteException("Payment not found", HttpStatus.NOT_FOUND));
        User user = userService.getUserById(payment.getUser().getId());
        UserDetails userDetails = user.getUserDetails();
        userDetails.setBalance(userDetails.getBalance() + payment.getAmount());
        user.setUserDetails(userDetails);
        userService.save(user);
        payment.setStatus(InternalPaymentStatus.APPROVED);
        paymentRepository.save(payment);
        emailConfirmService.sendPaymentEmail(user.getUsername(), payment.getAmount().toString(), PaymentType.REQUEST);
        return DepositResponse.builder().amount(payment.getAmount()).status(payment.getStatus()).build();
    }

    @Override
    public DepositResponse declinePayment(Long paymentId) {
        InternalPayments payment = paymentRepository.findById(paymentId).orElseThrow(() -> new NFTSiteException("Payment not found", HttpStatus.NOT_FOUND));
        payment.setStatus(InternalPaymentStatus.DECLINED);
        paymentRepository.save(payment);
        User user = userService.getUserById(payment.getUser().getId());
        emailConfirmService.sendPaymentEmail(user.getUsername(), payment.getAmount().toString(), PaymentType.REQUEST);
        return DepositResponse.builder().amount(payment.getAmount()).status(payment.getStatus()).build();
    }

}
