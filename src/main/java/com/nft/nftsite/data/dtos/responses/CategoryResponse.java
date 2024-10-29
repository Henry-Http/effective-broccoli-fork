package com.nft.nftsite.data.dtos.responses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {

    private Long id;

    private String name;

    private String description;

}
