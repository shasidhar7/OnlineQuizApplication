# Online Quiz Application

## Overview
A console-based Java application for managing and taking quizzes. This application allows admins to create, edit, delete quizzes and questions, while users can log in, take quizzes, and view scores.

## Features
- **User Authentication**: Sign up, log in, and secure session management.
- **Quiz Management**: Admins can create, edit, delete quizzes and add questions.
- **Quiz Taking**: Users can select quizzes, receive instant feedback, and see scores.
- **Progress Tracking**: Users can view past quiz attempts and scores.
- **Leaderboard**: Displays top scorers based on quiz performance.

## Prerequisites
   - Java JDK 8+
   - MySQL database
   - MySQL JDBC Connector `(mysql-connector-java)`
    
## Setup Instructions
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/shasidhar7/OnlineQuizApplication.git
   
2. **Database Setup**:

   - Create the quizapp database in MySQL and run the following SQL script to create the necessary tables:

```
CREATE DATABASE quizapp;
USE quizapp;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) UNIQUE NOT NULL,
    hashed_password VARCHAR(255) NOT NULL,
    salt VARCHAR(255) NOT NULL,
    role ENUM('admin', 'user') NOT NULL
);

CREATE TABLE quizzes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE questions (
    id SERIAL PRIMARY KEY,
    quiz_id INT NOT NULL,
    question TEXT NOT NULL,
    option1 TEXT NOT NULL,
    option2 TEXT NOT NULL,
    option3 TEXT NOT NULL,
    option4 TEXT NOT NULL,
    correct_answers TEXT NOT NULL,
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);

CREATE TABLE quiz_attempts (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    quiz_id INT NOT NULL,
    score INT NOT NULL,
    attempt_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (quiz_id) REFERENCES quizzes(id)
);
```
3. **Database Configuration**:
   Ensure your database is set up with the correct credentials, and replace them in the code where necessary:
   - Database URL, Username and password for the MySQL connection.
   - Update `DatabaseUtil` with your database credentials.
     
4. **Add the MySQL JDBC Connector**:
    - Download the MySQL Connector/J JDBC Driver and add it to your project directory.
    
## How to Use
1. **Login**:  Use the admin or user credentials to log in.
2. **Admin Features**:
   - Create new quizzes.
   - Add questions to quizzes.
   - Edit or delete existing quizzes and questions.
3. **Use Features**:
   - Take Quizzes.
   - View scores after each quiz attempt.
4. **Leaderboard**: View top scores based on each quiz.

## Assumptions & Limitations
  - Users have unique usernames.
  - Basic security features are implemented, including password hashing and salting.
  - Limited optimization for large datasets.
