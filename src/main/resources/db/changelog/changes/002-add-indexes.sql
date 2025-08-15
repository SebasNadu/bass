CREATE INDEX idx_cart_item_cover ON cart_item(added_at, meal_id);
CREATE INDEX idx_meal_cover ON meal(id, name);
