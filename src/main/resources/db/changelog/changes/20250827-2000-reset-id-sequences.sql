-- This script resets the auto-increment counters for all tables that have manually inserted IDs.
-- This ensures that new entities created in tests will get a unique ID and not conflict with the seeded data.
-- The syntax is specific to H2 and may need adjustment for other databases like PostgreSQL.
-- ALTER TABLE member ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM member);
-- ALTER TABLE meal ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM meal);
-- ALTER TABLE cart_item ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM cart_item);
-- ALTER TABLE tag ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM tag);
-- ALTER TABLE coupon ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM coupon);
-- ALTER TABLE "order" ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM "order");
-- ALTER TABLE order_item ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM order_item);
-- ALTER TABLE payment ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM payment);
-- Reset auto-increment sequences based on current MAX(id) values

SELECT setval(pg_get_serial_sequence('member', 'id'), COALESCE(MAX(id), 1), true) FROM member;
SELECT setval(pg_get_serial_sequence('meal', 'id'), COALESCE(MAX(id), 1), true) FROM meal;
SELECT setval(pg_get_serial_sequence('cart_item', 'id'), COALESCE(MAX(id), 1), true) FROM cart_item;
SELECT setval(pg_get_serial_sequence('tag', 'id'), COALESCE(MAX(id), 1), true) FROM tag;
SELECT setval(pg_get_serial_sequence('coupon', 'id'), COALESCE(MAX(id), 1), true) FROM coupon;
SELECT setval(pg_get_serial_sequence('"order"', 'id'), COALESCE(MAX(id), 1), true) FROM "order";
SELECT setval(pg_get_serial_sequence('order_item', 'id'), COALESCE(MAX(id), 1), true) FROM order_item;
SELECT setval(pg_get_serial_sequence('payment', 'id'), COALESCE(MAX(id), 1), true) FROM payment;
