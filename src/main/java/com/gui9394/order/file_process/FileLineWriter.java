package com.gui9394.order.file_process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@Slf4j
public class FileLineWriter implements ItemWriter<FileLine> {

    private final SimpleJdbcCall simpleJdbcCall;

    public FileLineWriter(DataSource dataSource) {
        this.simpleJdbcCall = new SimpleJdbcCall(dataSource)
                .withProcedureName("process_file_line");
    }

    @Override
    public void write(Chunk<? extends FileLine> chunk) {
        chunk.forEach(this::processLine);
    }

    private void processLine(FileLine item) {
        var param = new MapSqlParameterSource()
                .addValue("p_user_id", item.userId())
                .addValue("p_user_name", item.userName())
                .addValue("p_order_id", item.orderId())
                .addValue("p_order_date", item.orderDate())
                .addValue("p_product_id", item.productId())
                .addValue("p_product_value", item.productValue())
                .addValue("r_result", null);

        var result = FileLineProcessResult.valueOf(simpleJdbcCall.executeObject(String.class, param));

        if (result.isError()) {
            log.error(
                    "result={} userId={} userName={} orderId={} orderDate={} productId={} productValue={}",
                    result,
                    item.userId(),
                    item.userName(),
                    item.orderId(),
                    item.orderDate(),
                    item.productId(),
                    item.productValue()
            );
        }
    }

}