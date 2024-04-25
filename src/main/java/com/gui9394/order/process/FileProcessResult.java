package com.gui9394.order.process;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.batch.core.BatchStatus;

import java.time.Duration;

@Schema(description = "Informações da execução do processamento do arquivo")
public record FileProcessResult(

        @Schema(
                description = "Identificador de rastreio dos logs",
                example = "662a19fff04a55942b856d4a3adcffde"
        )
        String traceId,

        @Schema(
                description = "Identificador da execução do processamento do arquivo",
                example = "1"
        )
        Long id,

        @Schema(
                description = "Status da execução do processamento do arquivo",
                example = "COMPLETED"
        )
        BatchStatus status,

        @Schema(
                description = "Duração da execução do processamento do arquivo",
                example = "PT0.1327529S"
        )
        Duration duration

) {
}
