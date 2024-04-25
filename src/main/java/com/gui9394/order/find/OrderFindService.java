package com.gui9394.order.find;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

@Service
public class OrderFindService {

    private final Long findByDateIntervalMax;
    private final OrderFindRepository orderFindRepository;

    public OrderFindService(
            @Value("${order.consult.find-by-date-interval-max}") Long findByDateIntervalMax,
            OrderFindRepository orderFindRepository
    ) {
        this.findByDateIntervalMax = findByDateIntervalMax;
        this.orderFindRepository = orderFindRepository;
    }

    public Collection<User> findBy(Set<Long> ids, LocalDate startDate, LocalDate endDate) {
        validateParams(ids, startDate, endDate);

        var rowSet = Objects.nonNull(ids) && !ids.isEmpty()
                ? orderFindRepository.findByIdIn(ids)
                : orderFindRepository.findByDateBetween(startDate, endDate);

        return buildResult(rowSet);
    }

    private void validateParams(Set<Long> ids, LocalDate startDate, LocalDate endDate) {
        if (Objects.nonNull(ids) && !ids.isEmpty()) {
            if (Objects.nonNull(startDate) || Objects.nonNull(endDate)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Apenas é permitido pesquisar por 'id' ou 'start_date' e 'end_date'."
                );
            }
        }
        else {
            if (Objects.isNull(startDate) || Objects.isNull(endDate) || endDate.isBefore(startDate)) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "'start_date' deve ser igual ou anterior 'end_date'."
                );
            }

            if (endDate.isAfter(startDate.plusDays(findByDateIntervalMax))) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "O intervalo entre 'start_date' e 'end_date' deve ser no máximo " + findByDateIntervalMax + " dias."
                );
            }
        }
    }

    private Collection<User> buildResult(SqlRowSet rowSet) {
        var usersById = new HashMap<Long, User>();

        while (rowSet.next()) {
            usersById.computeIfAbsent(
                            rowSet.getLong("user_id"),
                            userId -> new User(
                                    userId,
                                    rowSet.getString("user_name")
                            )
                    )
                    .getOrdersById()
                    .computeIfAbsent(
                            rowSet.getLong("order_id"),
                            orderId -> new Order(
                                    orderId,
                                    rowSet.getTimestamp("order_date").toLocalDateTime().toLocalDate(),
                                    rowSet.getBigDecimal("order_total")
                            )
                    )
                    .getProductsById()
                    .computeIfAbsent(
                            rowSet.getLong("product_id"),
                            productId -> new Product(
                                    productId,
                                    rowSet.getBigDecimal("product_value")
                            )
                    );
        }

        return usersById.values();
    }

}
