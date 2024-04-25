package com.gui9394.order.find;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class OrderFindRepository {

    private final JdbcClient jdbcClient;

    private static final String FIND_BY_ID_IN = """
            SELECT
                u.id AS user_id,
                u.name AS user_name,
                o.id AS order_id,
                o.date AS order_date,
                o.total AS order_total,
                op.product_id AS product_id,
                op.product_value AS product_value
            FROM orders o
            LEFT JOIN users u ON u.id = o.user_id
            LEFT JOIN orders_products op ON op.order_id = o.id
            WHERE o.id IN (:orderIds)
            """;

    private static final String FIND_BY_DATE_BETWEEN = """
            SELECT
                u.id AS user_id,
                u.name AS user_name,
                o.id AS order_id,
                o.date AS order_date,
                o.total AS order_total,
                op.product_id AS product_id,
                op.product_value AS product_value
            FROM orders o
            LEFT JOIN users u ON u.id = o.user_id
            LEFT JOIN orders_products op ON op.order_id = o.id
            WHERE o.date BETWEEN :startDate AND :endDate
            """;

    public SqlRowSet findByIdIn(Set<Long> orderIds) {
        return jdbcClient.sql(FIND_BY_ID_IN)
                .param("orderIds", orderIds)
                .query()
                .rowSet();
    }

    public SqlRowSet findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return jdbcClient.sql(FIND_BY_DATE_BETWEEN)
                .param("startDate", startDate)
                .param("endDate", endDate)
                .query()
                .rowSet();
    }

}
