CREATE TABLE admins (
    admin_id INT IDENTITY(1,1) PRIMARY KEY,
    admin_name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(255)
);

CREATE TABLE Departments (
    department_id INT IDENTITY(1,1) PRIMARY KEY,
    department_name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Teachers (
    teacher_id INT IDENTITY(1,1) PRIMARY KEY,
    teacher_name VARCHAR(100) NOT NULL,
    teacher_email VARCHAR(100) UNIQUE,
    department_id INT NOT NULL,
    
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE
);

CREATE TABLE Courses (
    course_id INT IDENTITY(1,1) PRIMARY KEY,
    course_title VARCHAR(200) NOT NULL,
    credit_hours INT NOT NULL CHECK (credit_hours > 0 AND credit_hours <= 6),
    department_id INT NOT NULL,
    
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE
);

CREATE TABLE Batches (
    batch_id INT IDENTITY(1,1) PRIMARY KEY,
    batch_name VARCHAR(100) NOT NULL,
    year_of_admission INT NOT NULL CHECK (year_of_admission >= 1900 AND year_of_admission <= 2100),
    department_id INT NOT NULL,
    total_students INT DEFAULT 0 CHECK (total_students >= 0),
    
    FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE,
    
    -- One batch per year per department
    UNIQUE (year_of_admission, department_id)
);

CREATE TABLE Classrooms (
    room_id INT IDENTITY(1,1) PRIMARY KEY,
    room_name VARCHAR(50) NOT NULL UNIQUE,
    capacity INT NOT NULL CHECK (capacity > 0 AND capacity <= 500)
);

CREATE TABLE ClassSlots (
    slot_id INT IDENTITY(1,1) PRIMARY KEY,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    
    CHECK (start_time < end_time)
);

CREATE TABLE TeacherCourses (
    teacher_id INT NOT NULL,
    course_id INT NOT NULL,
    
    -- Composite Primary Key
    PRIMARY KEY (teacher_id, course_id),
    
    -- Use CASCADE for teacher (safer - teachers are less likely to be deleted)
    FOREIGN KEY (teacher_id) REFERENCES teachers(teacher_id) ON DELETE CASCADE,
    
    -- Use NO ACTION for course (prevents cascade cycles)
    FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE NO ACTION
);

CREATE TABLE Schedules (
    schedule_id INT IDENTITY(1,1) PRIMARY KEY,
    day_of_week VARCHAR(20) NOT NULL CHECK (day_of_week IN ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')),
    course_id INT NOT NULL,
    batch_id INT NOT NULL,
    room_id INT NOT NULL,
    slot_id INT NOT NULL,
    teacher_id INT NOT NULL,

    -- CRITICAL FIX: Use NO ACTION for batch to prevent cascade cycle
    FOREIGN KEY (course_id) REFERENCES Courses(course_id) ON DELETE NO ACTION,
    FOREIGN KEY (batch_id) REFERENCES Batches(batch_id) ON DELETE NO ACTION,
    FOREIGN KEY (room_id) REFERENCES ClassRooms(room_id) ON DELETE NO ACTION,
    FOREIGN KEY (slot_id) REFERENCES ClassSlots(slot_id) ON DELETE NO ACTION,
    FOREIGN KEY (teacher_id) REFERENCES Teachers(teacher_id) ON DELETE NO ACTION,
    
    -- Conflict Prevention
    UNIQUE (room_id, day_of_week, slot_id),
    UNIQUE (teacher_id, day_of_week, slot_id)
);

CREATE TABLE SpecialSchedules (
    special_schedule_id INT IDENTITY(1,1) PRIMARY KEY,
    course_id INT NOT NULL,
    room_id INT NOT NULL,
    teacher_id INT NOT NULL,
    schedule_date DATE NOT NULL,
    batch_id INT NOT NULL,
    day_of_week VARCHAR(20) NOT NULL CHECK (day_of_week IN ('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    type VARCHAR(20) NOT NULL CHECK (type IN ('makeup', 'extra')),
    
    CHECK (start_time < end_time),
    
    -- All NO ACTION to prevent cascade cycles
    FOREIGN KEY (course_id) REFERENCES Courses(course_id) ON DELETE NO ACTION,
    FOREIGN KEY (room_id) REFERENCES ClassRooms(room_id) ON DELETE NO ACTION,
    FOREIGN KEY (teacher_id) REFERENCES Teachers(teacher_id) ON DELETE NO ACTION,
    FOREIGN KEY (batch_id) REFERENCES Batches(batch_id) ON DELETE NO ACTION
);