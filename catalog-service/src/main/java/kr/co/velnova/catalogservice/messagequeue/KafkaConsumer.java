package kr.co.velnova.catalogservice.messagequeue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.velnova.catalogservice.jpa.CatalogEntity;
import kr.co.velnova.catalogservice.jpa.CatalogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Component
public class KafkaConsumer {
    private final CatalogRepository repository;

    @KafkaListener(topics = "example-catalog-topic")
    @Transactional
    public void updateQty(String kafkaMessage) {
        log.info("kafka Message: -> {}", kafkaMessage);

        Map<String, Object> map = new HashMap<>();

        ObjectMapper mapper = new ObjectMapper();
        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        Optional<CatalogEntity> optionalEntity = repository.findByProductId((String) map.get("productId"));

        if (optionalEntity.isPresent()) {
            CatalogEntity entity = optionalEntity.get();
            entity.setStock(entity.getStock() - (Integer) map.get("qty"));
//            repository.save(entity);
        }

    }
}
