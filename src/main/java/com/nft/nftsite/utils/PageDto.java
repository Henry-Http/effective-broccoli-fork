package com.nft.nftsite.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDto<T> {

    private Long totalElements;

    private Long totalPages;

    private Long pageNumber;

    private Long pageSize;

    private List<T> content;

}
