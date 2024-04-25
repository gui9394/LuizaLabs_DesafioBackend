package com.gui9394.order.find;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(
            summary = "Consulta de pedidos",
            description = """
                    # Consulta de pedidos
                    A consulta pode ser realizada por ids dos pedidos ou intervalo de data.\n
                    Não é possível realizar consulta por ids e intervalo de data simultaneamente.
                    
                    ## Consulta de pedidos por ids
                    Você pode passar o id que deseja consultar no parâmetro **id**.\n
                    Caso queria consultar mais de um pedido na mesma consulta pode repetir o parâmetro **id** para cada pedido que deseja consultar.
                    
                    ## Consulta de pedidos por intervalo de data
                    Você pode passar a data de inicio do intervalo no parâmetro **start_date** e a data final do intervalo no parâmetro **end_date** que deseja consultar.\n
                    O intervalo máximo e de ${order.consult.find-by-date-interval-max} dias.
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Itens encontrados na consulta."
    )
    @ApiResponse(
            responseCode = "500",
            description = "Erro ao executar consulta.",
            content = @Content
    )
    @GetMapping(path = "/orders")
    public Collection<User> find(

            @Schema(description = "Id do pedido")
            @RequestParam(value = "id", required = false)
            Set<Long> ids,

            @Schema(description = "Inicio do intervalo da data do pedido")
            @RequestParam(value = "start_date", required = false)
            LocalDate startDate,

            @Schema(description = "Fim do intervalo da data do pedido")
            @RequestParam(value = "end_date", required = false)
            LocalDate endDate

    ) {
        return orderFindService.findBy(ids, startDate, endDate);
    }

}
