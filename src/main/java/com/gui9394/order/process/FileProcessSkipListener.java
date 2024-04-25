package com.gui9394.order.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.file.FlatFileParseException;

@Slf4j
class FileProcessSkipListener implements SkipListener<FileLine, FileLine> {

    @Override
    public void onSkipInRead(Throwable error) {
        if (error instanceof FlatFileParseException parseException) {
            log.error(
                    "error=INVALID_FORMAT lineNumber={} lineRaw=\"{}\"",
                    parseException.getLineNumber(),
                    parseException.getInput()
            );
        }
        else {
            log.error("error=UNKNOWN", error);
        }
    }

    @Override
    public void onSkipInWrite(FileLine item, Throwable error) {
        if (error instanceof FileLineProcessException processException) {
            log.error(
                    "error={} userId={} userName=\"{}\" orderId={} orderDate={} productId={} productValue={}",
                    processException.getResult(),
                    item.userId(),
                    item.userName(),
                    item.orderId(),
                    item.orderDate(),
                    item.productId(),
                    item.productValue()
            );
        }
        else {
            log.error(
                    "error=UNKNOWN userId={} userName=\"{}\" orderId={} orderDate={} productId={} productValue={}",
                    item.userId(),
                    item.userName(),
                    item.orderId(),
                    item.orderDate(),
                    item.productId(),
                    item.productValue(),
                    error
            );
        }
    }

}
