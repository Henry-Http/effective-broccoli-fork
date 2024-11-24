package com.nft.nftsite.services.payment;

import com.nft.nftsite.data.dtos.responses.BuyNftResponse;


public interface CheckoutService {

    BuyNftResponse buyNft(Long nftId);
}
