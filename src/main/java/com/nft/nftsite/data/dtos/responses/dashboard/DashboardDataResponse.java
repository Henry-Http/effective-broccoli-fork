package com.nft.nftsite.data.dtos.responses.dashboard;


import com.nft.nftsite.data.dtos.responses.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDataResponse {

    private Long totalUsers;
    private long totalPurchases;
    private Double totalAmount;
    private List<UserDto> users;


}
