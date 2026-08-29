package com.trainreservation;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {

    private static final String DEFAULT_URL =
        "jdbc:mysql://localhost:3306/train_reservation_db"
            + "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=Asia/Colombo"
            + "&connectTimeout=5000";

    private static final String DEFAULT_USERNAME =
        "root";

    private static final String DEFAULT_PASSWORD =
        "";

    private static final Properties PROPERTIES =
        new Properties();

    static {
        loadProperties();
    }

    private DatabaseConnection() {
    }

    private static void loadProperties() {
        try (
            InputStream inputStream =
                DatabaseConnection.class
                    .getClassLoader()
                    .getResourceAsStream(
                        "database.properties"
                    )
        ) {
            if (inputStream != null) {
                PROPERTIES.load(inputStream);
            } else {
                System.out.println(
                    "database.properties was not found. "
                        + "Using local XAMPP defaults."
                );
            }

        } catch (IOException exception) {
            System.out.println(
                "Could not read database.properties. "
                    + "Using local XAMPP defaults."
            );

            exception.printStackTrace();
        }
    }

    public static Connection getConnection()
        throws SQLException {

        String url =
            getSetting(
                "TRAIN_DB_URL",
                "db.url",
                DEFAULT_URL
            );

        String username =
            getSetting(
                "TRAIN_DB_USERNAME",
                "db.username",
                DEFAULT_USERNAME
            );

        String password =
            getSetting(
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
            PROPERTIES.getProperty(
                propertyName
            );

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
            Connection connection =
                getConnection()
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