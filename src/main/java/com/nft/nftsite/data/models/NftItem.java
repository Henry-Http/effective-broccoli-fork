package com.nft.nftsite.data.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(mappedBy = "nft_item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> pictures;

    private String name;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private User owner;

    @ManyToOne(cascade = CascadeType.ALL, optional = false)
    private Category category;

    private String description;

    private String slug;

    private Double startingPrice;

    private Double currentBid;

    private LocalDateTime createdAt;

}
