-- Schema
CREATE SCHEMA IF NOT EXISTS school;
DROP TABLE IF EXISTS school.groups cascade;
CREATE TABLE IF NOT EXISTS school.groups 
(
	group_id SERIAL PRIMARY KEY,
	group_name VARCHAR(5) UNIQUE NOT NULL
);

DROP TABLE IF EXISTS school.students cascade;
CREATE TABLE IF NOT EXISTS school.students 
(
	student_id SERIAL PRIMARY KEY,
	group_id INTEGER, 
	FOREIGN KEY (group_id) REFERENCES school.groups(group_id),
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS school.courses cascade;
CREATE TABLE IF NOT EXISTS school.courses 
(
	course_id SERIAL PRIMARY KEY,
	course_name VARCHAR(50) UNIQUE NOT NULL,
	course_description VARCHAR(250) NOT NULL
);

DROP TABLE IF EXISTS school.course_student;
CREATE TABLE IF NOT EXISTS school.course_student
(
	junction_id SERIAL PRIMARY KEY,
	course_id INTEGER,
	student_id INTEGER,
	FOREIGN KEY (course_id) REFERENCES school.courses(course_id) ON DELETE CASCADE,
	FOREIGN KEY (student_id) REFERENCES school.students(student_id) ON DELETE CASCADE,
	UNIQUE (course_id, student_id)
);

-- Inserting data
INSERT INTO school.groups (group_name) VALUES ('G1'), ('G2'), ('G3'), ('G4'), ('G5');
INSERT INTO school.students (group_id, first_name, last_name) VALUES 
(1, 'John', 'Doe'), 
(1, 'Jane', 'Smith'), 
(1, 'Tom', 'Brown'), 
(1, 'Alice', 'Johnson'), 
(1, 'Bob', 'Williams'),
(1, 'Charlie', 'Miller'),
(1, 'David', 'Davis'),
(1, 'Eva', 'Taylor'),
(1, 'Frank', 'Anderson'),
(1, 'Grace', 'Thomas'),
(2, 'Henry', 'Jackson'),
(3, 'Irene', 'White'),
(4, 'Jack', 'Harris'),
(5, 'Kathy', 'Martin'),
(5, 'Larry', 'Thompson');
INSERT INTO school.courses (course_name, course_description) VALUES 
('Mathematics', 'Basic and Advanced Mathematics'), 
('Physics', 'Basic and Advanced Physics'), 
('Chemistry', 'Basic and Advanced Chemistry'), 
('Biology', 'Basic and Advanced Biology'), 
('Computer Science', 'Basic and Advanced Computer Science');
INSERT INTO school.course_student (course_id, student_id) VALUES 
(1, 1), (2, 2), (3, 3), (4, 4), (5, 5);
