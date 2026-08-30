package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFrame extends JFrame {

    private final JTextField emailField =
        new JTextField();

    private final JPasswordField passwordField =
        new JPasswordField();

    private final JCheckBox showPasswordBox =
        new JCheckBox("Show Password");

    private final JButton loginButton =
        createButton(
            "LOGIN",
            new Color(35, 110, 200)
        );

    public LoginFrame() {
        setTitle("Train Reservation System");
        setSize(930, 760);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        createInterface();
    }

    private void createInterface() {
        GradientPanel background =
            new GradientPanel();

        background.setLayout(
            new GridBagLayout()
        );

        background.setBorder(
            new EmptyBorder(30, 30, 30, 30)
        );

        RoundedPanel card =
            new RoundedPanel();

        card.setLayout(
            new BorderLayout(10, 15)
        );

        card.setPreferredSize(
            new Dimension(600, 630)
        );

        card.setBorder(
            new EmptyBorder(35, 65, 35, 65)
        );

        card.add(
            createHeadingPanel(),
            BorderLayout.NORTH
        );

        card.add(
            createLoginForm(),
            BorderLayout.CENTER
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

        TrainIcon trainIcon =
            new TrainIcon();

        trainIcon.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        trainIcon.setPreferredSize(
            new Dimension(85, 85)
        );

        trainIcon.setMaximumSize(
            new Dimension(85, 85)
        );

        JLabel title =
            new JLabel(
                "Train Reservation System"
            );

        title.setFont(
            new Font("Arial", Font.BOLD, 30)
        );

        title.setForeground(
            new Color(15, 75, 140)
        );

        title.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel subtitle =
            new JLabel("Sign in to continue");

        subtitle.setFont(
            new Font("Arial", Font.PLAIN, 15)
        );

        subtitle.setForeground(
            new Color(75, 85, 100)
        );

        subtitle.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        headingPanel.add(trainIcon);
        headingPanel.add(
            Box.createVerticalStrut(8)
        );
        headingPanel.add(title);
        headingPanel.add(
            Box.createVerticalStrut(8)
        );
        headingPanel.add(subtitle);

        return headingPanel;
    }

    private JPanel createLoginForm() {
        JPanel formPanel =
            new JPanel(new GridBagLayout());

        formPanel.setOpaque(false);

        GridBagConstraints constraints =
            new GridBagConstraints();

        constraints.gridx = 0;
        constraints.weightx = 1;
        constraints.fill =
            GridBagConstraints.HORIZONTAL;

        JLabel emailLabel =
            createLabel("Email");

        constraints.gridy = 0;
        constraints.insets =
            new Insets(15, 0, 6, 0);

        formPanel.add(
            emailLabel,
            constraints
        );

        prepareField(emailField);

        constraints.gridy = 1;
        constraints.insets =
            new Insets(0, 0, 13, 0);

        formPanel.add(
            emailField,
            constraints
        );

        JLabel passwordLabel =
            createLabel("Password");

        constraints.gridy = 2;
        constraints.insets =
            new Insets(0, 0, 6, 0);

        formPanel.add(
            passwordLabel,
            constraints
        );

        prepareField(passwordField);

        constraints.gridy = 3;
        constraints.insets =
            new Insets(0, 0, 8, 0);

        formPanel.add(
            passwordField,
            constraints
        );

        showPasswordBox.setOpaque(false);

        showPasswordBox.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        showPasswordBox.setFocusPainted(false);

        constraints.gridy = 4;
        constraints.insets =
            new Insets(0, 0, 20, 0);

        formPanel.add(
            showPasswordBox,
            constraints
        );

        JButton createAccountButton =
            createOutlineButton(
                "CREATE ACCOUNT"
            );

        JButton forgotPasswordButton =
            createLinkButton(
                "Forgot Password?"
            );

        JButton exitButton =
            createButton(
                "EXIT",
                new Color(95, 110, 125)
            );

        constraints.gridy = 5;
        constraints.insets =
            new Insets(0, 65, 10, 65);

        formPanel.add(
            loginButton,
            constraints
        );

        constraints.gridy = 6;

        formPanel.add(
            createAccountButton,
            constraints
        );

        constraints.gridy = 7;
        constraints.insets =
            new Insets(0, 65, 8, 65);

        formPanel.add(
            forgotPasswordButton,
            constraints
        );

        constraints.gridy = 8;
        constraints.insets =
            new Insets(0, 65, 0, 65);

        formPanel.add(
            exitButton,
            constraints
        );

        showPasswordBox.addActionListener(
            event -> showOrHidePassword()
        );

        loginButton.addActionListener(
            event -> login()
        );

        createAccountButton.addActionListener(
            event -> openRegistration()
        );

        forgotPasswordButton.addActionListener(
            event -> openForgotPassword()
        );

        exitButton.addActionListener(
            event -> exitApplication()
        );

        getRootPane().setDefaultButton(
            loginButton
        );

        return formPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);

        label.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        label.setForeground(
            new Color(25, 35, 50)
        );

        return label;
    }

    private void prepareField(
        JComponent field
    ) {
        field.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        field.setPreferredSize(
            new Dimension(440, 42)
        );

        field.setMinimumSize(
            new Dimension(440, 42)
        );
    }

    private JButton createButton(
        String text,
        Color backgroundColor
    ) {
        JButton button =
            new JButton(text);

        button.setPreferredSize(
            new Dimension(300, 44)
        );

        button.setMinimumSize(
            new Dimension(300, 44)
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

    private JButton createOutlineButton(
        String text
    ) {
        JButton button =
            new JButton(text);

        button.setPreferredSize(
            new Dimension(300, 44)
        );

        button.setMinimumSize(
            new Dimension(300, 44)
        );

        button.setBackground(Color.WHITE);

        button.setForeground(
            new Color(35, 110, 200)
        );

        button.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        button.setFocusPainted(false);

        button.setBorder(
            BorderFactory.createLineBorder(
                new Color(35, 110, 200),
                2
            )
        );

        button.setCursor(
            Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
            )
        );

        return button;
    }

    private JButton createLinkButton(
        String text
    ) {
        JButton button =
            new JButton(text);

        button.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        button.setForeground(
            new Color(25, 95, 190)
        );

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        button.setCursor(
            Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
            )
        );

        return button;
    }

    private void showOrHidePassword() {
        if (showPasswordBox.isSelected()) {
            passwordField.setEchoChar(
                (char) 0
            );
        } else {
            passwordField.setEchoChar(
                '\u2022'
            );
        }
    }

    private void login() {
        String email =
            emailField.getText().trim();

        String password =
            new String(
                passwordField.getPassword()
            );

        if (
            email.isEmpty()
                || password.isEmpty()
        ) {
            JOptionPane.showMessageDialog(
                this,
                "Enter your email and password.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String sql = """
            SELECT
                user_id,
                full_name,
                role
            FROM users
            WHERE LOWER(email) = LOWER(?)
              AND password_hash = SHA2(?, 256)
            """;

        loginButton.setEnabled(false);
        loginButton.setText(
            "SIGNING IN..."
        );

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (
                ResultSet result =
                    statement.executeQuery()
            ) {
                if (!result.next()) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Invalid email or password.",
                        "Login Failed",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                long userId =
                    result.getLong("user_id");

                String fullName =
                    result.getString(
                        "full_name"
                    );

                String role =
                    result.getString("role");

                UserSession.start(
                    userId,
                    fullName,
                    role
                );

                AccountDashboardFrame dashboard =
                    new AccountDashboardFrame();

                dashboard.setVisible(true);
                dispose();
            }

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not complete login:\n"
                    + exception.getMessage(),
                "Login Error",
                JOptionPane.ERROR_MESSAGE
            );

            exception.printStackTrace();

        } finally {
            loginButton.setEnabled(true);
            loginButton.setText("LOGIN");
        }
    }

    private void openRegistration() {
        try {
            RegistrationFrame frame =
                new RegistrationFrame(this);

            frame.setVisible(true);
            setVisible(false);

        } catch (Throwable error) {
            JOptionPane.showMessageDialog(
                this,
                "Could not open registration:\n"
                    + error.getMessage(),
                "Registration Error",
                JOptionPane.ERROR_MESSAGE
            );

            error.printStackTrace();
        }
    }

    private void openForgotPassword() {
        try {
            ForgotPasswordFrame frame =
                new ForgotPasswordFrame(this);

            frame.setVisible(true);
            setVisible(false);

        } catch (Throwable error) {
            JOptionPane.showMessageDialog(
                this,
                "Could not open forgot password page:\n"
                    + error.getMessage(),
                "Forgot Password Error",
                JOptionPane.ERROR_MESSAGE
            );

            error.printStackTrace();
        }
    }

    private void exitApplication() {
        int answer =
            JOptionPane.showConfirmDialog(
                this,
                "Do you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
            );

        if (
            answer
                == JOptionPane.YES_OPTION
        ) {
            System.exit(0);
        }
    }

    private static class GradientPanel
        extends JPanel {

        protected void paintComponent(
            Graphics graphics
        ) {
            super.paintComponent(graphics);

            Graphics2D g =
                (Graphics2D) graphics.create();

            GradientPaint gradient =
                new GradientPaint(
                    0,
                    0,
                    new Color(10, 55, 115),
                    getWidth(),
                    getHeight(),
                    new Color(0, 180, 220)
                );

            g.setPaint(gradient);

            g.fillRect(
                0,
                0,
                getWidth(),
                getHeight()
            );

            g.dispose();
        }
    }

    private static class RoundedPanel
        extends JPanel {

        public RoundedPanel() {
            setOpaque(false);
        }

        protected void paintComponent(
            Graphics graphics
        ) {
            Graphics2D g =
                (Graphics2D) graphics.create();

            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            g.setColor(
                new Color(0, 0, 0, 38)
            );

            g.fillRoundRect(
                10,
                12,
                getWidth() - 10,
                getHeight() - 12,
                28,
                28
            );

            g.setColor(Color.WHITE);

            g.fillRoundRect(
                0,
                0,
                getWidth() - 10,
                getHeight() - 12,
                28,
                28
            );

            g.dispose();

            super.paintComponent(graphics);
        }

        protected void paintBorder(
            Graphics graphics
        ) {
        }
    }

    private static class TrainIcon
        extends JPanel {

        public TrainIcon() {
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

            g.setColor(
                new Color(35, 110, 200)
            );

            g.fillRoundRect(
                center - 27,
                8,
                54,
                52,
                10,
                10
            );

            g.setColor(
                new Color(140, 220, 245)
            );

            g.fillRoundRect(
                center - 19,
                16,
                38,
                21,
                5,
                5
            );

            g.setColor(Color.WHITE);

            g.fillOval(
                center - 19,
                44,
                10,
                10
            );

            g.fillOval(
                center + 9,
                44,
                10,
                10
            );

            g.setColor(
                new Color(10, 45, 90)
            );

            g.setStroke(
                new BasicStroke(4)
            );

            g.drawLine(
                center - 20,
                60,
                center - 31,
                78
            );

            g.drawLine(
                center + 20,
                60,
                center + 31,
                78
            );

            g.drawLine(
                center - 31,
                78,
                center + 31,
                78
            );

            g.drawLine(
                center - 25,
                69,
                center + 25,
                69
            );

            g.dispose();
        }
    }
}