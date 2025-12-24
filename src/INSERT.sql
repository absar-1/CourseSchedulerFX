-- ============================================
-- INSERT DUMMY DATA FOR TESTING
-- CourseSchedulerFX Application
-- ============================================

-- ============================================
-- 1. INSERT DEPARTMENTS
-- ============================================
INSERT INTO Departments (department_name) VALUES 
('Computer Science'),
('Electrical Engineering'),
('Mathematics'),
('Physics'),
('Mechanical Engineering'),
('Civil Engineering'),
('Chemistry'),
('Business Administration');

PRINT 'Departments inserted:  8';

-- ============================================
-- 2. INSERT ADMINS
-- ============================================
-- Note: These passwords should be hashed in production using BCrypt
INSERT INTO admins (admin_name, email, password) VALUES 
('System Administrator', 'admin@university.edu', 'Admin@123'),
('John Doe', 'john. doe@university.edu', 'Password@123'),
('Jane Smith', 'jane.smith@university.edu', 'Secure@456'),
('Robert Johnson', 'robert.j@university.edu', 'RobertPass@789'),
('Emily Davis', 'emily.davis@university.edu', 'Emily@2024');

PRINT 'Admins inserted: 5';

-- ============================================
-- 3. INSERT TEACHERS
-- ============================================
INSERT INTO Teachers (teacher_name, teacher_email, department_id) VALUES 
-- Computer Science Teachers
('Dr. John Smith', 'john.smith@university.edu', 1),
('Dr. Sarah Johnson', 'sarah.johnson@university.edu', 1),
('Dr. Robert Wilson', 'robert.wilson@university.edu', 1),
('Prof. Lisa Anderson', 'lisa.anderson@university.edu', 1),

-- Electrical Engineering Teachers
('Dr. Michael Brown', 'michael.brown@university.edu', 2),
('Dr. David Martinez', 'david.martinez@university.edu', 2),
('Prof. Jennifer Lee', 'jennifer.lee@university.edu', 2),

-- Mathematics Teachers
('Dr. Emily Davis', 'emily.davis@university.edu', 3),
('Dr. James Taylor', 'james.taylor@university.edu', 3),
('Prof. Mary Thomas', 'mary.thomas@university.edu', 3),

-- Physics Teachers
('Dr. William Garcia', 'william.garcia@university.edu', 4),
('Dr. Patricia Martinez', 'patricia.martinez@university.edu', 4),

-- Mechanical Engineering Teachers
('Dr. Charles Rodriguez', 'charles.rodriguez@university.edu', 5),
('Prof. Barbara Wilson', 'barbara.wilson@university.edu', 5),

-- Civil Engineering Teachers
('Dr.  Joseph Anderson', 'joseph.anderson@university.edu', 6),

-- Chemistry Teachers
('Dr. Susan Thomas', 'susan.thomas@university.edu', 7),

-- Business Administration Teachers
('Prof. Richard Jackson', 'richard.jackson@university.edu', 8),
('Dr. Linda White', 'linda.white@university.edu', 8);

PRINT 'Teachers inserted:  18';

-- ============================================
-- 4. INSERT COURSES
-- ============================================
INSERT INTO Courses (course_title, credit_hours, department_id) VALUES 
-- Computer Science Courses
('Database Systems', 3, 1),
('Data Structures', 3, 1),
('Computer Networks', 3, 1),
('Operating Systems', 3, 1),
('Software Engineering', 3, 1),
('Artificial Intelligence', 3, 1),
('Web Development', 3, 1),

-- Electrical Engineering Courses
('Digital Logic Design', 3, 2),
('Circuit Analysis', 3, 2),
('Microprocessors', 3, 2),
('Signal Processing', 3, 2),
('Power Systems', 3, 2),

-- Mathematics Courses
('Calculus I', 4, 3),
('Calculus II', 4, 3),
('Linear Algebra', 3, 3),
('Differential Equations', 3, 3),
('Discrete Mathematics', 3, 3),

-- Physics Courses
('Physics I', 4, 4),
('Physics II', 4, 4),
('Quantum Mechanics', 3, 4),

-- Mechanical Engineering Courses
('Thermodynamics', 3, 5),
('Fluid Mechanics', 3, 5),
('Machine Design', 3, 5),

-- Civil Engineering Courses
('Structural Analysis', 3, 6),
('Construction Management', 3, 6),

-- Chemistry Courses
('Organic Chemistry', 4, 7),
('Physical Chemistry', 3, 7),

-- Business Administration Courses
('Principles of Management', 3, 8),
('Marketing Fundamentals', 3, 8),
('Financial Accounting', 3, 8);

