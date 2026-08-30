package com.trainreservation;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {

    private static final Properties properties = new Properties();

    // Change these defaults to match your database.
    private static final String DEFAULT_URL =
        "jdbc:mysql://localhost:3306/train_reservation";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "";

    static {
        try (
            InputStream input =
                DatabaseConnection.class
                    .getClassLoader()
                    .getResourceAsStream("database.properties")
        ) {
            if (input == null) {
                throw new RuntimeException(
                    "database.properties was not found."
                );
            }

            properties.load(input);

        } catch (Exception exception) {
            throw new RuntimeException(
                "Could not load database settings.",
                exception
            );
        }
    }

    private DatabaseConnection() {
        // Prevent creation of DatabaseConnection objects.
    }

    public static Connection getConnection()
        throws SQLException {

        String url = getSetting(
            "TRAIN_DB_URL",
            "db.url",
            DEFAULT_URL
        );

        String username = getSetting(
            "TRAIN_DB_USERNAME",
            "db.username",
            DEFAULT_USERNAME
        );

        String password = getSetting(
            "TRAIN_DB_PASSWORD",
            "db.password",
            DEFAULT_PASSWORD
        );

        return DriverManager.getConnection(
            url,
            username,
            password
        );
    }

    private static String getSetting(
        String environmentName,
        String propertyName,
        String defaultValue
    ) {
        String environmentValue =
            System.getenv(environmentName);

        if (
            environmentValue != null
                && !environmentValue.isBlank()
        ) {
            return environmentValue.trim();
        }

        String propertyValue =
            properties.getProperty(propertyName);

        if (
            propertyValue != null
                && !propertyValue.isBlank()
        ) {
            return propertyValue.trim();
        }

        return defaultValue;
    }

    public static boolean testConnection() {
        try (
            Connection connection = getConnection()
        ) {
            return connection != null
                && !connection.isClosed();

        } catch (SQLException exception) {
            System.out.println(
                "Database connection failed: "
                    + exception.getMessage()
            );

            return false;
        }
    }
}