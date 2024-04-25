package com.gui9394.order.find;

import com.gui9394.order.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.Matchers.empty;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class OrderFindTests extends TestBase {

    @Autowired
    public MockMvc mockMvc;

    @Value("${order.consult.find-by-date-interval-max}")
    Long findByDateIntervalMax;

    @Test
    void semFiltro() throws Exception {
        mockMvc.perform(get("/orders"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("'start_date' deve ser igual ou anterior 'end_date'."));
    }

    @Test
    void comTodosOsFiltros() throws Exception {
        mockMvc.perform(get("/orders")
                        .param("id", "1")
                        .param("start_date", "2020-01-01")
                        .param("end_date", "2020-01-01"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Apenas é permitido pesquisar por 'id' ou 'start_date' e 'end_date'."));
    }

    @Test
    void consultaPorIdSemResultado() throws Exception {
        mockMvc.perform(get("/orders")
                        .param("id", "1000000"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(empty()));
    }

    @Test
    void consultaPorIdCom1Resultado() throws Exception {
        mockMvc.perform(get("/orders")
                        .param("id", "2000"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].name").value("Gail Bradtke"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders.size()").value(1))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].total").value("100.10"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].date").value("2021-09-28"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].products.size()").value(1))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].products[?(@.product_id == 3000)].value").value(
                        "100.10"));
    }

    @Test
    void consultaPorIdCom2ResultadoDoMesmoUsuario() throws Exception {
        mockMvc.perform(get("/orders")
                        .param("id", "2000")
                        .param("id", "2001"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].name").value("Gail Bradtke"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders.size()").value(2))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].total").value("100.10"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].date").value("2021-09-28"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].products.size()").value(1))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].products[?(@.product_id == 3000)].value").value(
                        "100.10"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2001)].total").value("250.50"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2001)].date").value("2021-09-29"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2001)].products.size()").value(2))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1001)].orders[?(@.order_id == 2001)].products[?(@.product_id == 3000)].value").value(
                        "100.10"))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1001)].orders[?(@.order_id == 2001)].products[?(@.product_id == 3001)].value").value(
                        "150.40"));
    }

    @Test
    void consultaPorIdCom2ResultadoDeUsuariosDiferentes() throws Exception {
        mockMvc.perform(get("/orders")
                        .param("id", "2000")
                        .param("id", "2002"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[?(@.user_id == 1000)].name").value("Terra Daniel DDS"))
                .andExpect(jsonPath("$[?(@.user_id == 1000)].orders.size()").value(1))
                .andExpect(jsonPath("$[?(@.user_id == 1000)].orders[?(@.order_id == 2002)].total").value("550.60"))
                .andExpect(jsonPath("$[?(@.user_id == 1000)].orders[?(@.order_id == 2002)].date").value("2021-09-23"))
                .andExpect(jsonPath("$[?(@.user_id == 1000)].orders[?(@.order_id == 2002)].products.size()").value(3))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1000)].orders[?(@.order_id == 2002)].products[?(@.product_id == 3000)].value").value(
                        "100.10"))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1000)].orders[?(@.order_id == 2002)].products[?(@.product_id == 3001)].value").value(
                        "150.40"))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1000)].orders[?(@.order_id == 2002)].products[?(@.product_id == 3003)].value").value(
                        "300.10"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].name").value("Gail Bradtke"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders.size()").value(1))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].total").value("100.10"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].date").value("2021-09-28"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].products.size()").value(1))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].products[?(@.product_id == 3000)].value").value(
                        "100.10"));
    }

    @Test
    void consultaPorIntervaloDataFinalAnteriorAInicial() throws Exception {
        var now = LocalDate.of(2022, 9, 28);

        mockMvc.perform(get("/orders")
                        .param("start_date", now.plusDays(findByDateIntervalMax).toString())
                        .param("end_date", now.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("'start_date' deve ser igual ou anterior 'end_date'."));
    }

    @Test
    void consultaPorIntervaloSuperiorAoMaximo() throws Exception {
        var now = LocalDate.of(2022, 9, 28);

        mockMvc.perform(get("/orders")
                        .param("start_date", now.toString())
                        .param("end_date", now.plusDays(findByDateIntervalMax + 1).toString()))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("O intervalo entre 'start_date' e 'end_date' deve ser no máximo " + findByDateIntervalMax + " dias."));
    }

    @Test
    void consultaPorIntervaloMaximo() throws Exception {
        var now = LocalDate.of(2022, 9, 28);

        mockMvc.perform(get("/orders")
                        .param("start_date", now.toString())
                        .param("end_date", now.plusDays(findByDateIntervalMax).toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(empty()));
    }

    @Test
    void consultaPorIntervaloCom1Resultado() throws Exception {
        mockMvc.perform(get("/orders")
                        .param("start_date", "2021-09-28")
                        .param("end_date", "2021-09-28"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].name").value("Gail Bradtke"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders.size()").value(1))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].total").value("100.10"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].date").value("2021-09-28"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].products.size()").value(1))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].products[?(@.product_id == 3000)].value").value(
                        "100.10"));
    }

    @Test
    void consultaPorIntervaloCom2ResultadoDeUsuariosDiferentes() throws Exception {
        mockMvc.perform(get("/orders")
                        .param("start_date", "2021-09-23")
                        .param("end_date", "2021-09-28"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[?(@.user_id == 1000)].name").value("Terra Daniel DDS"))
                .andExpect(jsonPath("$[?(@.user_id == 1000)].orders.size()").value(1))
                .andExpect(jsonPath("$[?(@.user_id == 1000)].orders[?(@.order_id == 2002)].total").value("550.60"))
                .andExpect(jsonPath("$[?(@.user_id == 1000)].orders[?(@.order_id == 2002)].date").value("2021-09-23"))
                .andExpect(jsonPath("$[?(@.user_id == 1000)].orders[?(@.order_id == 2002)].products.size()").value(3))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1000)].orders[?(@.order_id == 2002)].products[?(@.product_id == 3000)].value").value(
                        "100.10"))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1000)].orders[?(@.order_id == 2002)].products[?(@.product_id == 3001)].value").value(
                        "150.40"))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1000)].orders[?(@.order_id == 2002)].products[?(@.product_id == 3003)].value").value(
                        "300.10"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].name").value("Gail Bradtke"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders.size()").value(1))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].total").value("100.10"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].date").value("2021-09-28"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].products.size()").value(1))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].products[?(@.product_id == 3000)].value").value(
                        "100.10"));
    }

    @Test
    void consultaPorIntervaloCom2ResultadoDoMesmoUsuario() throws Exception {
        mockMvc.perform(get("/orders")
                        .param("start_date", "2021-09-28")
                        .param("end_date", "2021-09-29"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].name").value("Gail Bradtke"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders.size()").value(2))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].total").value("100.10"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].date").value("2021-09-28"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].products.size()").value(1))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1001)].orders[?(@.order_id == 2000)].products[?(@.product_id == 3000)].value").value(
                        "100.10"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2001)].total").value("250.50"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2001)].date").value("2021-09-29"))
                .andExpect(jsonPath("$[?(@.user_id == 1001)].orders[?(@.order_id == 2001)].products.size()").value(2))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1001)].orders[?(@.order_id == 2001)].products[?(@.product_id == 3000)].value").value(
                        "100.10"))
                .andExpect(jsonPath(
                        "$[?(@.user_id == 1001)].orders[?(@.order_id == 2001)].products[?(@.product_id == 3001)].value").value(
                        "150.40"));
    }

}
