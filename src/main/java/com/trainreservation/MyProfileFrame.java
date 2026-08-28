package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MyProfileFrame extends JFrame {

    private final JFrame previousFrame;

    private final JTextField nameField =
        new JTextField();

    private final JTextField emailField =
        new JTextField();

    private final JTextField phoneField =
        new JTextField();

    private final JLabel roleLabel =
        new JLabel("-");

    private final JButton saveButton =
        makeButton("SAVE CHANGES");

    public MyProfileFrame(
        JFrame previousFrame
    ) {
        this.previousFrame = previousFrame;

        if (!UserSession.isLoggedIn()) {
            JOptionPane.showMessageDialog(
                null,
                "Please log in first.",
                "Access Denied",
                JOptionPane.WARNING_MESSAGE
            );

            new LoginFrame().setVisible(true);
            dispose();
            return;
        }

        setTitle("My Profile");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        createInterface();
        loadProfile();

        addWindowListener(
            new java.awt.event.WindowAdapter() {
                public void windowClosing(
                    java.awt.event.WindowEvent event
                ) {
                    returnToPreviousPage();
                }
            }
        );
    }

    private void createInterface() {
        JPanel mainPanel =
            new JPanel(new BorderLayout(15, 15));

        mainPanel.setBackground(
            new Color(238, 244, 250)
        );

        mainPanel.setBorder(
            new EmptyBorder(30, 40, 30, 40)
        );

        JLabel title =
            new JLabel(
                "My Profile",
                SwingConstants.CENTER
            );

        title.setFont(
            new Font("Arial", Font.BOLD, 30)
        );

        title.setForeground(
            new Color(15, 75, 140)
        );

        JPanel formPanel =
            new JPanel(new GridBagLayout());

        formPanel.setBackground(Color.WHITE);

        formPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(205, 216, 230)
                ),
                new EmptyBorder(30, 40, 30, 40)
            )
        );

        GridBagConstraints c =
            new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);

        addInput(
            formPanel,
            c,
            "Full Name",
            nameField,
            0
        );

        addInput(
            formPanel,
            c,
            "Email",
            emailField,
            1
        );

        addInput(
            formPanel,
            c,
            "Phone",
            phoneField,
            2
        );

        roleLabel.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        roleLabel.setForeground(
            new Color(15, 75, 140)
        );

        addInput(
            formPanel,
            c,
            "Account Role",
            roleLabel,
            3
        );

        JButton backButton =
            makeButton("BACK");

        JPanel buttonPanel =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT,
                    12,
                    0
                )
            );

        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        buttonPanel.add(saveButton);

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.insets = new Insets(25, 10, 5, 10);

        formPanel.add(buttonPanel, c);

        mainPanel.add(
            title,
            BorderLayout.NORTH
        );

        mainPanel.add(
            formPanel,
            BorderLayout.CENTER
        );

        setContentPane(mainPanel);

        saveButton.addActionListener(
            event -> updateProfile()
        );

        backButton.addActionListener(
            event -> returnToPreviousPage()
        );
    }

    private void addInput(
        JPanel panel,
        GridBagConstraints c,
        String labelText,
        JComponent input,
        int row
    ) {
        JLabel label =
            new JLabel(labelText);

        label.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        input.setPreferredSize(
            new Dimension(350, 42)
        );

        input.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        c.gridy = row;
        c.gridwidth = 1;

        c.gridx = 0;
        c.weightx = 0;

        panel.add(label, c);

        c.gridx = 1;
        c.weightx = 1;

        panel.add(input, c);
    }

    private static JButton makeButton(
        String text
    ) {
        JButton button =
            new JButton(text);

        button.setUI(new BasicButtonUI());

        button.setPreferredSize(
            new Dimension(175, 44)
        );

        button.setBackground(
            new Color(25, 105, 195)
        );

        button.setForeground(Color.WHITE);

        button.setFont(
            new Font("Arial", Font.BOLD, 12)
        );

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setCursor(
            Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
            )
        );

        return button;
    }

    private void loadProfile() {
        String sql = """
            SELECT
                full_name,
                email,
                phone,
                role
            FROM users
            WHERE user_id = ?
            """;

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setLong(
                1,
                UserSession.getUserId()
            );

            try (
                ResultSet result =
                    statement.executeQuery()
            ) {
                if (!result.next()) {
                    throw new Exception(
                        "The user account was not found."
                    );
                }

                nameField.setText(
                    result.getString("full_name")
                );

                emailField.setText(
                    result.getString("email")
                );

                phoneField.setText(
                    result.getString("phone")
                );

                roleLabel.setText(
                    result.getString("role")
                );
            }

        } catch (Exception exception) {
            showError(
                "Could not load profile:\n"
                    + exception.getMessage()
            );

            saveButton.setEnabled(false);
        }
    }

    private void updateProfile() {
        String fullName =
            nameField.getText().trim();

        String email =
            emailField.getText().trim();

        String phone =
            phoneField.getText().trim();

        if (
            fullName.isEmpty()
                || email.isEmpty()
                || phone.isEmpty()
        ) {
            showWarning(
                "Complete all profile details.",
                "Missing Information"
            );
            return;
        }

        if (fullName.length() < 3) {
            showWarning(
                "Enter a valid full name.",
                "Invalid Name"
            );
            return;
        }

        if (
            !email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
            )
        ) {
            showWarning(
                "Enter a valid email address.",
                "Invalid Email"
            );
            return;
        }

        if (!phone.matches("[0-9+\\- ]{7,20}")) {
            showWarning(
                "Enter a valid phone number.",
                "Invalid Phone"
            );
            return;
        }

        String duplicateSql = """
            SELECT user_id
            FROM users
            WHERE LOWER(email) = LOWER(?)
              AND user_id <> ?
            """;

        String updateSql = """
            UPDATE users
            SET
                full_name = ?,
                email = ?,
                phone = ?
            WHERE user_id = ?
            """;

        saveButton.setEnabled(false);
        saveButton.setText("SAVING...");

        try (
            Connection connection =
                DatabaseConnection.getConnection()
        ) {
            try (
                PreparedStatement duplicateStatement =
                    connection.prepareStatement(
                        duplicateSql
                    )
            ) {
                duplicateStatement.setString(
                    1,
                    email
                );

                duplicateStatement.setLong(
                    2,
                    UserSession.getUserId()
                );

                try (
                    ResultSet result =
                        duplicateStatement.executeQuery()
                ) {
                    if (result.next()) {
                        showWarning(
                            "This email is already used by another account.",
                            "Duplicate Email"
                        );
                        return;
                    }
                }
            }

            try (
                PreparedStatement updateStatement =
                    connection.prepareStatement(
                        updateSql
                    )
            ) {
                updateStatement.setString(
                    1,
                    fullName
                );

                updateStatement.setString(
                    2,
                    email
                );

                updateStatement.setString(
                    3,
                    phone
                );

                updateStatement.setLong(
                    4,
                    UserSession.getUserId()
                );

                int updatedRows =
                    updateStatement.executeUpdate();

                if (updatedRows != 1) {
                    throw new Exception(
                        "The profile was not updated."
                    );
                }
            }

            UserSession.start(
                UserSession.getUserId(),
                fullName,
                UserSession.getRole()
            );

            JOptionPane.showMessageDialog(
                this,
                "Profile updated successfully.",
                "Profile Updated",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception exception) {
            showError(
                "Could not update profile:\n"
                    + exception.getMessage()
            );

        } finally {
            saveButton.setEnabled(true);
            saveButton.setText("SAVE CHANGES");
        }
    }

    private void showWarning(
        String message,
        String title
    ) {
        JOptionPane.showMessageDialog(
            this,
            message,
            title,
            JOptionPane.WARNING_MESSAGE
        );
    }

    private void showError(
        String message
    ) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Profile Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private void returnToPreviousPage() {
        dispose();

        if (previousFrame != null) {
            previousFrame.setVisible(true);
            previousFrame.toFront();
        }
    }
}