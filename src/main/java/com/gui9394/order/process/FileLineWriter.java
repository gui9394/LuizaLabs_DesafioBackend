package com.gui9394.order.process;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class FileLineWriter implements ItemWriter<FileLine> {

    private final SimpleJdbcCall simpleJdbcCall;

    public FileLineWriter(DataSource dataSource) {
        this.simpleJdbcCall = new SimpleJdbcCall(dataSource)
                .withProcedureName("process_file_line");
    }

    @Override
    public void write(Chunk<? extends FileLine> chunk) {
        var iterator = chunk.iterator();

        while (iterator.hasNext()) {
            var item = iterator.next();
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
                iterator.remove(new FileLineProcessException(item, result));
            }
        }
    }

}