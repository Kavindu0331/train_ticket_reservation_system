package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AdminLoginFrame extends JFrame {

    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton cancelButton;

    public AdminLoginFrame() {
        setTitle("Train Reservation - Admin Login");
        setSize(750, 520);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        GradientPanel backgroundPanel = new GradientPanel();
        backgroundPanel.setLayout(new GridBagLayout());

        RoundedPanel loginCard = new RoundedPanel(22);
        loginCard.setLayout(new GridBagLayout());
        loginCard.setPreferredSize(new Dimension(440, 410));
        loginCard.setBackground(Color.WHITE);
        loginCard.setBorder(new EmptyBorder(18, 40, 18, 40));

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;

        JLabel trainLabel = new JLabel(
            "🚆",
            SwingConstants.CENTER
        );

        trainLabel.setFont(
            new Font("Segoe UI Emoji", Font.PLAIN, 42)
        );

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(0, 5, 0, 5);

        loginCard.add(trainLabel, constraints);

        JLabel titleLabel = new JLabel(
            "Admin Login",
            SwingConstants.CENTER
        );

        titleLabel.setFont(
            new Font("Arial", Font.BOLD, 29)
        );

        titleLabel.setForeground(
            new Color(17, 72, 130)
        );

        constraints.gridy = 1;
        constraints.insets = new Insets(-3, 5, 2, 5);

        loginCard.add(titleLabel, constraints);

        JLabel subtitleLabel = new JLabel(
            "Train Ticket Reservation System",
            SwingConstants.CENTER
        );

        subtitleLabel.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        subtitleLabel.setForeground(
            new Color(80, 90, 105)
        );

        constraints.gridy = 2;
        constraints.insets = new Insets(2, 5, 22, 5);

        loginCard.add(subtitleLabel, constraints);

        JLabel emailLabel = new JLabel("Email");

        emailLabel.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.insets = new Insets(8, 5, 8, 15);

        loginCard.add(emailLabel, constraints);

        emailField = new JTextField();

        emailField.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        emailField.setPreferredSize(
            new Dimension(250, 38)
        );

        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.insets = new Insets(8, 5, 8, 5);

        loginCard.add(emailField, constraints);

        JLabel passwordLabel = new JLabel("Password");

        passwordLabel.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.weightx = 0;
        constraints.insets = new Insets(8, 5, 8, 15);

        loginCard.add(passwordLabel, constraints);

        passwordField = new JPasswordField();

        passwordField.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        passwordField.setPreferredSize(
            new Dimension(250, 38)
        );

        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.insets = new Insets(8, 5, 8, 5);

        loginCard.add(passwordField, constraints);

        loginButton = createButton("LOGIN");
        cancelButton = createButton("CANCEL");

        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 15, 0)
        );

        buttonPanel.setOpaque(false);
        buttonPanel.add(loginButton);
        buttonPanel.add(cancelButton);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.insets = new Insets(20, 5, 5, 5);

        loginCard.add(buttonPanel, constraints);

        loginButton.addActionListener(event -> login());

        passwordField.addActionListener(
            event -> login()
        );

        cancelButton.addActionListener(
            event -> cancel()
        );

        backgroundPanel.add(loginCard);
        setContentPane(backgroundPanel);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);

        button.setPreferredSize(
            new Dimension(140, 42)
        );

        button.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        button.setBackground(
            new Color(21, 101, 192)
        );

        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setCursor(
            Cursor.getPredefinedCursor(
                Cursor.HAND_CURSOR
            )
        );

        return button;
    }

    private void login() {
        String email = emailField.getText().trim();

        String password =
            new String(passwordField.getPassword());

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter your email and password.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String sql = """
            SELECT user_id, full_name
            FROM users
            WHERE email = ?
              AND password_hash = SHA2(?, 256)
              AND role = 'ADMIN'
            """;

        loginButton.setEnabled(false);
        cancelButton.setEnabled(false);
        loginButton.setText("CHECKING...");

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    long adminId =
                        result.getLong("user_id");

                    String adminName =
                        result.getString("full_name");

                    dispose();

                    new AdminDashboard(
                        adminId,
                        adminName
                    ).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(
                        this,
                        "Incorrect administrator email or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                    );

                    passwordField.setText("");
                    passwordField.requestFocus();
                }
            }
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not connect to the database.\n"
                    + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        } finally {
            loginButton.setEnabled(true);
            cancelButton.setEnabled(true);
            loginButton.setText("LOGIN");
        }
    }

    private void cancel() {
        int answer = JOptionPane.showConfirmDialog(
            this,
            "Do you want to close the application?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (answer == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    private static class GradientPanel extends JPanel {

        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D graphics2D =
                (Graphics2D) graphics.create();

            graphics2D.setRenderingHint(
                RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY
            );

            GradientPaint gradient = new GradientPaint(
                0,
                0,
                new Color(3, 45, 105),
                getWidth(),
                getHeight(),
                new Color(0, 190, 240)
            );

            graphics2D.setPaint(gradient);

            graphics2D.fillRect(
                0,
                0,
                getWidth(),
                getHeight()
            );

            graphics2D.dispose();
        }
    }

    private static class RoundedPanel extends JPanel {

        private final int cornerRadius;

        public RoundedPanel(int cornerRadius) {
            this.cornerRadius = cornerRadius;
            setOpaque(false);
        }

        protected void paintComponent(Graphics graphics) {
            Graphics2D graphics2D =
                (Graphics2D) graphics.create();

            graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            graphics2D.setColor(
                new Color(0, 0, 0, 45)
            );

            graphics2D.fillRoundRect(
                6,
                7,
                getWidth() - 12,
                getHeight() - 12,
                cornerRadius,
                cornerRadius
            );

            graphics2D.setColor(getBackground());

            graphics2D.fillRoundRect(
                0,
                0,
                getWidth() - 12,
                getHeight() - 12,
                cornerRadius,
                cornerRadius
            );

            graphics2D.dispose();

            super.paintComponent(graphics);
        }
    }
}