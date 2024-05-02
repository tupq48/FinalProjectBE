package com.app.final_project.user;

import com.app.final_project.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> getAll(){
        return userRepository.getAllUsersCustom();
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

    public UserDto getUserById(Integer id) {
        try {
            User user= userRepository.findById(id).orElse(null);
            UserDto userDto = new UserDto();
            userDto.setId(user.getUser_id());
            userDto.setBirthday(user.getUserInfor().getDateOfBirth());
            userDto.setGmail(user.getEmail());
            userDto.setName(user.getUserInfor().getFullname());
            userDto.setUsername(user.getUsername());
            userDto.setGender(user.getUserInfor().getGender());
            userDto.setRole(user.getRole());
            userDto.setPhoneNumber(user.getPhoneNumber());


            return userDto;
        }
        catch (Exception e){
            logger.error("Error update user with id " + id,e);
            return null;
        }
    }
}
