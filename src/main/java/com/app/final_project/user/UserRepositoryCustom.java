package com.app.final_project.user;

import com.app.final_project.user.dto.TopUsersByEventPoints;
import com.app.final_project.user.dto.UserDto;

import java.util.List;

public interface UserRepositoryCustom {
    public List<UserDto> getAllUsersCustom();
    public List<UserDto> getLisOfEventRegistrants(Integer id, Integer filterBy);

    List<TopUsersByEventPoints> getTopUsersByEventPoints(String startDate, String endDate);
}
