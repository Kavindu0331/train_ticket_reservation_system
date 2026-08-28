package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class AccountDashboardFrame extends JFrame {

    public AccountDashboardFrame() {
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

        setTitle("Account Dashboard");
        setSize(780, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        createInterface();
    }

    private void createInterface() {
        JPanel mainPanel =
            new JPanel(new BorderLayout());

        JPanel header =
            new JPanel(new BorderLayout());

        header.setBackground(
            new Color(15, 75, 140)
        );

        header.setBorder(
            new EmptyBorder(20, 28, 20, 28)
        );

        JLabel title =
            new JLabel(
                UserSession.isAdmin()
                    ? "Administrator Dashboard"
                    : "Customer Dashboard"
            );

        title.setFont(
            new Font("Arial", Font.BOLD, 24)
        );

        title.setForeground(Color.WHITE);

        JLabel welcomeLabel =
            new JLabel(
                "Welcome, "
                    + UserSession.getFullName()
            );

        welcomeLabel.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        welcomeLabel.setForeground(Color.WHITE);

        header.add(title, BorderLayout.WEST);
        header.add(welcomeLabel, BorderLayout.EAST);

        DashboardBackground background =
            new DashboardBackground();

        background.setLayout(
            new GridBagLayout()
        );

        RoundedContentPanel card =
            new RoundedContentPanel();

        card.setPreferredSize(
            new Dimension(470, 340)
        );

        card.setLayout(
            new GridBagLayout()
        );

        card.setBorder(
            new EmptyBorder(30, 45, 30, 45)
        );

        GridBagConstraints c =
            new GridBagConstraints();

        c.gridx = 0;
        c.fill = GridBagConstraints.NONE;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(6, 5, 6, 5);

        JLabel accountTitle =
            new JLabel(
                "Account Information",
                SwingConstants.CENTER
            );

        accountTitle.setFont(
            new Font("Arial", Font.BOLD, 27)
        );

        accountTitle.setForeground(
            new Color(15, 75, 140)
        );

        c.gridy = 0;
        c.insets = new Insets(5, 5, 14, 5);

        card.add(accountTitle, c);

        JLabel roleLabel =
            new JLabel(
                "Role: " + UserSession.getRole(),
                SwingConstants.CENTER
            );

        roleLabel.setFont(
            new Font("Arial", Font.BOLD, 16)
        );

        roleLabel.setForeground(
            new Color(55, 65, 80)
        );

        c.gridy = 1;
        c.insets = new Insets(3, 5, 5, 5);

        card.add(roleLabel, c);

        JLabel accessLabel =
            new JLabel(
                UserSession.isAdmin()
                    ? "You have administrator access."
                    : "You have customer access.",
                SwingConstants.CENTER
            );

        accessLabel.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        accessLabel.setForeground(
            new Color(85, 95, 110)
        );

        c.gridy = 2;
        c.insets = new Insets(3, 5, 18, 5);

        card.add(accessLabel, c);

        JButton profileButton =
            makeButton("MY PROFILE");

        JButton passwordButton =
            makeButton("CHANGE PASSWORD");

        JButton logoutButton =
            makeButton("LOGOUT");

        logoutButton.setBackground(
            new Color(190, 55, 55)
        );

        c.gridy = 3;
        c.insets = new Insets(5, 5, 5, 5);

        card.add(profileButton, c);

        c.gridy = 4;

        card.add(passwordButton, c);

        c.gridy = 5;

        card.add(logoutButton, c);

        background.add(card);

        mainPanel.add(
            header,
            BorderLayout.NORTH
        );

        mainPanel.add(
            background,
            BorderLayout.CENTER
        );

        setContentPane(mainPanel);

        profileButton.addActionListener(
            event -> openProfile()
        );

        passwordButton.addActionListener(
            event ->
                JOptionPane.showMessageDialog(
                    this,
                    "Change Password page will be added next.",
                    "Change Password",
                    JOptionPane.INFORMATION_MESSAGE
                )
        );

        logoutButton.addActionListener(
            event -> logout()
        );
    }

    private JButton makeButton(
        String text
    ) {
        JButton button =
            new JButton(text);

        button.setUI(new BasicButtonUI());

        button.setPreferredSize(
            new Dimension(210, 38)
        );

        button.setMinimumSize(
            new Dimension(210, 38)
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

    private void openProfile() {
        try {
            MyProfileFrame profileFrame =
                new MyProfileFrame(this);

            profileFrame.setVisible(true);
            setVisible(false);

        } catch (Throwable error) {
            error.printStackTrace();

            JOptionPane.showMessageDialog(
                this,
                "Could not open My Profile.\n"
                    + error.getClass().getSimpleName()
                    + ": "
                    + error.getMessage(),
                "Profile Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void logout() {
        int answer =
            JOptionPane.showConfirmDialog(
                this,
                "Do you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
            );

        if (answer == JOptionPane.YES_OPTION) {
            UserSession.clear();

            new LoginFrame().setVisible(true);
            dispose();
        }
    }

    private static class DashboardBackground
        extends JPanel {

        protected void paintComponent(
            Graphics graphics
        ) {
            super.paintComponent(graphics);

            Graphics2D g =
                (Graphics2D) graphics.create();

            g.setPaint(
                new GradientPaint(
                    0,
                    0,
                    new Color(225, 238, 250),
                    getWidth(),
                    getHeight(),
                    new Color(195, 225, 245)
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

    private static class RoundedContentPanel
        extends JPanel {

        RoundedContentPanel() {
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
                new Color(0, 0, 0, 30)
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
}