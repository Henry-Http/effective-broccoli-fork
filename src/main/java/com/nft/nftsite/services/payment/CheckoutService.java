package com.nft.nftsite.services.payment;

import com.nft.nftsite.data.dtos.requests.payment.HelioWebhookPayload;
import com.nft.nftsite.data.dtos.responses.payment.BeginCheckoutResponse;
import com.nft.nftsite.data.dtos.responses.payment.WebhookResponse;

import java.util.List;

public interface CheckoutService {

    WebhookResponse receiveWebhook(HelioWebhookPayload webhookPayload);

    BeginCheckoutResponse buyNftNow(Long nftId);

    List<WebhookResponse> getPayments();

    Double calculateTotal();
}
