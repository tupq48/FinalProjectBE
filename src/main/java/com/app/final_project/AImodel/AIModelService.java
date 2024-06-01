package com.app.final_project.AImodel;

import com.app.final_project.registration.RegistrationService;
import com.app.final_project.registration.RegistrationStatus;
import com.app.final_project.user.*;
import com.app.final_project.util.AIModelAPIUtils;
import com.app.final_project.util.ImgBBUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AIModelService {

    private final UserService userService;
    private final UserImageService userImageService;
    private final RegistrationService registrationService;

    public Boolean isModelExist() {
        Integer userId = getCurrentUserId();
        if (userId == null)
            return false;
        return AIModelAPIUtils.isModelExist(userId);
    }

    public Boolean isTraining() {
        Integer userId = getCurrentUserId();
        if (userId == null)
            return false;
        return AIModelAPIUtils.isTraining(userId);
    }

    public Boolean trainModel() {
        Integer userId = getCurrentUserId();
        if (userId == null)
            return false;
        List<UserImage> userImages = userImageService.getByUserId(userId);
        List<String> imageUrls = userImages.stream().map(UserImage::getUrlImage).collect(Collectors.toList());
        return AIModelAPIUtils.trainingModel(imageUrls, userId);
    }

    public Boolean predict(MultipartFile image, Integer eventId) {
        Integer userId = getCurrentUserId();
        if (userId == null)
            return false;
        String imageUrl = ImgBBUtils.uploadImage(image);
        Boolean isLegit =  AIModelAPIUtils.predict(imageUrl, userId);
        var registration = registrationService.findByUserIdAndEventId(userId, eventId);
        registration.setImageUrl(imageUrl);
        if (isLegit) {
            registration.setStatus(RegistrationStatus.attended);
            registrationService.save(registration);
            return true;
        }
        registrationService.save(registration);
        return false;
    }

    public List<UserImage> getAllUserImage() {
        Integer userId = getCurrentUserId();
        if (userId == null)
            return null;
        return userImageService.getByUserId(userId);
    }

    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        Optional<User> userOpt = userService.getUserByUserName(userName);
        if (userOpt.isEmpty()) {
            return null;
        }
        return userOpt.get().getUser_id();
    }

    public List<String> uploadImage(List<MultipartFile> images) {
        Integer userId = getCurrentUserId();
        if (userId == null)
            return new ArrayList<>();
        List<String> imageUrls = ImgBBUtils.uploadImages(images);

        // check xem co image bi trung ko, neu co thi ko save
        var savedImages = userImageService.getByUserId(userId);
        savedImages.forEach(item -> {
            imageUrls.remove(item.getUrlImage());
        });
        List<UserImage> userImages = new ArrayList<>();
        for (var url : imageUrls) {
            userImages.add(UserImage.builder()
                    .userId(userId)
                    .urlImage(url)
                    .build());
        }
        userImageService.saveAll(userImages);
        return imageUrls;
    }

    public Boolean deleteImage(List<String> imageUrls) {
        Integer userId = getCurrentUserId();
        if (userId == null)
            return false;
        var userImages = userImageService.findByImageUrlIn(imageUrls);
        userImageService.deleteALl(userImages);
        return true;
    }
}
