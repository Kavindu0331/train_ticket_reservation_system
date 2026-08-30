package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ChangePasswordFrame extends JFrame {

    private final JFrame previousFrame;

    private final JPasswordField currentField =
        new JPasswordField();

    private final JPasswordField newField =
        new JPasswordField();

    private final JPasswordField confirmField =
        new JPasswordField();

    private final JCheckBox showPasswordBox =
        new JCheckBox("Show Passwords");

    private final JButton changeButton =
        makeButton("CHANGE PASSWORD");

    public ChangePasswordFrame(
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

        setTitle("Change Password");
        setSize(680, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        createInterface();

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
            new Color(225, 238, 250)
        );

        mainPanel.setBorder(
            new EmptyBorder(30, 40, 30, 40)
        );

        JLabel title =
            new JLabel(
                "Change Password",
                SwingConstants.CENTER
            );

        title.setFont(
            new Font("Arial", Font.BOLD, 29)
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
                    new Color(195, 210, 225)
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
            "Current Password",
            currentField,
            0
        );

        addInput(
            formPanel,
            c,
            "New Password",
            newField,
            1
        );

        addInput(
            formPanel,
            c,
            "Confirm Password",
            confirmField,
            2
        );

        showPasswordBox.setOpaque(false);
        showPasswordBox.setFocusPainted(false);

        showPasswordBox.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;

        formPanel.add(showPasswordBox, c);

        JButton backButton =
            makeButton("BACK");

        JPanel buttonPanel =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT,
                    10,
                    0
                )
            );

        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        buttonPanel.add(changeButton);

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 2;
        c.insets = new Insets(22, 10, 5, 10);

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

        changeButton.addActionListener(
            event -> changePassword()
        );

        backButton.addActionListener(
            event -> returnToPreviousPage()
        );

        confirmField.addActionListener(
            event -> changePassword()
        );
    }

    private void addInput(
        JPanel panel,
        GridBagConstraints c,
        String labelText,
        JPasswordField input,
        int row
    ) {
        JLabel label =
            new JLabel(labelText);

        label.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        input.setPreferredSize(
            new Dimension(330, 42)
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
            new Dimension(175, 40)
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

        currentField.setEchoChar(
            echoCharacter
        );

        newField.setEchoChar(
            echoCharacter
        );

        confirmField.setEchoChar(
            echoCharacter
        );
    }

    private void changePassword() {
        String currentPassword =
            new String(
                currentField.getPassword()
            );

        String newPassword =
            new String(
                newField.getPassword()
            );

        String confirmPassword =
            new String(
                confirmField.getPassword()
            );

        if (
            currentPassword.isEmpty()
                || newPassword.isEmpty()
                || confirmPassword.isEmpty()
        ) {
            showWarning(
                "Complete all password fields.",
                "Missing Information"
            );
            return;
        }

        if (newPassword.length() < 6) {
            showWarning(
                "The new password must contain at least 6 characters.",
                "Weak Password"
            );
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showWarning(
                "The new passwords do not match.",
                "Password Mismatch"
            );
            return;
        }

        if (currentPassword.equals(newPassword)) {
            showWarning(
                "The new password must be different from the current password.",
                "Same Password"
            );
            return;
        }

        String sql = """
            UPDATE users
            SET password_hash = SHA2(?, 256)
            WHERE user_id = ?
              AND password_hash = SHA2(?, 256)
            """;

        changeButton.setEnabled(false);
        changeButton.setText("CHANGING...");

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setString(
                1,
                newPassword
            );

            statement.setLong(
                2,
                UserSession.getUserId()
            );

            statement.setString(
                3,
                currentPassword
            );

            int updatedRows =
                statement.executeUpdate();

            if (updatedRows != 1) {
                JOptionPane.showMessageDialog(
                    this,
                    "The current password is incorrect.",
                    "Incorrect Password",
                    JOptionPane.ERROR_MESSAGE
                );

                currentField.setText("");
                currentField.requestFocus();
                return;
            }

            JOptionPane.showMessageDialog(
                this,
                "Password changed successfully.\n"
                    + "Please log in using your new password.",
                "Password Changed",
                JOptionPane.INFORMATION_MESSAGE
            );

            UserSession.clear();

            dispose();

            if (previousFrame != null) {
                previousFrame.dispose();
            }

            new LoginFrame().setVisible(true);

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not change password:\n"
                    + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );

        } finally {
            changeButton.setEnabled(true);
            changeButton.setText(
                "CHANGE PASSWORD"
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

    private void returnToPreviousPage() {
        dispose();

        if (previousFrame != null) {
            previousFrame.setVisible(true);
            previousFrame.toFront();
        }
    }
}