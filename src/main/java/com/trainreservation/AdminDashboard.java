package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;



import java.awt.*;

public class AdminDashboard extends JFrame {

    private final long adminId;
    private final String adminName;

    public AdminDashboard(long adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;

        setTitle("Admin Dashboard");
        setSize(1000, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(240, 245, 250));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(15, 67, 125));
        headerPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel(
            "Train Reservation System"
        );

        titleLabel.setFont(
            new Font("Arial", Font.BOLD, 25)
        );

        titleLabel.setForeground(Color.WHITE);

        JLabel welcomeLabel = new JLabel(
            "Welcome, " + adminName
        );

        welcomeLabel.setFont(
            new Font("Arial", Font.BOLD, 15)
        );

        welcomeLabel.setForeground(Color.WHITE);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(welcomeLabel, BorderLayout.EAST);

        JPanel contentPanel = new JPanel(
            new BorderLayout(25, 0)
        );

        contentPanel.setBackground(
            new Color(240, 245, 250)
        );

        contentPanel.setBorder(
            new EmptyBorder(30, 30, 30, 30)
        );

        JPanel menuPanel = new JPanel(
            new GridLayout(9, 1, 0, 8)
        );

        menuPanel.setBackground(Color.WHITE);

        menuPanel.setBorder(
            new EmptyBorder(18, 20, 18, 20)
        );

        menuPanel.setPreferredSize(
            new Dimension(230, 0)
        );
 JButton stationDetailsButton =
    makeButton("Station Details");

        JButton addTrainButton =
            makeButton("Add Train");

        JButton viewTrainsButton =
            makeButton("View Trains");

        JButton updateTrainButton =
            makeButton("Update Train");

        JButton addScheduleButton =
            makeButton("Add Schedule");

        JButton cancelScheduleButton =
            makeButton("Cancel Schedule");

        JButton bookingHistoryButton =
            makeButton("Booking History");

        JButton profileButton =
            makeButton("Admin Profile");

        JButton logoutButton =
            makeButton("Logout");

        menuPanel.add(addTrainButton);
        menuPanel.add(viewTrainsButton);
        menuPanel.add(updateTrainButton);
        menuPanel.add(stationDetailsButton);
        menuPanel.add(addScheduleButton);
        menuPanel.add(cancelScheduleButton);
        menuPanel.add(bookingHistoryButton);
        menuPanel.add(profileButton);
        menuPanel.add(logoutButton);

        JPanel dashboardPanel = new JPanel(
            new GridBagLayout()
        );

        dashboardPanel.setBackground(Color.WHITE);

        dashboardPanel.setBorder(
            BorderFactory.createLineBorder(
                new Color(220, 228, 238)
            )
        );

        JPanel dashboardText = new JPanel();
        dashboardText.setOpaque(false);

        dashboardText.setLayout(
            new BoxLayout(
                dashboardText,
                BoxLayout.Y_AXIS
            )
        );

        JLabel dashboardTitle =
            new JLabel("Admin Dashboard");

        dashboardTitle.setFont(
            new Font("Arial", Font.BOLD, 32)
        );

        dashboardTitle.setForeground(
            new Color(15, 67, 125)
        );

        dashboardTitle.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel dashboardMessage = new JLabel(
            "Select an option from the menu"
        );

        dashboardMessage.setFont(
            new Font("Arial", Font.PLAIN, 16)
        );

        dashboardMessage.setForeground(
            new Color(90, 100, 110)
        );

        dashboardMessage.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        dashboardText.add(dashboardTitle);
        dashboardText.add(
            Box.createVerticalStrut(12)
        );
        dashboardText.add(dashboardMessage);

        dashboardPanel.add(dashboardText);

        contentPanel.add(
            menuPanel,
            BorderLayout.WEST
        );

        contentPanel.add(
            dashboardPanel,
            BorderLayout.CENTER
        );

        mainPanel.add(
            headerPanel,
            BorderLayout.NORTH
        );

        mainPanel.add(
            contentPanel,
            BorderLayout.CENTER
        );

        setContentPane(mainPanel);

        addTrainButton.addActionListener(event -> {
            setVisible(false);

            new AddTrainFrame(this)
                .setVisible(true);
        });

        viewTrainsButton.addActionListener(event -> {
            setVisible(false);

            new ViewTrainsFrame(this)
                .setVisible(true);
        });

      updateTrainButton.addActionListener(event -> {
    setVisible(false);
    new UpdateTrainFrame(this).setVisible(true);
});

stationDetailsButton.addActionListener(event -> {
    setVisible(false);
    new StationDetailsFrame(this).setVisible(true);
});


       addScheduleButton.addActionListener(event -> {
    setVisible(false);
    new AddScheduleFrame(this).setVisible(true);
});

       cancelScheduleButton.addActionListener(event -> {
    setVisible(false);
    new CancelScheduleFrame(this).setVisible(true);
});

        bookingHistoryButton.addActionListener(event -> {
    setVisible(false);
    new BookingHistoryFrame(this).setVisible(true);
});

        profileButton.addActionListener(event -> {
    setVisible(false);

    new AdminProfileFrame(
        this,
        adminId
    ).setVisible(true);
});

        logoutButton.addActionListener(
            event -> logout()
        );
    }

    private JButton makeButton(String text) {
        JButton button = new JButton(text);

        button.setUI(new BasicButtonUI());

        button.setBackground(
            new Color(21, 101, 192)
        );

        button.setForeground(Color.WHITE);

        button.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        button.setPreferredSize(
            new Dimension(190, 50)
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

    private void showComingSoon(String pageName) {
        JOptionPane.showMessageDialog(
            this,
            pageName + " will be added next.",
            pageName,
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void logout() {
        int answer = JOptionPane.showConfirmDialog(
            this,
            "Do you want to log out?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (answer == JOptionPane.YES_OPTION) {
            dispose();
            new AdminLoginFrame().setVisible(true);
        }
    }
}