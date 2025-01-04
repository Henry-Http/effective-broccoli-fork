package com.nft.nftsite.services.payment;


import com.nft.nftsite.data.dtos.requests.payment.*;
import com.nft.nftsite.data.dtos.responses.BuyNftResponse;
import com.nft.nftsite.data.dtos.responses.NftResponse;
import com.nft.nftsite.data.dtos.responses.PaymentDetails;
import com.nft.nftsite.data.dtos.responses.UserDetailsDto;
import com.nft.nftsite.data.dtos.responses.payment.AdditionalInfoJson;
import com.nft.nftsite.data.dtos.responses.payment.BeginCheckoutResponse;
import com.nft.nftsite.data.dtos.responses.payment.WebhookResponse;
import com.nft.nftsite.data.models.*;
import com.nft.nftsite.data.models.enumerations.BalanceType;
import com.nft.nftsite.data.models.enumerations.NftStatus;
import com.nft.nftsite.data.models.enumerations.PaymentType;
import com.nft.nftsite.data.models.enumerations.TransactionType;
import com.nft.nftsite.data.repository.PaymentRepository;
import com.nft.nftsite.data.repository.TransactionRepository;
import com.nft.nftsite.exceptions.NFTSiteException;
import com.nft.nftsite.exceptions.UserNotFoundException;
import com.nft.nftsite.services.nft.NftService;
import com.nft.nftsite.services.users.EmailConfirmService;
import com.nft.nftsite.services.users.UserDetailsService;
import com.nft.nftsite.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckoutServiceImpl implements CheckoutService{

    private final NftService nftService;
    private final UserService userService;
    private final RestClient restClient;
    private final PaymentRepository paymentRepository;
    private final UserDetailsService userDetailsService;
    private final TransactionRepository transactionRepository;
    private final EmailConfirmService emailConfirmService;

    @Value("${helio.public.key}")
    private String apiKey;

    @Value("${helio.secret.key}")
    private String secretKey;

    @Value("${helio.pay-link.id}")
    private String payLinkId;

    @Value("${helio.url}")
    private String helioUrl;

    public WebhookResponse receiveWebhook(HelioWebhookPayload webhookPayload) {
        TransactionObject object = webhookPayload.getTransactionObject();
        TransactionMetaObject metaObject = object.getMeta();
        TransactionCustomerDetails customerDetails = metaObject.getCustomerDetails();
        JSONObject jsonObject = new JSONObject(customerDetails.getAdditionalJSON());
        String customerId = jsonObject.getString("customerId");
        String nftId = jsonObject.getString("nftId");
        User user = userService.getUserById(Long.valueOf(customerId));
        NftResponse nftItem = nftService.findById(Long.valueOf(nftId));
        Payments newPayment = new Payments();
        newPayment.setId(object.getId());
        newPayment.setPayLinkId(object.getPaylinkId());
        newPayment.setCreatedAt(object.getCreatedAt());
        newPayment.setAmountPaid(Double.valueOf(metaObject.getTotalAmount()));
        newPayment.setPayer(user);
        newPayment.setNftId(Long.valueOf(nftId));
        newPayment.setPaymentStatus(metaObject.getTransactionStatus());
        newPayment.setTransactionSignature(metaObject.getTransactionSignature());
        newPayment.setPaymentEmail(customerDetails.getEmail());
        newPayment.setPaymentFullName(customerDetails.getFullName());
        newPayment.setDeliveryAddress(customerDetails.getDeliveryAddress());
        newPayment.setAdditionalJson(customerDetails.getAdditionalJSON());
        newPayment.setItemPrice(nftItem.getStartingPrice());
        newPayment = paymentRepository.save(newPayment);
        return WebhookResponse.builder()
                .paymentId(newPayment.getId())
                .paymentEmail(newPayment.getPaymentEmail())
                .amountPaid(newPayment.getAmountPaid())
                .payLinkId(newPayment.getPayLinkId())
                .createdAt(newPayment.getCreatedAt())
                .deliveryAddress(newPayment.getDeliveryAddress())
                .itemPrice(newPayment.getItemPrice())
                .userId(newPayment.getPayer().getId())
                .build();
    }

    public BeginCheckoutResponse buyNftNow(Long nftId) {
        NftResponse nftItem = nftService.findById(nftId);
        User currentUser = userService.getAuthenticatedUser(true);
        AdditionalInfoJson additionalInfoJson = AdditionalInfoJson.builder()
                .customerId(String.valueOf(currentUser.getId()))
                .nftId(String.valueOf(nftItem.getId()))
                .build();
        ChargePrepareRequestBody chargePrepareRequestBody = ChargePrepareRequestBody.builder()
                .customerDetails(additionalInfoJson)
                .build();
        CreateChargeRequest request = CreateChargeRequest.builder()
                .paymentRequestId(payLinkId)
                .requestAmount(String.valueOf(nftItem.getStartingPrice()))
                .prepareRequestBody(chargePrepareRequestBody)
                .build();
        try {
            String builtUrl = helioUrl + "?apiKey=" + apiKey;
            return restClient.post().uri(builtUrl)
                    .header("Authorization", "Bearer " + secretKey)
                    .body(request)
                    .retrieve()
                    .body(BeginCheckoutResponse.class);
        } catch (HttpClientErrorException | HttpServerErrorException exception) {
            throw new NFTSiteException(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    @Transactional
    public BuyNftResponse buyNft(Long nftId) {
        NftResponse nft = nftService.findById(nftId);
        if (nft.getNftStatus() != NftStatus.FOR_SALE) {
            throw new NFTSiteException("NFT is not available for purchase", HttpStatus.BAD_REQUEST);
        }
        UserDetailsDto userDetails = userDetailsService.getUserDetails();
        if (userDetails.getBalance() < nft.getStartingPrice()) {
            throw new NFTSiteException("Insufficient balance", HttpStatus.BAD_REQUEST);
        }
        userDetailsService.deductBalance(nft.getStartingPrice());
        User formerOwnerUser = userService.getUserByUsername(nft.getOwner().getEmailAddress()).orElseThrow(() -> new UserNotFoundException("User not found"));
        nftService.updateNftOwner(nftId);
        emailConfirmService.sendPaymentEmail(userDetails.getEmailAddress(), nft.getStartingPrice().toString(), PaymentType.USER_PURCHASE, new PaymentDetails(nft.getName(), nft.getStartingPrice().toString()), userDetails.getFirstName());
        Transaction transaction = new Transaction();
        transaction.setAmount(nft.getStartingPrice() * (double) -1);
        transaction.setTransactionType(TransactionType.PURCHASE);
        transaction.setDebitOrCreditStatus(TransactionType.DEBIT);
        transaction.setUser(userService.getUserById(userService.getAuthenticatedUser().getId()));
        transactionRepository.save(transaction);


        userDetailsService.creditBalance(formerOwnerUser.getUserDetails().getId(), nft.getStartingPrice(), BalanceType.PROFIT_BALANCE);
        emailConfirmService.sendPaymentEmail(formerOwnerUser.getUsername(), nft.getStartingPrice().toString(), PaymentType.USER_SALE, new PaymentDetails(nft.getName(), nft.getStartingPrice().toString()), formerOwnerUser.getUserDetails().getFirstName());
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(nft.getStartingPrice());
        newTransaction.setTransactionType(TransactionType.SALE);
        newTransaction.setDebitOrCreditStatus(TransactionType.CREDIT);
        newTransaction.setUser(formerOwnerUser);
        transactionRepository.save(newTransaction);
        return BuyNftResponse.builder()
                .nftId(nftId)
                .nftName(nft.getName())
                .nftPrice(nft.getStartingPrice())
                .nftStatus(nft.getNftStatus())
                .build();
    }

}
