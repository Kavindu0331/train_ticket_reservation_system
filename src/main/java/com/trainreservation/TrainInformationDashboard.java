package com.trainreservation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;

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

public class TrainInformationDashboard
    extends JFrame {

    private final JFrame previousFrame;
    private final long customerId;

    /*
     * Main constructor:
     * Receives the Account Dashboard as previousFrame.
     */
    public TrainInformationDashboard(
        JFrame previousFrame,
        long customerId
    ) {
        this.previousFrame = previousFrame;
        this.customerId = customerId;

        configureFrame();
        createInterface();

        addWindowListener(
            new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(
                    java.awt.event.WindowEvent event
                ) {
                    goBack();
                }
            }
        );
    }

    /*
     * Keeps compatibility with the old constructor.
     */
    public TrainInformationDashboard(
        long customerId
    ) {
        this(
            null,
            customerId
        );
    }

    /*
     * Optional constructor for testing.
     */
    public TrainInformationDashboard() {
        this(
            null,
            0L
        );
    }

    private void configureFrame() {
        setTitle(
            "Train Information and Search"
        );

        setSize(900, 620);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
            JFrame.DO_NOTHING_ON_CLOSE
        );

        setResizable(false);
    }

    private void createInterface() {
        JPanel mainPanel =
            new JPanel(new BorderLayout());

        mainPanel.setBackground(
            new Color(238, 244, 250)
        );

        JPanel header =
            createHeaderPanel();

        JPanel content =
            createContentPanel();

        mainPanel.add(
            header,
            BorderLayout.NORTH
        );

        mainPanel.add(
            content,
            BorderLayout.CENTER
        );

        setContentPane(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel header =
            new JPanel(new BorderLayout());

        header.setBackground(
            new Color(15, 75, 140)
        );

        header.setBorder(
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
                25
            )
        );

        systemTitle.setForeground(
            Color.WHITE
        );

        JLabel moduleLabel =
            new JLabel(
                "Train Information & Search"
            );

        moduleLabel.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                15
            )
        );

        moduleLabel.setForeground(
            Color.WHITE
        );

        header.add(
            systemTitle,
            BorderLayout.WEST
        );

        header.add(
            moduleLabel,
            BorderLayout.EAST
        );

        return header;
    }

    private JPanel createContentPanel() {
        JPanel card =
            new JPanel(
                new GridBagLayout()
            );

        card.setBackground(Color.WHITE);

        card.setBorder(
            BorderFactory.createLineBorder(
                new Color(
                    205,
                    218,
                    232
                )
            )
        );

        JPanel menu =
            new JPanel();

        menu.setOpaque(false);

        menu.setLayout(
            new BoxLayout(
                menu,
                BoxLayout.Y_AXIS
            )
        );

        JLabel title =
            new JLabel(
                "Train Information Dashboard"
            );

        title.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                30
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
                "Search trains, view schedules and check fares"
            );

        subtitle.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                15
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

        JButton searchButton =
            makeButton(
                "SEARCH TRAINS"
            );

        JButton schedulesButton =
            makeButton(
                "VIEW TRAIN SCHEDULES"
            );

        JButton fareButton =
            makeButton(
                "FARE ENQUIRY"
            );

        JButton backButton =
            makeButton(
                "BACK"
            );

        backButton.setBackground(
            new Color(
                95,
                110,
                125
            )
        );

        searchButton.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        schedulesButton.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        fareButton.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        backButton.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        menu.add(title);

        menu.add(
            Box.createVerticalStrut(10)
        );

        menu.add(subtitle);

        menu.add(
            Box.createVerticalStrut(35)
        );

        menu.add(searchButton);

        menu.add(
            Box.createVerticalStrut(14)
        );

        menu.add(schedulesButton);

        menu.add(
            Box.createVerticalStrut(14)
        );

        menu.add(fareButton);

        menu.add(
            Box.createVerticalStrut(14)
        );

        menu.add(backButton);

        card.add(menu);

        JPanel content =
            new JPanel(
                new BorderLayout()
            );

        content.setOpaque(false);

        content.setBorder(
            new EmptyBorder(
                30,
                45,
                30,
                45
            )
        );

        content.add(
            card,
            BorderLayout.CENTER
        );

        searchButton.addActionListener(
            event -> openSearch()
        );

        schedulesButton.addActionListener(
            event -> openSchedules()
        );

        fareButton.addActionListener(
            event -> openFareEnquiry()
        );

        backButton.addActionListener(
            event -> goBack()
        );

        return content;
    }

    private JButton makeButton(
        String text
    ) {
        JButton button =
            new JButton(text);

        button.setUI(
            new BasicButtonUI()
        );

        button.setPreferredSize(
            new Dimension(
                300,
                48
            )
        );

        button.setMaximumSize(
            new Dimension(
                300,
                48
            )
        );

        button.setBackground(
            new Color(
                35,
                110,
                200
            )
        );

        button.setForeground(
            Color.WHITE
        );

        button.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                13
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

    private void openSearch() {
        setVisible(false);

        try {
            ScheduleSearchFrame searchFrame =
                new ScheduleSearchFrame(
                    this,
                    customerId
                );

            searchFrame.setVisible(true);

        } catch (Throwable error) {
            setVisible(true);

            showOpeningError(
                "Schedule Search",
                error
            );
        }
    }

    private void openSchedules() {
        setVisible(false);

        try {
            TrainSchedulesFrame schedulesFrame =
                new TrainSchedulesFrame(
                    this
                );

            schedulesFrame.setVisible(true);

        } catch (Throwable error) {
            setVisible(true);

            showOpeningError(
                "Train Schedules",
                error
            );
        }
    }

    private void openFareEnquiry() {
        setVisible(false);

        try {
            FareEnquiryFrame fareFrame =
                new FareEnquiryFrame(
                    this
                );

            fareFrame.setVisible(true);

        } catch (Throwable error) {
            setVisible(true);

            showOpeningError(
                "Fare Enquiry",
                error
            );
        }
    }

    /*
     * Returns to the Account Dashboard.
     */
    private void goBack() {
        dispose();

        if (previousFrame != null) {
            previousFrame.setVisible(true);
            previousFrame.toFront();
            previousFrame.requestFocus();
            return;
        }

        /*
         * Fallback used only when this dashboard was opened
         * using the old TrainInformationDashboard(long)
         * constructor.
         */
        try {
            AccountDashboardFrame accountDashboard =
                new AccountDashboardFrame(
                    customerId,
                    "Customer",
                    "CUSTOMER"
                );

            accountDashboard.setVisible(true);

        } catch (Throwable error) {
            showOpeningError(
                "Account Dashboard",
                error
            );
        }
    }

    private void showOpeningError(
        String windowName,
        Throwable error
    ) {
        error.printStackTrace();

        String message =
            error.getMessage();

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
                + error.getClass()
                    .getSimpleName()
                + ": "
                + message,
            windowName + " Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    public long getCustomerId() {
        return customerId;
    }

    /*
     * Allows this dashboard to be tested separately.
     */
    public static void main(
        String[] args
    ) {
        javax.swing.SwingUtilities.invokeLater(
            () -> {
                TrainInformationDashboard dashboard =
                    new TrainInformationDashboard(
                        1L
                    );

                dashboard.setVisible(true);
            }
        );
    }
}