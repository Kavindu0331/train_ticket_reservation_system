package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminLoginFrame extends JFrame {

    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;

    public AdminLoginFrame() {
        setTitle("Train Reservation - Admin Login");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(30, 45, 30, 45));
        mainPanel.setBackground(new Color(240, 244, 248));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(8, 5, 8, 5);

        JLabel titleLabel = new JLabel("Admin Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 26));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(25, 70, 120));

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        mainPanel.add(titleLabel, constraints);

        JLabel descriptionLabel =
            new JLabel("Train Ticket Reservation System");
        descriptionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        constraints.gridy = 1;
        mainPanel.add(descriptionLabel, constraints);

        constraints.gridwidth = 1;
        constraints.gridy = 2;
        constraints.gridx = 0;
        mainPanel.add(new JLabel("Email:"), constraints);

        emailField = new JTextField(20);

        constraints.gridx = 1;
        mainPanel.add(emailField, constraints);

        constraints.gridy = 3;
        constraints.gridx = 0;
        mainPanel.add(new JLabel("Password:"), constraints);

        passwordField = new JPasswordField(20);

        constraints.gridx = 1;
        mainPanel.add(passwordField, constraints);

        loginButton = new JButton("Login");
        loginButton.setBackground(new Color(25, 100, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        constraints.gridy = 4;
        constraints.gridx = 0;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(20, 5, 5, 5);
        mainPanel.add(loginButton, constraints);

        loginButton.addActionListener(event -> login());
        passwordField.addActionListener(event -> login());

        add(mainPanel);
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Enter your email and password.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String sql = """
            SELECT user_id, full_name
            FROM users
            WHERE email = ?
              AND password_hash = SHA2(?, 256)
              AND role = 'ADMIN'
            """;

        loginButton.setEnabled(false);

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    long adminId = result.getLong("user_id");
                    String adminName =
                        result.getString("full_name");

                    dispose();

                    new AdminDashboard(
                        adminId,
                        adminName
                    ).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Incorrect admin email or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                    );

                    passwordField.setText("");
                }
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not connect to the database.\n"
                    + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        } finally {
            loginButton.setEnabled(true);
        }
    }
}