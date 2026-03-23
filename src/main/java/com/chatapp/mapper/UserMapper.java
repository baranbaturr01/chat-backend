package com.chatapp.mapper;

import com.chatapp.dto.response.UserResponse;
import com.chatapp.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toUserResponse(User user);
}
