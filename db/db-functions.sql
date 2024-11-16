CREATE OR REPLACE FUNCTION total_age() RETURNS integer AS $$
DECLARE
    sum_age integer;
BEGIN
    SELECT SUM(age) INTO sum_age FROM dragons;
    RETURN sum_age;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_dragon_id_with_gigachad_killer() RETURNS TABLE (
    dragon_id INTEGER
) AS $$
BEGIN
    RETURN QUERY
    SELECT d.id AS dragon_id
    FROM dragons d
    JOIN people p ON d.killer_id = p.id
    ORDER BY p.height DESC
    LIMIT 1;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_dragon_ids_by_name_substring(name_substring VARCHAR) RETURNS TABLE (
    dragon_id INTEGER
) AS $$
BEGIN
    RETURN QUERY
    SELECT id
    FROM dragons
    WHERE name ILIKE '%' || name_substring || '%';
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_dragon_with_the_deepest_cave() RETURNS TABLE (
    dragon_id INTEGER
) AS $$
BEGIN
    RETURN QUERY
    SELECT d.id AS dragon_id
    FROM dragons d
    JOIN dragon_caves c ON d.cave_id = c.id
    ORDER BY c.number_of_treasures DESC
    LIMIT 1;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE PROCEDURE create_killers_gang(passport_id1 VARCHAR, passport_id2 VARCHAR, passport_id3 VARCHAR) AS $$
BEGIN
    INSERT INTO people (name, eye_color, height, passport_id, birthday)
    VALUES ('Arthur', 'BLUE', 180.5, passport_id1, '1985-01-15'),
       ('Morgan', 'GREEN', 170.2, passport_id2, '1990-05-20'),
       ('Gwen', 'BROWN', 165.8, passport_id3, '1995-10-30');
END;
$$ LANGUAGE plpgsql;
