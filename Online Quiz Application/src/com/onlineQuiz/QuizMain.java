package com.onlineQuiz;

import java.util.Scanner;

public class QuizMain {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
        	System.out.println("----------------------------------------");
            System.out.println("Welcome to the Online Quiz Application");
            System.out.println("----------------------------------------");
            System.out.println("1. Sign Up");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.println("----------------------------------------");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline character

            switch (choice) {
                case 1:
                    signUp(scanner);
                    break;
                case 2:
                    login(scanner);
                    break;
                case 3:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;  // Exit the application
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void signUp(Scanner scanner) {
        System.out.println("\nPlease Sign Up");
        try {
            System.out.print("\nEnter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Generate salt and hash the password
            String salt = PasswordUtil.getSalt();
            String hashedPassword = PasswordUtil.hashPassword(password, salt);

            System.out.print("Enter role (user/admin): ");
            String role = scanner.nextLine().toLowerCase();

            if (!role.equals("user") && !role.equals("admin")) {
                System.out.println("Invalid role. Must be either 'user' or 'admin'.");
                return;
            }

            // Insert user into the database
            String query = "INSERT INTO users (username, hashed_password, salt, role) VALUES (?, ?, ?, ?)";
            try (var connection = DatabaseUtil.getConnection();
                 var statement = connection.prepareStatement(query)) {

                statement.setString(1, username);
                statement.setString(2, hashedPassword);
                statement.setString(3, salt);
                statement.setString(4, role);

                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("\nSign-up successful!");
                } else {
                    System.out.println("\nSign-up failed. Please try again.");
                }
            }
        } catch (Exception e) {
        	System.out.println("An error occured during sign-up: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void login(Scanner scanner) {
        System.out.println("\nPlease Login with your credentials");
        try {
            System.out.print("\nEnter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            // Fetch user details from the database
            String query = "SELECT id, hashed_password, salt, role FROM users WHERE username = ?";
            try (var connection = DatabaseUtil.getConnection();
                 var statement = connection.prepareStatement(query)) {

                statement.setString(1, username);
                var resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String storedHash = resultSet.getString("hashed_password");
                    String storedSalt = resultSet.getString("salt");
                    String role = resultSet.getString("role");
                    int userId = resultSet.getInt("id");

                    // Verify the entered password
                    if (PasswordUtil.verifyPassword(password, storedHash, storedSalt)) {
                        System.out.println("\nLogin successful!");

                        // Redirect based on the role
                        if (role.equals("admin")) {
                            System.out.println("Welcome Admin!");
                            
                            while (true) {
                                System.out.println("\nAdmin Portal:");
                                System.out.println("1. Create a Quiz");
                                System.out.println("2. Add Questions to Quiz");
                                System.out.println("3. Edit question in Quiz");
                                System.out.println("4. Delete Question in quiz");
                                System.out.println("5. Edit Quiz");
                                System.out.println("6. Delete Quiz");
                                System.out.println("7. Exit");
                                System.out.print("Choose an option: ");
                                int choice = scanner.nextInt();
                                scanner.nextLine();  // Consume newline

                                switch (choice) {
                                    case 1:
                                        AdminPortal.createQuiz(scanner);  // Create a quiz
                                        break;
                                    case 2:
                                        AdminPortal.addQuestion(scanner);  // Add questions to a quiz
                                        break;
                                    case 3:
                                        AdminPortal.editQuestionByQuizId(scanner);  // Edit question
                                        break;
                                    case 4:
                                        AdminPortal.deleteQuestionByQuizId(scanner); // Delete Question
                                        break;
                                    case 5:
                                        AdminPortal.editQuiz(scanner);  // Edit a quiz
                                        break;
                                    case 6:
                                        AdminPortal.deleteQuiz(scanner);  // Delete a quizeleteQuiz(sc
                                        break;
                                    case 7:
                                        return;  // Exit admin portal
                                    default:
                                        System.out.println("Invalid choice. Please try again.");
                                }
                            }
                        
                        } else {
                            System.out.println("Welcome User!");
                            // User Portal logic here
                            while (true) {
                                System.out.println("\nUser Portal:");
                                System.out.println("1. List of Available Quizzes");
                                System.out.println("2. Take a Quiz");
                                System.out.println("3. View Past Quiz attempts");
                                System.out.println("4. View Leaderboard");
                                System.out.println("5. Exit");
                                System.out.print("Choose an option: ");
                                int choice = scanner.nextInt();
                                scanner.nextLine();  // Consume newline

                                switch (choice) {
                                    case 1:
                                        UserPortal.listQuizzes();  // List available quizzes
                                        break;
                                    case 2:
                                        UserPortal.takeQuiz(scanner, userId);  // Take a quiz
                                        break;
                                    case 3:
                                    	UserPortal.viewQuizAttempts(userId);
                                    	break;
                                    case 4:
                                    	UserPortal.displayLeaderboard(scanner);
                                    	break;
                                    case 5:
                                        return;  // Exit user portal
                                    default:
                                        System.out.println("\nInvalid choice. Please try again.");
                                }
                            }
                        }
                    } else {
                        System.out.println("\nInvalid password.");
                    }
                } else {
                    System.out.println("\nUser not found.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
