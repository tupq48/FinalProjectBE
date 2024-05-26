package com.app.final_project.AImodel;

import com.app.final_project.user.*;
import com.app.final_project.util.AIModelAPIUtils;
import com.app.final_project.util.ImgBBUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AIModelService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserImageService userImageService;

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

    public Boolean trainModel(List<MultipartFile> images) {
        Integer userId = getCurrentUserId();
        if (userId == null)
            return false;
        List<String> imageUrls = ImgBBUtils.uploadImages(images);
//        List<String> imageUrls = Arrays.asList(
//             "https://i.ibb.co/PxqyLMJ/1.jpg",
//            "https://i.ibb.co/VNL9K53/4.jpg",
//            "https://i.ibb.co/pbVx4f5/5.jpg",
//            "https://i.ibb.co/6vDB79M/6.jpg",
//            "https://i.ibb.co/Lr3BR0N/3.jpg",
//            "https://i.ibb.co/8zcyTL0/2.jpg"
//        );
        // save anh vao db

        if(AIModelAPIUtils.trainingModel(imageUrls, userId)) {
            List<UserImage> userImages = new ArrayList<>();
            for (var url : imageUrls) {
                userImages.add(UserImage.builder()
                        .userId(userId)
                        .urlImage(url)
                        .build());
            }
            userImageService.saveAll(userImages);
            return true;
        }
        return false;
    }

    public Boolean predict(MultipartFile image) {
        Integer userId = getCurrentUserId();
        String imageUrl = ImgBBUtils.uploadImage(image);
        return AIModelAPIUtils.predict(imageUrl, userId);
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
}
