package com.nft.nftsite.data.dtos.responses;


import com.nft.nftsite.data.models.Category;
import com.nft.nftsite.data.models.Image;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.data.models.enumerations.NftStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NftResponse {

    private Long id;

    private String slug;

    private String name;

    private CategoryResponse category;

    private Double startingPrice;

    private UserDetailsDto owner;

    private String description;

    private LocalDateTime createdAt;

    private ImageDto picture;

    private NftStatus nftStatus;

    public void setOwner(User owner) {
        if (owner == null) {
            this.owner = null;
            return;
        }
        this.owner = UserDetailsDto.builder()
                .id(owner.getUserDetails().getId())
                .emailAddress(owner.getUsername())
                .firstName(owner.getUserDetails().getFirstName())
                .lastName(owner.getUserDetails().getLastName())
                .displayPicture(ImageDto.fromImage(owner.getUserDetails().getDisplayPicture()))
                .tag(owner.getUserDetails().getTag())
                .verified(owner.getUserDetails().isVerified())
                .build();
    }

    public void setCategory(Category category) {
        if (category == null) {
            this.category = null;
            return;
        }
        this.category = CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public void setPicture(Image picture) {
        this.picture = ImageDto.fromImage(picture);
    }


}
