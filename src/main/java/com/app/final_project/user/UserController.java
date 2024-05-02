package com.app.final_project.user;

import com.app.final_project.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("getAll")
    public List<UserDto> getAll(){
        return userService.getAll();
    }
    @DeleteMapping("{id}/deleteBusiness/")
    Boolean delete(@PathVariable("id") Integer id){
        return userService.deleteteById(id);
    }
    @GetMapping("getUserById/{id}")
    public UserDto getUserById(
            @PathVariable("id") Integer id
    ){
        return userService.getUserById(id);
    }

}
