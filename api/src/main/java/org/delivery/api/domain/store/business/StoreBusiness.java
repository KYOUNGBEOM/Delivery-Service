package org.delivery.api.domain.store.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.Business;
import org.delivery.api.domain.store.controller.model.StoreRegisterRequest;
import org.delivery.api.domain.store.controller.model.StoreResponse;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.store.enums.StoreCategory;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Business
public class StoreBusiness {

    private final StoreService storeService;

    private final StoreConverter storeConverter;

    public StoreResponse register(
            StoreRegisterRequest storeRegisterRequest
    ) {
        StoreEntity storeEntity = storeConverter.toEntity(storeRegisterRequest);
        StoreEntity savedEntity = storeService.register(storeEntity);
        StoreResponse storeResponse = storeConverter.toResponse(savedEntity);
        return storeResponse;
    }

    public List<StoreResponse> searchCategory(
            StoreCategory storeCategory
    ) {
        List<StoreEntity> storeEntities = storeService.searchByCategory(storeCategory);

        return storeEntities.stream()
                .map(storeConverter::toResponse)
                .collect(Collectors.toList());
    }
}
