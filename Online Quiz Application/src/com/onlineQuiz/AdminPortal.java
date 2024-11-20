package com.onlineQuiz;

import java.sql.*;
import java.util.Scanner;

public class AdminPortal {

	    // Method to create a quiz
	    public static void createQuiz(Scanner scanner) {
	        System.out.print("\nEnter quiz title: ");
	        String title = scanner.nextLine();

	        System.out.print("Enter quiz description: ");
	        String description = scanner.nextLine();

	        String query = "INSERT INTO quizzes (title, description) VALUES (?, ?)";
	        try (Connection connection = DatabaseUtil.getConnection();
	             PreparedStatement statement = connection.prepareStatement(query)) {

	            statement.setString(1, title);
	            statement.setString(2, description);
	            int rowsAffected = statement.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Quiz created successfully.");
	            } else {
	                System.out.println("Failed to create quiz.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Method to add questions to a quiz
	    public static void addQuestion(Scanner scanner) {
	        System.out.print("\nEnter quiz ID to add questions: ");
	        int quizId = scanner.nextInt();
	        scanner.nextLine();  // Consume newline

	        System.out.print("Enter question title: ");
	        String question = scanner.nextLine();

	        System.out.println("Enter 4 options:");
	        String[] options = new String[4];
	        for (int i = 0; i < 4; i++) {
	            System.out.print("Option " + (i + 1) + ": ");
	            options[i] = scanner.nextLine();
	        }

	        System.out.print("Enter the correct option number (1-4): ");
	        int correctOption = scanner.nextInt();

	        if (correctOption < 1 || correctOption > 4) {
	            System.out.println("Invalid correct option. Please enter a number between 1 and 4.");
	            return;
	        }
	        
	        String correctOptions = String.valueOf(correctOption);
	        
	        String query = "INSERT INTO questions (quiz_id, question, option1, option2, option3, option4, correct_answers) " +
	                "VALUES (?, ?, ?, ?, ?, ?, ?)";
	        try (Connection connection = DatabaseUtil.getConnection();
	             PreparedStatement statement = connection.prepareStatement(query)) {

	            statement.setInt(1, quizId);
	            statement.setString(2, question);
	            statement.setString(3, options[0]);
	            statement.setString(4, options[1]);
	            statement.setString(5, options[2]);
	            statement.setString(6, options[3]);
	            statement.setString(7, correctOptions);

	            int rowsAffected = statement.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("Question added successfully.");
	            } else {
	                System.out.println("Failed to add question.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	    
	    //Method to edit a question using quiz ID
	    public static void editQuestionByQuizId(Scanner scanner) {
	        System.out.print("\nEnter quiz ID to edit questions: ");
	        int quizId = scanner.nextInt();
	        scanner.nextLine(); // Consume newline

	        //check if the quiz exists
	        String quizCheckQuery = "SELECT * FROM quizzes WHERE id = ?";
	        // Fetch and display questions for the given quiz ID
	        String fetchQuery = "SELECT id, question FROM questions WHERE quiz_id = ?";
	        try (Connection connection = DatabaseUtil.getConnection();
	        		PreparedStatement quizCheckStatement = connection.prepareStatement(quizCheckQuery);
	        		PreparedStatement statement = connection.prepareStatement(fetchQuery)) {

	        	quizCheckStatement.setInt(1, quizId);
	        	ResultSet quizResultSet = quizCheckStatement.executeQuery();
	        	
	        	if (!quizResultSet.next()) {
	                System.out.println("\nNo quiz found with the given quiz ID. Please check the quiz ID and try again.");
	                return; // Exit the method if the quiz is not found
	            }
	        	
	            statement.setInt(1, quizId);
	            ResultSet resultSet = statement.executeQuery();

	            // Check if no questions are found for the provided quiz ID
	            if (!resultSet.isBeforeFirst()) {
	                System.out.println("\nNo questions found for the given quiz ID.");
	                return; // Exit the method if no questions are found
	            }

	            // Display questions
	            System.out.println("\nQuestions for Quiz ID " + quizId + ":");
	            while (resultSet.next()) {
	                int questionId = resultSet.getInt("id");
	                String questionText = resultSet.getString("question");
	                System.out.println("Question ID: " + questionId + " - " + questionText);
	            }

	            // Prompt to select a question ID to edit
	            System.out.print("\nEnter the Question ID to edit: ");
	            int questionIdToEdit = scanner.nextInt();
	            scanner.nextLine(); // Consume newline

	            // Get new question details
	            System.out.print("Enter new question title: ");
	            String newQuestion = scanner.nextLine();

	            System.out.println("Enter new 4 options:");
	            String[] newOptions = new String[4];
	            for (int i = 0; i < 4; i++) {
	                System.out.print("Option " + (i + 1) + ": ");
	                newOptions[i] = scanner.nextLine();
	            }

	            System.out.print("Enter the correct option number (1-4): ");
	            int newCorrectOption = scanner.nextInt();
	            if (newCorrectOption < 1 || newCorrectOption > 4) {
	                System.out.println("Invalid correct option. Please enter a number between 1 and 4.");
	                return;
	            }

	            // Update the question in the database
	            String updateQuery = "UPDATE questions SET question = ?, option1 = ?, option2 = ?, option3 = ?, option4 = ?, correct_answers = ? WHERE id = ?";
	            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {

	                updateStatement.setString(1, newQuestion);
	                updateStatement.setString(2, newOptions[0]);
	                updateStatement.setString(3, newOptions[1]);
	                updateStatement.setString(4, newOptions[2]);
	                updateStatement.setString(5, newOptions[3]);
	                updateStatement.setString(6, String.valueOf(newCorrectOption));
	                updateStatement.setInt(7, questionIdToEdit);

	                int rowsAffected = updateStatement.executeUpdate();
	                if (rowsAffected > 0) {
	                    System.out.println("\nQuestion updated successfully.");
	                } else {
	                    System.out.println("\nFailed to update question.");
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Method to delete a question using quiz ID
	    public static void deleteQuestionByQuizId(Scanner scanner) {
	        System.out.print("\nEnter quiz ID to delete questions: ");
	        int quizId = scanner.nextInt();
	        scanner.nextLine(); // Consume newline

	        //check the quiz exists
	        String quizCheckQuery = "SELECT * FROM quizzes WHERE id = ?";
	        // Fetch and display questions for the given quiz ID
	        String fetchQuery = "SELECT id, question FROM questions WHERE quiz_id = ?";
	        try (Connection connection = DatabaseUtil.getConnection();
	        		PreparedStatement quizCheckStatement = connection.prepareStatement(quizCheckQuery);
	             PreparedStatement statement = connection.prepareStatement(fetchQuery)) {

	        	quizCheckStatement.setInt(1, quizId);
	        	ResultSet quizResultSet = quizCheckStatement.executeQuery();
	        	
	        	if (!quizResultSet.next()) {
	                System.out.println("\nNo quiz found with the given quiz ID. Please check the quiz ID and try again.");
	                return; // Exit the method if the quiz is not found
	            }
	        	
	            statement.setInt(1, quizId);
	            ResultSet resultSet = statement.executeQuery();

	            // Check if no questions are found for the provided quiz ID
	            if (!resultSet.isBeforeFirst()) {
	                System.out.println("\nNo questions found for the given quiz ID.");
	                return; // Exit the method if no questions are found
	            }

	            // Display questions
	            System.out.println("\nQuestions for Quiz ID " + quizId + ":");
	            while (resultSet.next()) {
	                int questionId = resultSet.getInt("id");
	                String questionText = resultSet.getString("question");
	                System.out.println("Question ID: " + questionId + " - " + questionText);
	            }

	            // Prompt to select a question ID to delete
	            System.out.print("\nEnter the Question ID to delete: ");
	            int questionIdToDelete = scanner.nextInt();
	            scanner.nextLine(); // Consume newline

	            // Delete the question from the database
	            String deleteQuery = "DELETE FROM questions WHERE id = ?";
	            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {

	                deleteStatement.setInt(1, questionIdToDelete);

	                int rowsAffected = deleteStatement.executeUpdate();
	                if (rowsAffected > 0) {
	                    System.out.println("\nQuestion deleted successfully.");
	                } else {
	                    System.out.println("\nFailed to delete question.");
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }


	    // Method to edit quiz details
	    public static void editQuiz(Scanner scanner) {
	        System.out.print("\nEnter quiz ID to edit: ");
	        int quizId = scanner.nextInt();
	        scanner.nextLine();  // Consume newline

	        //Check if there is quiz found with the given id or not
	        String selectQuery = "SELECT * FROM quizzes WHERE id = ?";
	        try (Connection connection = DatabaseUtil.getConnection();
	             PreparedStatement statement = connection.prepareStatement(selectQuery)) {

	            statement.setInt(1, quizId);
	            ResultSet resultSet = statement.executeQuery();

	            // Check if no quiz is found with the provided ID
	            if (!resultSet.next()) {
	                System.out.println("\nNo quiz found to edit with the provided ID.");
	                return;  // Exit the method if quiz is not found
	            }
	        } catch (SQLException e1) {
				e1.printStackTrace();
			}
	        
	        System.out.print("Enter new title for the quiz: ");
	        String newTitle = scanner.nextLine();

	        System.out.print("Enter new description for the quiz: ");
	        String newDescription = scanner.nextLine();

	        String query = "UPDATE quizzes SET title = ?, description = ? WHERE id = ?";
	        try (Connection connection = DatabaseUtil.getConnection();
	             PreparedStatement statement = connection.prepareStatement(query)) {

	            statement.setString(1, newTitle);
	            statement.setString(2, newDescription);
	            statement.setInt(3, quizId);

	            int rowsAffected = statement.executeUpdate();
	            
	            if (rowsAffected > 0) {
	                System.out.println("\nQuiz updated successfully.");
	            } else {
	                System.out.println("\nFailed to update quiz.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Method to delete a quiz
	    public static void deleteQuiz(Scanner scanner) {
	        System.out.print("\nEnter quiz ID to delete: ");
	        int quizId = scanner.nextInt();
	        scanner.nextLine();
	        
	        String selectQuery = "SELECT * FROM quizzes WHERE id = ?";
	        try (Connection connection = DatabaseUtil.getConnection();
	             PreparedStatement statement = connection.prepareStatement(selectQuery)) {

	            statement.setInt(1, quizId);
	            ResultSet resultSet = statement.executeQuery();

	            // Check if no quiz is found with the provided ID
	            if (!resultSet.next()) {
	                System.out.println("\nNo quiz found to delete with the provided ID.");
	                return;  // Exit the method if quiz is not found
	            }
	        } catch (SQLException e1) {
				e1.printStackTrace();
			}
	        
	        //first we need to delete associated questions
	        String deleteQuestionsQuery = "DELETE FROM questions WHERE quiz_id = ?";
	        try (Connection connection = DatabaseUtil.getConnection();
	             PreparedStatement deleteQuestionsStmt = connection.prepareStatement(deleteQuestionsQuery)) {

	            deleteQuestionsStmt.setInt(1, quizId);
	            deleteQuestionsStmt.executeUpdate();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            return;
	        }

	        //after deleteing associated question, delete quiz
	        String query = "DELETE FROM quizzes WHERE id = ?";
	        try (Connection connection = DatabaseUtil.getConnection();
	             PreparedStatement statement = connection.prepareStatement(query)) {

	            statement.setInt(1, quizId);

	            int rowsAffected = statement.executeUpdate();
	            if (rowsAffected > 0) {
	                System.out.println("\nQuiz deleted successfully.");
	            } else {
	                System.out.println("\nFailed to delete quiz.");
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
}

