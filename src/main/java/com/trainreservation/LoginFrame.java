package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginFrame extends JFrame {

    private static final Color DARK_BLUE =
        new Color(8, 63, 123);

    private static final Color LIGHT_BLUE =
        new Color(0, 174, 214);

    private static final Color PRIMARY_BLUE =
        new Color(28, 101, 215);

    private static final Color TEXT_COLOR =
        new Color(25, 30, 40);

    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox showPasswordBox;
    private JButton loginButton;

    public LoginFrame() {
        initializeWindow();
        initializeInterface();
    }

    private void initializeWindow() {
        setTitle("Train Reservation System");
        setSize(940, 800);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE
        );

        setResizable(false);
    }

    private void initializeInterface() {
        GradientPanel backgroundPanel =
            new GradientPanel();

        backgroundPanel.setLayout(
            new GridBagLayout()
        );

        LoginCard loginCard =
            new LoginCard();

        loginCard.setPreferredSize(
            new Dimension(570, 670)
        );

        loginCard.setLayout(
            new BorderLayout()
        );

        loginCard.setBorder(
            new EmptyBorder(
                32,
                55,
                30,
                55
            )
        );

        loginCard.add(
            createHeaderPanel(),
            BorderLayout.NORTH
        );

        loginCard.add(
            createFormPanel(),
            BorderLayout.CENTER
        );

        backgroundPanel.add(loginCard);

        setContentPane(backgroundPanel);

        SwingUtilities.invokeLater(
            () -> emailField
                .requestFocusInWindow()
        );
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel =
            new JPanel();

        headerPanel.setOpaque(false);

        headerPanel.setLayout(
            new BoxLayout(
                headerPanel,
                BoxLayout.Y_AXIS
            )
        );

        TrainIconPanel trainIcon =
            new TrainIconPanel();

        trainIcon.setPreferredSize(
            new Dimension(90, 90)
        );

        trainIcon.setMinimumSize(
            new Dimension(90, 90)
        );

        trainIcon.setMaximumSize(
            new Dimension(90, 90)
        );

        trainIcon.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel titleLabel =
            new JLabel(
                "Train Reservation System"
            );

        titleLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                29
            )
        );

        titleLabel.setForeground(DARK_BLUE);

        titleLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel subtitleLabel =
            new JLabel(
                "Sign in to continue"
            );

        subtitleLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                16
            )
        );

        subtitleLabel.setForeground(
            new Color(75, 80, 90)
        );

        subtitleLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        headerPanel.add(trainIcon);

        headerPanel.add(
            Box.createVerticalStrut(8)
        );

        headerPanel.add(titleLabel);

        headerPanel.add(
            Box.createVerticalStrut(7)
        );

        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel =
            new JPanel(
                new GridBagLayout()
            );

        formPanel.setOpaque(false);

        formPanel.setBorder(
            new EmptyBorder(
                27,
                0,
                0,
                0
            )
        );

        GridBagConstraints constraints =
            new GridBagConstraints();

        constraints.gridx = 0;
        constraints.weightx = 1.0;

        constraints.fill =
            GridBagConstraints.HORIZONTAL;

        JLabel emailLabel =
            createLabel("Email");

        emailField =
            createTextField();

        JLabel passwordLabel =
            createLabel("Password");

        passwordField =
            new JPasswordField();

        configureTextField(passwordField);

        passwordField.setEchoChar('\u2022');

        showPasswordBox =
            new JCheckBox("Show Password");

        configureCheckBox(
            showPasswordBox
        );

        loginButton =
            createPrimaryButton("LOGIN");

        JButton createAccountButton =
            createOutlineButton(
                "CREATE ACCOUNT"
            );

        JButton forgotPasswordButton =
            createLinkButton(
                "Forgot Password?"
            );

        JButton exitButton =
            createExitButton("EXIT");

        addFormComponent(
            formPanel,
            emailLabel,
            constraints,
            0,
            0,
            7
        );

        addFormComponent(
            formPanel,
            emailField,
            constraints,
            1,
            14,
            17
        );

        addFormComponent(
            formPanel,
            passwordLabel,
            constraints,
            2,
            0,
            7
        );

        addFormComponent(
            formPanel,
            passwordField,
            constraints,
            3,
            14,
            10
        );

        addFormComponent(
            formPanel,
            showPasswordBox,
            constraints,
            4,
            0,
            21
        );

        addFormComponent(
            formPanel,
            loginButton,
            constraints,
            5,
            12,
            11
        );

        addFormComponent(
            formPanel,
            createAccountButton,
            constraints,
            6,
            10,
            7
        );

        addFormComponent(
            formPanel,
            forgotPasswordButton,
            constraints,
            7,
            4,
            9
        );

        constraints.gridy = 8;
        constraints.ipady = 9;

        constraints.insets =
            new Insets(
                0,
                135,
                0,
                135
            );

        formPanel.add(
            exitButton,
            constraints
        );

        showPasswordBox.addActionListener(
            event -> togglePassword()
        );

        loginButton.addActionListener(
            event -> login()
        );

        passwordField.addActionListener(
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

        return formPanel;
    }

    private void addFormComponent(
        JPanel panel,
        Component component,
        GridBagConstraints constraints,
        int row,
        int verticalPadding,
        int bottomMargin
    ) {
        constraints.gridy = row;
        constraints.ipady = verticalPadding;

        constraints.insets =
            new Insets(
                0,
                0,
                bottomMargin,
                0
            );

        panel.add(component, constraints);
    }

    private JLabel createLabel(
        String text
    ) {
        JLabel label =
            new JLabel(text);

        label.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                15
            )
        );

        label.setForeground(TEXT_COLOR);

        return label;
    }

    private JTextField createTextField() {
        JTextField textField =
            new JTextField();

        configureTextField(textField);

        return textField;
    }

    private void configureTextField(
        JTextField textField
    ) {
        textField.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                16
            )
        );

        textField.setPreferredSize(
            new Dimension(460, 48)
        );

        textField.setMinimumSize(
            new Dimension(460, 48)
        );

        textField.setBackground(Color.WHITE);
        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(DARK_BLUE);

        textField.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(
                        175,
                        190,
                        210
                    ),
                    1
                ),
                new EmptyBorder(
                    8,
                    12,
                    8,
                    12
                )
            )
        );
    }

    private void configureCheckBox(
        JCheckBox checkBox
    ) {
        checkBox.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                14
            )
        );

        checkBox.setForeground(TEXT_COLOR);
        checkBox.setBackground(Color.WHITE);

        checkBox.setFocusPainted(false);

        checkBox.setCursor(
            Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
            )
        );
    }

    private JButton createButton(
        String text
    ) {
        JButton button =
            new JButton(text);

        button.setUI(
            new BasicButtonUI()
        );

        button.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                15
            )
        );

        button.setPreferredSize(
            new Dimension(460, 47)
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

    private JButton createPrimaryButton(
        String text
    ) {
        JButton button =
            createButton(text);

        button.setBackground(PRIMARY_BLUE);
        button.setForeground(Color.WHITE);

        return button;
    }

    private JButton createOutlineButton(
        String text
    ) {
        JButton button =
            createButton(text);

        button.setBackground(Color.WHITE);
        button.setForeground(PRIMARY_BLUE);

        button.setBorderPainted(true);

        button.setBorder(
            BorderFactory.createLineBorder(
                PRIMARY_BLUE,
                2
            )
        );

        return button;
    }

    private JButton createExitButton(
        String text
    ) {
        JButton button =
            createButton(text);

        button.setBackground(
            new Color(235, 240, 247)
        );

        button.setForeground(
            new Color(45, 50, 60)
        );

        return button;
    }

    private JButton createLinkButton(
        String text
    ) {
        JButton button =
            new JButton(text);

        button.setUI(
            new BasicButtonUI()
        );

        button.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                15
            )
        );

        button.setForeground(PRIMARY_BLUE);
        button.setBackground(Color.WHITE);

        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setCursor(
            Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
            )
        );

        return button;
    }

    private void togglePassword() {
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

        if (email.isEmpty()) {
            showWarning(
                "Please enter your email address.",
                "Email Required"
            );

            emailField.requestFocusInWindow();
            return;
        }

        if (!email.matches(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        )) {
            showWarning(
                "Please enter a valid email address.",
                "Invalid Email"
            );

            emailField.requestFocusInWindow();
            return;
        }

        if (password.isEmpty()) {
            showWarning(
                "Please enter your password.",
                "Password Required"
            );

            passwordField
                .requestFocusInWindow();

            return;
        }

        String sql = """
            SELECT
                user_id,
                full_name,
                email,
                role
            FROM users
            WHERE LOWER(email) = LOWER(?)
              AND password_hash = SHA2(?, 256)
            LIMIT 1
            """;

        loginButton.setEnabled(false);
        loginButton.setText("CHECKING...");

        try (
            Connection connection =
                DatabaseConnection
                    .getConnection();

            PreparedStatement statement =
                connection
                    .prepareStatement(sql)
        ) {
            statement.setString(1, email);
            statement.setString(
                2,
                password
            );

            try (
                ResultSet result =
                    statement.executeQuery()
            ) {
                if (!result.next()) {
                    showWarning(
                        "Incorrect email or password.",
                        "Login Failed"
                    );

                    passwordField.setText("");

                    passwordField
                        .requestFocusInWindow();

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

                JOptionPane.showMessageDialog(
                    this,
                    "Welcome, " + fullName + "!",
                    "Login Successful",
                    JOptionPane.INFORMATION_MESSAGE
                );

                openDashboard(
                    userId,
                    fullName,
                    role
                );
            }

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not complete login.\n"
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
            RegistrationFrame registrationFrame =
                new RegistrationFrame(this);

            registrationFrame.setVisible(true);
            setVisible(false);

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not open registration.\n"
                    + exception.getMessage(),
                "Registration Error",
                JOptionPane.ERROR_MESSAGE
            );

            exception.printStackTrace();
        }
    }

    private void openForgotPassword() {
        try {
            ForgotPasswordFrame forgotFrame =
                new ForgotPasswordFrame(this);

            forgotFrame.setVisible(true);
            setVisible(false);

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not open forgot password.\n"
                    + exception.getMessage(),
                "Forgot Password Error",
                JOptionPane.ERROR_MESSAGE
            );

            exception.printStackTrace();
        }
    }

    private void openDashboard(
        long userId,
        String fullName,
        String role
    ) {
        try {
            if (
                role != null
                    && role.equalsIgnoreCase(
                        "ADMIN"
                    )
            ) {
                AdminDashboard dashboard =
                    new AdminDashboard(
                        userId,
                        fullName
                    );

                dashboard.setVisible(true);

            } else {
                CustomerDashboard dashboard =
                    new CustomerDashboard(
                        userId,
                        fullName
                    );

                dashboard.setVisible(true);
            }

            dispose();

        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                this,
                "Login successful, but dashboard "
                    + "could not be opened.\n"
                    + exception.getMessage(),
                "Dashboard Error",
                JOptionPane.ERROR_MESSAGE
            );

            exception.printStackTrace();
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

    private void exitApplication() {
        int answer =
            JOptionPane.showConfirmDialog(
                this,
                "Do you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
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

        @Override
        protected void paintComponent(
            Graphics graphics
        ) {
            super.paintComponent(graphics);

            Graphics2D graphics2D =
                (Graphics2D)
                    graphics.create();

            graphics2D.setPaint(
                new GradientPaint(
                    0,
                    0,
                    DARK_BLUE,
                    getWidth(),
                    getHeight(),
                    LIGHT_BLUE
                )
            );

            graphics2D.fillRect(
                0,
                0,
                getWidth(),
                getHeight()
            );

            graphics2D.dispose();
        }
    }

    private static class LoginCard
        extends JPanel {

        LoginCard() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(
            Graphics graphics
        ) {
            Graphics2D graphics2D =
                (Graphics2D)
                    graphics.create();

            graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints
                    .VALUE_ANTIALIAS_ON
            );

            graphics2D.setColor(
                new Color(0, 50, 85, 90)
            );

            graphics2D.fillRoundRect(
                9,
                10,
                getWidth() - 10,
                getHeight() - 11,
                28,
                28
            );

            graphics2D.setColor(Color.WHITE);

            graphics2D.fillRoundRect(
                0,
                0,
                getWidth() - 10,
                getHeight() - 11,
                28,
                28
            );

            graphics2D.dispose();

            super.paintComponent(graphics);
        }
    }

    private static class TrainIconPanel
        extends JPanel {

        TrainIconPanel() {
            setOpaque(false);
        }

        @Override
        protected void paintComponent(
            Graphics graphics
        ) {
            super.paintComponent(graphics);

            Graphics2D graphics2D =
                (Graphics2D)
                    graphics.create();

            graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints
                    .VALUE_ANTIALIAS_ON
            );

            int center =
                getWidth() / 2;

            graphics2D.setColor(PRIMARY_BLUE);

            graphics2D.fillRoundRect(
                center - 28,
                4,
                56,
                58,
                9,
                9
            );

            graphics2D.setColor(
                new Color(120, 210, 245)
            );

            graphics2D.fillRoundRect(
                center - 19,
                13,
                38,
                23,
                4,
                4
            );

            graphics2D.setColor(Color.WHITE);

            graphics2D.fillOval(
                center - 19,
                45,
                11,
                11
            );

            graphics2D.fillOval(
                center + 8,
                45,
                11,
                11
            );

            graphics2D.setColor(DARK_BLUE);

            graphics2D.setStroke(
                new BasicStroke(5)
            );

            graphics2D.drawLine(
                center - 16,
                62,
                center - 28,
                80
            );

            graphics2D.drawLine(
                center + 16,
                62,
                center + 28,
                80
            );

            graphics2D.drawLine(
                center - 25,
                73,
                center + 25,
                73
            );

            graphics2D.drawLine(
                center - 31,
                83,
                center + 31,
                83
            );

            graphics2D.dispose();
        }
    }

    public static void main(
        String[] arguments
    ) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                    UIManager
                        .getSystemLookAndFeelClassName()
                );
            } catch (Exception ignored) {
            }

            new LoginFrame().setVisible(true);
        });
    }
}