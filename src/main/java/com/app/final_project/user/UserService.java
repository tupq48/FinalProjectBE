package com.app.final_project.user;

import com.app.final_project.auth.RegisterRequest;
import com.app.final_project.user.dto.TopUsersByEventPoints;
import com.app.final_project.user.dto.UserDto;
import com.app.final_project.user.dto.UserRequest;
import com.app.final_project.user.utils.UserUtils;
import com.app.final_project.userInfor.UserInfor;
import com.app.final_project.userInfor.UserInforRepository;
import com.app.final_project.util.ImgBBUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserInforRepository userInforRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAll(){
        return userRepository.getAllUsersCustom();
    }
    public List<UserDto> getEventRegistrants(Integer id, Integer filterBy){
        return userRepository.getLisOfEventRegistrants(id, filterBy);
    }

    public Boolean deleteteById(Integer id) {
        try {
            userRepository.deleteById(id);
            return true;
        }
        catch (Exception e){
            logger.error("Error delete user with id " + id,e);
            return false;
        }
    }

    public Optional<User> getUserByUserName(String userName) {
        return userRepository.findByUsername(userName);
    }

    public UserDto getUserById(Integer id) {
        try {
            User user= userRepository.findById(id).orElse(null);
            return UserDto.builder()
                    .id(user.getUser_id())
                    .birthday(user.getUserInfor().getDateOfBirth())
                    .gmail(user.getEmail())
                    .name(user.getUserInfor().getFullname())
                    .username(user.getUsername())
                    .gender(user.getUserInfor().getGender())
                    .role(user.getRole())
                    .phoneNumber(user.getPhoneNumber())
                    .urlImage(user.getUserInfor().getUrlAvatar())
                    .address(user.getUserInfor().getAddress())
                    .build();
        }
        catch (Exception e){
            logger.error("Error update user with id " + id,e);
            return null;
        }
    }
    @Transactional
    public void updateInfoUser(UserRequest userRequest, List<MultipartFile> image) {
        System.out.println("id là :" +userRequest.getId());
        User user = UserUtils.convertUserRequestToUser(userRequest);
        if (userRequest.getUsername() != null) {
            Optional<User> existingUser = userRepository.findById(userRequest.getId());
            existingUser.ifPresent(value -> {
                user.setUser_id(value.getUser_id());
                user.setPassword(value.getPassword().trim());
                user.setEnabled(true);
                user.setCreateAt(value.getCreateAt());
                user.setRole(value.getRole());
            });
        }
        User savedUser = userRepository.save(user);
        UserInfor userInfor = new UserInfor();
        Optional<UserInfor> existingUserInfo = userInforRepository.findByUser_User_Id(user.getUser_id());
        existingUserInfo.ifPresent(value -> {
            userInfor.setUser_info_id(value.getUser_info_id());
            userInfor.setFullname(userRequest.getName());
            userInfor.setAddress(userRequest.getAddress());
            userInfor.setGender(userRequest.getGender());
            userInfor.setUser(savedUser);
            List<String> imgUrls;
            if(image!=null){
                imgUrls = ImgBBUtils.uploadImages(image);
                imgUrls.forEach(System.out::println);
                userInfor.setUrlAvatar(imgUrls.get(0));

            } else {
                userInfor.setUrlAvatar(value.getUrlAvatar());
            }
            userInfor.setDateOfBirth(userRequest.getDateOfBirth().atStartOfDay());
        });
        userInforRepository.save(userInfor);
    }

    public List<TopUsersByEventPoints> getTopUsersByEventPoints(String startDate, String endDate) {
        return userRepository.getTopUsersByEventPoints(startDate, endDate);
    }
}
