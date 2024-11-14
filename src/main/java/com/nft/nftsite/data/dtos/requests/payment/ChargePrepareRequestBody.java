package com.nft.nftsite.data.dtos.requests.payment;

import com.nft.nftsite.data.dtos.responses.payment.AdditionalInfoJson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChargePrepareRequestBody {

    private AdditionalInfoJson customerDetails;

}