PRINT 'Courses inserted: 30';

-- ============================================
-- 5. INSERT BATCHES
-- ============================================
INSERT INTO Batches (batch_name, year_of_admission, department_id, total_students) VALUES 
-- Computer Science Batches
('Computer Science - 2021', 2021, 1, 85),
('Computer Science - 2022', 2022, 1, 90),
('Computer Science - 2023', 2023, 1, 95),
('Computer Science - 2024', 2024, 1, 100),

-- Electrical Engineering Batches
('Electrical Engineering - 2021', 2021, 2, 70),
('Electrical Engineering - 2022', 2022, 2, 75),
('Electrical Engineering - 2023', 2023, 2, 80),
('Electrical Engineering - 2024', 2024, 2, 82),

-- Mathematics Batches
('Mathematics - 2021', 2021, 3, 50),
('Mathematics - 2022', 2022, 3, 55),
('Mathematics - 2023', 2023, 3, 60),
('Mathematics - 2024', 2024, 3, 58),

-- Physics Batches
('Physics - 2022', 2022, 4, 45),
('Physics - 2023', 2023, 4, 48),
('Physics - 2024', 2024, 4, 50),

-- Mechanical Engineering Batches
('Mechanical Engineering - 2022', 2022, 5, 65),
('Mechanical Engineering - 2023', 2023, 5, 70),
('Mechanical Engineering - 2024', 2024, 5, 68),

-- Civil Engineering Batches
('Civil Engineering - 2022', 2022, 6, 55),
('Civil Engineering - 2023', 2023, 6, 60),

-- Chemistry Batches
('Chemistry - 2023', 2023, 7, 40),
('Chemistry - 2024', 2024, 7, 42),

-- Business Administration Batches
('Business Administration - 2022', 2022, 8, 120),
('Business Administration - 2023', 2023, 8, 125),
('Business Administration - 2024', 2024, 8, 130);

PRINT 'Batches inserted: 25';

-- ============================================
-- 6. INSERT CLASSROOMS
-- ============================================
INSERT INTO Classrooms (room_name, capacity) VALUES 
-- Regular Classrooms
('Room 101', 50),
('Room 102', 45),
('Room 103', 40),
('Room 201', 55),
('Room 202', 50),
('Room 203', 60),
('Room 301', 45),
('Room 302', 50),
('Room 303', 40),

-- Labs
('Lab 101', 30),
('Lab 102', 30),
('Lab 201', 35),
('Lab 202', 35),
('Lab 301', 25),

-- Large Halls
('Auditorium A', 150),
('Auditorium B', 120),
('Seminar Hall 1', 80),
('Seminar Hall 2', 70),

-- Computer Labs
('Computer Lab 1', 40),
('Computer Lab 2', 40),
('Computer Lab 3', 35);

PRINT 'Classrooms inserted: 21';

-- ============================================
-- 7. INSERT CLASS SLOTS
-- ============================================
INSERT INTO ClassSlots (start_time, end_time) VALUES 
('08:00:00', '09:30:00'),  -- Slot 1
('09:45:00', '11:15:00'),  -- Slot 2
('11:30:00', '13:00:00'),  -- Slot 3
('13:30:00', '15:00:00'),  -- Slot 4 (after lunch)
('15:15:00', '16:45:00'),  -- Slot 5
('17:00:00', '18:30:00'),  -- Slot 6 (evening)
('08:00:00', '10:00:00'),  -- Slot 7 (2-hour lab)
('10:15:00', '12:15:00'),  -- Slot 8 (2-hour lab)
('14:00:00', '16:00:00'),  -- Slot 9 (2-hour lab)
('16:15:00', '18:15:00');  -- Slot 10 (2-hour lab)

PRINT 'Class slots inserted: 10';

-- ============================================
-- 8. INSERT TEACHER-COURSE ASSIGNMENTS
-- ============================================
INSERT INTO TeacherCourses (teacher_id, course_id) VALUES 
-- Dr. John Smith (CS) - Database Systems expert
(1, 1),  -- Database Systems
(1, 2),  -- Data Structures
(1, 3),  -- Computer Networks

-- Dr. Sarah Johnson (CS) - Software Engineering expert
(2, 2),  -- Data Structures
(2, 5),  -- Software Engineering
(2, 7),  -- Web Development

-- Dr. Robert Wilson (CS) - AI expert
(3, 4),  -- Operating Systems
(3, 6),  -- Artificial Intelligence
(3, 3),  -- Computer Networks

-- Prof. Lisa Anderson (CS)
(4, 2),  -- Data Structures
(4, 5),  -- Software Engineering

