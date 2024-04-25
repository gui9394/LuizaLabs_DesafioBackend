# Desafio técnico - Vertical Logística

## Endpoints

## Processamento do arquivo de pedidos
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

Exemplo do arquivo:
```plain
0000000198                            Aurelia Davis DVM00000018220000000001      617.0920210805
0000000124                                Roosevelt Orn00000011360000000004       496.920210613
0000000074                          Orlando Hagenes DVM00000006930000000004      269.8220211009
0000000075                                    Sha Olson00000007020000000002     1419.6920210718
```

Ao processar os registros pode acontecer os seguintes cenários:

- Sucesso
  - ORDER_SAVED - Um novo produto e pedido foi processado.
  - USER_UPDATED - O produto e pedido já foi processado, apenas informações do usuário foi atualizada.
- Erro
  - ORDER_PRODUCT_DUPLICATED - No mesmo pedido o mesmo produto estava com outro valor.
  - ORDER_USER_DIFFERENT - O pedido já tinha sido processado, mas para outro usuário.
  - INVALID_FORMAT - Registro com formato inválido.

Em caso de erro ao processar alguma linha o processamento vai prosseguir e registrar no log as informações do erro.

Exemplo de log:
```log
2024-04-19 06:35:09,562 type=INFO thread=http-nio-8080-exec-8 traceId=662a23cda2cc6e5141b49f59f26432fd spanId=10884a5f965434e5 class=FileProcessJobListener method=beforeJob line=17 id=152 status=STARTED fileName=data_3.txt
2024-04-19 06:35:09,671 type=ERROR thread=http-nio-8080-exec-8 traceId=662a23cda2cc6e5141b49f59f26432fd spanId=4297f15ed38fc731 class=FileProcessSkipListener method=onSkipInWrite line=27 error=ORDER_PRODUCT_DUPLICATED userId=74 userName="Orlando Hagenes DVM" orderId=693 orderDate=2021-10-09 productId=4 productValue=269.82
2024-04-19 06:35:09,671 type=ERROR thread=http-nio-8080-exec-8 traceId=662a23cda2cc6e5141b49f59f26432fd spanId=4297f15ed38fc731 class=FileProcessSkipListener method=onSkipInWrite line=27 error=ORDER_USER_DIFFERENT userId=72 userName="Sha Olson" orderId=702 orderDate=2021-07-18 productId=2 productValue=1419.69
2024-04-19 06:35:09,672 type=ERROR thread=http-nio-8080-exec-8 traceId=662a23cda2cc6e5141b49f59f26432fd spanId=4297f15ed38fc731 class=FileProcessSkipListener method=onSkipInWrite line=27 error=ORDER_PRODUCT_DUPLICATED userId=75 userName="Sha Olson" orderId=702 orderDate=2021-07-18 productId=2 productValue=1409.69
2024-04-19 06:35:09,672 type=ERROR thread=http-nio-8080-exec-8 traceId=662a23cda2cc6e5141b49f59f26432fd spanId=4297f15ed38fc731 class=FileProcessSkipListener method=onSkipInRead line=13 error=FILE_INVALID_FORMAT lineNumber=7 lineRaw="0000000074                          Orlando Hagenes  DVM00000006930000912123      269.8220211009"
2024-04-19 06:35:09,688 type=INFO thread=http-nio-8080-exec-8 traceId=662a23cda2cc6e5141b49f59f26432fd spanId=41b49f59f26432fd class=FileProcessJobListener method=afterJob line=37 id=152 status=COMPLETED durationMs=131 fileName=data_3.txt
```

## Consulta de pedidos
A consulta pode ser realizada por ids dos pedidos ou intervalo de data.

Não é possível realizar consulta por ids e intervalo de data simultaneamente.

### Consulta de pedidos por ids
Você pode passar o id que deseja consultar no parâmetro **id**.

Caso queria consultar mais de um pedido na mesma consulta pode repetir o parâmetro **id** para cada pedido que deseja consultar.

### Consulta de pedidos por intervalo de data
Você pode passar a data de inicio do intervalo no parâmetro **start_date** e a data final do intervalo no parâmetro **end_date** que deseja consultar.

O intervalo máximo pode ser configurado pela variável ORDER_CONSULT_FIND_BY_DATE_INTERVAL_MAX em dias.

## Paulo Silva

- [Github](https://github.com/gui9394)
- [LinkedIn](https://www.linkedin.com/in/gui9394)
