package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicButtonUI;
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
        makeButton(
            "LOGIN",
            new Color(25, 105, 195),
            Color.WHITE
        );

    public LoginFrame() {
        setTitle("Train Reservation System");
        setSize(760, 620);
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

        RoundedPanel card =
            new RoundedPanel();

        card.setPreferredSize(
            new Dimension(450, 500)
        );

        card.setLayout(
            new GridBagLayout()
        );

        card.setBorder(
            new EmptyBorder(25, 45, 25, 45)
        );

        GridBagConstraints c =
            new GridBagConstraints();

        c.gridx = 0;
        c.gridwidth = 2;
        c.weightx = 1;

        TrainIcon trainIcon =
            new TrainIcon();

        trainIcon.setPreferredSize(
            new Dimension(100, 75)
        );

        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 5, 3, 5);

        card.add(trainIcon, c);

        JLabel title =
            new JLabel(
                "Train Reservation System",
                SwingConstants.CENTER
            );

        title.setFont(
            new Font("Arial", Font.BOLD, 26)
        );

        title.setForeground(
            new Color(15, 75, 140)
        );

        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 5, 3, 5);

        card.add(title, c);

        JLabel subtitle =
            new JLabel(
                "Sign in to continue",
                SwingConstants.CENTER
            );

        subtitle.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        subtitle.setForeground(
            new Color(85, 95, 110)
        );

        c.gridy = 2;
        c.insets = new Insets(3, 5, 16, 5);

        card.add(subtitle, c);

        addLabel(
            card,
            c,
            "Email",
            3
        );

        prepareField(emailField);

        c.gridy = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(4, 5, 9, 5);

        card.add(emailField, c);

        addLabel(
            card,
            c,
            "Password",
            5
        );

        prepareField(passwordField);

        c.gridy = 6;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(4, 5, 4, 5);

        card.add(passwordField, c);

        showPasswordBox.setOpaque(false);
        showPasswordBox.setFocusPainted(false);

        showPasswordBox.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        c.gridy = 7;
        c.insets = new Insets(2, 5, 10, 5);

        card.add(showPasswordBox, c);

        loginButton.setPreferredSize(
            new Dimension(220, 36)
        );

        c.gridy = 8;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(3, 5, 7, 5);

        card.add(loginButton, c);

        JButton createAccountButton =
            makeButton(
                "CREATE ACCOUNT",
                Color.WHITE,
                new Color(25, 105, 195)
            );

        createAccountButton.setBorder(
            new LineBorder(
                new Color(25, 105, 195),
                2
            )
        );

        createAccountButton.setPreferredSize(
            new Dimension(220, 36)
        );

        c.gridy = 9;
        c.insets = new Insets(3, 5, 5, 5);

        card.add(createAccountButton, c);

        JButton forgotButton =
            new JButton("Forgot Password?");

        forgotButton.setUI(
            new BasicButtonUI()
        );

        forgotButton.setBackground(Color.WHITE);

        forgotButton.setForeground(
            new Color(25, 105, 195)
        );

        forgotButton.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        forgotButton.setBorderPainted(false);
        forgotButton.setFocusPainted(false);

        forgotButton.setCursor(
            Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
            )
        );

        c.gridy = 10;
        c.insets = new Insets(2, 5, 0, 5);

        card.add(forgotButton, c);

        background.add(card);
        setContentPane(background);

        showPasswordBox.addActionListener(
            event ->
                passwordField.setEchoChar(
                    showPasswordBox.isSelected()
                        ? '\0'
                        : '•'
                )
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

        forgotButton.addActionListener(
            event ->
                JOptionPane.showMessageDialog(
                    this,
                    "Forgot Password page will be added later.",
                    "Forgot Password",
                    JOptionPane.INFORMATION_MESSAGE
                )
        );
    }

    private void addLabel(
        JPanel panel,
        GridBagConstraints c,
        String text,
        int row
    ) {
        JLabel label =
            new JLabel(text);

        label.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        label.setForeground(
            new Color(30, 40, 55)
        );

        c.gridy = row;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(4, 5, 1, 5);

        panel.add(label, c);
    }

    private void prepareField(
        JTextField field
    ) {
        field.setPreferredSize(
            new Dimension(350, 39)
        );

        field.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        field.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(
                    new Color(185, 195, 210)
                ),
                new EmptyBorder(7, 10, 7, 10)
            )
        );
    }

    private static JButton makeButton(
        String text,
        Color background,
        Color foreground
    ) {
        JButton button =
            new JButton(text);

        button.setUI(new BasicButtonUI());
        button.setBackground(background);
        button.setForeground(foreground);

        button.setFont(
            new Font("Arial", Font.BOLD, 12)
        );

        button.setFocusPainted(false);
        button.setOpaque(true);

        button.setCursor(
            Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
            )
        );

        return button;
    }

    private void openRegistration() {
        try {
            RegistrationFrame registrationFrame =
                new RegistrationFrame(this);

            registrationFrame.setVisible(true);
            setVisible(false);

        } catch (Throwable error) {
            error.printStackTrace();

            JOptionPane.showMessageDialog(
                this,
                "Could not open registration.\n"
                    + error.getClass().getSimpleName()
                    + ": "
                    + error.getMessage(),
                "Registration Error",
                JOptionPane.ERROR_MESSAGE
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
        loginButton.setText("CHECKING...");

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
                        "Incorrect email or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                    );

                    passwordField.setText("");
                    passwordField.requestFocus();
                    return;
                }

                long userId =
                    result.getLong("user_id");

                String fullName =
                    result.getString("full_name");

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

        } catch (Throwable error) {
            error.printStackTrace();

            JOptionPane.showMessageDialog(
                this,
                "Could not complete login.\n"
                    + error.getClass().getSimpleName()
                    + ": "
                    + error.getMessage(),
                "Login Error",
                JOptionPane.ERROR_MESSAGE
            );

        } finally {
            loginButton.setEnabled(true);
            loginButton.setText("LOGIN");
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

            g.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
            );

            g.setPaint(
                new GradientPaint(
                    0,
                    0,
                    new Color(8, 55, 115),
                    getWidth(),
                    getHeight(),
                    new Color(25, 175, 220)
                )
            );

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

        RoundedPanel() {
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
                new Color(0, 0, 0, 45)
            );

            g.fillRoundRect(
                7,
                8,
                getWidth() - 14,
                getHeight() - 14,
                25,
                25
            );

            g.setColor(Color.WHITE);

            g.fillRoundRect(
                0,
                0,
                getWidth() - 14,
                getHeight() - 14,
                25,
                25
            );

            g.dispose();
            super.paintComponent(graphics);
        }
    }

    private static class TrainIcon
        extends JPanel {

        TrainIcon() {
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
                new Color(25, 105, 195)
            );

            g.fillRoundRect(
                center - 24,
                5,
                48,
                43,
                12,
                12
            );

            g.setColor(
                new Color(160, 225, 250)
            );

            g.fillRoundRect(
                center - 17,
                12,
                34,
                16,
                6,
                6
            );

            g.setColor(Color.WHITE);

            g.fillOval(
                center - 16,
                36,
                8,
                8
            );

            g.fillOval(
                center + 8,
                36,
                8,
                8
            );

            g.setColor(
                new Color(10, 48, 105)
            );

            g.setStroke(
                new BasicStroke(4)
            );

            g.drawLine(
                center - 14,
                50,
                center - 25,
                65
            );

            g.drawLine(
                center + 14,
                50,
                center + 25,
                65
            );

            g.drawLine(
                center - 21,
                59,
                center + 21,
                59
            );

            g.drawLine(
                center - 27,
                67,
                center + 27,
                67
            );

            g.dispose();
        }
    }
}