-- Insert users with enum-based roles
INSERT INTO users (username, password, role)
VALUES
('Чебурек2000', 'password123', 'USER'),       -- Role from Role.USER
('Кот_Сибири', 'securepass', 'USER'),          -- Role from Role.USER
('Батя_в_Здании', 'admin123', 'ADMIN');        -- Role from Role.ADMIN

-- Insert admin_request
INSERT INTO admin_request (id, requester_id, is_approved)
VALUES
(1, 'Чебурек2000', FALSE),
(2, 'Кот_Сибири', TRUE);

-- Insert coordinates
INSERT INTO coordinates (id, x, y)
VALUES
(1, 13.37, 42),    -- Coordinates referencing meme numbers
(2, 90.00, 300);

-- Insert dragon_caves
INSERT INTO dragon_caves (id, number_of_treasures)
VALUES
(1, 111),        
(2, 228);

-- Insert dragon_heads
INSERT INTO dragon_heads (id, size)
VALUES
(1, 7),
(2, 42);

-- Insert locations
INSERT INTO locations (id, x, y, name)
VALUES
(1, 404, 500.5, 'Берлога_Забугорного'),  -- Inspired by memes
(2, 1488, 777.7, 'Атлантида');

-- Insert people
INSERT INTO people (id, name, eye_color, hair_color, location_id, birthday, height, passport_id)
VALUES
(1, 'Хитрейший_Лис', 'GREEN', 'GREEN', 1, '1988-10-11', 170.2, 'RUS-112345'),
(2, 'dacha', 'GREEN', 'GREEN', 2, '1975-06-30', 195.5, 'G-67890');

-- Insert dragons with enum-based fields
INSERT INTO dragons (id, name, coordinates_id, creation_date, cave_id, killer_id, age, color, type, character, head_id, owner_id, can_be_edited_by_admin)
VALUES
(1, 'Bezzubik', 1, current_timestamp, 1, NULL, 228, 'BLACK', 'FIRE', 'EVIL', 1, 'Чебурек2000', TRUE),  -- Enums for Color.BLACK, DragonType.FIRE, DragonCharacter.EVIL
(2, 'Жоский', 2, current_timestamp, 2, NULL, 666, 'GREEN', 'AIR', 'WISE', 2, 'Кот_Сибири', FALSE); -- Enums for Color.GREEN, DragonType.AIR, DragonCharacter.WISE

INSERT INTO imports (status, owner_id, number_of_added_objects) VALUES
    ('COMPLETED', 'Кот_Сибири', 15),
    ('FAILED', 'Кот_Сибири', NULL),
    ('COMPLETED', 'Чебурек2000', 42),
    ('FAILED', 'Чебурек2000', NULL);