-- Dr. Michael Brown (EE) - Digital Logic expert
(5, 8),  -- Digital Logic Design
(5, 9),  -- Circuit Analysis
(5, 10), -- Microprocessors

-- Dr. David Martinez (EE)
(6, 11), -- Signal Processing
(6, 12), -- Power Systems
(6, 9),  -- Circuit Analysis

-- Prof. Jennifer Lee (EE)
(7, 8),  -- Digital Logic Design
(7, 10), -- Microprocessors

-- Dr. Emily Davis (Math) - Calculus expert
(8, 13), -- Calculus I
(8, 14), -- Calculus II
(8, 15), -- Linear Algebra

-- Dr. James Taylor (Math)
(9, 16), -- Differential Equations
(9, 17), -- Discrete Mathematics
(9, 13), -- Calculus I

-- Prof. Mary Thomas (Math)
(10, 15), -- Linear Algebra
(10, 17), -- Discrete Mathematics

-- Dr. William Garcia (Physics)
(11, 18), -- Physics I
(11, 19), -- Physics II

-- Dr. Patricia Martinez (Physics)
(12, 19), -- Physics II
(12, 20), -- Quantum Mechanics

-- Dr.  Charles Rodriguez (Mechanical)
(13, 21), -- Thermodynamics
(13, 22), -- Fluid Mechanics

-- Prof. Barbara Wilson (Mechanical)
(14, 23), -- Machine Design
(14, 22), -- Fluid Mechanics

-- Dr. Joseph Anderson (Civil)
(15, 24), -- Structural Analysis
(15, 25), -- Construction Management

-- Dr. Susan Thomas (Chemistry)
(16, 26), -- Organic Chemistry
(16, 27), -- Physical Chemistry

-- Prof. Richard Jackson (Business)
(17, 28), -- Principles of Management
(17, 29), -- Marketing Fundamentals

-- Dr. Linda White (Business)
(18, 30), -- Financial Accounting
(18, 28); -- Principles of Management

PRINT 'Teacher-course assignments inserted:  42';

-- ============================================
-- 9. INSERT SCHEDULES (Regular Weekly Classes)
-- ============================================
INSERT INTO Schedules (day_of_week, course_id, batch_id, room_id, slot_id, teacher_id) VALUES
-- MONDAY SCHEDULES
('Monday', 1, 2, 1, 1, 1),      -- Database Systems, CS 2022, Room 101, 8:00-9:30, Dr. Smith
('Monday', 2, 3, 2, 2, 2),      -- Data Structures, CS 2023, Room 102, 9:45-11:15, Dr. Johnson
('Monday', 8, 6, 10, 3, 5),     -- Digital Logic, EE 2022, Lab 101, 11:30-13:00, Dr. Brown
('Monday', 13, 10, 15, 4, 8),   -- Calculus I, Math 2022, Auditorium A, 13:30-15:00, Dr. Davis
('Monday', 3, 1, 3, 5, 3),      -- Computer Networks, CS 2021, Room 103, 15:15-16:45, Dr. Wilson

-- TUESDAY SCHEDULES
('Tuesday', 1, 3, 1, 1, 1),     -- Database Systems, CS 2023, Room 101, 8:00-9:30, Dr.  Smith
('Tuesday', 5, 2, 4, 2, 2),     -- Software Engineering, CS 2022, Room 201, 9:45-11:15, Dr. Johnson
('Tuesday', 9, 5, 11, 7, 6),    -- Circuit Analysis, EE 2021, Lab 102, 8:00-10:00, Dr. Martinez
('Tuesday', 18, 13, 15, 4, 11), -- Physics I, Physics 2022, Auditorium A, 13:30-15:00, Dr. Garcia
('Tuesday', 21, 16, 5, 5, 13),  -- Thermodynamics, Mech 2022, Room 202, 15:15-16:45, Dr. Rodriguez

-- WEDNESDAY SCHEDULES
('Wednesday', 2, 2, 2, 1, 1),   -- Data Structures, CS 2022, Room 102, 8:00-9:30, Dr. Smith
('Wednesday', 4, 1, 19, 7, 3),  -- Operating Systems, CS 2021, Comp Lab 1, 8:00-10:00, Dr. Wilson
('Wednesday', 15, 9, 6, 3, 8),  -- Linear Algebra, Math 2021, Room 203, 11:30-13:00, Dr. Davis
('Wednesday', 10, 7, 12, 8, 5), -- Microprocessors, EE 2023, Lab 201, 10:15-12:15, Dr. Brown
('Wednesday', 28, 23, 17, 4, 17), -- Management, Business 2022, Seminar Hall 1, 13:30-15:00, Jackson

