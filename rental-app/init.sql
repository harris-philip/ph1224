CREATE TABLE tool_type (
    tool_type_id UUID PRIMARY KEY,
    tool_type_name VARCHAR(30) NOT NULL UNIQUE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_tool_type_modified_date
ON tool_type (modified_date);

CREATE TABLE tool (
    tool_id UUID PRIMARY KEY,
    tool_code VARCHAR(4) NOT NULL UNIQUE,
    tool_type_id UUID NOT NULL REFERENCES tool_type(tool_type_id) ON DELETE CASCADE,
    brand VARCHAR(30) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Index on tool_type_id for faster joins between tool and tool_type tables
CREATE INDEX idx_tool_tool_type_id ON tool (tool_type_id);
CREATE INDEX idx_tool_modified_date
ON tool (modified_date);

CREATE TABLE tool_rental_info (
    tool_rental_info_id UUID PRIMARY KEY,
    tool_type_id UUID NOT NULL REFERENCES tool_type(tool_type_id) ON DELETE CASCADE,
    daily_charge NUMERIC(10, 2) NOT NULL,
    has_weekday_charge BOOLEAN NOT NULL,
    has_weekend_charge BOOLEAN NOT NULL,
    has_holiday_charge BOOLEAN NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

-- Index on tool_type_id for faster lookups based on tool type in the rental info
CREATE INDEX idx_tool_rental_info_tool_type_id ON tool_rental_info (tool_type_id);
CREATE INDEX idx_tool_rental_info_modified_date
ON tool_rental_info (modified_date);

CREATE TABLE users (
    user_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    last_logged_in_date TIMESTAMP
);

CREATE TABLE authorities (
    authority_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    authority VARCHAR(50) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE user_authority (
    user_authority_id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    authority_id UUID NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (authority_id) REFERENCES authorities(authority_id) ON DELETE CASCADE
);

CREATE TABLE static_holiday (
    holiday_id UUID PRIMARY KEY,
    holiday_name VARCHAR(100) NOT NULL,
    holiday_date DATE NOT NULL,  -- The month and day part of the holiday (e.g., '12-25' for Christmas)
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE variable_holiday (
    holiday_id UUID PRIMARY KEY,
    holiday_name VARCHAR(100) NOT NULL,
    holiday_date DATE NOT NULL,  -- Full date for that year's holiday occurrence
    year INT NOT NULL,  -- The year of the holiday
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE rental_agreement (
    rental_agreement_id UUID PRIMARY KEY,
    tool_id UUID NOT NULL,
    rental_days INTEGER NOT NULL,
    checkout_date TIMESTAMP NOT NULL,
    due_date TIMESTAMP NOT NULL,
    daily_rental_charge NUMERIC(10, 2) NOT NULL,
    chargeable_days INTEGER NOT NULL,
    discount_percent NUMERIC(5, 2) NOT NULL,
    discount_amount NUMERIC(10, 2) NOT NULL,
    final_charge NUMERIC(10, 2) NOT NULL,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,

    -- Foreign key constraints
    CONSTRAINT fk_tool FOREIGN KEY (tool_id) REFERENCES tool (tool_id) ON DELETE CASCADE,
);

-- Optional index for faster querying on tool_id and tool_type_id
CREATE INDEX idx_rental_agreement_tool_id ON rental_agreement (tool_id);
CREATE INDEX idx_rental_agreement_tool_type_id ON rental_agreement (tool_type_id);


CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; -- Needed to use uuid_generate_v4

-- Insert into tool_type table
INSERT INTO tool_type (tool_type_id, tool_type_name, created_date, modified_date)
VALUES
    (uuid_generate_v4(), 'Chainsaw', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (uuid_generate_v4(), 'Ladder', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (uuid_generate_v4(), 'Jackhammer', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert into tool table
INSERT INTO tool (tool_id, tool_code, tool_type_id, brand, created_date, modified_date)
VALUES
    (uuid_generate_v4(), 'CHNS', (SELECT tool_type_id FROM tool_type WHERE tool_type_name = 'Chainsaw'), 'Stihl', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (uuid_generate_v4(), 'LADW', (SELECT tool_type_id FROM tool_type WHERE tool_type_name = 'Ladder'), 'Werner', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (uuid_generate_v4(), 'JAKD', (SELECT tool_type_id FROM tool_type WHERE tool_type_name = 'Jackhammer'), 'DeWalt', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (uuid_generate_v4(), 'JAKR', (SELECT tool_type_id FROM tool_type WHERE tool_type_name = 'Jackhammer'), 'Ridgid', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert into tool_rental_info table
INSERT INTO tool_rental_info (tool_rental_info_id, tool_type_id, daily_charge, has_weekday_charge, has_weekend_charge, has_holiday_charge, created_date, modified_date)
VALUES
    (uuid_generate_v4(), (SELECT tool_type_id FROM tool_type WHERE tool_type_name = 'Ladder'), 1.99, true, true, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (uuid_generate_v4(), (SELECT tool_type_id FROM tool_type WHERE tool_type_name = 'Chainsaw'), 1.49, true, false, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (uuid_generate_v4(), (SELECT tool_type_id FROM tool_type WHERE tool_type_name = 'Jackhammer'), 2.99, true, false, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insert sample user into users table
INSERT INTO users (username, password, enabled, created_date, last_logged_in_date)
VALUES
    ('user', '{noop}password', TRUE, CURRENT_TIMESTAMP, NULL),
    ('admin', '{noop}admin', TRUE, CURRENT_TIMESTAMP, NULL);

-- Insert sample authorities (roles)
INSERT INTO authorities (authority, created_date)
VALUES
    ('ROLE_USER', CURRENT_TIMESTAMP),
    ('ROLE_ADMIN', CURRENT_TIMESTAMP);

-- Insert associations between users and authorities
INSERT INTO user_authority (user_id, authority_id)
VALUES
    ((SELECT user_id FROM users WHERE username = 'user'), (SELECT authority_id FROM authorities WHERE authority = 'ROLE_USER')),
    ((SELECT user_id FROM users WHERE username = 'admin'), (SELECT authority_id FROM authorities WHERE authority = 'ROLE_ADMIN')),
    ((SELECT user_id FROM users WHERE username = 'admin'), (SELECT authority_id FROM authorities WHERE authority = 'ROLE_USER'));

CREATE OR REPLACE FUNCTION update_modified_date()
RETURNS TRIGGER AS $$
BEGIN
    NEW.modified_date = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_modified_date_tool_type
BEFORE UPDATE ON tool_type
FOR EACH ROW
EXECUTE FUNCTION update_modified_date();

CREATE TRIGGER set_modified_date_tool
BEFORE UPDATE ON tool
FOR EACH ROW
EXECUTE FUNCTION update_modified_date();

CREATE TRIGGER set_modified_date_tool_rental_info
BEFORE UPDATE ON tool_rental_info
FOR EACH ROW
EXECUTE FUNCTION update_modified_date();
