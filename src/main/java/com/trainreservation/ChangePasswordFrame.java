package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ChangePasswordFrame extends JFrame {

    private final JFrame previousFrame;
    private final long customerId;

    private final JPasswordField currentPasswordField =
        new JPasswordField();

    private final JPasswordField newPasswordField =
        new JPasswordField();

    private final JPasswordField confirmPasswordField =
        new JPasswordField();

    private final JCheckBox showPasswordBox =
        new JCheckBox("Show Passwords");

    private final JButton changeButton =
        makeButton("CHANGE PASSWORD");

    public ChangePasswordFrame(
        JFrame previousFrame,
        long customerId
    ) {
        this.previousFrame = previousFrame;
        this.customerId = customerId;

        setTitle("Change Password");
        setSize(700, 570);
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
            new Color(238, 244, 250)
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
            "Current Password",
            currentPasswordField,
            0
        );

        addInput(
            formPanel,
            c,
            "New Password",
            newPasswordField,
            1
        );

        addInput(
            formPanel,
            c,
            "Confirm Password",
            confirmPasswordField,
            2
        );

        showPasswordBox.setOpaque(false);

        showPasswordBox.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        showPasswordBox.setFocusPainted(false);

        c.gridx = 1;
        c.gridy = 3;
        c.gridwidth = 1;
        c.weightx = 1;

        formPanel.add(showPasswordBox, c);

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
        buttonPanel.add(changeButton);

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

        showPasswordBox.addActionListener(
            event -> showOrHidePasswords()
        );

        changeButton.addActionListener(
            event -> changePassword()
        );

        backButton.addActionListener(
            event -> returnToPreviousPage()
        );

        confirmPasswordField.addActionListener(
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

    private static JButton makeButton(String text) {
        JButton button =
            new JButton(text);

        button.setUI(new BasicButtonUI());

        button.setPreferredSize(
            new Dimension(185, 44)
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

        currentPasswordField.setEchoChar(
            echoCharacter
        );

        newPasswordField.setEchoChar(
            echoCharacter
        );

        confirmPasswordField.setEchoChar(
            echoCharacter
        );
    }

    private void changePassword() {
        String currentPassword =
            new String(
                currentPasswordField.getPassword()
            );

        String newPassword =
            new String(
                newPasswordField.getPassword()
            );

        String confirmPassword =
            new String(
                confirmPasswordField.getPassword()
            );

        if (
            currentPassword.isEmpty()
                || newPassword.isEmpty()
                || confirmPassword.isEmpty()
        ) {
            JOptionPane.showMessageDialog(
                this,
                "Complete all password fields.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (newPassword.length() < 6) {
            JOptionPane.showMessageDialog(
                this,
                "The new password must contain at least 6 characters.",
                "Weak Password",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(
                this,
                "The new passwords do not match.",
                "Password Mismatch",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (currentPassword.equals(newPassword)) {
            JOptionPane.showMessageDialog(
                this,
                "The new password must be different from the current password.",
                "Same Password",
                JOptionPane.WARNING_MESSAGE
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
                customerId
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

                currentPasswordField.setText("");
                currentPasswordField.requestFocus();
                return;
            }

            JOptionPane.showMessageDialog(
                this,
                "Password changed successfully.\n"
                    + "Please log in using your new password.",
                "Password Changed",
                JOptionPane.INFORMATION_MESSAGE
            );

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

    private void returnToPreviousPage() {
        dispose();

        if (previousFrame != null) {
            previousFrame.setVisible(true);
            previousFrame.toFront();
        }
    }
}