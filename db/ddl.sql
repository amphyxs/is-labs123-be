CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,                          -- Auto-incrementing primary key
    username VARCHAR(100) NOT NULL,                 -- Username column with a max length of 100, cannot be null
    password VARCHAR(255) NOT NULL,                 -- Password column with a max length of 255, cannot be null
    role VARCHAR(20) NOT NULL                       -- Role column, storing string representation of the enum
);

CREATE TABLE IF NOT EXISTS admin_request (
    id SERIAL PRIMARY KEY,                              -- Auto-incrementing primary key
    requester_id BIGINT,                                -- Foreign key referencing the User entity
    is_approved BOOLEAN NOT NULL,                       -- Boolean column to track approval status
    CONSTRAINT fk_requester FOREIGN KEY (requester_id)  -- Foreign key constraint
    REFERENCES users (id) ON DELETE SET NULL            -- Set requester_id to NULL if the user is deleted
);

-- Create the 'coordinates' table
CREATE TABLE IF NOT EXISTS coordinates (
    id SERIAL PRIMARY KEY,                             -- Auto-incrementing primary key
    x REAL,                                            -- Column for X coordinate, float type
    y BIGINT NOT NULL                                 -- Column for Y coordinate, cannot be NULL        -- If user is deleted, set owner_id to NULL
);

-- Create the 'dragon_caves' table
CREATE TABLE IF NOT EXISTS dragon_caves (
    id SERIAL PRIMARY KEY,                             -- Auto-incrementing primary key
    number_of_treasures BIGINT CHECK (number_of_treasures >= 1) -- Column for the number of treasures with a minimum constraint
);

-- Create the 'dragon_heads' table
CREATE TABLE IF NOT EXISTS dragon_heads (
    id SERIAL PRIMARY KEY,                             -- Auto-incrementing primary key
    size BIGINT NOT NULL CHECK (size >= 1)            -- Column for size with a minimum constraint of 1, cannot be NULL
);

-- Create the 'locations' table
CREATE TABLE IF NOT EXISTS locations (
    id SERIAL PRIMARY KEY,                             -- Auto-incrementing primary key
    x INTEGER NOT NULL,                                -- Column for X coordinate, cannot be NULL
    y REAL NOT NULL,                                   -- Column for Y coordinate, cannot be NULL
    name VARCHAR(214)                                 -- Column for name, with a maximum length of 214 characters
);

-- Create the 'people' table
CREATE TABLE IF NOT EXISTS people (
    id SERIAL PRIMARY KEY,                                -- Auto-incrementing primary key
    name VARCHAR(255) NOT NULL,                           -- Name column, cannot be NULL
    eye_color VARCHAR(255) NOT NULL,                      -- Eye color column, stored as a string, cannot be NULL
    hair_color VARCHAR(255),                              -- Hair color column, stored as a string, can be NULL
    location_id BIGINT,                                   -- Foreign key referencing the 'locations' table
    birthday TIMESTAMP,                                   -- Birthday column of type TIMESTAMP, can be NULL
    height DOUBLE PRECISION NOT NULL CHECK (height > 0),  -- Height column with a minimum constraint of 1, cannot be NULL
    passport_id VARCHAR(39) NOT NULL UNIQUE,              -- Passport ID column, must be unique and cannot be NULL
    CONSTRAINT fk_location FOREIGN KEY (location_id)      -- Foreign key constraint on location_id
        REFERENCES locations (id) ON DELETE SET NULL     -- If location is deleted, set location_id to NULL
);

-- Create the 'dragons' table
CREATE TABLE IF NOT EXISTS dragons (
    id SERIAL PRIMARY KEY,                                  -- Auto-incrementing primary key
    name VARCHAR(255) NOT NULL,                             -- Name column, cannot be NULL
    coordinates_id BIGINT NOT NULL,                         -- Foreign key referencing the 'coordinates' table, cannot be NULL
    creation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Creation date, non-updatable, default to current time
    cave_id BIGINT NOT NULL,                                -- Foreign key referencing the 'dragon_caves' table, cannot be NULL
    killer_id BIGINT,                                       -- Foreign key referencing the 'people' table, can be NULL
    age BIGINT CHECK (age >= 1),                            -- Age column, must be greater than or equal to 1, can be NULL
    color VARCHAR(255) NOT NULL,                            -- Color column, stored as a string, cannot be NULL
    type VARCHAR(255),                                      -- Type column, stored as a string, can be NULL
    character VARCHAR(255) NOT NULL,                        -- Character column, stored as a string, cannot be NULL
    head_id BIGINT,                                         -- Foreign key referencing the 'dragon_heads' table, can be NULL
    owner_id BIGINT NOT NULL,                               -- Foreign key referencing the 'users' table, cannot be NULL
    can_be_edited_by_admin BOOLEAN NOT NULL,

    CONSTRAINT fk_coordinates FOREIGN KEY (coordinates_id)   -- Foreign key constraint on coordinates_id
        REFERENCES coordinates (id) ON DELETE RESTRICT,      -- Prevent deletion of coordinates if referenced
    CONSTRAINT fk_cave FOREIGN KEY (cave_id)                 -- Foreign key constraint on cave_id
        REFERENCES dragon_caves (id) ON DELETE RESTRICT,     -- Prevent deletion of cave if referenced
    CONSTRAINT fk_killer FOREIGN KEY (killer_id)             -- Foreign key constraint on killer_id
        REFERENCES people (id) ON DELETE SET NULL,           -- If killer is deleted, set killer_id to NULL
    CONSTRAINT fk_head FOREIGN KEY (head_id)                 -- Foreign key constraint on head_id
        REFERENCES dragon_heads (id) ON DELETE SET NULL,     -- If head is deleted, set head_id to NULL
    CONSTRAINT fk_owner FOREIGN KEY (owner_id)               -- Foreign key constraint on owner_id
        REFERENCES users (id) ON DELETE RESTRICT             -- Prevent deletion of owner if referenced
);
