package com.nft.nftsite.utils;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ValidationUtil {

    public static boolean isValidImage(MultipartFile file) {
        return List.of("image/jpeg", "image/png", "image/webp").contains(file.getContentType());
    }
}
