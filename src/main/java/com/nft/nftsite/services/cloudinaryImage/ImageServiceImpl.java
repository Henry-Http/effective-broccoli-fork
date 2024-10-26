package com.nft.nftsite.services.cloudinaryImage;

import com.nft.nftsite.data.models.enumerations.UploadUserType;
import com.nft.nftsite.exceptions.FileNotFoundException;
import com.nft.nftsite.data.models.Image;
import com.nft.nftsite.data.repository.ImageRepository;
import com.nft.nftsite.data.models.User;
import com.nft.nftsite.services.users.UserService;
import com.nft.nftsite.utils.PageDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.List;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final ImageUploader imageUploader;
    private final UserService userService;
    private final ModelMapper modelMapper;


    @Override
    public List<Image> uploadNewImage(MultipartFile[] file) {
        User user = userService.getAuthenticatedUser();
        List<Image> images = imageUploader.uploadMultiple(file);

        images = images.stream().peek(image -> {
            image.setUploadedBy(user);
            image.setUploadUserType(userService.isControlCenterUser() ? UploadUserType.STAFF : UploadUserType.CUSTOMER);
        }).toList();
        return imageRepository.saveAll(images);
    }

    @Override
    public Image uploadNewImage(MultipartFile file) {
        return this.uploadNewImage(new MultipartFile[]{file}).get(0);
    }

    @Override
    public Image uploadImage(String url) {
        Image image = imageUploader.upload(url);
        return imageRepository.save(image);
    }

    @Override
    public Image getImage(Long id) {
        return imageRepository.findById(id).orElseThrow(FileNotFoundException::new);
    }

    @Override
    public Image getImage(Long id, User user) {
        return imageRepository.findByIdAndUploadedBy(id, user).orElseThrow(FileNotFoundException::new);
    }

    @Override
    public PageDto<Image> getAllImages(Pageable pageable) {
        Page<Image> images = imageRepository.findAll(pageable);

        Type pageDtoTypeToken = new TypeToken<PageDto<Image>>() {
        }.getType();
        return modelMapper.map(images, pageDtoTypeToken);
    }
}
