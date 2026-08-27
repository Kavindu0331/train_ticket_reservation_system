package com.trainreservation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class AdminDashboard extends JFrame {

    private final long adminId;
    private final String adminName;

    public AdminDashboard(long adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;

        setTitle("Train Reservation - Admin Dashboard");
        setSize(750, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        createInterface();
    }

    private void createInterface() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel welcomeLabel = new JLabel("Welcome, " + adminName);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));

        mainPanel.add(welcomeLabel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel(new GridLayout(3, 2, 15, 15));

        JButton viewTrainsButton = new JButton("View Trains");
        JButton addTrainButton = new JButton("Add Train");
        JButton updateTrainButton = new JButton("Update Train");
        JButton cancelTrainButton = new JButton("Cancel Schedule");
        JButton bookingHistoryButton = new JButton("Booking History");
        JButton profileButton = new JButton("Admin Profile");

        menuPanel.add(viewTrainsButton);
        menuPanel.add(addTrainButton);
        menuPanel.add(updateTrainButton);
        menuPanel.add(cancelTrainButton);
        menuPanel.add(bookingHistoryButton);
        menuPanel.add(profileButton);

        mainPanel.add(menuPanel, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");

        logoutButton.addActionListener(event -> {
            dispose();
            new AdminLoginFrame().setVisible(true);
        });

        JPanel bottomPanel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT)
        );

        bottomPanel.add(logoutButton);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    public long getAdminId() {
        return adminId;
    }
}