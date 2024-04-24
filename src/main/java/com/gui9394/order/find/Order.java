package com.gui9394.order.find;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Schema(description = "Informações do pedido")
@RequiredArgsConstructor
@Getter
public class Order {

    @Schema(
            description = "Identificador do pedido",
            example = "1"
    )
    @JsonProperty("order_id")
    private final Long id;

    @Schema(
            description = "Data do pedido",
            example = "2021-01-01"
    )
    private final LocalDate date;

    @Schema(
            description = "Data do pedido",
            example = "500.00",
            type = "string"
    )
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final BigDecimal total;

    @Getter(AccessLevel.PACKAGE)
    private final Map<Long, Product> productsById = new HashMap<>();

    public Collection<Product> getProducts() {
        return productsById.values();
    }

}
