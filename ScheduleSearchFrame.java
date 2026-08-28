package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Calendar;

public class ScheduleSearchFrame extends JFrame {

    private final JFrame previousFrame;
    private final long customerId;

    private final JComboBox<String> departureBox =
        new JComboBox<>();

    private final JComboBox<String> arrivalBox =
        new JComboBox<>();

    private final JSpinner dateSpinner =
        new JSpinner(createDateModel());

    private final DefaultTableModel tableModel =
        new DefaultTableModel(
            new String[]{
                "Schedule ID",
                "Train Number",
                "Train Name",
                "Departure",
                "Arrival",
                "Journey Date",
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
        setSize(1280, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(true);

        departureBox.setEditable(true);
        arrivalBox.setEditable(true);

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

    public ScheduleSearchFrame(long customerId) {
        this(null, customerId);
    }

    public ScheduleSearchFrame(
        JFrame previousFrame,
        long customerId,
        String customerName
    ) {
        this(previousFrame, customerId);
    }

    public ScheduleSearchFrame(
        long customerId,
        String customerName
    ) {
        this(null, customerId);
    }

    private void createInterface() {
        JPanel mainPanel =
            new JPanel(new BorderLayout(12, 12));

        mainPanel.setBackground(
            new Color(238, 244, 250)
        );

        mainPanel.setBorder(
            new EmptyBorder(25, 32, 22, 32)
        );

        JLabel title =
            new JLabel(
                "Search Available Schedules",
                SwingConstants.CENTER
            );

        title.setFont(
            new Font("Arial", Font.BOLD, 32)
        );

        title.setForeground(
            new Color(15, 75, 140)
        );

        JPanel searchPanel =
            new JPanel(new GridBagLayout());

        searchPanel.setBackground(Color.WHITE);

        searchPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(205, 216, 230)
                ),
                new EmptyBorder(25, 30, 25, 30)
            )
        );

        GridBagConstraints c =
            new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(8, 8, 8, 8);

        addInput(
            searchPanel,
            c,
            "Departure Station",
            departureBox,
            0
        );

        addInput(
            searchPanel,
            c,
            "Arrival Station",
            arrivalBox,
            1
        );

        JSpinner.DateEditor dateEditor =
            new JSpinner.DateEditor(
                dateSpinner,
                "yyyy-MM-dd"
            );

        dateSpinner.setEditor(dateEditor);

        addInput(
            searchPanel,
            c,
            "Journey Date",
            dateSpinner,
            2
        );

        JButton searchButton =
            makeButton("SEARCH");

        JButton clearButton =
            makeButton("CLEAR");

        JButton backButton =
            makeButton("BACK");

        JPanel searchButtons =
            new JPanel(
                new FlowLayout(
                    FlowLayout.CENTER,
                    15,
                    0
                )
            );

        searchButtons.setOpaque(false);
        searchButtons.add(searchButton);
        searchButtons.add(clearButton);
        searchButtons.add(backButton);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        c.weightx = 1;
        c.insets = new Insets(18, 8, 2, 8);

        searchPanel.add(searchButtons, c);

        configureTable();

        JScrollPane scrollPane =
            new JScrollPane(scheduleTable);

        scrollPane.setBorder(
            BorderFactory.createLineBorder(
                new Color(195, 208, 225)
            )
        );

        JButton continueButton =
            makeButton("CONTINUE BOOKING");

        continueButton.setPreferredSize(
            new Dimension(230, 50)
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

        JPanel centerPanel =
            new JPanel(new BorderLayout(12, 12));

        centerPanel.setOpaque(false);

        centerPanel.add(
            searchPanel,
            BorderLayout.NORTH
        );

        centerPanel.add(
            scrollPane,
            BorderLayout.CENTER
        );

        centerPanel.add(
            bottomPanel,
            BorderLayout.SOUTH
        );

        mainPanel.add(
            title,
            BorderLayout.NORTH
        );

        mainPanel.add(
            centerPanel,
            BorderLayout.CENTER
        );

        setContentPane(mainPanel);

        searchButton.addActionListener(
            event -> searchSchedules()
        );

        clearButton.addActionListener(
            event -> clearSearch()
        );

        backButton.addActionListener(
            event -> returnToPreviousPage()
        );

        continueButton.addActionListener(
            event -> continueBooking()
        );

        scheduleTable.addMouseListener(
            new java.awt.event.MouseAdapter() {
                public void mouseClicked(
                    java.awt.event.MouseEvent event
                ) {
                    if (event.getClickCount() == 2) {
                        continueBooking();
                    }
                }
            }
        );
    }

    private void addInput(
        JPanel panel,
        GridBagConstraints c,
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
            new Dimension(650, 48)
        );

        input.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        c.gridy = row;
        c.gridwidth = 1;

        c.gridx = 0;
        c.weightx = 0;

        panel.add(label, c);

        c.gridx = 1;
        c.weightx = 1;

        panel.add(input, c);
    }

    private void configureTable() {
        scheduleTable.setRowHeight(32);

        scheduleTable.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        scheduleTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        scheduleTable.setAutoCreateRowSorter(true);
        scheduleTable.setFillsViewportHeight(true);

        scheduleTable.setGridColor(
            new Color(205, 215, 228)
        );

        scheduleTable.setSelectionBackground(
            new Color(205, 225, 250)
        );

        scheduleTable.setSelectionForeground(
            Color.BLACK
        );

        DefaultTableCellRenderer headerRenderer =
            new DefaultTableCellRenderer();

        headerRenderer.setBackground(
            new Color(15, 75, 140)
        );

        headerRenderer.setForeground(Color.WHITE);

        headerRenderer.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        headerRenderer.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        headerRenderer.setOpaque(true);

        for (
            int column = 0;
            column < scheduleTable.getColumnCount();
            column++
        ) {
            scheduleTable
                .getColumnModel()
                .getColumn(column)
                .setHeaderRenderer(headerRenderer);
        }

        scheduleTable
            .getTableHeader()
            .setPreferredSize(
                new Dimension(0, 38)
            );
    }

