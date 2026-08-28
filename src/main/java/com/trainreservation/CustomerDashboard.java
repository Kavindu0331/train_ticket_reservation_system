package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class CustomerDashboard extends JFrame {

    private final long customerId;
    private final String customerName;

    public CustomerDashboard(
        long customerId,
        String customerName
    ) {
        this.customerId = customerId;
        this.customerName = customerName;

        setTitle(
            "Train Reservation - Customer Dashboard"
        );

        setSize(1000, 650);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
            EXIT_ON_CLOSE
        );

        setResizable(false);

        createInterface();
    }

    private void createInterface() {
        JPanel mainPanel =
            new JPanel(new BorderLayout());

        mainPanel.setBackground(
            new Color(238, 244, 250)
        );

        JPanel header =
            new JPanel(new BorderLayout());

        header.setBackground(
            new Color(15, 75, 140)
        );

        header.setBorder(
            new EmptyBorder(22, 30, 22, 30)
        );

        JLabel systemTitle =
            new JLabel(
                "Train Reservation System"
            );

        systemTitle.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                27
            )
        );

        systemTitle.setForeground(Color.WHITE);

        JLabel welcomeLabel =
            new JLabel(
                "Welcome, " + customerName
            );

        welcomeLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                16
            )
        );

        welcomeLabel.setForeground(Color.WHITE);

        header.add(
            systemTitle,
            BorderLayout.WEST
        );

        header.add(
            welcomeLabel,
            BorderLayout.EAST
        );

        JPanel menu =
            new JPanel(
                new GridLayout(5, 1, 0, 12)
            );

        menu.setBackground(Color.WHITE);

        menu.setBorder(
            new EmptyBorder(25, 22, 25, 22)
        );

        menu.setPreferredSize(
            new Dimension(260, 0)
        );

        JButton searchButton =
            makeButton("Search Schedules");

        JButton bookingsButton =
            makeButton("My Bookings");

        JButton profileButton =
            makeButton("My Profile");

        JButton passwordButton =
            makeButton("Change Password");

        JButton logoutButton =
            makeButton("Logout");

        menu.add(searchButton);
        menu.add(bookingsButton);
        menu.add(profileButton);
        menu.add(passwordButton);
        menu.add(logoutButton);

        JPanel content =
            new JPanel(new GridBagLayout());

        content.setBackground(Color.WHITE);

        content.setBorder(
            BorderFactory.createLineBorder(
                new Color(210, 220, 232)
            )
        );

        JPanel welcomePanel =
            new JPanel();

        welcomePanel.setOpaque(false);

        welcomePanel.setLayout(
            new BoxLayout(
                welcomePanel,
                BoxLayout.Y_AXIS
            )
        );

        JLabel title =
            new JLabel(
                "Customer Dashboard"
            );

        title.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                32
            )
        );

        title.setForeground(
            new Color(15, 75, 140)
        );

        title.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel subtitle =
            new JLabel(
                "Search trains and manage your bookings"
            );

        subtitle.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                16
            )
        );

        subtitle.setForeground(
            new Color(80, 90, 105)
        );

        subtitle.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        welcomePanel.add(title);

        welcomePanel.add(
            Box.createVerticalStrut(10)
        );

        welcomePanel.add(subtitle);

        content.add(welcomePanel);

        JPanel center =
            new JPanel(
                new BorderLayout(25, 0)
            );

        center.setOpaque(false);

        center.setBorder(
            new EmptyBorder(25, 25, 25, 25)
        );

        center.add(
            menu,
            BorderLayout.WEST
        );

        center.add(
            content,
            BorderLayout.CENTER
        );

        mainPanel.add(
            header,
            BorderLayout.NORTH
        );

        mainPanel.add(
            center,
            BorderLayout.CENTER
        );

        setContentPane(mainPanel);

        /*
         * Correct button name:
         * searchButton, not scheduleButton.
         */
        searchButton.addActionListener(
            event -> openScheduleSearch()
        );

        bookingsButton.addActionListener(
            event ->
                JOptionPane.showMessageDialog(
                    this,
                    "My Bookings page will open here.",
                    "My Bookings",
                    JOptionPane.INFORMATION_MESSAGE
                )
        );

        profileButton.addActionListener(
            event ->
                JOptionPane.showMessageDialog(
                    this,
                    "Customer Profile page will open here.",
                    "My Profile",
                    JOptionPane.INFORMATION_MESSAGE
                )
        );

        passwordButton.addActionListener(
            event ->
                JOptionPane.showMessageDialog(
                    this,
                    "Change Password page will open here.",
                    "Change Password",
                    JOptionPane.INFORMATION_MESSAGE
                )
        );

        logoutButton.addActionListener(
            event -> logout()
        );
    }

    private void openScheduleSearch() {
        try {
            ScheduleSearchFrame scheduleFrame =
                new ScheduleSearchFrame(
                    this,
                    customerId
                );

            scheduleFrame.setVisible(true);

            /*
             * Hide dashboard only after the schedule
             * window has been created successfully.
             */
            setVisible(false);

        } catch (Throwable error) {
            error.printStackTrace();

            JOptionPane.showMessageDialog(
                this,
                "Could not open schedule search.\n"
                    + error.getClass().getSimpleName()
                    + ": "
                    + error.getMessage(),
                "Schedule Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private JButton makeButton(String text) {
        JButton button =
            new JButton(text);

        button.setUI(
            new BasicButtonUI()
        );

        button.setBackground(
            new Color(25, 105, 195)
        );

        button.setForeground(Color.WHITE);

        button.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
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

    private void logout() {
        int answer =
            JOptionPane.showConfirmDialog(
                this,
                "Do you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

        if (answer == JOptionPane.YES_OPTION) {
            LoginFrame loginFrame =
                new LoginFrame();

            loginFrame.setVisible(true);
            dispose();
        }
    }

    public long getCustomerId() {
        return customerId;
    }

        public String getCustomerName() {
        return customerName;
    }
}