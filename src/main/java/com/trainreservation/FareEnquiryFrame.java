package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class FareEnquiryFrame extends JFrame {

    private final JFrame previousFrame;

    private final JComboBox<String> departureBox =
        new JComboBox<>();

    private final JComboBox<String> arrivalBox =
        new JComboBox<>();

    private final JSpinner journeyDate =
        createDateSpinner();

    private final DefaultTableModel tableModel =
        new DefaultTableModel(
            new String[]{
                "Train Number",
                "Train Name",
                "Departure",
                "Arrival",
                "Departure Time",
                "Arrival Time",
                "Available Seats",
                "Base Fare"
            },
            0
        ) {
            public boolean isCellEditable(
                int row,
                int column
            ) {
                return false;
            }
        };

    private final JTable fareTable =
        new JTable(tableModel);

    public FareEnquiryFrame(
        JFrame previousFrame
    ) {
        this.previousFrame = previousFrame;

        setTitle("Fare Enquiry");
        setSize(1200, 720);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(
            DO_NOTHING_ON_CLOSE
        );
        setResizable(true);

        createInterface();
        loadStations();

        addWindowListener(
            new java.awt.event.WindowAdapter() {
                public void windowClosing(
                    java.awt.event.WindowEvent event
                ) {
                    returnToPreviousPage();
                }
            }
        );
    }

    private void createInterface() {
        JPanel mainPanel =
            new JPanel(new BorderLayout(15, 15));

        mainPanel.setBackground(
            new Color(238, 244, 250)
        );

        mainPanel.setBorder(
            new EmptyBorder(25, 25, 25, 25)
        );

        JLabel title = new JLabel(
            "Train Fare Enquiry",
            SwingConstants.CENTER
        );

        title.setFont(
            new Font("Arial", Font.BOLD, 30)
        );

        title.setForeground(
            new Color(15, 75, 140)
        );

        JPanel searchPanel =
            createSearchPanel();

        configureTable();

        JScrollPane scrollPane =
            new JScrollPane(fareTable);

        scrollPane.setBorder(
            BorderFactory.createLineBorder(
                new Color(190, 210, 230)
            )
        );

        mainPanel.add(
            title,
            BorderLayout.NORTH
        );

        JPanel content =
            new JPanel(new BorderLayout(15, 15));

        content.setOpaque(false);

        content.add(
            searchPanel,
            BorderLayout.NORTH
        );

        content.add(
            scrollPane,
            BorderLayout.CENTER
        );

        mainPanel.add(
            content,
            BorderLayout.CENTER
        );

        setContentPane(mainPanel);
    }

    private JPanel createSearchPanel() {
        JPanel panel =
            new JPanel(new GridBagLayout());

        panel.setBackground(Color.WHITE);

        panel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(205, 218, 232)
                ),
                new EmptyBorder(18, 30, 18, 30)
            )
        );

        GridBagConstraints constraints =
            new GridBagConstraints();

        constraints.fill =
            GridBagConstraints.HORIZONTAL;

        addInput(
            panel,
            constraints,
            "Departure Station",
            departureBox,
            0
        );

        addInput(
            panel,
            constraints,
            "Arrival Station",
            arrivalBox,
            1
        );

        addInput(
            panel,
            constraints,
            "Journey Date",
            journeyDate,
            2
        );

        JButton checkButton =
            makeButton("CHECK FARE");

        JButton clearButton =
            makeButton("CLEAR");

        JButton backButton =
            makeButton("BACK");

        JPanel buttons =
            new JPanel(
                new FlowLayout(
                    FlowLayout.CENTER,
                    12,
                    0
                )
            );

        buttons.setOpaque(false);
        buttons.add(checkButton);
        buttons.add(clearButton);
        buttons.add(backButton);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.weightx = 1;
        constraints.insets =
            new Insets(14, 5, 0, 5);

        panel.add(buttons, constraints);

        checkButton.addActionListener(
            event -> checkFare()
        );

        clearButton.addActionListener(
            event -> clearSearch()
        );

        backButton.addActionListener(
            event -> returnToPreviousPage()
        );

        return panel;
    }

    private void addInput(
        JPanel panel,
        GridBagConstraints constraints,
        String labelText,
        JComponent input,
        int row
    ) {
        JLabel label =
            new JLabel(labelText);

        label.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        input.setPreferredSize(
            new Dimension(620, 42)
        );

        input.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        constraints.insets =
            new Insets(7, 5, 7, 20);

        panel.add(label, constraints);

        constraints.gridx = 1;
        constraints.weightx = 1;
        constraints.insets =
            new Insets(7, 5, 7, 5);

        panel.add(input, constraints);
    }

    private void configureTable() {
        fareTable.setRowHeight(31);

        fareTable.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        fareTable.setFillsViewportHeight(true);

        fareTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        fareTable.getTableHeader()
            .setPreferredSize(
                new Dimension(0, 45)
            );

        fareTable.getTableHeader()
            .setReorderingAllowed(false);

        DefaultTableCellRenderer renderer =
            new DefaultTableCellRenderer();

        renderer.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        renderer.setBackground(
            new Color(15, 82, 145)
        );

        renderer.setForeground(Color.WHITE);
        renderer.setOpaque(true);

        renderer.setFont(
            new Font("Arial", Font.BOLD, 12)
        );

        for (
            int column = 0;
            column < fareTable.getColumnCount();
            column++
        ) {
            fareTable.getColumnModel()
                .getColumn(column)
                .setHeaderRenderer(renderer);
        }
    }

    private JButton makeButton(String text) {
        JButton button =
            new JButton(text);

        button.setUI(new BasicButtonUI());

        button.setPreferredSize(
            new Dimension(180, 44)
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

    private JSpinner createDateSpinner() {
        SpinnerDateModel model =
            new SpinnerDateModel();

        model.setValue(new Date());

        JSpinner spinner =
            new JSpinner(model);

        spinner.setEditor(
            new JSpinner.DateEditor(
                spinner,
                "yyyy-MM-dd"
            )
        );

        return spinner;
    }

    private void loadStations() {
        String sql = """
            SELECT station_name
            FROM stations
            WHERE active = TRUE
            ORDER BY station_name
            """;

        departureBox.removeAllItems();
        arrivalBox.removeAllItems();

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql);

            ResultSet result =
                statement.executeQuery()
        ) {
            while (result.next()) {
                String station =
                    result.getString(
                        "station_name"
                    );

                departureBox.addItem(station);
                arrivalBox.addItem(station);
            }

            if (
                departureBox.getItemCount() > 0
            ) {
                departureBox.setSelectedIndex(0);
            }

            if (
                arrivalBox.getItemCount() > 1
            ) {
                arrivalBox.setSelectedIndex(1);
            }

        } catch (SQLException exception) {
            showDatabaseError(
                "Could not load stations:\n"
                    + exception.getMessage()
            );
        }
    }

    private void checkFare() {
        String departure =
            (String) departureBox.getSelectedItem();

        String arrival =
            (String) arrivalBox.getSelectedItem();

        if (
            departure == null
                || arrival == null
        ) {
            showWarning(
                "Select departure and arrival stations."
            );
            return;
        }

        if (
            departure.equalsIgnoreCase(arrival)
        ) {
            showWarning(
                "Departure and arrival stations cannot be the same."
            );
            return;
        }

        LocalDate date =
            ((Date) journeyDate.getValue())
                .toInstant()
                .atZone(
                    ZoneId.systemDefault()
                )
                .toLocalDate();

        String sql = """
            SELECT
                t.train_number,
                t.train_name,
                s.departure_station,
                s.arrival_station,
                s.departure_time,
                s.arrival_time,
                s.available_seats,
                s.base_fare
            FROM schedules s
            JOIN trains t
                ON t.train_id = s.train_id
            WHERE s.departure_station = ?
              AND s.arrival_station = ?
              AND s.journey_date = ?
              AND s.status = 'SCHEDULED'
            ORDER BY
                s.base_fare,
                s.departure_time
            """;

        tableModel.setRowCount(0);

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setString(1, departure);
            statement.setString(2, arrival);

            statement.setDate(
                3,
                java.sql.Date.valueOf(date)
            );

            try (
                ResultSet result =
                    statement.executeQuery()
            ) {
                while (result.next()) {
                    tableModel.addRow(
                        new Object[]{
                            result.getString(
                                "train_number"
                            ),
                            result.getString(
                                "train_name"
                            ),
                            result.getString(
                                "departure_station"
                            ),
                            result.getString(
                                "arrival_station"
                            ),
                            formatTime(
                                result.getTime(
                                    "departure_time"
                                )
                            ),
                            formatTime(
                                result.getTime(
                                    "arrival_time"
                                )
                            ),
                            result.getInt(
                                "available_seats"
                            ),
                            "Rs. "
                                + result.getBigDecimal(
                                    "base_fare"
                                )
                        }
                    );
                }
            }

            if (
                tableModel.getRowCount() == 0
            ) {
                JOptionPane.showMessageDialog(
                    this,
                    "No fare information was found for:\n"
                        + departure
                        + " to "
                        + arrival
                        + "\nDate: "
                        + date,
                    "No Results",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (SQLException exception) {
            showDatabaseError(
                "Could not load fare information:\n"
                    + exception.getMessage()
            );
        }
    }

    private String formatTime(
        java.sql.Time time
    ) {
        if (time == null) {
            return "--:--";
        }

        return String.format(
            "%02d:%02d",
            time.toLocalTime().getHour(),
            time.toLocalTime().getMinute()
        );
    }

    private void clearSearch() {
        tableModel.setRowCount(0);

        if (
            departureBox.getItemCount() > 0
        ) {
            departureBox.setSelectedIndex(0);
        }

        if (
            arrivalBox.getItemCount() > 1
        ) {
            arrivalBox.setSelectedIndex(1);
        }

        journeyDate.setValue(new Date());
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Invalid Information",
            JOptionPane.WARNING_MESSAGE
        );
    }

    private void showDatabaseError(
        String message
    ) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private void returnToPreviousPage() {
        dispose();

        if (previousFrame != null) {
            previousFrame.setVisible(true);
            previousFrame.toFront();
        }
    }
}