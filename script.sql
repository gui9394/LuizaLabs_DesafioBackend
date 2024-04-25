-- SPRING BATCH

CREATE TABLE BATCH_JOB_INSTANCE  (
	JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,
	VERSION BIGINT ,
	JOB_NAME VARCHAR(100) NOT NULL,
	JOB_KEY VARCHAR(32) NOT NULL,
	constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ;

CREATE TABLE BATCH_JOB_EXECUTION  (
	JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
	VERSION BIGINT  ,
	JOB_INSTANCE_ID BIGINT NOT NULL,
	CREATE_TIME TIMESTAMP NOT NULL,
	START_TIME TIMESTAMP DEFAULT NULL ,
	END_TIME TIMESTAMP DEFAULT NULL ,
	STATUS VARCHAR(10) ,
	EXIT_CODE VARCHAR(2500) ,
	EXIT_MESSAGE VARCHAR(2500) ,
	LAST_UPDATED TIMESTAMP,
	constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
	references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ;

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
	JOB_EXECUTION_ID BIGINT NOT NULL ,
	PARAMETER_NAME VARCHAR(100) NOT NULL ,
	PARAMETER_TYPE VARCHAR(100) NOT NULL ,
	PARAMETER_VALUE VARCHAR(2500) ,
	IDENTIFYING CHAR(1) NOT NULL ,
	constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE TABLE BATCH_STEP_EXECUTION  (
	STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
	VERSION BIGINT NOT NULL,
	STEP_NAME VARCHAR(100) NOT NULL,
	JOB_EXECUTION_ID BIGINT NOT NULL,
	CREATE_TIME TIMESTAMP NOT NULL,
	START_TIME TIMESTAMP DEFAULT NULL ,
	END_TIME TIMESTAMP DEFAULT NULL ,
	STATUS VARCHAR(10) ,
	COMMIT_COUNT BIGINT ,
	READ_COUNT BIGINT ,
	FILTER_COUNT BIGINT ,
	WRITE_COUNT BIGINT ,
	READ_SKIP_COUNT BIGINT ,
	WRITE_SKIP_COUNT BIGINT ,
	PROCESS_SKIP_COUNT BIGINT ,
	ROLLBACK_COUNT BIGINT ,
	EXIT_CODE VARCHAR(2500) ,
	EXIT_MESSAGE VARCHAR(2500) ,
	LAST_UPDATED TIMESTAMP,
	constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
	STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
	SHORT_CONTEXT VARCHAR(2500) NOT NULL,
	SERIALIZED_CONTEXT TEXT ,
	constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
	references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ;

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
	JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
	SHORT_CONTEXT VARCHAR(2500) NOT NULL,
	SERIALIZED_CONTEXT TEXT ,
	constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
	references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ;

CREATE SEQUENCE BATCH_STEP_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE BATCH_JOB_EXECUTION_SEQ MAXVALUE 9223372036854775807 NO CYCLE;
CREATE SEQUENCE BATCH_JOB_SEQ MAXVALUE 9223372036854775807 NO CYCLE;

-- Order API

CREATE TABLE "user" (
	id int8 NOT NULL,
	"name" varchar(45) NOT NULL,
	CONSTRAINT user_pk PRIMARY KEY (id)
);

CREATE TABLE "order" (
	id int8 NOT NULL,
	"date" timestamp NOT NULL,
	user_id int8 NOT NULL,
	total numeric(10, 2) NOT NULL,
	CONSTRAINT order_pk PRIMARY KEY (id),
	CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES public."user"(id)
);

CREATE TABLE "order_product" (
	order_id int8 NOT NULL,
	product_id int8 NOT NULL,
	product_value numeric(10, 2) NOT NULL,
	value numeric(38, 2) NULL,
	CONSTRAINT order_product_pk PRIMARY KEY (order_id, product_id),
	CONSTRAINT order_fk FOREIGN KEY (order_id) REFERENCES public."order"(id)
);

CREATE OR REPLACE PROCEDURE process_file_line(
	IN p_user_id "user".id%type,
	IN p_user_name "user"."name"%type,
	IN p_order_id "order".id%type,
	IN p_order_date "order"."date"%type,
	IN p_product_id "order_product".product_id%type,
	IN p_product_value "order_product".product_value%type,
	INOUT r_result varchar(45)
)
LANGUAGE plpgsql
AS $$
BEGIN
    SELECT
    CASE
        WHEN product_value != p_product_value THEN 'ORDER_PRODUCT_DUPLICATED'
        ELSE 'ORDER_PRODUCT_SAVED_BEFORE'
    END
    INTO r_result
    FROM "order_product" op
    WHERE op.order_id = p_order_id
    AND op.product_id = p_product_id;

    IF r_result = 'ORDER_PRODUCT_DUPLICATED' THEN
        RETURN;
    END IF;

    SELECT
    CASE
        WHEN o.user_id != p_user_id THEN 'ORDER_USER_DIFFERENT'
        ELSE r_result
    END
    INTO r_result
    FROM "order" o
    WHERE o.id = p_order_id;

    IF r_result = 'ORDER_USER_DIFFERENT' THEN
        RETURN;
    END IF;

    MERGE INTO "user" u
    USING (
        SELECT
            p_user_id AS id,
            p_user_name AS name
    ) AS ut
    ON (u.id = ut.id)
    WHEN MATCHED AND (u.name != ut.name) THEN UPDATE SET
        name = ut.name
    WHEN NOT MATCHED THEN
        INSERT (id, name)
        VALUES (ut.id, ut.name);

    IF r_result = 'ORDER_PRODUCT_SAVED_BEFORE' THEN
        r_result := 'USER_UPDATED';
        RETURN;
    END IF;

    MERGE INTO "order" o
    USING (
        SELECT
            p_order_id AS id,
            p_order_date AS date,
            p_product_value AS total,
            p_user_id AS user_id
    ) AS ot
    ON (
        o.id = ot.id
        AND o.user_id = ot.user_id
    )
    WHEN MATCHED THEN UPDATE SET
        total = o.total + ot.total
    WHEN NOT MATCHED THEN
        INSERT (id, date, user_id, total)
        VALUES (ot.id, ot.date, ot.user_id, ot.total);

    INSERT INTO "order_product"(order_id, product_id, product_value)
    VALUES (p_order_id, p_product_id, p_product_value);

    r_result := 'ORDER_SAVED';
END;
$$