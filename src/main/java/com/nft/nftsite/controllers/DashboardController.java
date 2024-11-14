package com.nft.nftsite.controllers;


import com.nft.nftsite.data.dtos.responses.dashboard.DashboardDataResponse;
import com.nft.nftsite.services.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@AllArgsConstructor
@Slf4j
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/data")
    @Operation(summary = "Get dashboard details")
    @Secured("ROLE_ADMIN")
    public ResponseEntity<DashboardDataResponse> getDashboardData() {
        return new ResponseEntity<>(dashboardService.getDashboardData(), HttpStatus.OK);
    }

}
