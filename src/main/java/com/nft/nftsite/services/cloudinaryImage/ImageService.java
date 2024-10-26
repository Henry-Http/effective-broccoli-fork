package com.nft.nftsite.services.cloudinaryImage;

import com.nft.nftsite.data.models.Image;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.utils.PageDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    List<Image> uploadNewImage(MultipartFile[] file);

    Image uploadNewImage(MultipartFile file);

    Image uploadImage(String url);

    Image getImage(Long id);

    Image getImage(Long id, User user);

    PageDto<Image> getAllImages(Pageable pageable);

}
