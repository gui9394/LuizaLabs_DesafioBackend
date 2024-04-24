package com.gui9394.order.file_process;

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
