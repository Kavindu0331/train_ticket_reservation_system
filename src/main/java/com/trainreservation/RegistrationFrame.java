package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RegistrationFrame extends JFrame {

    private final JFrame previousFrame;

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

    private final JCheckBox showPasswordBox =
        new JCheckBox("Show Passwords");

    private final JButton registerButton =
        createButton("CREATE ACCOUNT");

    public RegistrationFrame(JFrame previousFrame) {
        this.previousFrame = previousFrame;

        setTitle("Customer Registration");
        setSize(900, 790);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        createInterface();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(
                java.awt.event.WindowEvent event
            ) {
                returnToLogin();
            }
        });
    }

    private void createInterface() {
        JPanel background = new JPanel(
            new GridBagLayout()
        );

        background.setBackground(
            new Color(235, 243, 251)
        );

        background.setBorder(
            new EmptyBorder(25, 35, 25, 35)
        );

        JPanel card = new JPanel(
            new BorderLayout(15, 20)
        );

        card.setBackground(Color.WHITE);
        card.setPreferredSize(
            new Dimension(780, 680)
        );

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(205, 218, 232)
                ),
                new EmptyBorder(30, 55, 30, 55)
            )
        );

        JPanel headingPanel = new JPanel();
        headingPanel.setOpaque(false);
        headingPanel.setLayout(
            new BoxLayout(
                headingPanel,
                BoxLayout.Y_AXIS
            )
        );

        JLabel icon = new JLabel("\uD83D\uDC64");
        icon.setFont(
            new Font(
                "Segoe UI Emoji",
                Font.PLAIN,
                43
            )
        );
        icon.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel title =
            new JLabel("Create Account");

        title.setFont(
            new Font("Arial", Font.BOLD, 31)
        );

        title.setForeground(
            new Color(15, 75, 140)
        );

        title.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel subtitle = new JLabel(
            "Enter your details to create a customer account"
        );

        subtitle.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        subtitle.setForeground(
            new Color(75, 85, 100)
        );

        subtitle.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        headingPanel.add(icon);
        headingPanel.add(
            Box.createVerticalStrut(5)
        );
        headingPanel.add(title);
        headingPanel.add(
            Box.createVerticalStrut(7)
        );
        headingPanel.add(subtitle);

        JPanel formPanel = new JPanel(
            new GridBagLayout()
        );

        formPanel.setOpaque(false);

        GridBagConstraints constraints =
            new GridBagConstraints();

        constraints.fill =
            GridBagConstraints.HORIZONTAL;

        constraints.insets =
            new Insets(8, 8, 8, 8);

        addInput(
            formPanel,
            constraints,
            "Full Name",
            nameField,
            0
        );

        addInput(
            formPanel,
            constraints,
            "Email",
            emailField,
            1
        );

        addInput(
            formPanel,
            constraints,
            "Phone",
            phoneField,
            2
        );

        addInput(
            formPanel,
            constraints,
            "Password",
            passwordField,
            3
        );

        addInput(
            formPanel,
            constraints,
            "Confirm Password",
            confirmField,
            4
        );

        showPasswordBox.setOpaque(false);

        showPasswordBox.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.weightx = 1;
        constraints.insets =
            new Insets(3, 8, 8, 8);

        formPanel.add(
            showPasswordBox,
            constraints
        );

        JButton backButton =
            createButton("BACK");

        JButton clearButton =
            createButton("CLEAR");

        backButton.setBackground(
            new Color(90, 105, 120)
        );

        clearButton.setBackground(
            new Color(50, 130, 175)
        );

        JPanel buttonPanel = new JPanel(
            new FlowLayout(
                FlowLayout.CENTER,
                14,
                0
            )
        );

        buttonPanel.setOpaque(false);
        buttonPanel.setPreferredSize(
            new Dimension(680, 55)
        );

        buttonPanel.add(backButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(registerButton);

        card.add(
            headingPanel,
            BorderLayout.NORTH
        );

        card.add(
            formPanel,
            BorderLayout.CENTER
        );

        card.add(
            buttonPanel,
            BorderLayout.SOUTH
        );

        background.add(card);
        setContentPane(background);

        showPasswordBox.addActionListener(
            event -> showOrHidePasswords()
        );

        backButton.addActionListener(
            event -> returnToLogin()
        );

        clearButton.addActionListener(
            event -> clearFields()
        );

        registerButton.addActionListener(
            event -> registerAccount()
        );

        getRootPane().setDefaultButton(
            registerButton
        );
    }

    private void addInput(
        JPanel panel,
        GridBagConstraints constraints,
        String labelText,
        JComponent input,
        int row
    ) {
        JLabel label =
            new JLabel(labelText);

        label.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        label.setForeground(
            new Color(25, 35, 50)
        );

        input.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        input.setPreferredSize(
            new Dimension(440, 40)
        );

        constraints.gridy = row;

        constraints.gridx = 0;
        constraints.weightx = 0;
        constraints.insets =
            new Insets(8, 8, 8, 18);

        panel.add(label, constraints);

        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.insets =
            new Insets(8, 8, 8, 8);

        panel.add(input, constraints);
    }

    private JButton createButton(String text) {
        JButton button =
            new JButton(text);

        button.setPreferredSize(
            new Dimension(195, 44)
        );

        button.setMinimumSize(
            new Dimension(195, 44)
        );

        button.setBackground(
            new Color(35, 110, 200)
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

    private void showOrHidePasswords() {
        char echoCharacter =
            showPasswordBox.isSelected()
                ? (char) 0
                : '\u2022';

        passwordField.setEchoChar(
            echoCharacter
        );

        confirmField.setEchoChar(
            echoCharacter
        );
    }

    private void registerAccount() {
        String fullName =
            nameField.getText().trim();

        String email =
            emailField.getText().trim();

        String phone =
            phoneField.getText().trim();

        String password =
            new String(
                passwordField.getPassword()
            );

        String confirmPassword =
            new String(
                confirmField.getPassword()
            );

        if (
            fullName.isEmpty()
                || email.isEmpty()
                || phone.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()
        ) {
            showWarning(
                "Complete all registration fields.",
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

        if (
            !phone.matches(
                "[0-9+\\- ]{7,20}"
            )
        ) {
            showWarning(
                "Enter a valid phone number.",
                "Invalid Phone"
            );
            return;
        }

        if (password.length() < 6) {
            showWarning(
                "Password must contain at least 6 characters.",
                "Weak Password"
            );
            return;
        }

        if (!password.equals(confirmPassword)) {
            showWarning(
                "Passwords do not match.",
                "Password Mismatch"
            );
            return;
        }

        String duplicateSql = """
            SELECT user_id
            FROM users
            WHERE LOWER(email) = LOWER(?)
            """;

        String insertSql = """
            INSERT INTO users (
                full_name,
                email,
                phone,
                password_hash,
                role
            )
            VALUES (
                ?,
                ?,
                ?,
                SHA2(?, 256),
                'CUSTOMER'
            )
            """;

        registerButton.setEnabled(false);
        registerButton.setText("CREATING...");

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

                try (
                    ResultSet result =
                        duplicateStatement.executeQuery()
                ) {
                    if (result.next()) {
                        showWarning(
                            "An account already exists with this email.",
                            "Duplicate Email"
                        );
                        return;
                    }
                }
            }

            try (
                PreparedStatement insertStatement =
                    connection.prepareStatement(
                        insertSql
                    )
            ) {
                insertStatement.setString(
                    1,
                    fullName
                );

                insertStatement.setString(
                    2,
                    email
                );

                insertStatement.setString(
                    3,
                    phone
                );

                insertStatement.setString(
                    4,
                    password
                );

                int insertedRows =
                    insertStatement.executeUpdate();

                if (insertedRows != 1) {
                    throw new Exception(
                        "The account was not created."
                    );
                }
            }

            JOptionPane.showMessageDialog(
                this,
                "Account created successfully.\n"
                    + "You can now log in.",
                "Registration Successful",
                JOptionPane.INFORMATION_MESSAGE
            );

            returnToLogin();

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not create account:\n"
                    + exception.getMessage(),
                "Registration Error",
                JOptionPane.ERROR_MESSAGE
            );

            exception.printStackTrace();

        } finally {
            registerButton.setEnabled(true);
            registerButton.setText(
                "CREATE ACCOUNT"
            );
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

    private void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        passwordField.setText("");
        confirmField.setText("");

        showPasswordBox.setSelected(false);
        showOrHidePasswords();

        nameField.requestFocusInWindow();
    }

    private void returnToLogin() {
        dispose();

        if (previousFrame != null) {
            previousFrame.setVisible(true);
            previousFrame.toFront();
        } else {
            new LoginFrame().setVisible(true);
        }
    }
}