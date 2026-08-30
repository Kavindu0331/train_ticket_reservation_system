package com.trainreservation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class CustomerDashboard extends JFrame {

    private final long customerId;
    private final String customerName;

    public CustomerDashboard(
        long customerId,
        String customerName
    ) {
        this.customerId = customerId;

        this.customerName =
            customerName == null
                || customerName.isBlank()
                ? "Customer"
                : customerName;

        configureFrame();
        createInterface();
    }

    /*
     * Optional constructor for testing.
     */
    public CustomerDashboard() {
        this(
            0L,
            "Customer"
        );
    }

    private void configureFrame() {
        setTitle(
            "Train Reservation - Booking Dashboard"
        );

        setSize(1000, 650);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
            JFrame.EXIT_ON_CLOSE
        );

        setResizable(false);
    }

    private void createInterface() {
        JPanel mainPanel =
            new JPanel(new BorderLayout());

        mainPanel.setBackground(
            new Color(238, 244, 250)
        );

        JPanel headerPanel =
            createHeaderPanel();

        JPanel centerPanel =
            createCenterPanel();

        mainPanel.add(
            headerPanel,
            BorderLayout.NORTH
        );

        mainPanel.add(
            centerPanel,
            BorderLayout.CENTER
        );

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel =
            new JPanel(new BorderLayout());

        headerPanel.setBackground(
            new Color(15, 75, 140)
        );

        headerPanel.setBorder(
            new EmptyBorder(
                22,
                30,
                22,
                30
            )
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

        systemTitle.setForeground(
            Color.WHITE
        );

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

        welcomeLabel.setForeground(
            Color.WHITE
        );

        headerPanel.add(
            systemTitle,
            BorderLayout.WEST
        );

        headerPanel.add(
            welcomeLabel,
            BorderLayout.EAST
        );

        return headerPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel =
            new JPanel(
                new BorderLayout(25, 0)
            );

        centerPanel.setOpaque(false);

        centerPanel.setBorder(
            new EmptyBorder(
                25,
                25,
                25,
                25
            )
        );

        JPanel menuPanel =
            createMenuPanel();

        JPanel contentPanel =
            createContentPanel();

        centerPanel.add(
            menuPanel,
            BorderLayout.WEST
        );

        centerPanel.add(
            contentPanel,
            BorderLayout.CENTER
        );

        return centerPanel;
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel =
            new JPanel(
                new GridLayout(
                    3,
                    1,
                    0,
                    15
                )
            );

        menuPanel.setBackground(
            Color.WHITE
        );

        menuPanel.setBorder(
            new EmptyBorder(
                70,
                22,
                70,
                22
            )
        );

        menuPanel.setPreferredSize(
            new Dimension(
                280,
                0
            )
        );

        JButton searchButton =
            makeButton(
                "SEARCH SCHEDULES"
            );

        JButton bookingsButton =
            makeButton(
                "MY BOOKINGS"
            );

        JButton backButton =
            makeButton(
                "BACK TO ACCOUNT"
            );

        backButton.setBackground(
            new Color(90, 105, 120)
        );

        menuPanel.add(searchButton);
        menuPanel.add(bookingsButton);
        menuPanel.add(backButton);

        searchButton.addActionListener(
            event -> openScheduleSearch()
        );

        bookingsButton.addActionListener(
            event -> openMyBookings()
        );

        backButton.addActionListener(
            event -> returnToAccountDashboard()
        );

        return menuPanel;
    }

    private JPanel createContentPanel() {
        JPanel contentPanel =
            new JPanel(
                new GridBagLayout()
            );

        contentPanel.setBackground(
            Color.WHITE
        );

        contentPanel.setBorder(
            BorderFactory.createLineBorder(
                new Color(
                    210,
                    220,
                    232
                )
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
                "Customer Booking Dashboard"
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
                "Search schedules and manage your bookings"
            );

        subtitle.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                16
            )
        );

        subtitle.setForeground(
            new Color(
                80,
                90,
                105
            )
        );

        subtitle.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JLabel informationLabel =
            new JLabel(
                "Passenger details, fare calculation, "
                    + "confirmation and cancellation"
            );

        informationLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                14
            )
        );

        informationLabel.setForeground(
            new Color(
                100,
                108,
                120
            )
        );

        informationLabel.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        welcomePanel.add(title);

        welcomePanel.add(
            Box.createVerticalStrut(12)
        );

        welcomePanel.add(subtitle);

        welcomePanel.add(
            Box.createVerticalStrut(8)
        );

        welcomePanel.add(
            informationLabel
        );

        contentPanel.add(welcomePanel);

        return contentPanel;
    }

    private JButton makeButton(
        String buttonText
    ) {
        JButton button =
            new JButton(buttonText);

        button.setUI(
            new BasicButtonUI()
        );

        button.setBackground(
            new Color(25, 105, 195)
        );

        button.setForeground(
            Color.WHITE
        );

        button.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                14
            )
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

    /*
     * Opens the schedule-search window.
     */
    private void openScheduleSearch() {
        try {
            ScheduleSearchFrame scheduleFrame =
                new ScheduleSearchFrame(
                    this,
                    customerId
                );

            scheduleFrame.setVisible(true);
            setVisible(false);

        } catch (Throwable error) {
            showError(
                "Could not open schedule search.",
                "Schedule Search Error",
                error
            );
        }
    }

    /*
     * Opens Saumaya's booking-management window.
     */
    private void openMyBookings() {
        try {
            MyBookingsFrame bookingsFrame =
                new MyBookingsFrame(
                    this,
                    customerId
                );

            bookingsFrame.setVisible(true);
            setVisible(false);

        } catch (Throwable error) {
            showError(
                "Could not open My Bookings.",
                "My Bookings Error",
                error
            );
        }
    }

    /*
     * Returns to AccountDashboardFrame.
     */
    private void returnToAccountDashboard() {
        try {
            AccountDashboardFrame accountDashboard =
                new AccountDashboardFrame(
                    customerId,
                    customerName,
                    "CUSTOMER"
                );

            accountDashboard.setVisible(true);
            dispose();

        } catch (Throwable error) {
            showError(
                "Could not return to the Account Dashboard.",
                "Dashboard Error",
                error
            );
        }
    }

    private void showError(
        String description,
        String title,
        Throwable error
    ) {
        error.printStackTrace();

        String errorMessage =
            error.getMessage();

        if (
            errorMessage == null
                || errorMessage.isBlank()
        ) {
            errorMessage =
                "Unknown error";
        }

        JOptionPane.showMessageDialog(
            this,
            description
                + "\n"
                + error.getClass()
                    .getSimpleName()
                + ": "
                + errorMessage,
            title,
            JOptionPane.ERROR_MESSAGE
        );
    }

    public long getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    /*
     * Runs this dashboard separately for testing.
     */
    public static void main(
        String[] args
    ) {
        javax.swing.SwingUtilities.invokeLater(
            () -> {
                CustomerDashboard dashboard =
                    new CustomerDashboard(
                        1L,
                        "Customer"
                    );

                dashboard.setVisible(true);
            }
        );
    }
}