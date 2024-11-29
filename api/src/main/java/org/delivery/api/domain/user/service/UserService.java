package org.delivery.api.domain.user.service;

import org.delivery.api.common.error.ErrorCode;
import org.delivery.api.common.error.UserErrorCode;
import org.delivery.api.common.exception.ApiException;
import org.delivery.db.user.UserEntity;
import org.delivery.db.user.UserRepository;
import org.delivery.db.user.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.Optional;

public class UserService {

    private UserRepository userRepository;

    public UserEntity register(UserEntity userEntity) {
        return Optional.ofNullable(userEntity)
                .map(entity -> {
                    entity.setStatus(UserStatus.REGISTERED);
                    entity.setRegisteredAt(LocalDateTime.now());
                    return userRepository.save(userEntity);
                })
                .orElseThrow(() -> new ApiException(ErrorCode.NULL_POINT, "User Entity null"));
    }

    public UserEntity login(
            String email,
            String password
    ) {
        return getUserWithTrow(email, password);
    }

    public UserEntity getUserWithTrow(
            String email,
            String password
    ) {
        return userRepository.findFirstByEmailAndPasswordAndStatusOrderByIdDesc(
                email,
                password,
                UserStatus.REGISTERED
        ).orElseThrow(() -> new ApiException(UserErrorCode.USER_NOT_FOUND));
    }

}
