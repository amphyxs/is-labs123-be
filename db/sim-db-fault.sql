CREATE OR REPLACE FUNCTION raise_error_on_insert() 
RETURNS TRIGGER AS $$
BEGIN
    -- Raise an exception with a custom error message
    RAISE EXCEPTION 'Inserting into the dragons table is not allowed.';
    RETURN NULL;  -- Returning NULL prevents the insert operation
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_raise_error_on_insert
BEFORE INSERT ON dragons
FOR EACH ROW
EXECUTE FUNCTION raise_error_on_insert();

