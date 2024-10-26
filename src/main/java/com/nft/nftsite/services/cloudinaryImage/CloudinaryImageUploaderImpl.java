package com.nft.nftsite.services.cloudinaryImage;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nft.nftsite.exceptions.FileUploadFailedException;
import com.nft.nftsite.data.models.Image;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Primary
@RequiredArgsConstructor
@Slf4j
public class CloudinaryImageUploaderImpl implements ImageUploader {

    private final Cloudinary cloudinary;

    @Value("${cloudinary.upload_folder}")
    private String uploadFolder;

    @Override
    public Image upload(MultipartFile image) {
        try {
            return this.handleUpload(image.getBytes(), null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Image upload(String url) {
        return this.handleUpload(null, url);
    }

    @Override
    public List<Image> uploadMultiple(MultipartFile[] images) {
        return Arrays.stream(images).map(this::upload).toList();
    }

    private Image handleUpload(byte[] bytes, String url) {
        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    url == null ? bytes : url,
                    ObjectUtils.asMap(
                            "folder", uploadFolder,
                            "colors", true
                    ));

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            String resulString = objectMapper.writeValueAsString(result);

            CloudinaryResponse response = objectMapper.readValue(resulString, CloudinaryResponse.class);

            return Image.builder()
                    .width(response.getWidth())
                    .height(response.getHeight())
                    .url(response.getSecureUrl())
                    .metadata(resulString)
                    .color(response.getColors().get(0).get(0))
                    .build();
        } catch (IOException exception) {
            log.error(exception.getMessage());
            throw new FileUploadFailedException("Image upload failed");
        }
    }
}
