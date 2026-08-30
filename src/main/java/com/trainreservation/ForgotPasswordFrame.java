package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class ForgotPasswordFrame extends JFrame {

    private final JFrame previousFrame;

    private final JTextField emailField =
        new JTextField();

    private final JTextField phoneField =
        new JTextField();

    private final JPasswordField newPasswordField =
        new JPasswordField();

    private final JPasswordField confirmPasswordField =
        new JPasswordField();

    private final JCheckBox showPasswordBox =
        new JCheckBox("Show Password");

    private final JButton resetButton =
        createButton(
            "RESET PASSWORD",
            new Color(35, 110, 200)
        );

    public ForgotPasswordFrame(
        JFrame previousFrame
    ) {
        this.previousFrame = previousFrame;

        setTitle("Forgot Password");
        setSize(700, 780);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(
            DO_NOTHING_ON_CLOSE
        );
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
        JPanel background =
            new JPanel(new GridBagLayout());

        background.setBackground(
            new Color(235, 243, 251)
        );

        background.setBorder(
            new EmptyBorder(25, 30, 25, 30)
        );

        JPanel card =
            new JPanel(
                new BorderLayout(10, 15)
            );

        card.setBackground(Color.WHITE);

        card.setPreferredSize(
            new Dimension(560, 670)
        );

        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(205, 218, 232)
                ),
                new EmptyBorder(
                    18,
                    55,
                    25,
                    55
                )
            )
        );

        card.add(
            createHeadingPanel(),
            BorderLayout.NORTH
        );

        card.add(
            createFormPanel(),
            BorderLayout.CENTER
        );

        card.add(
            createButtonPanel(),
            BorderLayout.SOUTH
        );

        background.add(card);
        setContentPane(background);
    }

    private JPanel createHeadingPanel() {
        JPanel headingPanel =
            new JPanel();

        headingPanel.setOpaque(false);

        headingPanel.setLayout(
            new BoxLayout(
                headingPanel,
                BoxLayout.Y_AXIS
            )
        );

        LockIcon lockIcon =
            new LockIcon();

        lockIcon.setPreferredSize(
            new Dimension(80, 75)
        );

        lockIcon.setMaximumSize(
            new Dimension(80, 75)
        );

        lockIcon.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel title =
            new JLabel("Reset Password");

        title.setFont(
            new Font("Arial", Font.BOLD, 29)
        );

        title.setForeground(
            new Color(15, 75, 140)
        );

        title.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel subtitle = new JLabel(
            "Verify your account and enter a new password"
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

        headingPanel.add(lockIcon);

        headingPanel.add(
            Box.createVerticalStrut(3)
        );

        headingPanel.add(title);

        headingPanel.add(
            Box.createVerticalStrut(7)
        );

        headingPanel.add(subtitle);

        return headingPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel =
            new JPanel(new GridBagLayout());

        formPanel.setOpaque(false);

        GridBagConstraints constraints =
            new GridBagConstraints();

        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.fill =
            GridBagConstraints.HORIZONTAL;

        addInput(
            formPanel,
            constraints,
            "Email",
            emailField,
            0
        );

        addInput(
            formPanel,
            constraints,
            "Registered Phone Number",
            phoneField,
            2
        );

        addInput(
            formPanel,
            constraints,
            "New Password",
            newPasswordField,
            4
        );

        addInput(
            formPanel,
            constraints,
            "Confirm New Password",
            confirmPasswordField,
            6
        );

        showPasswordBox.setOpaque(false);

        showPasswordBox.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        showPasswordBox.setFocusPainted(false);

        constraints.gridy = 8;
        constraints.insets =
            new Insets(2, 0, 0, 0);

        formPanel.add(
            showPasswordBox,
            constraints
        );

        showPasswordBox.addActionListener(
            event -> showOrHidePasswords()
        );

        return formPanel;
    }

    private void addInput(
        JPanel panel,
        GridBagConstraints constraints,
        String labelText,
        JComponent field,
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

        constraints.gridy = row;

        constraints.insets =
            new Insets(
                row == 0 ? 12 : 7,
                0,
                5,
                0
            );

        panel.add(label, constraints);

        field.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        field.setPreferredSize(
            new Dimension(430, 39)
        );

        field.setMinimumSize(
            new Dimension(430, 39)
        );

        constraints.gridy = row + 1;

        constraints.insets =
            new Insets(0, 0, 5, 0);

        panel.add(field, constraints);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel =
            new JPanel();

        buttonPanel.setOpaque(false);

        buttonPanel.setLayout(
            new BoxLayout(
                buttonPanel,
                BoxLayout.Y_AXIS
            )
        );

        JButton backButton =
            createButton(
                "BACK TO LOGIN",
                new Color(95, 110, 125)
            );

        resetButton.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        backButton.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        buttonPanel.add(resetButton);

        buttonPanel.add(
            Box.createVerticalStrut(10)
        );

        buttonPanel.add(backButton);

        resetButton.addActionListener(
            event -> resetPassword()
        );

        backButton.addActionListener(
            event -> returnToLogin()
        );

        getRootPane().setDefaultButton(
            resetButton
        );

        return buttonPanel;
    }

    private JButton createButton(
        String text,
        Color backgroundColor
    ) {
        JButton button =
            new JButton(text);

        button.setPreferredSize(
            new Dimension(275, 43)
        );

        button.setMinimumSize(
            new Dimension(275, 43)
        );

        button.setMaximumSize(
            new Dimension(275, 43)
        );

        button.setBackground(
            backgroundColor
        );

        button.setForeground(Color.WHITE);

        button.setFont(
            new Font("Arial", Font.BOLD, 13)
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
        char echoCharacter;

        if (showPasswordBox.isSelected()) {
            echoCharacter = (char) 0;
        } else {
            echoCharacter = '\u2022';
        }

        newPasswordField.setEchoChar(
            echoCharacter
        );

        confirmPasswordField.setEchoChar(
            echoCharacter
        );
    }

    private void resetPassword() {
        String email =
            emailField.getText().trim();

        String phone =
            phoneField.getText().trim();

        String newPassword =
            new String(
                newPasswordField.getPassword()
            );

        String confirmPassword =
            new String(
                confirmPasswordField.getPassword()
            );

        if (
            email.isEmpty()
                || phone.isEmpty()
                || newPassword.isEmpty()
                || confirmPassword.isEmpty()
        ) {
            showWarning(
                "Complete all fields."
            );
            return;
        }

        if (
            !email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
            )
        ) {
            showWarning(
                "Enter a valid email address."
            );
            return;
        }

        if (
            !phone.matches(
                "[0-9+\\- ]{7,20}"
            )
        ) {
            showWarning(
                "Enter a valid phone number."
            );
            return;
        }

        if (newPassword.length() < 6) {
            showWarning(
                "The new password must contain at least 6 characters."
            );
            return;
        }

        if (
            !newPassword.equals(
                confirmPassword
            )
        ) {
            showWarning(
                "The new passwords do not match."
            );
            return;
        }

        String sql = """
            UPDATE users
            SET password_hash = SHA2(?, 256)
            WHERE LOWER(TRIM(email)) =
                  LOWER(TRIM(?))
              AND REPLACE(
                    REPLACE(
                        TRIM(phone),
                        ' ',
                        ''
                    ),
                    '-',
                    ''
                  ) =
                  REPLACE(
                    REPLACE(
                        TRIM(?),
                        ' ',
                        ''
                    ),
                    '-',
                    ''
                  )
            """;

        resetButton.setEnabled(false);

        resetButton.setText(
            "RESETTING..."
        );

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

            statement.setString(
                2,
                email
            );

            statement.setString(
                3,
                phone
            );

            int updatedRows =
                statement.executeUpdate();

            if (updatedRows == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "The email and phone number do not match.\n"
                        + "Use the same details entered during registration.",
                    "Account Not Found",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            JOptionPane.showMessageDialog(
                this,
                "Password reset successfully.\n"
                    + "You can now log in with your new password.",
                "Password Reset",
                JOptionPane.INFORMATION_MESSAGE
            );

            returnToLogin();

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not reset the password:\n"
                    + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );

            exception.printStackTrace();

        } finally {
            resetButton.setEnabled(true);

            resetButton.setText(
                "RESET PASSWORD"
            );
        }
    }

    private void showWarning(
        String message
    ) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Invalid Information",
            JOptionPane.WARNING_MESSAGE
        );
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

    private static class LockIcon
        extends JPanel {

        public LockIcon() {
            setOpaque(false);
        }

        protected void paintComponent(
            Graphics graphics
        ) {
            super.paintComponent(graphics);

            Graphics2D g =
                (Graphics2D) graphics.create();

            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            int center =
                getWidth() / 2;

            g.setStroke(
                new BasicStroke(5)
            );

            g.setColor(
                new Color(15, 75, 140)
            );

            g.drawArc(
                center - 18,
                8,
                36,
                38,
                0,
                180
            );

            g.setColor(
                new Color(35, 110, 200)
            );

            g.fillRoundRect(
                center - 27,
                28,
                54,
                40,
                9,
                9
            );

            g.setColor(Color.WHITE);

            g.fillOval(
                center - 5,
                39,
                10,
                10
            );

            g.fillRect(
                center - 2,
                46,
                4,
                11
            );

            g.dispose();
        }
    }
}