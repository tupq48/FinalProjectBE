package com.app.final_project.user;

import com.app.final_project.auth.RegisterRequest;
import com.app.final_project.user.dto.UserDto;
import com.app.final_project.user.dto.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("getAll")
    public List<UserDto> getAll() {
        return userService.getAll();
    }
    @GetMapping("getEventRegistrants")
    public List<UserDto> getEventRegistrants(@RequestParam(value = "eventId") Integer id) {
        return userService.getEventRegistrants(id);
    }

    @DeleteMapping("{id}/deleteBusiness/")
    Boolean delete(@PathVariable("id") Integer id) {
        return userService.deleteteById(id);
    }

    @GetMapping("getUserById/{id}")
    public UserDto getUserById(
            @PathVariable("id") Integer id
    ) {
        return userService.getUserById(id);
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public void updateInfoUser(
            @ModelAttribute UserRequest userRequest,
            @RequestParam(value = "image", required = false)List<MultipartFile> image) {
        userService.updateInfoUser(userRequest,image);
    }
}
