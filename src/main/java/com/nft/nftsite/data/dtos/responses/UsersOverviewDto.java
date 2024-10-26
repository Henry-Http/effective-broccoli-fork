package com.nft.nftsite.data.dtos.responses;

import lombok.Data;

@Data
public class UsersOverviewDto {

    private long all;

    private long active;

    private long inactive;

    public UsersOverviewDto(long all, long active, long inactive) {
        this.all = all;
        this.active = active;
        this.inactive = inactive;
    }
}
