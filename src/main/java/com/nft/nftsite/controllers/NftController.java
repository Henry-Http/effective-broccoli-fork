package com.nft.nftsite.controllers;

import com.nft.nftsite.data.dtos.requests.CreateNftRequest;
import com.nft.nftsite.data.dtos.responses.NftResponse;
import com.nft.nftsite.services.nft.NftService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/nft")
@AllArgsConstructor
@Slf4j
public class NftController {

    private final NftService nftService;

    @PostMapping("/new")
    @Operation(summary = "Create new NFT")
    public ResponseEntity<NftResponse> createNewNft(@Valid @RequestBody CreateNftRequest nftRequest) {
        return new ResponseEntity<>(nftService.createNewNft(nftRequest), HttpStatus.OK);
    }

}
