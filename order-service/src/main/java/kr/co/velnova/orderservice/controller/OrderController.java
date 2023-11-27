package kr.co.velnova.orderservice.controller;

import kr.co.velnova.orderservice.dto.OrderDto;
import kr.co.velnova.orderservice.jpa.OrderRepository;
import kr.co.velnova.orderservice.messagequeue.KafkaProducer;
import kr.co.velnova.orderservice.messagequeue.OrderProducer;
import kr.co.velnova.orderservice.service.OrderService;
import kr.co.velnova.orderservice.vo.RequestOrder;
import kr.co.velnova.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/order-service")
public class OrderController {

    private final OrderService service;
    private final Environment env;
    private final OrderRepository orderRepository;
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @GetMapping("/health_check")
    public String status() {
        return String.format("It's Working in Order Service on PORT %s", env.getProperty("local.server.port"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/orders")
    public ResponseOrder createOrder(@PathVariable String userId, @RequestBody RequestOrder orderDetails) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderDto orderDto = mapper.map(orderDetails, OrderDto.class);
        orderDto.setUserId(userId);

        /* jpa */
//        OrderDto createDto = service.createOrder(orderDto);
//        ResponseOrder returnValue = mapper.map(createDto, ResponseOrder.class);

        /* kafka */
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDetails.getQty() * orderDetails.getUnitPrice());


        /* send this order to the kafka */
        kafkaProducer.send("example-catalog-topic", orderDto);
        orderProducer.send("orders", orderDto);

        ResponseOrder returnValue = mapper.map(orderDetails, ResponseOrder.class);
        return returnValue;
    }

//    @GetMapping("/{userId}/orders/{orderId}")
//    public ResponseOrder getOrder(@PathVariable String userId, @PathVariable String orderId) {
//
//                return
//    }

    @GetMapping("/{userId}/orders")
    public List<ResponseOrder> getOrders(@PathVariable String userId) {
        List<ResponseOrder> result = new ArrayList<>();
        service.getOrdersByUserId(userId).forEach(orderEntity -> {
            result.add(new ModelMapper().map(orderEntity, ResponseOrder.class));
        });

        return result;
    }
}
