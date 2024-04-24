package com.gui9394.order.process;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FileLineProcessResult {

    // SUCCESS
    ORDER_SAVED(false),
    USER_UPDATED(false),

    // ERROR
    ORDER_PRODUCT_DUPLICATED(true),
    ORDER_USER_DIFFERENT(true);

    private final boolean error;

}
