package org.delivery.api.domain.userorder.business;

import lombok.RequiredArgsConstructor;
import org.delivery.api.common.annotation.Business;
import org.delivery.api.domain.store.converter.StoreConverter;
import org.delivery.api.domain.store.service.StoreService;
import org.delivery.api.domain.storemenu.converter.StoreMenuConverter;
import org.delivery.api.domain.storemenu.service.StoreMenuService;
import org.delivery.api.domain.user.model.User;
import org.delivery.api.domain.userorder.controller.model.UserOrderDetailResponse;
import org.delivery.api.domain.userorder.controller.model.UserOrderRequest;
import org.delivery.api.domain.userorder.controller.model.UserOrderResponse;
import org.delivery.api.domain.userorder.coverter.UserOrderConverter;
import org.delivery.api.domain.userorder.service.UserOrderService;
import org.delivery.api.domain.userordermenu.converter.UserOrderMenuConverter;
import org.delivery.api.domain.userordermenu.service.UserOrderMenuService;
import org.delivery.db.store.StoreEntity;
import org.delivery.db.storemenu.StoreMenuEntity;
import org.delivery.db.userorder.UserOrderEntity;
import org.delivery.db.userordermenu.UserOrderMenuEntity;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Business
public class UserOrderBusiness {

    private final UserOrderService userOrderService;
    private final UserOrderConverter userOrderConverter;

    private final StoreMenuService storeMenuService;
    private final StoreMenuConverter storeMenuConverter;

    private final UserOrderMenuService userOrderMenuService;
    private final UserOrderMenuConverter userOrderMenuConverter;

    private final StoreService storeService;
    private final StoreConverter storeConverter;

    public UserOrderResponse userOrder(User user, UserOrderRequest request) {
        List<StoreMenuEntity> storeMenuEntityList = request.getStoreMenuIdList()
                .stream()
                .map(storeMenuId -> storeMenuService.getStoreMenuWithThrow(storeMenuId))
                .collect(Collectors.toList());

        UserOrderEntity userOrderEntity = userOrderConverter.toEntity(user, storeMenuEntityList);

        // 주문
        UserOrderEntity savedUserOrderEntity = userOrderService.order(userOrderEntity);

        // 맵핑
        List<UserOrderMenuEntity> userOrderMenuEntities = storeMenuEntityList.stream()
                .map(storeMenuEntity -> {
                    return userOrderMenuConverter.toEntity(savedUserOrderEntity, storeMenuEntity);
                })
                .collect(Collectors.toList());

        // 주문내역 기록 남기기
        userOrderMenuEntities.stream()
                .map(userOrderMenuEntity -> {
                    return userOrderMenuService.order(userOrderMenuEntity);
                });

        // response
        return userOrderConverter.toResponse(savedUserOrderEntity);

    }


    public List<UserOrderDetailResponse> current(User user) {
        List<UserOrderEntity> userOrderEntities = userOrderService.current(user.getId());

        // 주문 1건씩 처리
        return userOrderEntities.stream()
                .map(userOrderEntity -> {

                    // 사용자가 주문한 메뉴
                    List<UserOrderMenuEntity> userOrderMenuEntities = userOrderMenuService.getUserOrderMenu(userOrderEntity.getId());

                    List<StoreMenuEntity> storeMenuEntities =  userOrderMenuEntities.stream()
                            .map(userOrderMenuEntity -> {
                                return storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                            })
                            .collect(Collectors.toList());
                    
                    // 사용자가 주문한 스토어
                    StoreEntity storeEntity = storeService.getStoreWithThrow(storeMenuEntities.stream().findFirst().get().getStoreId());

                    return UserOrderDetailResponse.builder()
                            .userOrderResponse(userOrderConverter.toResponse(userOrderEntity))
                            .storeMenuResponses(storeMenuConverter.toResponse(storeMenuEntities))
                            .storeResponse(storeConverter.toResponse(storeEntity))
                            .build();
                }).collect(Collectors.toList());
    }

    public List<UserOrderDetailResponse> history(User user) {
        List<UserOrderEntity> userOrderEntities = userOrderService.history(user.getId());

        // 주문 1건씩 처리
        return userOrderEntities.stream()
                .map(userOrderEntity -> {

                    // 사용자가 주문한 메뉴
                    List<UserOrderMenuEntity> userOrderMenuEntities = userOrderMenuService.getUserOrderMenu(userOrderEntity.getId());

                    List<StoreMenuEntity> storeMenuEntities =  userOrderMenuEntities.stream()
                            .map(userOrderMenuEntity -> {
                                return storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                            })
                            .collect(Collectors.toList());

                    // 사용자가 주문한 스토어
                    StoreEntity storeEntity = storeService.getStoreWithThrow(storeMenuEntities.stream().findFirst().get().getStoreId());

                    return UserOrderDetailResponse.builder()
                            .userOrderResponse(userOrderConverter.toResponse(userOrderEntity))
                            .storeMenuResponses(storeMenuConverter.toResponse(storeMenuEntities))
                            .storeResponse(storeConverter.toResponse(storeEntity))
                            .build();
                }).collect(Collectors.toList());
    }

    public UserOrderDetailResponse read(User user, Long orderId) {
        UserOrderEntity userOrderEntity = userOrderService.getUserOrderWithOutStatusWithThrow(orderId, user.getId());

        // 사용자가 주문한 메뉴
        List<UserOrderMenuEntity> userOrderMenuEntities = userOrderMenuService.getUserOrderMenu(userOrderEntity.getId());

        List<StoreMenuEntity> storeMenuEntities =  userOrderMenuEntities.stream()
                .map(userOrderMenuEntity -> {
                    return storeMenuService.getStoreMenuWithThrow(userOrderMenuEntity.getStoreMenuId());
                })
                .collect(Collectors.toList());

        // 사용자가 주문한 스토어
        StoreEntity storeEntity = storeService.getStoreWithThrow(storeMenuEntities.stream().findFirst().get().getStoreId());

        return UserOrderDetailResponse.builder()
                .userOrderResponse(userOrderConverter.toResponse(userOrderEntity))
                .storeMenuResponses(storeMenuConverter.toResponse(storeMenuEntities))
                .storeResponse(storeConverter.toResponse(storeEntity))
                .build();
    }

}
