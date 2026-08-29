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
        new Color(7, 61, 121);

    private static final Color LIGHT_BLUE =
        new Color(0, 180, 215);

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
        setSize(920, 740);
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
            new Dimension(550, 620)
        );

        loginCard.setLayout(
            new BorderLayout()
        );

        loginCard.setBorder(
            new EmptyBorder(
                28,
                50,
                27,
                50
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
            new Dimension(82, 82)
        );

        trainIcon.setMinimumSize(
            new Dimension(82, 82)
        );

        trainIcon.setMaximumSize(
            new Dimension(82, 82)
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
                28
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
                15
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
            Box.createVerticalStrut(7)
        );

        headerPanel.add(titleLabel);

        headerPanel.add(
            Box.createVerticalStrut(6)
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
                20,
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

        emailField.setToolTipText(
            "Enter your registered email"
        );

        JLabel passwordLabel =
            createLabel("Password");

        passwordField =
            new JPasswordField();

        configureTextField(passwordField);

        passwordField.setEchoChar('\u2022');

        passwordField.setToolTipText(
            "Enter your password"
        );

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
            6
        );

        addFormComponent(
            formPanel,
            emailField,
            constraints,
            1,
            0,
            14
        );

        addFormComponent(
            formPanel,
            passwordLabel,
            constraints,
            2,
            0,
            6
        );

        addFormComponent(
            formPanel,
            passwordField,
            constraints,
            3,
            0,
            8
        );

        addFormComponent(
            formPanel,
            showPasswordBox,
            constraints,
            4,
            0,
            17
        );

        addFormComponent(
            formPanel,
            loginButton,
            constraints,
            5,
            9,
            10
        );

        addFormComponent(
            formPanel,
            createAccountButton,
            constraints,
            6,
            8,
            6
        );

        addFormComponent(
            formPanel,
            forgotPasswordButton,
            constraints,
            7,
            2,
            7
        );

        constraints.gridy = 8;
        constraints.ipady = 7;

        constraints.insets =
            new Insets(
                0,
                125,
                0,
                125
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

        if (component instanceof JTextField) {
            constraints.ipady = 0;
        } else {
            constraints.ipady =
                verticalPadding;
        }

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
                15
            )
        );

        textField.setPreferredSize(
            new Dimension(440, 42)
        );

        textField.setMinimumSize(
            new Dimension(440, 42)
        );

        textField.setBackground(
            new Color(250, 252, 255)
        );

        textField.setForeground(TEXT_COLOR);
        textField.setCaretColor(DARK_BLUE);

        textField.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(
                        175,
                        193,
                        215
                    ),
                    1
                ),
                new EmptyBorder(
                    6,
                    11,
                    6,
                    11
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
            new Dimension(440, 43)
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

        } catch (Throwable error) {
            JOptionPane.showMessageDialog(
                this,
                "Could not complete login.\n"
                    + error.getClass()
                        .getSimpleName()
                    + ": "
                    + error.getMessage(),
                "Login Error",
                JOptionPane.ERROR_MESSAGE
            );

            error.printStackTrace();

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

        } catch (Throwable error) {
            JOptionPane.showMessageDialog(
                this,
                "Could not open registration.\n"
                    + error.getMessage(),
                "Registration Error",
                JOptionPane.ERROR_MESSAGE
            );

            error.printStackTrace();
        }
    }

    private void openForgotPassword() {
        try {
            ForgotPasswordFrame forgotFrame =
                new ForgotPasswordFrame(this);

            forgotFrame.setVisible(true);
            setVisible(false);

        } catch (Throwable error) {
            JOptionPane.showMessageDialog(
                this,
                "Could not open forgot password.\n"
                    + error.getMessage(),
                "Forgot Password Error",
                JOptionPane.ERROR_MESSAGE
            );

            error.printStackTrace();
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
                AdminDashboard adminDashboard =
                    new AdminDashboard(
                        userId,
                        fullName
                    );

                adminDashboard.setVisible(true);

            } else {
                CustomerDashboard customerDashboard =
                    new CustomerDashboard(
                        userId,
                        fullName
                    );

                customerDashboard.setVisible(true);
            }

            dispose();

        } catch (Throwable error) {
            JOptionPane.showMessageDialog(
                this,
                "Login successful, but dashboard "
                    + "could not be opened.\n"
                    + error.getMessage(),
                "Dashboard Error",
                JOptionPane.ERROR_MESSAGE
            );

            error.printStackTrace();
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

            Graphics2D g =
                (Graphics2D)
                    graphics.create();

            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints
                    .VALUE_ANTIALIAS_ON
            );

            GradientPaint gradient =
                new GradientPaint(
                    0,
                    0,
                    new Color(5, 54, 112),
                    getWidth(),
                    getHeight(),
                    new Color(0, 183, 215)
                );

            g.setPaint(gradient);

            g.fillRect(
                0,
                0,
                getWidth(),
                getHeight()
            );

            /*
             * Soft decorative circles
             */
            g.setColor(
                new Color(
                    255,
                    255,
                    255,
                    22
                )
            );

            g.fillOval(
                -110,
                -100,
                340,
                340
            );

            g.fillOval(
                getWidth() - 230,
                50,
                310,
                310
            );

            g.fillOval(
                getWidth() - 330,
                getHeight() - 245,
                390,
                390
            );

            g.setColor(
                new Color(
                    255,
                    255,
                    255,
                    13
                )
            );

            g.fillOval(
                30,
                getHeight() - 220,
                285,
                285
            );

            /*
             * Railway track design
             */
            g.setColor(
                new Color(
                    255,
                    255,
                    255,
                    30
                )
            );

            g.setStroke(
                new BasicStroke(4)
            );

            int trackY =
                getHeight() - 52;

            g.drawLine(
                0,
                trackY,
                getWidth(),
                trackY
            );

            g.drawLine(
                0,
                trackY + 18,
                getWidth(),
                trackY + 18
            );

            g.setStroke(
                new BasicStroke(3)
            );

            for (
                int x = -25;
                x < getWidth() + 30;
                x += 48
            ) {
                g.drawLine(
                    x,
                    trackY - 5,
                    x + 22,
                    trackY + 23
                );
            }

            /*
             * Small decorative dots
             */
            g.setColor(
                new Color(
                    255,
                    255,
                    255,
                    40
                )
            );

            g.fillOval(65, 115, 9, 9);
            g.fillOval(112, 182, 6, 6);

            g.fillOval(
                getWidth() - 100,
                145,
                8,
                8
            );

            g.fillOval(
                getWidth() - 150,
                225,
                6,
                6
            );

            g.dispose();
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
            Graphics2D g =
                (Graphics2D)
                    graphics.create();

            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints
                    .VALUE_ANTIALIAS_ON
            );

            /*
             * Card shadow
             */
            g.setColor(
                new Color(
                    0,
                    45,
                    80,
                    85
                )
            );

            g.fillRoundRect(
                9,
                10,
                getWidth() - 10,
                getHeight() - 11,
                28,
                28
            );

            /*
             * White card
             */
            g.setColor(Color.WHITE);

            g.fillRoundRect(
                0,
                0,
                getWidth() - 10,
                getHeight() - 11,
                28,
                28
            );

            g.dispose();

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

            Graphics2D g =
                (Graphics2D)
                    graphics.create();

            g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints
                    .VALUE_ANTIALIAS_ON
            );

            int center =
                getWidth() / 2;

            g.setColor(PRIMARY_BLUE);

            g.fillRoundRect(
                center - 26,
                2,
                52,
                54,
                9,
                9
            );

            g.setColor(
                new Color(
                    120,
                    210,
                    245
                )
            );

            g.fillRoundRect(
                center - 18,
                11,
                36,
                21,
                4,
                4
            );

            g.setColor(Color.WHITE);

            g.fillOval(
                center - 18,
                40,
                10,
                10
            );

            g.fillOval(
                center + 8,
                40,
                10,
                10
            );

            g.setColor(DARK_BLUE);

            g.setStroke(
                new BasicStroke(5)
            );

            g.drawLine(
                center - 15,
                56,
                center - 27,
                74
            );

            g.drawLine(
                center + 15,
                56,
                center + 27,
                74
            );

            g.drawLine(
                center - 24,
                67,
                center + 24,
                67
            );

            g.drawLine(
                center - 30,
                77,
                center + 30,
                77
            );

            g.dispose();
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

            new LoginFrame()
                .setVisible(true);
        });
    }
}
