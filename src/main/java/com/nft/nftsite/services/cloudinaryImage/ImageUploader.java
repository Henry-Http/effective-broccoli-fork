package com.nft.nftsite.services.cloudinaryImage;

import com.nft.nftsite.data.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageUploader {

    Image upload(MultipartFile image);

    Image upload(String url);

    List<Image> uploadMultiple(MultipartFile[] images);

}
