package com.onlineQuiz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtil {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/quizapp";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";

    // Method to establish a database connection
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
    }

    // Method to add a new user to the database
//    public static void addUser(String username, String hashedPassword, String salt, String role) {
//        String query = "INSERT INTO users (username, password, salt, role) VALUES (?, ?, ?, ?)";
//        try (Connection connection = getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1, username);
//            statement.setString(2, hashedPassword);
//            statement.setString(3, salt);
//            statement.setString(4, role);
//            statement.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println("Error adding user: " + e.getMessage());
//        }
//    }
//
//    // Method to retrieve the hashed password for a user
//    public static String getHashedPassword(String username) {
//        String query = "SELECT password FROM users WHERE username = ?";
//        try (Connection connection = getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1, username);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getString("password");
//            }
//        } catch (SQLException e) {
//            System.out.println("Error retrieving hashed password: " + e.getMessage());
//        }
//        return null;
//    }
//
//    // Method to retrieve the salt for a user
//    public static String getSalt(String username) {
//        String query = "SELECT salt FROM users WHERE username = ?";
//        try (Connection connection = getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1, username);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getString("salt");
//            }
//        } catch (SQLException e) {
//            System.out.println("Error retrieving salt: " + e.getMessage());
//        }
//        return null;
//    }
//
//    // Method to retrieve the role of a user
//    public static String getUserRole(String username) {
//        String query = "SELECT role FROM users WHERE username = ?";
//        try (Connection connection = getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setString(1, username);
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getString("role");
//            }
//        } catch (SQLException e) {
//            System.out.println("Error retrieving user role: " + e.getMessage());
//        }
//        return null;
//    }
}
