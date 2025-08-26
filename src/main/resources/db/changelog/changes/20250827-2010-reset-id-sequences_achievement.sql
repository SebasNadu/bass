-- This script resets the auto-increment counters for all tables that have manually inserted IDs.
-- This ensures that new entities created in tests will get a unique ID and not conflict with the seeded data.
-- The syntax is specific to H2 and may need adjustment for other databases like PostgreSQL.

SELECT setval(pg_get_serial_sequence('achievement', 'id'), COALESCE(MAX(id), 1), true) FROM achievement;
