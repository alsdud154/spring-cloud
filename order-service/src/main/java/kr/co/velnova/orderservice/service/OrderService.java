package kr.co.velnova.orderservice.service;

import kr.co.velnova.orderservice.dto.OrderDto;
import kr.co.velnova.orderservice.jpa.OrderEntity;

public interface OrderService {
    OrderDto createOrder(OrderDto orderDto);
    OrderDto getOrderByOrderId(String orderId);
    Iterable<OrderEntity> getOrdersByUserId(String userId);
}
