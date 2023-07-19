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
