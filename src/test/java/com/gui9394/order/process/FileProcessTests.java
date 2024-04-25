package com.gui9394.order.process;

import com.gui9394.order.TestBase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.mock.web.MockMultipartFile;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FileProcessTests extends TestBase {

    @Autowired
    JdbcClient jdbcClient;

    @Test
    void semArquivo() throws Exception {
        mockMvc.perform(multipart("/orders/process"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void executadoComSucesso() throws Exception {
        var resource = new ClassPathResource("data.txt");

        mockMvc.perform(multipart("/orders/process")
                        .file(new MockMultipartFile(
                                "file",
                                resource.getFilename(),
                                MediaType.MULTIPART_FORM_DATA_VALUE,
                                resource.getInputStream()
                        )))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.traceId").value(notNullValue()))
                .andExpect(jsonPath("$.id").value(notNullValue()))
                .andExpect(jsonPath("$.status").value("COMPLETED"))
                .andExpect(jsonPath("$.duration").value(notNullValue()));

        var countOrdersProducts = jdbcClient.sql("""
                SELECT COUNT(*)
                FROM orders_products
                WHERE (order_id = 1822 AND product_id = 1 AND product_value = 617.09)
                OR (order_id = 702 AND product_id = 2 AND product_value = 1419.69)
                OR (order_id = 702 AND product_id = 3 AND product_value = 1419.69)
                OR (order_id = 1136 AND product_id = 4 AND product_value = 496.90)
                """)
                .query()
                .singleValue();
        assertEquals(4L, countOrdersProducts);

        var countOrders = jdbcClient.sql("""
                SELECT COUNT(*)
                FROM orders
                WHERE (user_id = 198 AND id = 1822 AND date = '2021-10-09 00:00:00.000' AND total = 617.09)
                OR (user_id = 198 AND id = 1136 AND date = '2021-10-09 00:00:00.000' AND total = 496.90)
                OR (user_id = 75 AND id = 702 AND date = '2021-10-09 00:00:00.000' AND total = 2839.38)
                """)
                .query()
                .singleValue();
        assertEquals(3L, countOrders);

        var countUsers = jdbcClient.sql("""
                SELECT COUNT(*)
                FROM users
                WHERE (id = 198 AND name = 'Roosevelt')
                OR (id = 75 AND name = 'Sha Olson')
                """)
                .query()
                .singleValue();
        assertEquals(2L, countUsers);
    }

}
