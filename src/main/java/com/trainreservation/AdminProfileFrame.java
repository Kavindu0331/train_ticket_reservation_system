package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class AdminProfileFrame extends JFrame {

    private final JFrame dashboard;
    private final long adminId;

    private final JTextField nameField =
        new JTextField();

    private final JTextField emailField =
        new JTextField();

    private final JTextField phoneField =
        new JTextField();

    private final JPasswordField passwordField =
        new JPasswordField();

    private final JPasswordField confirmField =
        new JPasswordField();

    public AdminProfileFrame(
        JFrame dashboard,
        long adminId
    ) {
        this.dashboard = dashboard;
        this.adminId = adminId;

        setTitle("Admin Profile");
        setSize(650, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(
            new GridBagLayout()
        );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
            new EmptyBorder(30, 55, 30, 55)
        );

        GridBagConstraints c =
            new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel(
            "Admin Profile",
            SwingConstants.CENTER
        );

        title.setFont(
            new Font("Arial", Font.BOLD, 28)
        );

        title.setForeground(
            new Color(15, 67, 125)
        );

        JLabel description = new JLabel(
            "View and update your account",
            SwingConstants.CENTER
        );

        description.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        description.setForeground(
            new Color(90, 100, 110)
        );

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(5, 5, 3, 5);

        panel.add(title, c);

        c.gridy = 1;
        c.insets = new Insets(3, 5, 22, 5);

        panel.add(description, c);

        addField(
            panel,
            c,
            "Full Name",
            nameField,
            2
        );

        addField(
            panel,
            c,
            "Email",
            emailField,
            3
        );

        addField(
            panel,
            c,
            "Phone",
            phoneField,
            4
        );

        addField(
            panel,
            c,
            "New Password",
            passwordField,
            5
        );

        addField(
            panel,
            c,
            "Confirm Password",
            confirmField,
            6
        );

        JLabel passwordNote = new JLabel(
            "Leave password fields empty to keep the current password."
        );

        passwordNote.setFont(
            new Font("Arial", Font.ITALIC, 12)
        );

        passwordNote.setForeground(
            new Color(100, 105, 115)
        );

        c.gridx = 1;
        c.gridy = 7;
        c.gridwidth = 1;
        c.insets = new Insets(0, 5, 12, 5);

        panel.add(passwordNote, c);

        JButton saveButton =
            makeButton("SAVE");

        JButton resetButton =
            makeButton("RESET");

        JButton backButton =
            makeButton("BACK");

        JPanel buttonPanel = new JPanel(
            new FlowLayout(
                FlowLayout.CENTER,
                12,
                0
            )
        );

        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(backButton);

        c.gridx = 0;
        c.gridy = 8;
        c.gridwidth = 2;
        c.insets = new Insets(20, 5, 5, 5);

        panel.add(buttonPanel, c);

        setContentPane(panel);

        saveButton.addActionListener(
            event -> saveProfile()
        );

        resetButton.addActionListener(
            event -> loadProfile()
        );

        backButton.addActionListener(
            event -> returnToDashboard()
        );

        addWindowListener(new WindowAdapter() {
            public void windowClosing(
                WindowEvent event
            ) {
                returnToDashboard();
            }
        });

        loadProfile();
    }

    private void addField(
        JPanel panel,
        GridBagConstraints c,
        String text,
        JComponent field,
        int row
    ) {
        JLabel label = new JLabel(text);

        label.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        label.setForeground(
            new Color(35, 40, 50)
        );

        field.setPreferredSize(
            new Dimension(320, 40)
        );

        field.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.weightx = 0;
        c.insets = new Insets(9, 5, 9, 20);

        panel.add(label, c);

        c.gridx = 1;
        c.weightx = 1;
        c.insets = new Insets(9, 5, 9, 5);

        panel.add(field, c);
    }

    private JButton makeButton(String text) {
        JButton button = new JButton(text);

        button.setUI(new BasicButtonUI());

        button.setPreferredSize(
            new Dimension(120, 42)
        );

        button.setBackground(
            new Color(21, 101, 192)
        );

        button.setForeground(Color.WHITE);

        button.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(
            Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
            )
        );

        return button;
    }

    private void loadProfile() {
        String sql = """
            SELECT full_name, email, phone
            FROM users
            WHERE user_id = ?
              AND role = 'ADMIN'
            """;

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setLong(1, adminId);

            try (
                ResultSet result =
                    statement.executeQuery()
            ) {
                if (result.next()) {
                    nameField.setText(
                        result.getString("full_name")
                    );

                    emailField.setText(
                        result.getString("email")
                    );

                    String phone =
                        result.getString("phone");

                    phoneField.setText(
                        phone == null ? "" : phone
                    );

                    passwordField.setText("");
                    confirmField.setText("");
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Administrator profile was not found.",
                        "Profile Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not load the profile:\n"
                    + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void saveProfile() {
        String fullName =
            nameField.getText().trim();

        String email =
            emailField.getText().trim();

        String phone =
            phoneField.getText().trim();

        String password =
            new String(passwordField.getPassword());

        String confirmation =
            new String(confirmField.getPassword());

        if (fullName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Full name and email are required.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (
            !email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
            )
        ) {
            JOptionPane.showMessageDialog(
                this,
                "Enter a valid email address.",
                "Invalid Email",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (
            !phone.isEmpty()
                && !phone.matches("[0-9+\\- ]{7,20}")
        ) {
            JOptionPane.showMessageDialog(
                this,
                "Enter a valid phone number.",
                "Invalid Phone",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (!password.equals(confirmation)) {
            JOptionPane.showMessageDialog(
                this,
                "The passwords do not match.",
                "Password Error",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (
            !password.isEmpty()
                && password.length() < 6
        ) {
            JOptionPane.showMessageDialog(
                this,
                "The new password must have at least 6 characters.",
                "Password Error",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (password.isEmpty()) {
            updateWithoutPassword(
                fullName,
                email,
                phone
            );
        } else {
            updateWithPassword(
                fullName,
                email,
                phone,
                password
            );
        }
    }

    private void updateWithoutPassword(
        String fullName,
        String email,
        String phone
    ) {
        String sql = """
            UPDATE users
            SET full_name = ?,
                email = ?,
                phone = ?,
                updated_at = CURRENT_TIMESTAMP
            WHERE user_id = ?
              AND role = 'ADMIN'
            """;

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setString(1, fullName);
            statement.setString(2, email);
            statement.setString(3, phone);
            statement.setLong(4, adminId);

            finishUpdate(statement);
        } catch (
            SQLIntegrityConstraintViolationException exception
        ) {
            showDuplicateEmail();
        } catch (SQLException exception) {
            showDatabaseError(exception);
        }
    }

    private void updateWithPassword(
        String fullName,
        String email,
        String phone,
        String password
    ) {
        String sql = """
            UPDATE users
            SET full_name = ?,
                email = ?,
                phone = ?,
                password_hash = SHA2(?, 256),
                updated_at = CURRENT_TIMESTAMP
            WHERE user_id = ?
              AND role = 'ADMIN'
            """;

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setString(1, fullName);
            statement.setString(2, email);
            statement.setString(3, phone);
            statement.setString(4, password);
            statement.setLong(5, adminId);

            finishUpdate(statement);
        } catch (
            SQLIntegrityConstraintViolationException exception
        ) {
            showDuplicateEmail();
        } catch (SQLException exception) {
            showDatabaseError(exception);
        }
    }

    private void finishUpdate(
        PreparedStatement statement
    ) throws SQLException {
        int changedRows =
            statement.executeUpdate();

        if (changedRows > 0) {
            JOptionPane.showMessageDialog(
                this,
                "Profile updated successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            passwordField.setText("");
            confirmField.setText("");
        } else {
            JOptionPane.showMessageDialog(
                this,
                "The profile could not be updated.",
                "Update Failed",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void showDuplicateEmail() {
        JOptionPane.showMessageDialog(
            this,
            "That email address is already in use.",
            "Duplicate Email",
            JOptionPane.WARNING_MESSAGE
        );
    }

    private void showDatabaseError(
        SQLException exception
    ) {
        JOptionPane.showMessageDialog(
            this,
            "Could not update the profile:\n"
                + exception.getMessage(),
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private void returnToDashboard() {
        dispose();

        if (dashboard != null) {
            dashboard.setVisible(true);
            dashboard.toFront();
            dashboard.requestFocus();
        }
    }
}