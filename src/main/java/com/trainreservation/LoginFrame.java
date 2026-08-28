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
            new Color(20, 95, 220),
            Color.WHITE
        );

    public LoginFrame() {
        setTitle("Train Reservation System");
        setSize(760, 650);
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
            new Dimension(460, 550)
        );

        card.setLayout(
            new GridBagLayout()
        );

        card.setBorder(
            new EmptyBorder(20, 40, 20, 40)
        );

        GridBagConstraints c =
            new GridBagConstraints();

        c.gridx = 0;
        c.gridwidth = 2;
        c.weightx = 1;

        TrainIcon trainIcon =
            new TrainIcon();

        trainIcon.setPreferredSize(
            new Dimension(120, 80)
        );

        trainIcon.setMinimumSize(
            new Dimension(120, 80)
        );

        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.weightx = 0;
        c.insets = new Insets(0, 5, 3, 5);

        card.add(trainIcon, c);

        JLabel title =
            new JLabel(
                "Train Reservation System",
                SwingConstants.CENTER
            );

        title.setFont(
            new Font("Arial", Font.BOLD, 25)
        );

        title.setForeground(
            new Color(10, 48, 105)
        );

        c.gridy = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(0, 5, 2, 5);

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
            new Color(90, 95, 105)
        );

        c.gridy = 2;
        c.insets = new Insets(3, 5, 15, 5);

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
        c.insets = new Insets(4, 5, 8, 5);

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
        c.insets = new Insets(4, 5, 3, 5);

        card.add(passwordField, c);

        showPasswordBox.setOpaque(false);

        showPasswordBox.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        showPasswordBox.setFocusPainted(false);

        c.gridy = 7;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(2, 5, 10, 5);

        card.add(showPasswordBox, c);

        loginButton.setPreferredSize(
            new Dimension(280, 36)
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
                new Color(20, 95, 220)
            );

        createAccountButton.setBorder(
            new LineBorder(
                new Color(20, 95, 220),
                2
            )
        );

        createAccountButton.setPreferredSize(
            new Dimension(280, 36)
        );

        c.gridy = 9;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(3, 5, 5, 5);

        card.add(createAccountButton, c);

        JButton forgotButton =
            new JButton("Forgot Password?");

        forgotButton.setUI(
            new BasicButtonUI()
        );

        forgotButton.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        forgotButton.setForeground(
            new Color(20, 95, 220)
        );

        forgotButton.setBackground(Color.WHITE);
        forgotButton.setBorderPainted(false);
        forgotButton.setFocusPainted(false);

        forgotButton.setCursor(
            Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
            )
        );

        c.gridy = 10;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(2, 5, 6, 5);

        card.add(forgotButton, c);

        JButton exitButton =
            makeButton(
                "EXIT",
                new Color(245, 247, 250),
                new Color(65, 70, 80)
            );

        exitButton.setPreferredSize(
            new Dimension(100, 32)
        );

        c.gridy = 11;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(3, 5, 0, 5);

        card.add(exitButton, c);

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
                    "The password recovery page will open here.",
                    "Forgot Password",
                    JOptionPane.INFORMATION_MESSAGE
                )
        );

        exitButton.addActionListener(
            event -> exitApplication()
        );
    }

    private void addLabel(
        JPanel card,
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

        card.add(label, c);
    }

    private void prepareField(
        JTextField field
    ) {
        field.setPreferredSize(
            new Dimension(350, 38)
        );

        field.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        field.setBorder(
            BorderFactory.createCompoundBorder(
                new LineBorder(
                    new Color(185, 195, 210)
                ),
                new EmptyBorder(6, 10, 6, 10)
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

        button.setUI(
            new BasicButtonUI()
        );

        button.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        button.setBackground(background);
        button.setForeground(foreground);
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
                "Please enter your email and password.",
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

                JFrame dashboard;

                if (
                    "ADMIN".equalsIgnoreCase(
                        role
                    )
                ) {
                    dashboard =
                        new AdminDashboard(
                            userId,
                            fullName
                        );
                } else {
                    dashboard =
                        new CustomerDashboard(
                            userId,
                            fullName
                        );
                }

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

    private void exitApplication() {
        int answer =
            JOptionPane.showConfirmDialog(
                this,
                "Do you want to close the application?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
            );

        if (answer == JOptionPane.YES_OPTION) {
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

            g.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
            );

            g.setPaint(
                new GradientPaint(
                    0,
                    0,
                    new Color(4, 36, 92),
                    getWidth(),
                    getHeight(),
                    new Color(0, 190, 240)
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
                new Color(20, 95, 220)
            );

            g.fillRoundRect(
                center - 26,
                5,
                52,
                46,
                12,
                12
            );

            g.setColor(
                new Color(150, 220, 250)
            );

            g.fillRoundRect(
                center - 18,
                12,
                36,
                17,
                6,
                6
            );

            g.setColor(Color.WHITE);

            g.fillOval(
                center - 17,
                38,
                8,
                8
            );

            g.fillOval(
                center + 9,
                38,
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
                center - 15,
                52,
                center - 27,
                68
            );

            g.drawLine(
                center + 15,
                52,
                center + 27,
                68
            );

            g.drawLine(
                center - 22,
                62,
                center + 22,
                62
            );

            g.drawLine(
                center - 29,
                70,
                center + 29,
                70
            );

            g.dispose();
        }
    }
}