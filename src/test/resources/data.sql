INSERT INTO projects (name, description)
VALUES ('Business Calculator', 'App for calculating income'),
       ('Restaurant Application', 'Restaurant Management Application'),
       ('Banking Application','M-Banking for Belgazprombank');

DELETE FROM employees;

INSERT INTO employees (username, email, password_hash)
VALUES ('petrov403', 'petrov@gmail.com', '$2a$10$oHVi32elkA5xTUxThOChi.4fmtNeeZVZtLZ/ZUtvrUCS1DnAfKCMi'),
        ('smirnyaga','smirnov@gmail.com', '$2a$10$PlaZ4mgNdATucghM2nuYz.dRoe/1l7NJXsqDS3QgrDOO4ZLWC68sK'),
        ('antonchik', 'antonov@gmail.com', '$2a$10$tVNMlelB.2Xd.A.lzZR5hOg6VZ9BrxPS4sk0hbLFKFbpbStoKIPYS');
