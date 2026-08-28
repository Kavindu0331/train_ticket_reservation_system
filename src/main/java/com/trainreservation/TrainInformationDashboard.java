package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

public class TrainInformationDashboard
    extends JFrame {

    private final long customerId;

    public TrainInformationDashboard(
        long customerId
    ) {
        this.customerId = customerId;

        setTitle(
            "Train Information and Search"
        );

        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
            new Font("Arial", Font.BOLD, 25)
        );

        systemTitle.setForeground(Color.WHITE);

        JLabel moduleLabel =
            new JLabel(
                "Train Information & Search"
            );

        moduleLabel.setFont(
            new Font("Arial", Font.BOLD, 15)
        );

        moduleLabel.setForeground(Color.WHITE);

        header.add(
            systemTitle,
            BorderLayout.WEST
        );

        header.add(
            moduleLabel,
            BorderLayout.EAST
        );

        JPanel card =
            new JPanel(new GridBagLayout());

        card.setBackground(Color.WHITE);

        card.setBorder(
            BorderFactory.createLineBorder(
                new Color(205, 218, 232)
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
            new Font("Arial", Font.BOLD, 30)
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
            new Font("Arial", Font.PLAIN, 15)
        );

        subtitle.setForeground(
            new Color(80, 90, 105)
        );

        subtitle.setAlignmentX(
            Component.CENTER_ALIGNMENT
        );

        JButton searchButton =
            makeButton("SEARCH TRAINS");

        JButton schedulesButton =
            makeButton("VIEW TRAIN SCHEDULES");

        JButton fareButton =
            makeButton("FARE ENQUIRY");

        JButton exitButton =
            makeButton("EXIT");

        exitButton.setBackground(
            new Color(95, 110, 125)
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

        exitButton.setAlignmentX(
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

        menu.add(exitButton);

        card.add(menu);

        JPanel content =
            new JPanel(new BorderLayout());

        content.setOpaque(false);

        content.setBorder(
            new EmptyBorder(30, 45, 30, 45)
        );

        content.add(
            card,
            BorderLayout.CENTER
        );

        mainPanel.add(
            header,
            BorderLayout.NORTH
        );

        mainPanel.add(
            content,
            BorderLayout.CENTER
        );

        setContentPane(mainPanel);

        searchButton.addActionListener(
            event -> openSearch()
        );

        schedulesButton.addActionListener(
            event -> openSchedules()
        );

        fareButton.addActionListener(
            event -> openFareEnquiry()
        );

        exitButton.addActionListener(
            event -> exitApplication()
        );
    }

    private JButton makeButton(String text) {
        JButton button =
            new JButton(text);

        button.setUI(new BasicButtonUI());

        button.setPreferredSize(
            new Dimension(300, 48)
        );

        button.setMaximumSize(
            new Dimension(300, 48)
        );

        button.setBackground(
            new Color(35, 110, 200)
        );

        button.setForeground(Color.WHITE);

        button.setFont(
            new Font("Arial", Font.BOLD, 13)
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

        new ScheduleSearchFrame(
            this,
            customerId
        ).setVisible(true);
    }

    private void openSchedules() {
        setVisible(false);

        new TrainSchedulesFrame(
            this
        ).setVisible(true);
    }

    private void openFareEnquiry() {
        setVisible(false);

        new FareEnquiryFrame(
            this
        ).setVisible(true);
    }

    private void exitApplication() {
        int answer =
            JOptionPane.showConfirmDialog(
                this,
                "Do you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION
            );

        if (
            answer
                == JOptionPane.YES_OPTION
        ) {
            System.exit(0);
        }
    }
}
