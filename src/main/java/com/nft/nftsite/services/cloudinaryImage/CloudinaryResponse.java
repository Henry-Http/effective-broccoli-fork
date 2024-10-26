package com.nft.nftsite.services.cloudinaryImage;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloudinaryResponse {

    @JsonProperty("asset_id")
    private String assetId;

    @JsonProperty("public_id")
    private String publicId;

    private int version;

    @JsonProperty("version_id")
    private String versionId;

    private String signature;

    private long width;

    private long height;

    private String format;

    private String resourceType;

    private LocalDateTime createdAt;

    private List<String> tags;

    private int pages;

    private int bytes;

    private String type;

    private String etag;

    private boolean placeholder;

    private String url;

    @JsonProperty("secure_url")
    private String secureUrl;

    private String folder;

    @JsonProperty("access_mode")
    private String accessMode;

    private boolean existing;

    @JsonProperty("original_filename")
    private String originalFilename;

    private List<List<String>> colors;

}