-- THURSDAY SCHEDULES
('Thursday', 1, 1, 1, 2, 1),    -- Database Systems, CS 2021, Room 101, 9:45-11:15, Dr. Smith
('Thursday', 6, 3, 20, 3, 3),   -- AI, CS 2023, Comp Lab 2, 11:30-13:00, Dr. Wilson
('Thursday', 11, 6, 13, 9, 6),  -- Signal Processing, EE 2022, Lab 202, 14:00-16:00, Dr. Martinez
('Thursday', 19, 14, 15, 4, 12), -- Physics II, Physics 2023, Auditorium A, 13:30-15:00, Dr. Martinez
('Thursday', 26, 21, 14, 5, 16), -- Organic Chemistry, Chem 2023, Lab 301, 15:15-16:45, Dr. Thomas

-- FRIDAY SCHEDULES
('Friday', 3, 2, 3, 1, 1),      -- Computer Networks, CS 2022, Room 103, 8:00-9:30, Dr. Smith
('Friday', 7, 4, 21, 7, 2),     -- Web Development, CS 2024, Comp Lab 3, 8:00-10:00, Dr. Johnson
('Friday', 14, 11, 15, 3, 8),   -- Calculus II, Math 2023, Auditorium A, 11:30-13:00, Dr. Davis
('Friday', 12, 8, 5, 4, 6),     -- Power Systems, EE 2024, Room 202, 13:30-15:00, Dr. Martinez
('Friday', 24, 19, 17, 5, 15),  -- Structural Analysis, Civil 2022, Seminar Hall 1, 15:15-16:45, Dr. Anderson

-- SATURDAY (Extra classes)
('Saturday', 17, 10, 7, 2, 10), -- Discrete Math, Math 2022, Room 301, 9:45-11:15, Prof. Thomas
('Saturday', 30, 24, 16, 3, 18), -- Financial Accounting, Business 2023, Auditorium B, 11:30-13:00, Dr. White

-- ADDITIONAL SCHEDULES
('Monday', 20, 15, 8, 6, 12),   -- Quantum Mechanics, Physics 2024, Room 302, 17:00-18:30, Dr. Martinez
('Thursday', 23, 18, 9, 5, 14), -- Machine Design, Mech 2024, Room 303, 15:15-16:45, Prof. Wilson
('Friday', 29, 25, 18, 4, 17);  -- Marketing, Business 2024, Seminar Hall 2, 13:30-15:00, Jackson

PRINT 'Schedules inserted: 30';

-- ============================================
-- 10. INSERT SPECIAL SCHEDULES
-- ============================================
INSERT INTO SpecialSchedules (course_id, room_id, teacher_id, schedule_date, batch_id, day_of_week, start_time, end_time, type) VALUES 
-- MAKEUP CLASSES
(1, 1, 1, '2024-12-20', 2, 'Friday', '14:00:00', '15:30:00', 'makeup'),       -- Database makeup
(2, 2, 2, '2024-12-21', 3, 'Saturday', '09:00:00', '10:30:00', 'makeup'),     -- Data Structures makeup
(8, 10, 5, '2024-12-22', 6, 'Sunday', '10:00:00', '12:00:00', 'makeup'),      -- Digital Logic makeup
(13, 15, 8, '2024-12-23', 10, 'Monday', '16:00:00', '17:30:00', 'makeup'),    -- Calculus makeup
(18, 15, 11, '2024-12-28', 13, 'Saturday', '14:00:00', '16:00:00', 'makeup'), -- Physics makeup

-- EXTRA CLASSES (Exam Preparation)
(1, 19, 1, '2024-12-19', 2, 'Thursday', '17:00:00', '18:30:00', 'extra'),     -- Database exam prep
(2, 20, 2, '2024-12-18', 3, 'Wednesday', '16:00:00', '17:30:00', 'extra'),    -- Data Structures exam prep
(5, 4, 2, '2024-12-26', 2, 'Thursday', '15:00:00', '16:30:00', 'extra'),      -- Software Eng review
(6, 21, 3, '2024-12-27', 3, 'Friday', '17:00:00', '18:30:00', 'extra'),       -- AI extra lecture
(15, 6, 8, '2024-12-30', 9, 'Monday', '16:00:00', '17:30:00', 'extra'),       -- Linear Algebra review
(11, 13, 6, '2025-01-02', 6, 'Thursday', '14:00:00', '16:00:00', 'extra'),    -- Signal Processing lab
(28, 17, 17, '2025-01-05', 23, 'Sunday', '10:00:00', '12:00:00', 'extra');    -- Management workshop

PRINT 'Special schedules inserted: 12';