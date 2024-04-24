package com.gui9394.order.find;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Tag(name = "Pedidos")
@RestController
@RequiredArgsConstructor
public class OrderFindController {

    private final OrderFindService orderFindService;

    @GetMapping(path = "/orders")
    public Collection<User> find(

            @Schema(description = "Id do pedido")
            @RequestParam(value = "id", required = false)
            Set<Long> ids,

            @Schema(description = "Inicio do período da data do pedido")
            @RequestParam(value = "start_date", required = false)
            LocalDate startDate,

            @Schema(description = "Fim do período da data do pedido")
            @RequestParam(value = "end_date", required = false)
            LocalDate endDate

    ) {
        return orderFindService.findBy(ids, startDate, endDate);
    }

}
