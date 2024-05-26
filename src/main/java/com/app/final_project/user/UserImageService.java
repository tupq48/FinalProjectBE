package com.app.final_project.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserImageService {

    @Autowired
    UserImageRepository userImageRepository;

    public List<UserImage> saveAll(List<UserImage> userImages) {
        return userImageRepository.saveAll(userImages);
    }

    public List<UserImage> getByUserId(Integer userId) {
        return userImageRepository.findAllByUserId(userId);
    }

}
