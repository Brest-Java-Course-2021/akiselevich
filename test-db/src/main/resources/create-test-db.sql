DROP TABLE IF EXISTS Project;
DROP TABLE IF EXISTS Employee;
DROP TABLE IF EXISTS Role;
DROP TABLE IF EXISTS ProjectEmployee;
DROP TABLE IF EXISTS EmployeeRole;

CREATE TABLE Project
(
    projectId INT NOT NULL AUTO_INCREMENT,
    projectName VARCHAR(255) NOT NULL,
    startDate DATE NOT NULL,
    finishDate DATE NOT NULL,
    CONSTRAINT ProjectPK PRIMARY KEY (projectId)
);

CREATE TABLE Employee (
	employeeId INT NOT NULL AUTO_INCREMENT,
	firstName VARCHAR(255) NOT NULL,
	lastName VARCHAR(255) NOT NULL,
	middleName VARCHAR(255) NOT NULL,
	email VARCHAR(255) NOT NULL UNIQUE,
	CONSTRAINT EmployeePK PRIMARY KEY (employeeId)
);

CREATE TABLE Role
(
    roleId INT NOT NULL AUTO_INCREMENT,
    roleName VARCHAR(255) NOT NULL,
    CONSTRAINT RolePK PRIMARY KEY (roleId)
);

CREATE TABLE ProjectEmployee
(
    projectId INT,
    employeeId INT,
    CONSTRAINT ProjectFK FOREIGN KEY (projectId) REFERENCES Project(projectId),
    CONSTRAINT EmployeeFK FOREIGN KEY (employeeId) REFERENCES Employee(employeeId)
);

CREATE TABLE EmployeeRole
(
    employeeId INT,
    roleId INT,
    CONSTRAINT EmployeeFK FOREIGN KEY (employeeId) REFERENCES Employee(employeeId),
    CONSTRAINT RoleFK FOREIGN KEY (roleId) REFERENCES Role(roleId)
);