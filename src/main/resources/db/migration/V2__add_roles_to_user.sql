ALTER TABLE user
    ADD COLUMN roles VARCHAR(255) AFTER password;