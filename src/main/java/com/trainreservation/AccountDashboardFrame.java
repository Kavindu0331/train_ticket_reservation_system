package com.trainreservation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.lang.reflect.Constructor;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;

public class AccountDashboardFrame extends JFrame {

    private static final Color HEADER_COLOR =
        new Color(24, 82, 145);

    private static final Color BACKGROUND_COLOR =
        new Color(218, 235, 248);

    private static final Color BUTTON_COLOR =
        new Color(38, 108, 199);

    private static final Color LOGOUT_COLOR =
        new Color(195, 52, 52);

    private static final Color TITLE_COLOR =
        new Color(10, 73, 140);

    private final long customerId;
    private final String customerName;
    private final String userRole;

    /*
     * Main constructor.
     * LoginFrame can pass the logged-in user's information.
     */
    public AccountDashboardFrame(
        long customerId,
        String customerName,
        String userRole
    ) {
        this.customerId = customerId;

        this.customerName =
            customerName == null || customerName.isBlank()
                ? "Customer"
                : customerName;

        this.userRole =
            userRole == null || userRole.isBlank()
                ? "CUSTOMER"
                : userRole.toUpperCase();

        configureFrame();
        createInterface();
    }

    /*
     * Constructor used when the role is not provided.
     */
    public AccountDashboardFrame(
        long customerId,
        String customerName
    ) {
        this(
            customerId,
            customerName,
            "CUSTOMER"
        );
    }

    /*
     * Constructor for compatibility with older LoginFrame code.
     */
    public AccountDashboardFrame(
        String customerName,
        String userRole
    ) {
        this(
            0L,
            customerName,
            userRole
        );
    }

    /*
     * Default constructor for testing.
     */
    public AccountDashboardFrame() {
        this(
            0L,
            "Customer",
            "CUSTOMER"
        );
    }

    private void configureFrame() {
        setTitle("Account Dashboard");
        setSize(1160, 880);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void createInterface() {
        JPanel mainPanel =
            new JPanel(new BorderLayout());

        mainPanel.setBackground(BACKGROUND_COLOR);

        JPanel headerPanel = createHeaderPanel();
        JPanel dashboardPanel = createDashboardPanel();

        mainPanel.add(
            headerPanel,
            BorderLayout.NORTH
        );

        mainPanel.add(
            dashboardPanel,
            BorderLayout.CENTER
        );

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel =
            new JPanel(new BorderLayout());

        headerPanel.setBackground(HEADER_COLOR);

        headerPanel.setBorder(
            new EmptyBorder(28, 36, 28, 36)
        );

        JLabel dashboardTitle =
            new JLabel("Customer Dashboard");

        dashboardTitle.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                30
            )
        );

        dashboardTitle.setForeground(Color.WHITE);

        JLabel welcomeLabel =
            new JLabel(
                "Welcome, " + customerName
            );

        welcomeLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                19
            )
        );

        welcomeLabel.setForeground(Color.WHITE);

        headerPanel.add(
            dashboardTitle,
            BorderLayout.WEST
        );

        headerPanel.add(
            welcomeLabel,
            BorderLayout.EAST
        );

        return headerPanel;
    }

    private JPanel createDashboardPanel() {
        JPanel outerPanel =
            new JPanel(new BorderLayout());

        outerPanel.setOpaque(false);

        outerPanel.setBorder(
            new EmptyBorder(54, 203, 68, 203)
        );

        JPanel shadowPanel =
            new JPanel(new BorderLayout());

        shadowPanel.setBackground(
            new Color(187, 207, 222)
        );

        shadowPanel.setBorder(
            new EmptyBorder(0, 0, 12, 12)
        );

        RoundedPanel cardPanel =
            new RoundedPanel(
                25,
                Color.WHITE
            );

        cardPanel.setLayout(
            new BoxLayout(
                cardPanel,
                BoxLayout.Y_AXIS
            )
        );

        cardPanel.setBorder(
            new EmptyBorder(42, 105, 50, 105)
        );

        JLabel informationTitle =
            createCenteredLabel(
                "Account Information",
                new Font(
                    "Arial",
                    Font.BOLD,
                    36
                ),
                TITLE_COLOR
            );

        JLabel roleLabel =
            createCenteredLabel(
                "Role: " + userRole,
                new Font(
                    "Arial",
                    Font.BOLD,
                    22
                ),
                new Color(45, 45, 45)
            );

        JLabel accessLabel =
            createCenteredLabel(
                getAccessMessage(),
                new Font(
                    "Arial",
                    Font.PLAIN,
                    18
                ),
                new Color(70, 75, 85)
            );

        JButton bookingsButton =
            createButton(
                "BOOKINGS",
                BUTTON_COLOR
            );

        JButton schedulesButton =
            createButton(
                "VIEW SCHEDULES",
                BUTTON_COLOR
            );

        JButton profileButton =
            createButton(
                "MY PROFILE",
                BUTTON_COLOR
            );

        JButton passwordButton =
            createButton(
                "CHANGE PASSWORD",
                BUTTON_COLOR
            );

        JButton logoutButton =
            createButton(
                "LOGOUT",
                LOGOUT_COLOR
            );

        cardPanel.add(informationTitle);
        cardPanel.add(Box.createVerticalStrut(22));

        cardPanel.add(roleLabel);
        cardPanel.add(Box.createVerticalStrut(10));

        cardPanel.add(accessLabel);
        cardPanel.add(Box.createVerticalStrut(30));

        cardPanel.add(bookingsButton);
        cardPanel.add(Box.createVerticalStrut(11));

        cardPanel.add(schedulesButton);
        cardPanel.add(Box.createVerticalStrut(11));

        cardPanel.add(profileButton);
        cardPanel.add(Box.createVerticalStrut(11));

        cardPanel.add(passwordButton);
        cardPanel.add(Box.createVerticalStrut(11));

        cardPanel.add(logoutButton);

        shadowPanel.add(
            cardPanel,
            BorderLayout.CENTER
        );

        outerPanel.add(
            shadowPanel,
            BorderLayout.CENTER
        );

        /*
         * BOOKINGS:
         * Opens Saumaya's CustomerDashboard.
         */
        bookingsButton.addActionListener(
            event -> openBookingDashboard()
        );

        /*
         * Opens Dilruwan's train-information dashboard.
         */
        schedulesButton.addActionListener(
            event -> openTrainDashboard()
        );

        profileButton.addActionListener(
            event -> openOptionalFrame(
                "com.trainreservation.MyProfileFrame",
                "My Profile"
            )
        );

        passwordButton.addActionListener(
            event -> openOptionalFrame(
                "com.trainreservation.ChangePasswordFrame",
                "Change Password"
            )
        );

        logoutButton.addActionListener(
            event -> logout()
        );

        return outerPanel;
    }

    /*
     * Opens Saumaya's booking dashboard.
     */
    private void openBookingDashboard() {
        try {
            CustomerDashboard bookingDashboard =
                new CustomerDashboard(
                    customerId,
                    customerName
                );

            bookingDashboard.setVisible(true);
            dispose();

        } catch (Throwable error) {
            showOpeningError(
                "Booking Dashboard",
                error
            );
        }
    }

    /*
     * Opens Dilruwan's train-information dashboard.
     *
     * Reflection is used so that different constructor
     * versions do not cause a compilation error.
     */
    private void openTrainDashboard() {
        openOptionalFrame(
            "com.trainreservation.TrainInformationDashboard",
            "Train Information Dashboard"
        );
    }

    /*
     * Attempts the common constructors used by the
     * different team modules.
     */
    private void openOptionalFrame(
        String className,
        String windowName
    ) {
        try {
            Class<?> frameClass =
                Class.forName(className);

            Object frameObject =
                createFrameObject(frameClass);

            if (!(frameObject instanceof JFrame)) {
                throw new IllegalStateException(
                    windowName
                        + " must extend JFrame."
                );
            }

            JFrame frame =
                (JFrame) frameObject;

            frame.setVisible(true);
            dispose();

        } catch (Throwable error) {
            showOpeningError(
                windowName,
                getOriginalError(error)
            );
        }
    }

    private Object createFrameObject(
        Class<?> frameClass
    ) throws Exception {

        /*
         * First try:
         * Constructor(JFrame, long)
         */
        try {
            Constructor<?> constructor =
                frameClass.getConstructor(
                    JFrame.class,
                    long.class
                );

            return constructor.newInstance(
                this,
                customerId
            );

        } catch (NoSuchMethodException ignored) {
        }

        /*
         * Second try:
         * Constructor(long, String)
         */
        try {
            Constructor<?> constructor =
                frameClass.getConstructor(
                    long.class,
                    String.class
                );

            return constructor.newInstance(
                customerId,
                customerName
            );

        } catch (NoSuchMethodException ignored) {
        }

        /*
         * Third try:
         * Constructor(long)
         */
        try {
            Constructor<?> constructor =
                frameClass.getConstructor(
                    long.class
                );

            return constructor.newInstance(
                customerId
            );

        } catch (NoSuchMethodException ignored) {
        }

        /*
         * Fourth try:
         * Constructor(JFrame)
         */
        try {
            Constructor<?> constructor =
                frameClass.getConstructor(
                    JFrame.class
                );

            return constructor.newInstance(this);

        } catch (NoSuchMethodException ignored) {
        }

        /*
         * Fifth try:
         * Empty constructor
         */
        try {
            Constructor<?> constructor =
                frameClass.getConstructor();

            return constructor.newInstance();

        } catch (NoSuchMethodException ignored) {
        }

        throw new NoSuchMethodException(
            "A supported constructor was not found in "
                + frameClass.getSimpleName()
                + "."
        );
    }

    private Throwable getOriginalError(
        Throwable error
    ) {
        if (error.getCause() != null) {
            return error.getCause();
        }

        return error;
    }

    private void showOpeningError(
        String windowName,
        Throwable error
    ) {
        error.printStackTrace();

        String message = error.getMessage();

        if (
            message == null
                || message.isBlank()
        ) {
            message = "Unknown error";
        }

        JOptionPane.showMessageDialog(
            this,
            "Could not open "
                + windowName
                + ".\n"
                + error.getClass().getSimpleName()
                + ": "
                + message,
            windowName + " Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private JLabel createCenteredLabel(
        String text,
        Font font,
        Color color
    ) {
        JLabel label =
            new JLabel(
                text,
                SwingConstants.CENTER
            );

        label.setFont(font);
        label.setForeground(color);

        label.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        return label;
    }

    private JButton createButton(
        String text,
        Color backgroundColor
    ) {
        JButton button =
            new JButton(text);

        button.setUI(new BasicButtonUI());

        button.setPreferredSize(
            new Dimension(375, 58)
        );

        button.setMaximumSize(
            new Dimension(375, 58)
        );

        button.setMinimumSize(
            new Dimension(375, 58)
        );

        button.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);

        button.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                16
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

        button.setBorder(
            BorderFactory.createEmptyBorder(
                12,
                20,
                12,
                20
            )
        );

        return button;
    }

    private String getAccessMessage() {
        if (
            "ADMIN".equalsIgnoreCase(userRole)
        ) {
            return "You have administrator access.";
        }

        return "You have customer access.";
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

        if (
            answer == JOptionPane.YES_OPTION
        ) {
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

    public String getUserRole() {
        return userRole;
    }

    /*
     * Creates the rounded white dashboard card.
     */
    private static class RoundedPanel
        extends JPanel {

        private final int cornerRadius;
        private final Color panelColor;

        RoundedPanel(
            int cornerRadius,
            Color panelColor
        ) {
            this.cornerRadius = cornerRadius;
            this.panelColor = panelColor;

            setOpaque(false);
        }

        @Override
        protected void paintComponent(
            Graphics graphics
        ) {
            Graphics2D graphics2D =
                (Graphics2D) graphics.create();

            graphics2D.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
            );

            graphics2D.setColor(panelColor);

            graphics2D.fillRoundRect(
                0,
                0,
                getWidth(),
                getHeight(),
                cornerRadius,
                cornerRadius
            );

            graphics2D.dispose();

            super.paintComponent(graphics);
        }
    }

    /*
     * Allows this frame to be tested separately.
     */
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(
            () -> {
                AccountDashboardFrame frame =
                    new AccountDashboardFrame(
                        1L,
                        "Customer",
                        "CUSTOMER"
                    );

                frame.setVisible(true);
            }
        );
    }
}