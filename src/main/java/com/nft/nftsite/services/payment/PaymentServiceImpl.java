package com.nft.nftsite.services.payment;

import com.nft.nftsite.data.dtos.requests.WithdrawalRequest;
import com.nft.nftsite.data.dtos.requests.payment.CreateDeposit;
import com.nft.nftsite.data.dtos.requests.payment.PaymentCardDto;
import com.nft.nftsite.data.dtos.requests.payment.PaymentRequestDto;
import com.nft.nftsite.data.dtos.responses.WithdrawalDto;
import com.nft.nftsite.data.dtos.responses.payment.DepositResponse;
import com.nft.nftsite.data.dtos.responses.payment.UserTransaction;
import com.nft.nftsite.data.models.InternalPayments;
import com.nft.nftsite.data.models.Transaction;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.models.UserDetails;
import com.nft.nftsite.data.models.enumerations.InternalPaymentStatus;
import com.nft.nftsite.data.models.enumerations.PaymentType;
import com.nft.nftsite.data.models.enumerations.TransactionType;
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
        emailConfirmService.sendPaymentRequestEmail(userService.getAllAdmins(), payment.getAmount() + "---" + payment.getId());
        emailConfirmService.sendPaymentEmail(user.getUsername(), payment.getAmount() + "---" + payment.getId(), PaymentType.USER_REQUEST, null);
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
        List<PaymentRequestDto> declined = getAllFailedPayments();

        Double pendingTotal = pending.stream().mapToDouble(PaymentRequestDto::getAmount).sum();
        Double declinedTotal = declined.stream().mapToDouble(PaymentRequestDto::getAmount).sum();

        return PaymentCardDto.builder()
                .declinedPayments(declinedTotal)
                .pendingPayments(pendingTotal)
                .totalTransactions(transactionRepository.findAll().size())
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
        Transaction transaction = new Transaction();
        transaction.setAmount(payment.getAmount());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setDebitOrCreditStatus(TransactionType.CREDIT);
        transaction.setUser(user);
        transactionRepository.save(transaction);
        payment.setStatus(InternalPaymentStatus.APPROVED);
        payment = paymentRepository.save(payment);
        emailConfirmService.sendPaymentEmail(user.getUsername(), payment.getAmount() + "---" + payment.getId(), PaymentType.APPROVAL, null);
        return DepositResponse.builder().amount(payment.getAmount()).status(payment.getStatus()).build();
    }

    @Override
    public DepositResponse declinePayment(Long paymentId) {
        InternalPayments payment = paymentRepository.findById(paymentId).orElseThrow(() -> new NFTSiteException("Payment not found", HttpStatus.NOT_FOUND));
        if (payment.getStatus() == InternalPaymentStatus.APPROVED) {
            throw new NFTSiteException("Payment has already been approved", HttpStatus.BAD_REQUEST);
        }
        payment.setStatus(InternalPaymentStatus.DECLINED);
        payment = paymentRepository.save(payment);
        User user = userService.getUserById(payment.getUser().getId());
        emailConfirmService.sendPaymentEmail(user.getUsername(), payment.getAmount() + "---" + payment.getId(), PaymentType.DECLINE, null);
        return DepositResponse.builder().amount(payment.getAmount()).status(payment.getStatus()).build();
    }

    @Override
    public Double calculateTotal() {
        Double sum = 0.0;
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction: transactions) {
            if (transaction.getTransactionType() == TransactionType.GAS_FEE_REMOVAL) {
                sum += (transaction.getAmount() * (double) -1);
            }
        }
        return sum;
    }

    @Override
    public long getPayments() {
        long sum = 0L;
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction: transactions) {
            if (transaction.getTransactionType() == TransactionType.PURCHASE) {
                sum += 1;
            }
        }
        return sum;
    }

    @Override
    public WithdrawalDto withdraw(WithdrawalRequest requestDto) {
        return null;
    }

    @Override
    public WithdrawalDto approveWithdrawal(Long withdrawalId) {
        return null;
    }

    @Override
    public List<WithdrawalDto> getAllPendingWithdrawals() {
        return List.of();
    }

}
