INSERT INTO projects (name, description)
VALUES ('Business Calculator', 'App for calculating income'),
       ('Restaurant Application', 'Restaurant Management Application'),
       ('Banking Application','M-Banking for Belgazprombank');


INSERT INTO employees (username, email, password_hash)
VALUES ('petrov403', 'petrov@gmail.com', '$2a$10$oHVi32elkA5xTUxThOChi.4fmtNeeZVZtLZ/ZUtvrUCS1DnAfKCMi'),
        ('smirnyaga','smirnov@gmail.com', '$2a$10$PlaZ4mgNdATucghM2nuYz.dRoe/1l7NJXsqDS3QgrDOO4ZLWC68sK'),
        ('antonchik', 'antonov@gmail.com', '$2a$10$tVNMlelB.2Xd.A.lzZR5hOg6VZ9BrxPS4sk0hbLFKFbpbStoKIPYS');

INSERT INTO tasks (title, description, project_id)
VALUES ('Creating registration module', 'Create registration module in banking app', 3),
        ('Fixing bags M-Banking', 'Fix bags in payment module', 3),
        ('Add new feature', 'Add new feature for shows menu', 2);

INSERT INTO time_entries (task_id, employee_id)
VALUES (1, 2),
        (2, 3);

INSERT INTO time_entries(task_id, employee_id, start_time)
VALUES (3, 1, '2024-10-29 15:30:00')