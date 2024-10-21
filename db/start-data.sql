INSERT INTO users (id, username, password, role)
VALUES (1, 'user1', 'password1', 'USER'),
       (2, 'user2', 'password2', 'USER'),
       (3, 'user3', 'password3', 'ADMIN'),
       (4, 'user4', 'password4', 'USER'),
       (5, 'user5', 'password5', 'ADMIN');

-- Inserting into the coordinates table
INSERT INTO coordinates (id, x, y)
VALUES (1, 10.5, 20.3),
       (2, 5.4, 15.2),
       (3, 8.1, 18.7);

-- Inserting into the dragon_caves table
INSERT INTO dragon_caves (id, number_of_treasures)
VALUES (1, 50),
       (2, 75),
       (3, 100);

-- Inserting into the people table (representing killers)
INSERT INTO people (id, name, eye_color, height, passport_id, birthday)
VALUES (1, 'Arthur', 'BLUE', 180.5, 'ARTH12345', '1985-01-15'),
       (2, 'Morgan', 'GREEN', 170.2, 'MORG67890', '1990-05-20'),
       (3, 'Gwen', 'BROWN', 165.8, 'GWEN54321', '1995-10-30');


-- Inserting into the dragon_heads table
INSERT INTO dragon_heads (id, size)
VALUES (1, 50.0),
       (2, 55.5),
       (3, 60.2);

-- Inserting dragons into the dragons table
INSERT INTO dragons (id, name, coordinates_id, creation_date, cave_id, killer_id, age, color, type, character, head_id,
                     owner_id, can_be_edited_by_admin)
VALUES (1, 'Fafnir', 1, NOW(), 1, 1, 300, 'GREEN', 'FIRE', 'CUNNING', 1, 1, false),
       (2, 'Smaug', 2, NOW(), 2, 2, 500, 'BLACK', 'AIR', 'EVIL', 2, 2, true),
       (3, 'Alduin', 3, NOW(), 3, 3, 1000, 'BLUE', 'UNDERGROUND', 'WISE', 3, 3, true);

