package com.trainreservation;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {

    private static final Color NAVY = new Color(7, 45, 105);
    private static final Color BLUE = new Color(18, 96, 225);
    private static final Color CYAN = new Color(49, 202, 255);
    private static final Color TEXT = new Color(14, 39, 80);
    private static final Color MUTED = new Color(77, 96, 126);

    private final JTextField emailField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();
    private final JCheckBox showPasswordBox = new JCheckBox("Show Password");
    private final RoundedButton loginButton =
        new RoundedButton("LOGIN  →", ButtonStyle.PRIMARY);

    public LoginFrame() {
        configureFrame();
        createInterface();
    }

    private void configureFrame() {
        setTitle("Train Reservation System");
        setSize(1180, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void createInterface() {
        BackgroundPanel background = new BackgroundPanel();
        background.setLayout(new GridBagLayout());
        background.setBorder(new EmptyBorder(30, 30, 30, 30));

        GlassCard card = new GlassCard();
        card.setPreferredSize(new Dimension(620, 750));
        card.setLayout(new BorderLayout());
        card.setBorder(new EmptyBorder(30, 70, 32, 70));
        card.add(createForm(), BorderLayout.CENTER);

        background.add(card);
        setContentPane(background);
        getRootPane().setDefaultButton(loginButton);
    }

    private JPanel createForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1.0;
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;

        TrainIconPanel trainIcon = new TrainIconPanel();
        trainIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
        trainIcon.setPreferredSize(new Dimension(110, 110));
        trainIcon.setMaximumSize(new Dimension(110, 110));

        JLabel title = createLabel(
            "Welcome Aboard",
            new Font("Arial", Font.BOLD, 35),
            TEXT
        );

        JLabel subtitle = createLabel(
            "Sign in to continue your journey",
            new Font("Arial", Font.PLAIN, 17),
            MUTED
        );

        passwordField.setEchoChar('\u2022');

        JLabel emailLabel = fieldLabel("Email");
        JPanel emailInput = createInputPanel(IconType.EMAIL, emailField);

        JLabel passwordLabel = fieldLabel("Password");
        JPanel passwordInput = createInputPanel(IconType.LOCK, passwordField);

        showPasswordBox.setOpaque(false);
        showPasswordBox.setFont(new Font("Arial", Font.PLAIN, 14));
        showPasswordBox.setForeground(TEXT);
        showPasswordBox.setFocusPainted(false);
        JPanel showPasswordRow = new JPanel(new BorderLayout());
        showPasswordRow.setOpaque(false);
        showPasswordRow.setPreferredSize(new Dimension(480, 28));
        showPasswordRow.setMinimumSize(new Dimension(480, 28));
        showPasswordRow.setMaximumSize(new Dimension(480, 28));
        showPasswordRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        showPasswordRow.add(showPasswordBox, BorderLayout.WEST);

        RoundedButton createAccountButton =
            new RoundedButton("CREATE ACCOUNT", ButtonStyle.OUTLINE);

        JButton forgotButton = new JButton("Forgot Password?");
        forgotButton.setFont(new Font("Arial", Font.BOLD, 15));
        forgotButton.setForeground(BLUE);
        forgotButton.setOpaque(false);
        forgotButton.setContentAreaFilled(false);
        forgotButton.setBorderPainted(false);
        forgotButton.setFocusPainted(false);
        forgotButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        RoundedButton exitButton =
            new RoundedButton("←  EXIT", ButtonStyle.DARK);

        c.gridy = 0;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 0, 8, 0);
        form.add(trainIcon, c);

        c.gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 0, 0);
        form.add(title, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 17, 0);
        form.add(subtitle, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 5, 0);
        form.add(emailLabel, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 14, 0);
        form.add(emailInput, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 5, 0);
        form.add(passwordLabel, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 7, 0);
        form.add(passwordInput, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 12, 0);
        form.add(showPasswordRow, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 10, 0);
        form.add(loginButton, c);

        c.gridy++;
        c.insets = new Insets(0, 0, 5, 0);
        form.add(createAccountButton, c);

        c.gridy++;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 0, 5, 0);
        form.add(forgotButton, c);

        c.gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 0, 0);
        form.add(exitButton, c);

        showPasswordBox.addActionListener(event -> showOrHidePassword());
        loginButton.addActionListener(event -> login());
        createAccountButton.addActionListener(event -> openRegistration());
        forgotButton.addActionListener(event -> openForgotPassword());
        exitButton.addActionListener(event -> exitApplication());
        emailField.addActionListener(event -> passwordField.requestFocusInWindow());
        passwordField.addActionListener(event -> login());

        SwingUtilities.invokeLater(() -> emailField.requestFocusInWindow());
        return form;
    }

    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(font);
        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        int labelHeight = font.getSize() >= 25 ? 48 : 28;
        label.setPreferredSize(new Dimension(480, labelHeight));
        label.setMinimumSize(new Dimension(480, labelHeight));
        label.setMaximumSize(new Dimension(480, labelHeight));
        return label;
    }

    private JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 15));
        label.setForeground(TEXT);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JPanel createInputPanel(IconType type, JTextField field) {
        RoundedInputPanel panel = new RoundedInputPanel();
        panel.setLayout(new BorderLayout(10, 0));
        panel.setPreferredSize(new Dimension(480, 55));
        panel.setMinimumSize(new Dimension(480, 55));
        panel.setMaximumSize(new Dimension(480, 55));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(new EmptyBorder(5, 17, 5, 14));

        FieldIcon icon = new FieldIcon(type);
        icon.setPreferredSize(new Dimension(27, 27));

        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setForeground(TEXT);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder());
        field.setCaretColor(BLUE);

        panel.add(icon, BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private void showOrHidePassword() {
        passwordField.setEchoChar(showPasswordBox.isSelected() ? (char) 0 : '\u2022');
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (email.isEmpty()) {
            showWarning("Please enter your email address.", "Email Required");
            emailField.requestFocusInWindow();
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showWarning("Please enter a valid email address.", "Invalid Email");
            emailField.requestFocusInWindow();
            return;
        }

        if (password.isEmpty()) {
            showWarning("Please enter your password.", "Password Required");
            passwordField.requestFocusInWindow();
            return;
        }

        String sql = """
            SELECT user_id, full_name, role
            FROM users
            WHERE LOWER(email) = LOWER(?)
              AND password_hash = SHA2(?, 256)
            LIMIT 1
            """;

        loginButton.setEnabled(false);
        loginButton.setText("SIGNING IN...");

        try (
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, email);
            statement.setString(2, password);

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    showWarning("Incorrect email or password.", "Login Failed");
                    passwordField.setText("");
                    passwordField.requestFocusInWindow();
                    return;
                }

                long userId = result.getLong("user_id");
                String fullName = result.getString("full_name");
                String role = result.getString("role");

                UserSession.start(userId, fullName, role);

                AccountDashboardFrame dashboard =
                    new AccountDashboardFrame(userId, fullName, role);

                dashboard.setVisible(true);
                dispose();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Could not complete login.\n" + exception.getMessage(),
                "Login Error",
                JOptionPane.ERROR_MESSAGE
            );
        } finally {
            loginButton.setEnabled(true);
            loginButton.setText("LOGIN  →");
        }
    }

    private void openRegistration() {
        try {
            RegistrationFrame frame = new RegistrationFrame(this);
            frame.setVisible(true);
            setVisible(false);
        } catch (Throwable error) {
            showOpenError("registration", error);
        }
    }

    private void openForgotPassword() {
        try {
            ForgotPasswordFrame frame = new ForgotPasswordFrame(this);
            frame.setVisible(true);
            setVisible(false);
        } catch (Throwable error) {
            showOpenError("password recovery", error);
        }
    }

    private void exitApplication() {
        int answer = JOptionPane.showConfirmDialog(
            this,
            "Do you want to exit the application?",
            "Confirm Exit",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (answer == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void showWarning(String message, String title) {
        JOptionPane.showMessageDialog(
            this,
            message,
            title,
            JOptionPane.WARNING_MESSAGE
        );
    }

    private void showOpenError(String name, Throwable error) {
        error.printStackTrace();
        JOptionPane.showMessageDialog(
            this,
            "Could not open " + name + ".\n" + error.getMessage(),
            "Navigation Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private enum ButtonStyle { PRIMARY, OUTLINE, DARK }
    private enum IconType { EMAIL, LOCK }

    private static class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        BackgroundPanel() {
            URL imageUrl = LoginFrame.class.getResource("/images/login-background.png");
            if (imageUrl != null) {
                backgroundImage = new ImageIcon(imageUrl).getImage();
                return;
            }

            File developmentImage = new File(
                "src/main/resources/images/login-background.png"
            );

            if (developmentImage.isFile()) {
                backgroundImage = new ImageIcon(
                    developmentImage.getAbsolutePath()
                ).getImage();
            }
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setPaint(new GradientPaint(
                    0, 0, new Color(4, 35, 85),
                    getWidth(), getHeight(), new Color(0, 154, 205)
                ));
                g.fillRect(0, 0, getWidth(), getHeight());
            }

            g.setColor(new Color(0, 15, 45, 50));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.dispose();
        }
    }

    private static class GlassCard extends JPanel {
        GlassCard() { setOpaque(false); }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(new Color(0, 8, 30, 95));
            g.fillRoundRect(10, 12, getWidth() - 12, getHeight() - 14, 42, 42);

            g.setColor(new Color(249, 252, 255, 238));
            g.fillRoundRect(0, 0, getWidth() - 12, getHeight() - 14, 42, 42);

            g.setStroke(new BasicStroke(2f));
            g.setColor(new Color(255, 255, 255, 210));
            g.drawRoundRect(1, 1, getWidth() - 14, getHeight() - 16, 42, 42);
            g.dispose();
            super.paintComponent(graphics);
        }
    }

    private static class RoundedInputPanel extends JPanel {
        RoundedInputPanel() { setOpaque(false); }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(255, 255, 255, 235));
            g.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);
            g.setColor(new Color(150, 174, 207));
            g.setStroke(new BasicStroke(1.4f));
            g.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);
            g.dispose();
            super.paintComponent(graphics);
        }
    }

    private static class RoundedButton extends JButton {
        private final ButtonStyle style;
        private boolean hover;

        RoundedButton(String text, ButtonStyle style) {
            super(text);
            this.style = style;
            setPreferredSize(new Dimension(480, 56));
            setMinimumSize(new Dimension(480, 56));
            setMaximumSize(new Dimension(480, 56));
            setAlignmentX(Component.CENTER_ALIGNMENT);
            setFont(new Font("Arial", Font.BOLD, 15));
            setForeground(style == ButtonStyle.OUTLINE ? BLUE : Color.WHITE);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

            if (style == ButtonStyle.PRIMARY) {
                g.setColor(new Color(0, 54, 150, hover ? 120 : 80));
                g.fillRoundRect(2, 7, getWidth() - 4, getHeight() - 5, 20, 20);
                g.setPaint(new GradientPaint(
                    0, 0, hover ? new Color(35, 141, 255) : new Color(30, 119, 245),
                    0, getHeight(), hover ? new Color(1, 64, 186) : new Color(3, 70, 190)
                ));
                g.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 8, 18, 18);
                g.setColor(CYAN);
                g.setStroke(new BasicStroke(1.5f));
                g.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 8, 18, 18);
            } else if (style == ButtonStyle.OUTLINE) {
                g.setColor(hover ? new Color(225, 241, 255) : new Color(255, 255, 255, 125));
                g.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);
                g.setColor(BLUE);
                g.setStroke(new BasicStroke(1.7f));
                g.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);
            } else {
                g.setColor(hover ? new Color(55, 82, 119) : new Color(39, 60, 88));
                g.fillRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);
                g.setColor(new Color(185, 214, 240));
                g.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 18, 18);
            }

            g.dispose();
            super.paintComponent(graphics);
        }
    }

    private static class FieldIcon extends JPanel {
        private final IconType type;
        FieldIcon(IconType type) { this.type = type; setOpaque(false); }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(79, 112, 157));
            g.setStroke(new BasicStroke(2f));

            if (type == IconType.EMAIL) {
                g.drawRoundRect(2, 6, 21, 16, 3, 3);
                g.drawLine(3, 8, 12, 15);
                g.drawLine(22, 8, 12, 15);
            } else {
                g.drawRoundRect(4, 10, 17, 14, 3, 3);
                g.drawArc(7, 2, 11, 14, 0, 180);
                g.fillOval(11, 15, 4, 4);
            }
            g.dispose();
        }
    }

    private static class TrainIconPanel extends JPanel {
        TrainIconPanel() { setOpaque(false); }

        @Override
        protected void paintComponent(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(new Color(22, 103, 218));
            g.fillOval(5, 5, 100, 100);
            g.setColor(new Color(237, 248, 255));
            g.fillOval(12, 12, 86, 86);
            g.setColor(new Color(22, 88, 185));
            g.fillRoundRect(35, 25, 40, 47, 9, 9);
            g.setColor(new Color(195, 235, 255));
            g.fillRoundRect(41, 32, 28, 17, 4, 4);
            g.setColor(Color.WHITE);
            g.fillOval(41, 56, 8, 8);
            g.fillOval(61, 56, 8, 8);
            g.setStroke(new BasicStroke(4f));
            g.setColor(new Color(7, 45, 105));
            g.drawLine(41, 72, 31, 88);
            g.drawLine(69, 72, 79, 88);
            g.drawLine(31, 88, 79, 88);
            g.drawLine(36, 80, 74, 80);
            g.dispose();
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
