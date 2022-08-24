INSERT INTO developers (first_name, last_name, age ,sex) VALUES
('Ivan', 'Pysar', 20, 'male'),
('Anna', 'Yurina', 35, 'female'),
('Ihor', 'Viter', 28, 'male'),
('Oleksandra', 'Boyko', 41, 'female'); 

INSERT INTO skills (language, level) VALUES 
('JS','Junior'),
('Java','Middle'),
('C++','Senior'),
('Java','Senior');

INSERT INTO companies(company_name, residence) VALUES 
('Best Soft', 'UA'),
('Banana Solutions','US'),
('Foogle', 'BG');

INSERT INTO projects (project_name, type, description, creation_date) VALUES
('King of rock and roll', 'website', 'Website for a musician', '2004-12-09'),
('Chew deliciously', 'website', 'restaurant site', '2020-11-01'),
('Roses to all', 'mobile app', 'Flower delivery', '2022-01-14');

INSERT INTO customers(first_name, last_name,email) VALUES 
('Elvis', 'Presley', 'rock&rollking@email.com'),
('Ariana', 'Grande', 'ariana1993@email.com'),
('John', 'Elton', 'sorryseemstobe@email.com');

INSERT INTO developers_projects (developer_id, project_id) VALUES
(1,1),
(1,2),
(1,3),
(2,1),
(2,2),
(2,3),
(3,3),
(4,3);

INSERT INTO developer_skills (developer_id, skill_id) VALUES
(1,1),
(1,2),
(2,2),
(2,3),
(3,3),
(4,4),
(4,2);

INSERT INTO companies_projects (company_id, project_id) VALUES
(1,1),
(1,2),
(2,1),
(2,2),
(2,3),
(3,2),
(3,3);

INSERT INTO customers_projects(customer_id, project_id) VALUES
(1,1),
(1,2),
(2,1),
(2,2),
(2,3),
(3,2),
(3,3);
