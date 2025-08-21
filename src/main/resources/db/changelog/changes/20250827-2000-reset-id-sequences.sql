-- This script resets the auto-increment counters for all tables that have manually inserted IDs.
-- This ensures that new entities created in tests will get a unique ID and not conflict with the seeded data.
-- The syntax is specific to H2 and may need adjustment for other databases like PostgreSQL.
ALTER TABLE member ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM member);
ALTER TABLE meal ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM meal);
ALTER TABLE cart_item ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM cart_item);
ALTER TABLE tag ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM tag);
ALTER TABLE coupon ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM coupon);
ALTER TABLE "order" ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM "order");
ALTER TABLE order_item ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM order_item);
ALTER TABLE payment ALTER COLUMN id RESTART WITH (SELECT MAX(id) + 1 FROM payment);
