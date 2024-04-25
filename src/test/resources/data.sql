INSERT INTO users(id, name)
VALUES
    (1000, 'Terra Daniel DDS'),
    (1001, 'Gail Bradtke');

INSERT INTO orders(id, date, user_id, total)
VALUES
    (2000, '2021-09-28 00:00:00.000', 1001, 100.1),
    (2001, '2021-09-29 00:00:00.000', 1001, 250.5),
    (2002, '2021-09-23 00:00:00.000', 1000, 550.6);

INSERT INTO orders_products(order_id, product_id, product_value)
VALUES
    (2000, 3000, 100.1),
    (2001, 3000, 100.1),
    (2001, 3001, 150.4),
    (2002, 3000, 100.1),
    (2002, 3001, 150.4),
    (2002, 3003, 300.1);
