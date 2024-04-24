package com.gui9394.order.process;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FileLine(

        Long userId,

        String userName,

        Long orderId,

        LocalDate orderDate,

        Long productId,

        BigDecimal productValue

) {
}
