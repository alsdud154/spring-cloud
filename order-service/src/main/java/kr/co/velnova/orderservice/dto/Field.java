package kr.co.velnova.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Field {
    private String type;
    private boolean optional;
    private String field;
}
