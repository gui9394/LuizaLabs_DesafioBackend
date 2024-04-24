package com.gui9394.order.find;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Schema(description = "Informações do Usuário")
@RequiredArgsConstructor
@Getter
public class User {

    @Schema(
            description = "Identificador do usuário",
            example = "1"
    )
    @JsonProperty("user_id")
    private final Long id;

    @Schema(
            description = "Nome do usuário",
            example = "João"
    )
    private final String name;

    @Getter(AccessLevel.PACKAGE)
    private final Map<Long, Order> ordersById = new HashMap<>();

    public Collection<Order> getOrders() {
        return ordersById.values();
    }

}
