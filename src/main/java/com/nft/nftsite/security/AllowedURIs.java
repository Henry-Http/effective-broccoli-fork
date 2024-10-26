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
            "/api/v1/admin/setup", // admin
            "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**", // swagger
            "/api/v1/movie/search", "/api/v1/movie/sitemap", // movie
            "/api/v1/film/trending", "/api/v1/film/slug/**", "/api/v1/film/latest-release", "/api/v1/film/coming-soon", "/api/v1/film/filter", // films
            "/api/v1/series/trending", "/api/v1/series/slug/**", "/api/v1/series/latest-release", // series
            "/api/v1/trailer/slug/**", "/api/v1/trailer/movie/**", "/api/v1/trailer/coming-soon", "/api/v1/trailer/now-showing", // trailers
            "/api/v1/movie-role/movie/**", "/api/v1/movie-role/person/**", // movie roles
            "/api/v1/person/slug/**", "/api/v1/person/sitemap", // persons (actors and filmmakers)
            "/api/v1/review/movie/**",
            "/api/v1/webhook/squad", "/api/v1/recommendation/hero-movie/all",
            "/api/v1/corner/search", "/api/v1/corner/recommend",
            "/api/v1/post/spotlight", "/api/v1/post/slug/**", "/api/v1/post/sitemap", "/api/v1/post/public",
            "/api/v1/poll/**", // polls
            "/socket.io", "/socket.io/**",
            "/api/v1/corner/sitemap", "/api/v1/corner/post/sitemap",
            "/api/v1/comment/**" // post comments
    };

    public static boolean isAllowed(String uri) {
        return Arrays.asList(AllowedURIs.getAllowedEndpoints()).contains(uri);
    }

}
