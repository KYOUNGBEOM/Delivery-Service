package org.delivery.storeadmin.domain.user.business;

import lombok.RequiredArgsConstructor;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.store.StoreRepository;
import org.delivery.db.store.enums.StoreStatus;
import org.delivery.db.storeuser.StoreUserEntity;
import org.delivery.storeadmin.common.annotation.Business;
import org.delivery.storeadmin.domain.user.controller.model.StoreUserRegisterRequest;
import org.delivery.storeadmin.domain.user.controller.model.StoreUserResponse;
import org.delivery.storeadmin.domain.user.converter.StoreUserConverter;
import org.delivery.storeadmin.domain.user.service.StoreUserService;

import java.util.Optional;

@RequiredArgsConstructor
@Business
public class StoreUserBusiness {

    private final StoreUserConverter storeUserConverter;

    private final StoreUserService storeUserService;
    
    private final StoreRepository storeRepository; // TODO SERVICE 로 변경하기

    public StoreUserResponse register(
        StoreUserRegisterRequest request
    ) {
        Optional<StoreEntity> storeEntity = storeRepository.findFirstByNameAndStatusOrderByIdDesc(request.getStoreName(), StoreStatus.REGISTERED);

        StoreUserEntity storeUserEntity = storeUserConverter.toEntity(request, storeEntity.get());

        StoreUserEntity savedStoreUserEntity = storeUserService.register(storeUserEntity);

        return storeUserConverter.toResponse(savedStoreUserEntity, storeEntity.get());
    }
}
