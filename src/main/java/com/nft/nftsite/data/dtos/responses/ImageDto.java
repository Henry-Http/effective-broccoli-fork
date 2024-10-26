package com.nft.nftsite.data.dtos.responses;

import com.nft.nftsite.data.models.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ImageDto {

    private Long id;

    private String url;

    private String title;

    private Long width;

    private Long height;

    private LocalDateTime createdAt;

    public static ImageDto fromImage(Image image) {
        if (image == null) return null;

        return ImageDto.builder()
                .id(image.getId())
                .url(image.getUrl())
                .title(image.getTitle())
                .width(image.getWidth())
                .height(image.getHeight())
                .createdAt(image.getCreatedAt())
                .build();
    }
}
