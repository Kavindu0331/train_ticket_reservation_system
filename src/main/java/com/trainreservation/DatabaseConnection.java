package com.trainreservation;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class DatabaseConnection {

    private static final Properties properties =
        new Properties();

    static {
        try (
            InputStream input =
                DatabaseConnection.class
                    .getClassLoader()
                    .getResourceAsStream(
                        "database.properties"
                    )
        ) {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseConnection.class
                .getClassLoader()
                .getResourceAsStream("database.properties")) {

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
    }

    public static Connection getConnection()
        throws SQLException {

        String url =
            properties.getProperty("db.url");

        String username =
            properties.getProperty("db.username");

        String password =
            properties.getProperty(
                "db.password",
                ""
            );

        if (
            url == null
                || url.isBlank()
        ) {
            throw new SQLException(
                "Database URL is missing."
            );
        }

        return DriverManager.getConnection(
            url,
            username,
            password
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            properties.getProperty("db.url"),
            properties.getProperty("db.username"),
            properties.getProperty("db.password")
        );
    }
}