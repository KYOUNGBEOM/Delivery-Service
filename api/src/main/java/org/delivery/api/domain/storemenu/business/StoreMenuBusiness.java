package org.delivery.api.domain.storemenu.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.Business;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuRegisterRequest;
import org.delivery.api.domain.storemenu.controller.model.StoreMenuResponse;
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;
import org.delivery.db.storemenu.StoreMenuEntity;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Business
public class StoreMenuBusiness {

    private final StoreMenuService storeMenuService;

    private final StoreMenuConverter storeMenuConverter;

    public StoreMenuResponse register(
            StoreMenuRegisterRequest request
    ) {
        StoreMenuEntity entity = storeMenuConverter.toEntity(request);
        StoreMenuEntity savedEntity = storeMenuService.register(entity);
        StoreMenuResponse response = storeMenuConverter.toResponse(savedEntity);
        return  response;
    }

    public List<StoreMenuResponse> search(
            Long storeId
    ) {
        List<StoreMenuEntity>  storeMenuEntities = storeMenuService.getStoreMenuByStoreId(storeId);
         return storeMenuEntities.stream()
                 .map(storeMenuConverter::toResponse)
                 .collect(Collectors.toList());
    }


}
