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

public class ScheduleSearchFrame extends JFrame {

    private final JFrame previousFrame;
    private final long customerId;

    private final JComboBox<String> departureBox =
        new JComboBox<>();

    private final JComboBox<String> arrivalBox =
        new JComboBox<>();

    private final JSpinner journeyDate =
        createDateSpinner();

    private final DefaultTableModel tableModel =
        new DefaultTableModel(
            new String[]{
                "Schedule ID",
                "Train Number",
                "Train Name",
                "Departure",
                "Arrival",
                "Journey Date",
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

    private final JTable scheduleTable =
        new JTable(tableModel);

    public ScheduleSearchFrame(
        JFrame previousFrame,
        long customerId
    ) {
        this.previousFrame = previousFrame;
        this.customerId = customerId;

        setTitle("Search Available Schedules");
        setSize(1400, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
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
            "Search Available Schedules",
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
            new JScrollPane(scheduleTable);

        scrollPane.setBorder(
            BorderFactory.createLineBorder(
                new Color(190, 210, 230)
            )
        );

        JButton continueButton =
            makeButton("CONTINUE BOOKING");

        continueButton.setPreferredSize(
            new Dimension(225, 48)
        );

        JPanel bottomPanel =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT,
                    0,
                    0
                )
            );

        bottomPanel.setOpaque(false);
        bottomPanel.add(continueButton);

        JPanel contentPanel =
            new JPanel(new BorderLayout(15, 15));

        contentPanel.setOpaque(false);

        contentPanel.add(
            searchPanel,
            BorderLayout.NORTH
        );

        contentPanel.add(
            scrollPane,
            BorderLayout.CENTER
        );

        contentPanel.add(
            bottomPanel,
            BorderLayout.SOUTH
        );

        mainPanel.add(
            title,
            BorderLayout.NORTH
        );

        mainPanel.add(
            contentPanel,
            BorderLayout.CENTER
        );

        setContentPane(mainPanel);

        continueButton.addActionListener(
            event -> continueBooking()
        );
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel =
            new JPanel(new GridBagLayout());

        searchPanel.setBackground(Color.WHITE);

        searchPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(205, 218, 232)
                ),
                new EmptyBorder(20, 35, 20, 35)
            )
        );

        GridBagConstraints constraints =
            new GridBagConstraints();

        constraints.fill =
            GridBagConstraints.HORIZONTAL;

        addInput(
            searchPanel,
            constraints,
            "Departure Station",
            departureBox,
            0
        );

        addInput(
            searchPanel,
            constraints,
            "Arrival Station",
            arrivalBox,
            1
        );

        addInput(
            searchPanel,
            constraints,
            "Journey Date",
            journeyDate,
            2
        );

        JButton searchButton =
            makeButton("SEARCH");

        JButton clearButton =
            makeButton("CLEAR");

        JButton backButton =
            makeButton("BACK");

        JPanel buttonPanel =
            new JPanel(
                new FlowLayout(
                    FlowLayout.CENTER,
                    15,
                    0
                )
            );

        buttonPanel.setOpaque(false);

        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.weightx = 1;

        constraints.insets =
            new Insets(15, 5, 0, 5);

        searchPanel.add(
            buttonPanel,
            constraints
        );

        searchButton.addActionListener(
            event -> searchSchedules()
        );

        clearButton.addActionListener(
            event -> clearSearch()
        );

        backButton.addActionListener(
            event -> returnToPreviousPage()
        );

        return searchPanel;
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

        label.setForeground(
            new Color(25, 35, 50)
        );

        input.setPreferredSize(
            new Dimension(760, 45)
        );

        input.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        constraints.gridx = 0;
        constraints.gridy = row;
        constraints.gridwidth = 1;
        constraints.weightx = 0;

        constraints.insets =
            new Insets(8, 5, 8, 20);

        panel.add(label, constraints);

        constraints.gridx = 1;
        constraints.weightx = 1;

        constraints.insets =
            new Insets(8, 5, 8, 5);

        panel.add(input, constraints);
    }

    private void configureTable() {
        scheduleTable.setRowHeight(31);

        scheduleTable.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        scheduleTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        scheduleTable.setAutoResizeMode(
            JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        scheduleTable.setFillsViewportHeight(true);

        scheduleTable.getTableHeader()
            .setPreferredSize(
                new Dimension(0, 46)
            );

        scheduleTable.getTableHeader()
            .setReorderingAllowed(false);

        DefaultTableCellRenderer headerRenderer =
            new DefaultTableCellRenderer();

        headerRenderer.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        headerRenderer.setBackground(
            new Color(15, 82, 145)
        );

        headerRenderer.setForeground(
            Color.WHITE
        );

        headerRenderer.setFont(
            new Font("Arial", Font.BOLD, 12)
        );

        headerRenderer.setOpaque(true);

        for (
            int column = 0;
            column < scheduleTable.getColumnCount();
            column++
        ) {
            scheduleTable.getColumnModel()
                .getColumn(column)
                .setHeaderRenderer(
                    headerRenderer
                );
        }

        int[] widths = {
            75,
            95,
            125,
            120,
            120,
            95,
            95,
            95,
            105,
            85
        };

        for (
            int column = 0;
            column < widths.length;
            column++
        ) {
            scheduleTable.getColumnModel()
                .getColumn(column)
                .setPreferredWidth(
                    widths[column]
                );
        }
    }

    private JButton makeButton(String text) {
        JButton button =
            new JButton(text);

        button.setUI(
            new BasicButtonUI()
        );

        button.setPreferredSize(
            new Dimension(210, 47)
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

    private void searchSchedules() {
        String departure =
            (String) departureBox.getSelectedItem();

        String arrival =
            (String) arrivalBox.getSelectedItem();

        if (
            departure == null
                || arrival == null
        ) {
            JOptionPane.showMessageDialog(
                this,
                "Select departure and arrival stations.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (
            departure.equalsIgnoreCase(arrival)
        ) {
            JOptionPane.showMessageDialog(
                this,
                "Departure and arrival stations cannot be the same.",
                "Invalid Stations",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Date selectedDate =
            (Date) journeyDate.getValue();

        LocalDate date = selectedDate
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

        String sql = """
            SELECT
                s.schedule_id,
                t.train_number,
                t.train_name,
                s.departure_station,
                s.arrival_station,
                s.journey_date,
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
              AND s.available_seats > 0
            ORDER BY
                s.departure_time,
                t.train_number
            """;

        tableModel.setRowCount(0);

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setString(
                1,
                departure
            );

            statement.setString(
                2,
                arrival
            );

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
                            result.getLong(
                                "schedule_id"
                            ),
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
                            result.getDate(
                                "journey_date"
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
                    "No schedules were found for:\n"
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
                "Could not search schedules:\n"
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

    private void continueBooking() {
        int selectedRow =
            scheduleTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Select a schedule from the table.",
                "No Schedule Selected",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        long scheduleId =
            Long.parseLong(
                tableModel.getValueAt(
                    selectedRow,
                    0
                ).toString()
            );

        setVisible(false);

        new PassengerDetailsFrame(
            this,
            customerId,
            scheduleId
        ).setVisible(true);
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