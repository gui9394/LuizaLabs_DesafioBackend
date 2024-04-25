package com.gui9394.order.process;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Pedidos")
@RestController
@RequiredArgsConstructor
public class FileProcessController {

    private final FileProcessService fileProcessService;

    @Operation(
            summary = "Processamento do arquivo de pedidos",
            description = """
                    # Processamento do arquivo de pedidos
                    Um arquivo e utilizado para alimentar os registros da aplicação.
                    Cada linha representa um registro e deve possuir o seguinte formato:
                    
                    | Campo            | Tamanho | Tipo                         |
                    |------------------|---------|------------------------------|
                    | Id do usuário    | 10      | Numérico                     |
                    | Nome do usuário  | 45      | Texto                        |
                    | Id do pedido     | 10      | Numérico                     |
                    | Id do produto    | 10      | Numérico                     |
                    | Valor do produto | 12      | Decimal                      |
                    | Data do pedido   | 8       | Numérico (formato: yyyymmdd) |
                    
                    Ao processar os registros pode acontecer os seguintes cenários:
                    
                    - Sucesso
                      - ORDER_SAVED - Um novo produto e pedido foi processado.
                      - USER_UPDATED - O produto e pedido já foi processado, apenas informações do usuário foi atualizada.
                    - Erro
                      - ORDER_PRODUCT_DUPLICATED - No mesmo pedido o mesmo produto estava com outro valor.
                      - ORDER_USER_DIFFERENT - O pedido já tinha sido processado, mas para outro usuário.
                    
                    Em caso de erro ao processar alguma linha o processamento vai prosseguir e registrar no log as informações do erro.
                    
                    Exemplo do arquivo:
                    ```plain
                    0000000198                            Aurelia Davis DVM00000018220000000001      617.0920210805
                    0000000124                                Roosevelt Orn00000011360000000004       496.920210613
                    0000000074                          Orlando Hagenes DVM00000006930000000004      269.8220211009
                    0000000075                                    Sha Olson00000007020000000002     1419.6920210718
                    ```
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Processamento executado."
    )
    @ApiResponse(
            responseCode = "500",
            description = "Erro desconhecido ao executar o processamento."
    )
    @PostMapping(
            path = "/orders/process",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public FileProcessResult process(

            @RequestParam("file")
            MultipartFile file

    ) {
        return fileProcessService.process(file.getResource());
    }

}
