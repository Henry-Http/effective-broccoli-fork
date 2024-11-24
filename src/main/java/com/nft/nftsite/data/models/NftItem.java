package com.nft.nftsite.data.models;

import com.nft.nftsite.data.models.enumerations.NftStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "nft_item")
public class NftItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private User owner;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private Category category;

    private String description;

    private String slug;

    private Double startingPrice;

    @OneToOne(optional = false)
    private Image picture;

    @Enumerated(EnumType.STRING)
    private NftStatus nftStatus;

    private final LocalDateTime createdAt = LocalDateTime.now();

}
