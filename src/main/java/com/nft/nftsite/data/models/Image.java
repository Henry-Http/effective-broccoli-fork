package com.nft.nftsite.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nft.nftsite.data.models.enumerations.UploadUserType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    private String title;

    private Long width;

    private Long height;

    private String color;

    @JsonIgnore
    @Column(columnDefinition = "TEXT")
    private String metadata;

    @Enumerated(EnumType.STRING)
    private UploadUserType uploadUserType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User uploadedBy;

    private final LocalDateTime createdAt = LocalDateTime.now();

}
