package com.nft.nftsite.services.users;

import com.nft.nftsite.data.dtos.requests.UpdateUserDetailsDto;
import com.nft.nftsite.data.dtos.responses.UserDetailsDto;
import com.nft.nftsite.data.dtos.responses.payment.DepositResponse;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.models.enumerations.BalanceType;
import org.springframework.web.multipart.MultipartFile;

public interface UserDetailsService {

    UserDetailsDto getUserDetails();

    UserDetailsDto getUserDetailsDto(User user);

    UserDetailsDto updateUserDetails(UpdateUserDetailsDto updateUserDetailsDto);

    UserDetailsDto uploadDisplayPicture(MultipartFile picture);

    DepositResponse getUserBalance();

    void deductBalance(double amount);

    void creditBalance(Long id, Double startingPrice, BalanceType type);
}
