package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
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

    private final JFrame dashboard;
    private final long customerId;

    private final JComboBox<String> departureBox =
        new JComboBox<>();

    private final JComboBox<String> arrivalBox =
        new JComboBox<>();

    private final JSpinner dateSpinner = new JSpinner(
        new SpinnerDateModel()
    );

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

    private final JTable resultTable =
        new JTable(tableModel);

    public ScheduleSearchFrame(
        JFrame dashboard,
        long customerId
    ) {
        this.dashboard = dashboard;
        this.customerId = customerId;

        setTitle("Search Available Schedules");
        setSize(1050, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        createInterface();
        loadStations();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(
                java.awt.event.WindowEvent event
            ) {
                returnToDashboard();
            }
        });
    }

    private void createInterface() {
        JPanel mainPanel =
            new JPanel(new BorderLayout(15, 15));

        mainPanel.setBackground(
            new Color(238, 244, 250)
        );

        mainPanel.setBorder(
            new EmptyBorder(25, 30, 25, 30)
        );

        JLabel title = new JLabel(
            "Search Available Schedules",
            SwingConstants.CENTER
        );

        title.setFont(
            new Font("Arial", Font.BOLD, 28)
        );

        title.setForeground(
            new Color(15, 75, 140)
        );

        mainPanel.add(title, BorderLayout.NORTH);

        JPanel searchPanel =
            new JPanel(new GridBagLayout());

        searchPanel.setBackground(Color.WHITE);

        searchPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(210, 220, 232)
                ),
                new EmptyBorder(18, 20, 18, 20)
            )
        );

        GridBagConstraints c =
            new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(7, 8, 7, 8);

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

        dateSpinner.setEditor(
            new JSpinner.DateEditor(
                dateSpinner,
                "yyyy-MM-dd"
            )
        );

        dateSpinner.setValue(
            Date.from(
                LocalDate.now()
                    .atStartOfDay(
                        ZoneId.systemDefault()
                    )
                    .toInstant()
            )
        );

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

        JPanel searchButtons = new JPanel(
            new FlowLayout(
                FlowLayout.CENTER,
                12,
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
        c.insets = new Insets(15, 8, 3, 8);

        searchPanel.add(searchButtons, c);

        resultTable.setRowHeight(30);

        resultTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        resultTable.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        resultTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        resultTable.getTableHeader().setBackground(
            new Color(15, 75, 140)
        );

        resultTable.getTableHeader().setForeground(
            Color.WHITE
        );

        JScrollPane scrollPane =
            new JScrollPane(resultTable);

        scrollPane.setBorder(
            BorderFactory.createLineBorder(
                new Color(200, 212, 225)
            )
        );

        JButton continueButton =
            makeButton("CONTINUE BOOKING");

        JPanel bottomPanel = new JPanel(
            new FlowLayout(
                FlowLayout.RIGHT,
                0,
                0
            )
        );

        bottomPanel.setOpaque(false);
        bottomPanel.add(continueButton);

        JPanel centerPanel = new JPanel(
            new BorderLayout(10, 10)
        );

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
            event -> returnToDashboard()
        );

        continueButton.addActionListener(
            event -> continueBooking()
        );
    }

    private void addInput(
        JPanel panel,
        GridBagConstraints c,
        String labelText,
        JComponent input,
        int row
    ) {
        JLabel label = new JLabel(labelText);

        label.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        input.setPreferredSize(
            new Dimension(360, 38)
        );

        input.setFont(
            new Font("Arial", Font.PLAIN, 13)
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

    private JButton makeButton(String text) {
        JButton button = new JButton(text);

        button.setUI(new BasicButtonUI());

        button.setPreferredSize(
            new Dimension(165, 40)
        );

        button.setBackground(
            new Color(25, 105, 195)
        );

        button.setForeground(Color.WHITE);

        button.setFont(
            new Font("Arial", Font.BOLD, 12)
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

    private void loadStations() {
        String sql = """
            SELECT station_name
            FROM stations
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
                    result.getString("station_name");

                departureBox.addItem(station);
                arrivalBox.addItem(station);
            }

            if (arrivalBox.getItemCount() > 1) {
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

        if (departure == null || arrival == null) {
            JOptionPane.showMessageDialog(
                this,
                "No stations are available.",
                "Missing Stations",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (departure.equals(arrival)) {
            JOptionPane.showMessageDialog(
                this,
                "Departure and arrival stations must be different.",
                "Invalid Route",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Date selectedDate =
            (Date) dateSpinner.getValue();

        LocalDate journeyDate = selectedDate
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
            ORDER BY t.train_number
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
                java.sql.Date.valueOf(journeyDate)
            );

            try (
                ResultSet result =
                    statement.executeQuery()
            ) {
                while (result.next()) {
                    tableModel.addRow(new Object[]{
                        result.getLong("schedule_id"),
                        result.getString("train_number"),
                        result.getString("train_name"),
                        result.getString("departure_station"),
                        result.getString("arrival_station"),
                        result.getDate("journey_date"),
                        result.getInt("available_seats"),
                        result.getBigDecimal("base_fare")
                    });
                }
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "No schedules were found for this route and date.",
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

    private void continueBooking() {
        int selectedRow =
            resultTable.getSelectedRow();

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
            ((Number) tableModel.getValueAt(
                selectedRow,
                0
            )).longValue();

        setVisible(false);

        new PassengerDetailsFrame(
            this,
            customerId,
            scheduleId
        ).setVisible(true);
    }

    private void clearSearch() {
        tableModel.setRowCount(0);

        if (departureBox.getItemCount() > 0) {
            departureBox.setSelectedIndex(0);
        }

        if (arrivalBox.getItemCount() > 1) {
            arrivalBox.setSelectedIndex(1);
        }

        dateSpinner.setValue(new Date());
    }

    private void showDatabaseError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Database Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private void returnToDashboard() {
        dispose();

        if (dashboard != null) {
            dashboard.setVisible(true);
            dashboard.toFront();
        }
    }
}

