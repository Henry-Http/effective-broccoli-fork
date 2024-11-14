package com.nft.nftsite.security;

import lombok.Getter;

import java.util.Arrays;

public class AllowedURIs {

    @Getter
    private final static String[] allowedEndpoints = {
            "/actuator", "/actuator/**",
            "/api/v1/user/signup", "/api/v1/user/signup/complete", "/api/v1/user/login",
            "/api/v1/user/reset-password/init", "/api/v1/user/otp/resend/**",
            "/api/v1/user/reset-password/complete", "/api/v1/user/third-party-signin", // all users
            "/api/v1/admin/setup", "/api/v1/user/admin/login", // admin
            "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**", // swagger
            "/socket.io", "/socket.io/**",
            "/api/v1/corner/sitemap", "/api/v1/corner/post/sitemap",
            "/api/v1/nft/find", "/api/v1/nft/find/**",
            "/api/v1/helio/webhook", "/api/v1/helio/webhook/**",
            "/api/v1/category/find", "/api/v1/category/find/**"
    };

    public static boolean isAllowed(String uri) {
        return Arrays.asList(AllowedURIs.getAllowedEndpoints()).contains(uri);
    }

}
