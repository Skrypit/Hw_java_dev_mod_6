ALTER TABLE developers
ADD COLUMN salary INT;

UPDATE developers SET salary = 20000 WHERE developer_id = 1;

UPDATE developers SET salary = 40000 WHERE developer_id = 2;

UPDATE developers SET salary = 15500 WHERE developer_id = 3;

UPDATE developers SET salary = 100000 WHERE developer_id = 4;