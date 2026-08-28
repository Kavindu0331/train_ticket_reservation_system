package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
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
        makeButton("CREATE ACCOUNT");

    public RegistrationFrame(
        JFrame previousFrame
    ) {
        this.previousFrame = previousFrame;

        setTitle("Customer Registration");
        setSize(720, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        createInterface();

        addWindowListener(
            new java.awt.event.WindowAdapter() {
                public void windowClosing(
                    java.awt.event.WindowEvent event
                ) {
                    returnToLogin();
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
            new EmptyBorder(25, 40, 25, 40)
        );

        JLabel title =
            new JLabel(
                "Create Account",
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
                new EmptyBorder(25, 35, 25, 35)
            )
        );

        GridBagConstraints c =
            new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(8, 10, 8, 10);

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

        addInput(
            formPanel,
            c,
            "Password",
            passwordField,
            3
        );

        addInput(
            formPanel,
            c,
            "Confirm Password",
            confirmField,
            4
        );

        showPasswordBox.setOpaque(false);
        showPasswordBox.setFocusPainted(false);

        showPasswordBox.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 1;

        formPanel.add(showPasswordBox, c);

        JButton clearButton =
            makeButton("CLEAR");

        JButton backButton =
            makeButton("BACK");

        JPanel buttonPanel =
            new JPanel(
                new FlowLayout(
                    FlowLayout.CENTER,
                    10,
                    0
                )
            );

        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(registerButton);

        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        c.insets = new Insets(20, 10, 5, 10);

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

        showPasswordBox.addActionListener(
            event -> showOrHidePasswords()
        );

        registerButton.addActionListener(
            event -> registerCustomer()
        );

        clearButton.addActionListener(
            event -> clearFields()
        );

        backButton.addActionListener(
            event -> returnToLogin()
        );

        confirmField.addActionListener(
            event -> registerCustomer()
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
            new Dimension(165, 42)
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

    private void showOrHidePasswords() {
        char echoCharacter =
            showPasswordBox.isSelected()
                ? '\0'
                : '•';

        passwordField.setEchoChar(
            echoCharacter
        );

        confirmField.setEchoChar(
            echoCharacter
        );
    }

    private void registerCustomer() {
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
                "Complete all registration details.",
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