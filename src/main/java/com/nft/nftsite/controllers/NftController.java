package com.nft.nftsite.controllers;

import com.nft.nftsite.data.dtos.requests.CreateNftRequest;
import com.nft.nftsite.data.dtos.requests.NftFilterDto;
import com.nft.nftsite.data.dtos.responses.NftResponse;
import com.nft.nftsite.services.nft.NftService;
import com.nft.nftsite.utils.PageDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/nft")
@AllArgsConstructor
@Slf4j
public class NftController {

    private final NftService nftService;

    @PostMapping(value = "/new", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create new NFT")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<NftResponse> createNewNft(@Valid @RequestBody @ModelAttribute CreateNftRequest nftRequest) {
        return new ResponseEntity<>(nftService.createNewNft(nftRequest), HttpStatus.OK);
    }

    @GetMapping("/find")
    @Operation(summary = "Get all NFTs", description = "Get all NFTs in the system with optional filters")
    public ResponseEntity<PageDto<NftResponse>> getAllNfts(
            @ParameterObject Pageable pageable,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double currentBid) {
        NftFilterDto filterDto = new NftFilterDto(search, categoryId, minPrice, maxPrice, currentBid);
        return ResponseEntity.ok(nftService.getAllNfts(pageable, filterDto));
    }

}
