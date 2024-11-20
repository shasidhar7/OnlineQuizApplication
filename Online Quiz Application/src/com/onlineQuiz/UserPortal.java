package com.onlineQuiz;

import java.sql.*;
import java.util.Scanner;


public class UserPortal {

    // Method to display quizzes
    public static void listQuizzes() {
        String query = "SELECT * FROM quizzes";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

        	// Check if there are any quizzes
            if (!resultSet.isBeforeFirst()) {
                System.out.println("\nNo quizzes are available right now.");
                return;
            }
            
            System.out.println("\nAvailable Quizzes:");
            System.out.println("----------");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                System.out.println(id + ". " + title);
            }
            System.out.println("----------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to take a quiz
    public static void takeQuiz(Scanner scanner, int userId) {
    
        System.out.print("\nEnter quiz ID to take the quiz: ");
        int quizId = scanner.nextInt();
        scanner.nextLine(); 

        String query = "SELECT * FROM questions WHERE quiz_id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, quizId);
            ResultSet resultSet = statement.executeQuery();

            // Check if there are no questions for the selected quiz
            if (!resultSet.isBeforeFirst()) {
                System.out.println("\nNo quiz found with the selected ID.");
                return;  // Exit the method if no questions are found
            }
            
            int score = 0;
            int qId=1;
            while (resultSet.next()) {
                String question = resultSet.getString("question");
                String option1 = resultSet.getString("option1");
                String option2 = resultSet.getString("option2");
                String option3 = resultSet.getString("option3");
                String option4 = resultSet.getString("option4");
                String correctAnswers = resultSet.getString("correct_answers");

                System.out.println("\nQuestion: "+(qId++) + "\n\n"+question);
                System.out.println("1. " + option1);
                System.out.println("2. " + option2);
                System.out.println("3. " + option3);
                System.out.println("4. " + option4);
                
                int userAnswer = -1;
                boolean validAnswer = false;

                // Input validation: Ensure the answer is between 1 and 4
                while (!validAnswer) {
                    System.out.print("\nEnter your answer: ");
                    if (scanner.hasNextInt()) {
                        userAnswer = scanner.nextInt();
                        scanner.nextLine();  // Consume newline
                        if (userAnswer >= 1 && userAnswer <= 4) {
                            validAnswer = true;  // Valid answer
                        } else {
                            System.out.println("\nPlease select a proper option (1-4).");
                        }
                    } else {
                        System.out.println("Invalid input. Please enter a number between 1 and 4.");
                        scanner.nextLine();  // Consume invalid input
                    }
                }

                // Check if the user's answer matches the correct answers
                if (correctAnswers.contains(String.valueOf(userAnswer))) {
                    score++;
                    System.out.println("Correct!");
                } else {
                    System.out.println("Incorrect. The correct answer was: " + correctAnswers);
                }
            }

            // Insert the score for the current quiz attempt into the quiz_attempts table
            String insertQuery = "INSERT INTO quiz_attempts (user_id, quiz_id, score) VALUES (?, ?, ?)";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
                insertStatement.setInt(1, userId);
                insertStatement.setInt(2, quizId);
                insertStatement.setInt(3, score);
                insertStatement.executeUpdate();
            }
            
            System.out.println("\nYour total score is: " + score);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void viewQuizAttempts(int userId) {
        String query = "SELECT q.title, qa.score, qa.attempt_date FROM quiz_attempts qa " +
                       "JOIN quizzes q ON qa.quiz_id = q.id WHERE qa.user_id = ? ORDER BY qa.attempt_date DESC";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.isBeforeFirst()) {
                System.out.println("\nYou have not attempted any quizzes yet.");
                return;
            }

            System.out.println("\nYour Quiz Attempts:");
            System.out.println("-----------");
            while (resultSet.next()) {
                String quizTitle = resultSet.getString("title");
                int score = resultSet.getInt("score");
                Timestamp attemptDate = resultSet.getTimestamp("attempt_date");

                System.out.println("Quiz: " + quizTitle);
                System.out.println("Score: " + score);
                System.out.println("Attempt Date: " + attemptDate);
                System.out.println("-----------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void displayLeaderboard(Scanner scanner) {
    	
    	 System.out.print("\nEnter quiz ID to see the leaderboard: ");
    	    int quizId = scanner.nextInt();
    	    scanner.nextLine();
    	    
        String query = "SELECT u.username, MAX(qa.score) AS best_score FROM quiz_attempts qa " +
                "JOIN users u ON qa.user_id = u.id WHERE qa.quiz_id = ? " +
                "GROUP BY qa.user_id ORDER BY best_score DESC";
        
        		
        

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
             {
            	 statement.setInt(1, quizId);
            	 ResultSet resultSet= statement.executeQuery();

        	 if (!resultSet.isBeforeFirst()) {
        		 System.out.println("\nNo leaderboard data available for this quiz.");
                 return;
             }
        	 
        	
             System.out.println("\nLeaderboard for Quiz ID: " + quizId);
             System.out.println("------------------------------------------------");
             System.out.printf("%-10s %-20s %-10s\n", "Rank", "Username", "Best Score");
             System.out.println("------------------------------------------------");

            
            int rank = 1;
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                int bestScore = resultSet.getInt("best_score");

                System.out.printf("%-10d %-20s %-10d\n", rank, username, bestScore);
                rank++;
            }
            System.out.println("------------------------------------------------");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}