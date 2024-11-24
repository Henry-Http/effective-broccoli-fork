package com.nft.nftsite.services.payment;

import com.nft.nftsite.data.dtos.requests.payment.CreateDeposit;
import com.nft.nftsite.data.dtos.requests.payment.PaymentCardDto;
import com.nft.nftsite.data.dtos.requests.payment.PaymentRequestDto;
import com.nft.nftsite.data.dtos.responses.payment.DepositResponse;
import com.nft.nftsite.data.dtos.responses.payment.UserTransaction;
import com.nft.nftsite.data.models.InternalPayments;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.models.enumerations.InternalPaymentStatus;
import com.nft.nftsite.data.repository.InternalPaymentRepository;
import com.nft.nftsite.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PaymentServiceImpl implements InternalPaymentService{

    private final InternalPaymentRepository paymentRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    public DepositResponse deposit(CreateDeposit request) {
        User user = userService.getUserById(userService.getAuthenticatedUser().getId());
        InternalPayments payment = new InternalPayments();
        payment.setAmount(request.getAmount());
        payment.setUser(user);
        payment.setStatus(InternalPaymentStatus.PENDING);
        payment = paymentRepository.save(payment);
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
        return null;
    }

    @Override
    public List<UserTransaction> getUserTransactionList() {
        return List.of();
    }

}
