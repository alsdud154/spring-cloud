package kr.co.velnova.apigatewayservice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {
//    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(r -> r.path("/first-service/**") // 해당 path로 요청이 오면
                        .filters(f -> f.addRequestHeader("first-request", "first-request-value")
                                .addResponseHeader("first-response", "first-response-value")) // filter를 적용해서
                        .uri("http://localhost:8081")) // 해당 uri로 보낸다
                .route(r -> r.path("/second-service/**") // 해당 path로 요청이 오면
                        .filters(f -> f.addRequestHeader("second-request", "second-request-value")
                                .addResponseHeader("second-response", "second-response-value")) // filter를 적용해서
                        .uri("http://localhost:8082")) // 해당 uri로 보낸다
                .build();
    }
}
