INSERT INTO Project (projectName, startDate, finishDate)
    VALUES ( 'Price Calculator', '2020-02-01', '2021-02-07' );
INSERT INTO Project (projectName, startDate, finishDate)
    VALUES ( 'JPEG Restore', '2020-11-10', '2021-01-25' );

INSERT INTO Employee (firstName, lastName, middleName, email)
    VALUES ( 'Ivan', 'Ivanov', 'Ivanovich' , 'ivanov_ivan@gmail.com');
INSERT INTO Employee (firstName, lastName, middleName, email)
    VALUES ( 'Aleksey', 'Mihno', 'Nikolaevich' , 'mihno_alex@mail.ru');
INSERT INTO Employee (firstName, lastName, middleName, email)
    VALUES ( 'Alexandra', 'Drozd', 'Igorevna' , 'drozd_sasha@gmail.com');

INSERT INTO Role (roleName) VALUES ( 'Project Manager');
INSERT INTO Role (roleName) VALUES ( 'Java Software Engineer');
INSERT INTO Role (roleName) VALUES ( 'QA Engineer');

INSERT INTO ProjectEmployee (projectId, employeeId) VALUES (1, 1);
INSERT INTO ProjectEmployee (projectId, employeeId) VALUES (1, 2);
INSERT INTO ProjectEmployee (projectId, employeeId) VALUES (2, 3);
INSERT INTO ProjectEmployee (projectId, employeeId) VALUES (2, 2);

INSERT INTO EmployeeRole (employeeId, roleId) VALUES (1, 1);
INSERT INTO EmployeeRole (employeeId, roleId) VALUES (2, 2);
INSERT INTO EmployeeRole (employeeId, roleId) VALUES (3, 3);
INSERT INTO EmployeeRole (employeeId, roleId) VALUES (1, 2);
INSERT INTO EmployeeRole (employeeId, roleId) VALUES (2, 1);
INSERT INTO EmployeeRole (employeeId, roleId) VALUES (3, 1);