package com.gui9394.order.find;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Informações do produto")
public record Product(
        @Schema(
                description = "Identificador do produto",
                example = "1"
        )
        @JsonProperty("product_id")
        Long id,
        @Schema(
                description = "Data do pedido",
                example = "500.00",
                type = "string"
        )
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal value
) {
}
