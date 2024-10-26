package com.nft.nftsite.controllers;

import com.nft.nftsite.services.users.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@AllArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/all")
    @Operation(summary = "Get all roles", description = "Gets all available roles in the system")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<List<String>> getAllRoles() {
        return new ResponseEntity<>(roleService.findAllRoles(), HttpStatus.OK);
    }
}
