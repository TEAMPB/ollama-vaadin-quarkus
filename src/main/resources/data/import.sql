-- Orders mit verschiedenen Zuständen
INSERT INTO T_ORDER (order_start, order_shipping_date, customer_id, status)
VALUES
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '2' DAY, '1234567890', 'OPEN'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '2' DAY, '1234567891', 'READY'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '1' DAY, '1234567892', 'SHIPPED'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '2' DAY, '1234567893', 'IN_PROCESS'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '3' DAY, '1234567894', 'OPEN'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '1' DAY, '1234567895', 'SHIPPED'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '1' DAY, '1234567896', 'READY'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '4' DAY, '1234567897', 'OPEN'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '1' DAY, '1234567898', 'IN_PROCESS'),
    (CURRENT_TIMESTAMP, CURRENT_TIMESTAMP + INTERVAL '3' DAY, '1234567899', 'READY');

-- OrderItems für die jeweiligen Orders
INSERT INTO T_ORDERITEM (order_id, item, total_qty, act_qty)
VALUES
    (1, 'Item1', 10, 0),
    (2, 'Item2', 5, 5),
    (3, 'Item3', 7, 7),
    (4, 'Item4', 3, 1),
    (5, 'Item5', 8, 0),
    (6, 'Item6', 4, 4),
    (7, 'Item7', 6, 6),
    (8, 'Item8', 2, 0),
    (9, 'Item9', 9, 7),
    (10, 'Item10', 1, 1);