    private static JButton makeButton(String text) {
        JButton button =
            new JButton(text);

        button.setUI(new BasicButtonUI());

        button.setPreferredSize(
            new Dimension(210, 50)
        );

        button.setBackground(
            new Color(30, 105, 200)
        );

        button.setForeground(Color.WHITE);

        button.setFont(
            new Font("Arial", Font.BOLD, 13)
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

    private static SpinnerDateModel createDateModel() {
        Calendar calendar =
            Calendar.getInstance();

        calendar.set(
            Calendar.HOUR_OF_DAY,
            0
        );

        calendar.set(
            Calendar.MINUTE,
            0
        );

        calendar.set(
            Calendar.SECOND,
            0
        );

        calendar.set(
            Calendar.MILLISECOND,
            0
        );

        return new SpinnerDateModel(
            calendar.getTime(),
            null,
            null,
            Calendar.DAY_OF_MONTH
        );
    }

    private void loadStations() {
        String sql = """
            SELECT station_name
            FROM (
                SELECT station_name
                FROM stations

                UNION

                SELECT departure_station AS station_name
                FROM schedules
                WHERE status = 'SCHEDULED'
                  AND departure_station IS NOT NULL
                  AND TRIM(departure_station) <> ''

                UNION

                SELECT arrival_station AS station_name
                FROM schedules
                WHERE status = 'SCHEDULED'
                  AND arrival_station IS NOT NULL
                  AND TRIM(arrival_station) <> ''
            ) available_stations
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
                String stationName =
                    result.getString("station_name");

                departureBox.addItem(stationName);
                arrivalBox.addItem(stationName);
            }

            if (departureBox.getItemCount() == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "No stations are available.",
                    "No Stations",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            departureBox.setSelectedIndex(0);

            if (arrivalBox.getItemCount() > 1) {
                arrivalBox.setSelectedIndex(1);
            }

        } catch (Exception exception) {
            showDatabaseError(
                "Could not load stations:\n"
                    + exception.getMessage()
            );
        }
    }

    private String getComboText(
        JComboBox<String> comboBox
    ) {
        Object selectedItem =
            comboBox.getEditor().getItem();

        if (selectedItem == null) {
            selectedItem =
                comboBox.getSelectedItem();
        }

        if (selectedItem == null) {
            return "";
        }

        return selectedItem.toString().trim();
    }

    private void searchSchedules() {
        String departure =
            getComboText(departureBox);

        String arrival =
            getComboText(arrivalBox);

        if (
            departure.isEmpty()
                || arrival.isEmpty()
        ) {
            JOptionPane.showMessageDialog(
                this,
                "Enter departure and arrival stations.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (departure.equalsIgnoreCase(arrival)) {
            JOptionPane.showMessageDialog(
                this,
                "Departure and arrival stations must be different.",
                "Invalid Route",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        java.util.Date selectedDate =
            (java.util.Date) dateSpinner.getValue();

        Date journeyDate =
            new Date(selectedDate.getTime());

        String sql = """
            SELECT
                s.schedule_id,
                t.train_number,
                t.train_name,
                s.departure_station,
                s.arrival_station,
                s.journey_date,
                s.available_seats,
                s.base_fare
            FROM schedules s
            JOIN trains t
                ON t.train_id = s.train_id
            WHERE LOWER(TRIM(s.departure_station))
                    = LOWER(TRIM(?))
              AND LOWER(TRIM(s.arrival_station))
                    = LOWER(TRIM(?))
              AND s.journey_date = ?
              AND UPPER(s.status) = 'SCHEDULED'
              AND s.available_seats > 0
            ORDER BY s.schedule_id
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
            statement.setDate(3, journeyDate);

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

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "No schedules were found for:\n"
                        + departure
                        + " to "
                        + arrival
                        + "\nDate: "
                        + journeyDate,
                    "No Results",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                scheduleTable.setRowSelectionInterval(
                    0,
                    0
                );
            }

        } catch (Exception exception) {
            showDatabaseError(
                "Could not search schedules:\n"
                    + exception.getMessage()
            );
        }
    }

    private void continueBooking() {
        int selectedViewRow =
            scheduleTable.getSelectedRow();

        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Select a schedule from the table first.",
                "No Schedule Selected",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int selectedModelRow =
            scheduleTable.convertRowIndexToModel(
                selectedViewRow
            );

        long scheduleId =
            Long.parseLong(
                tableModel.getValueAt(
                    selectedModelRow,
                    0
                ).toString()
            );

        try {
            PassengerDetailsFrame passengerFrame =
                new PassengerDetailsFrame(
                    this,
                    customerId,
                    scheduleId
                );

            passengerFrame.setVisible(true);
            setVisible(false);

        } catch (Throwable error) {
            error.printStackTrace();

            JOptionPane.showMessageDialog(
                this,
                "Could not open passenger details.\n"
                    + error.getClass().getSimpleName()
                    + ": "
                    + error.getMessage(),
                "Booking Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearSearch() {
        tableModel.setRowCount(0);

        departureBox.getEditor().setItem("");
        arrivalBox.getEditor().setItem("");

        dateSpinner.setValue(
            Date.valueOf(LocalDate.now())
        );
    }

    private void showDatabaseError(String message) {
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