package org.delivery.api.domain.user.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.Business;
import org.delivery.api.domain.token.business.TokenBusiness;
import org.delivery.api.domain.token.controller.model.TokenResponse;
import org.delivery.api.domain.user.controller.model.UserLoginRequest;
import org.delivery.api.domain.user.controller.model.UserRegisterRequest;
import org.delivery.api.domain.user.controller.model.UserResponse;
import org.delivery.api.domain.user.conveter.UserConverter;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.user.service.UserService;
import org.delivery.db.user.UserEntity;

@RequiredArgsConstructor
@Business
public class UserBusiness {

    private final UserService userService;
    private final UserConverter userConverter;

    private final TokenBusiness tokenBusiness;

    public UserResponse register(UserRegisterRequest request) {
        UserEntity entity = userConverter.toEntity(request);
        UserEntity savedEntity = userService.register(entity);

        return userConverter.toResponse(savedEntity);
    }

    public TokenResponse login(UserLoginRequest request) {
        UserEntity savedEntity = userService.login(request.getEmail(), request.getPassword());

        TokenResponse tokenResponse = tokenBusiness.issueToken(savedEntity);

        return tokenResponse;
    }

    public UserResponse me(User user) {
        UserEntity userEntity = userService.getUserWithTrow(user.getId());
        UserResponse userResponse = userConverter.toResponse(userEntity);
        return userResponse;
    }

}
