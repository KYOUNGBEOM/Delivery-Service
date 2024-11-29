package org.delivery.api.domain.user.business;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.Business;
import org.delivery.api.common.api.Api;
import org.delivery.api.domain.user.controller.model.UserLoginRequest;
import org.delivery.api.domain.user.controller.model.UserRegisterRequest;
import org.delivery.api.domain.user.controller.model.UserResponse;
import org.delivery.api.domain.user.conveter.UserConverter;
import org.delivery.api.domain.user.service.UserService;
import org.delivery.db.user.UserEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Business
public class UserBusiness {

    private final UserService userService;

    private final UserConverter userConverter;

    public UserResponse register(UserRegisterRequest request) {
        UserEntity entity = userConverter.toEntity(request);
        UserEntity savedEntity = userService.register(entity);

        return userConverter.toResponse(savedEntity);
    }

    public UserResponse login(UserLoginRequest request) {
        UserEntity savedEntity = userService.login(request.getEmail(), request.getPassword());

        return userConverter.toResponse(savedEntity);
    }
}
