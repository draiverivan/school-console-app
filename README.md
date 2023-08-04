# school-console-app

## Description

- This is a sql-jdbc-school application  that inserts/updates/deletes data in the database using JDBC.

>_Using PostgreSQL DB._


## Step-by-step

On startup, it runs SQL script with table creation from previously created files. If tables already exist - drop them.

Auto-generate the test data:

* 10 groups with randomly generated names. The name should contain 2 characters, hyphen, 2 numbers

* Create 10 courses (math, biology, etc)

* 200 students. Take 20 first names and 20 last names and randomly combine them to generate students.

* Randomly assign students to groups. Each group could contain from 10 to 30 students. It is possible that some groups will be without students or students without groups

* Create the MANY-TO-MANY relation  between STUDENTS and COURSES tables. Randomly assign from 1 to 3 courses for each student

It should be available from the console menu:

* a. Find all groups with less or equal students’ number.

* b. Find all students related to the course with the given name.

* c. Add a new student.

* d. Delete a student by the STUDENT_ID.

* e. Add a student to the course (from a list).

* f. Remove the student from one of their courses.

## Usage

- In the root folder of this project enter command to build project in Command Line:

> mvn clean package spring-boot:repackage

- To execute in the root folder of this project enter command in Command Line:

> java -jar target/school-console-app.jar
