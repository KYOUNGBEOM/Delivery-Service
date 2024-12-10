package org.delivery.storeadmin.domain.authorization;

import lombok.RequiredArgsConstructor;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.store.StoreRepository;
import org.delivery.db.store.enums.StoreStatus;
import org.delivery.db.storeuser.StoreUserEntity;
import org.delivery.storeadmin.domain.authorization.model.UserSession;
import org.delivery.storeadmin.domain.user.service.StoreUserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthorizationService implements UserDetailsService {

    private final StoreUserService storeUserService;

    private final StoreRepository storeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<StoreUserEntity> storeUserEntity = storeUserService.getRegisterUser(username);
        Optional<StoreEntity> storeEntity = storeRepository.findFirstByIdAndStatusOrderByIdDesc(storeUserEntity.get().getStoreId(), StoreStatus.REGISTERED);

        return storeUserEntity.map(entity -> {
            return UserSession.builder()
                    .userId(entity.getId())
                    .password(entity.getPassword())
                    .email(entity.getEmail())
                    .status(entity.getStatus())
                    .role(entity.getRole())
                    .registeredAt(entity.getRegisteredAt())
                    .unregisteredAt(entity.getUnregisteredAt())
                    .lastLoginAt(entity.getLastLoginAt())

                    .storeId(storeEntity.get().getId())
                    .storeName(storeEntity.get().getName())
                    .build();
        }).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
