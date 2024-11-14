package com.nft.nftsite.services;


import com.nft.nftsite.data.dtos.responses.UserDto;
import com.nft.nftsite.data.dtos.responses.dashboard.DashboardDataResponse;
import com.nft.nftsite.services.payment.CheckoutService;
import com.nft.nftsite.services.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DashboardServiceImpl implements DashboardService {

    private final UserService userService;
    private final CheckoutService checkoutService;


    @Override
    public DashboardDataResponse getDashboardData() {
        List<UserDto> users = userService.getAllUsers();
        DashboardDataResponse response = new DashboardDataResponse();
        response.setTotalUsers((long) users.size());
        response.setTotalAmount(checkoutService.calculateTotal());
        response.setTotalPurchases((long) checkoutService.getPayments().size());
        response.setUsers(users);
        return response;
    }
}
