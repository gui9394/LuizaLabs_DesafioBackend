package com.gui9394.order.file_process;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequestScope
@Component
public class FileLineReader extends FlatFileItemReader<FileLine> {

    public FileLineReader() {
        var tokenizer = new FixedLengthTokenizer();
        tokenizer.setNames("userId", "userName", "orderId", "productId", "productValue", "orderDate");
        tokenizer.setColumns(
                new Range(1, 10),
                new Range(11, 55),
                new Range(56, 65),
                new Range(66, 75),
                new Range(76, 87),
                new Range(88, 95)
        );

        var mapper = new DefaultLineMapper<FileLine>();
        mapper.setFieldSetMapper(this::mapper);
        mapper.setLineTokenizer(tokenizer);

        setLineMapper(mapper);
    }

    private FileLine mapper(FieldSet fieldSet) {
        var userId = fieldSet.readLong("userId");
        var userName = fieldSet.readString("userName");
        var orderId = fieldSet.readLong("orderId");
        var orderDate = LocalDate.parse(fieldSet.readString("orderDate"), DateTimeFormatter.BASIC_ISO_DATE);
        var productId = fieldSet.readLong("productId");
        var productValue = fieldSet.readBigDecimal("productValue");

        return new FileLine(userId, userName, orderId, orderDate, productId, productValue);
    }

}
