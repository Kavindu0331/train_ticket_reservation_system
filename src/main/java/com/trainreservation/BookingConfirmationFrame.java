package com.trainreservation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;

public class BookingConfirmationFrame extends JFrame {

    private final JFrame previousFrame;
    private final long customerId;
    private final long scheduleId;
    private final List<PassengerDetailsFrame.Passenger> passengers;

    private String customerName;
    private String trainDetails;
    private String route;
    private Date journeyDate;
    private BigDecimal baseFare = BigDecimal.ZERO;
    private long sourceStationId;
    private long destinationStationId;

    private final JLabel customerLabel = new JLabel("-");
    private final JLabel trainLabel = new JLabel("-");
    private final JLabel routeLabel = new JLabel("-");
    private final JLabel dateLabel = new JLabel("-");
    private final JLabel passengerCountLabel = new JLabel("-");
    private final JLabel totalFareLabel = new JLabel("-");

    private final JButton confirmButton =
        makeButton("CONFIRM BOOKING");

    private final DefaultTableModel tableModel =
        new DefaultTableModel(
            new String[]{
                "Passenger Name",
                "NIC / Passport",
                "Age",
                "Gender",
                "Class"
            },
            0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

    public BookingConfirmationFrame(
        JFrame previousFrame,
        long customerId,
        long scheduleId,
        List<PassengerDetailsFrame.Passenger> passengers
    ) {
        this.previousFrame = previousFrame;
        this.customerId = customerId;
        this.scheduleId = scheduleId;
        this.passengers = passengers;

        setTitle("Booking Confirmation");
        setSize(950, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        createInterface();
        loadBookingDetails();
        loadPassengers();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(
                java.awt.event.WindowEvent event
            ) {
                returnToPreviousPage();
            }
        });
    }

    private void createInterface() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(238, 244, 250));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel(
            "Confirm Your Booking",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(15, 75, 140));

        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(Color.WHITE);
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(210, 220, 232)
            ),
            new EmptyBorder(15, 25, 15, 25)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(6, 10, 6, 10);

        addDetail(detailsPanel, c, "Customer", customerLabel, 0);
        addDetail(detailsPanel, c, "Train", trainLabel, 1);
        addDetail(detailsPanel, c, "Route", routeLabel, 2);
        addDetail(detailsPanel, c, "Journey Date", dateLabel, 3);
        addDetail(
            detailsPanel,
            c,
            "Passenger Count",
            passengerCountLabel,
            4
        );
        addDetail(
            detailsPanel,
            c,
            "Total Fare",
            totalFareLabel,
            5
        );

        JTable passengerTable = new JTable(tableModel);
        passengerTable.setRowHeight(29);
        passengerTable.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        passengerTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        passengerTable.getTableHeader().setBackground(
            new Color(15, 75, 140)
        );

        passengerTable.getTableHeader().setForeground(
            Color.WHITE
        );

        JScrollPane scrollPane =
            new JScrollPane(passengerTable);

        JButton backButton = makeButton("BACK");

        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 12, 0)
        );

        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        buttonPanel.add(confirmButton);

        JPanel centerPanel = new JPanel(
            new BorderLayout(12, 12)
        );

        centerPanel.setOpaque(false);
        centerPanel.add(detailsPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(buttonPanel, BorderLayout.SOUTH);

        mainPanel.add(title, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);

        backButton.addActionListener(
            event -> returnToPreviousPage()
        );

        confirmButton.addActionListener(
            event -> confirmBooking()
        );
    }

    private void addDetail(
        JPanel panel,
        GridBagConstraints c,
        String text,
        JLabel valueLabel,
        int row
    ) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 13));

        valueLabel.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        c.gridy = row;
        c.gridx = 0;
        c.weightx = 0;
        panel.add(label, c);

        c.gridx = 1;
        c.weightx = 1;
        panel.add(valueLabel, c);
    }

    private static JButton makeButton(String text) {
        JButton button = new JButton(text);

        button.setUI(new BasicButtonUI());
        button.setPreferredSize(new Dimension(175, 42));
        button.setBackground(new Color(25, 105, 195));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        return button;
    }

    private void loadBookingDetails() {
        if (passengers == null || passengers.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Please add at least one passenger.",
                "Passenger Required",
                JOptionPane.WARNING_MESSAGE
            );
            confirmButton.setEnabled(false);
            return;
        }

        String scheduleSql = """
            SELECT
                t.train_number,
                t.train_name,
                s.departure_station,
                s.arrival_station,
                s.journey_date,
                s.base_fare
            FROM schedules s
            JOIN trains t
                ON t.train_id = s.train_id
            WHERE s.schedule_id = ?
              AND s.status = 'SCHEDULED'
            """;

        String customerSql = """
            SELECT full_name
            FROM users
            WHERE user_id = ?
            LIMIT 1
            """;

        String stationSql = """
            SELECT station_id
            FROM stations
            WHERE LOWER(TRIM(station_name)) = LOWER(TRIM(?))
            LIMIT 1
            """;

        try (
            Connection connection =
                DatabaseConnection.getConnection()
        ) {
            try (
                PreparedStatement customerStatement =
                    connection.prepareStatement(customerSql)
            ) {
                customerStatement.setLong(1, customerId);

                try (
                    ResultSet result =
                        customerStatement.executeQuery()
                ) {
                    if (!result.next()) {
                        throw new SQLException(
                            "The logged-in customer was not found. "
                                + "Received customer ID: "
                                + customerId
                                + ". Please log out and log in again."
                        );
                    }

                    customerName =
                        result.getString("full_name");
                }
            }

            String departureStation;
            String arrivalStation;

            try (
                PreparedStatement scheduleStatement =
                    connection.prepareStatement(scheduleSql)
            ) {
                scheduleStatement.setLong(1, scheduleId);

                try (
                    ResultSet result =
                        scheduleStatement.executeQuery()
                ) {
                    if (!result.next()) {
                        throw new SQLException(
                            "The selected active schedule was not found. "
                                + "Received schedule ID: "
                                + scheduleId
                                + "."
                        );
                    }

                    trainDetails =
                        result.getString("train_number")
                            + " - "
                            + result.getString("train_name");

                    departureStation =
                        result.getString("departure_station");

                    arrivalStation =
                        result.getString("arrival_station");

                    route =
                        departureStation
                            + " to "
                            + arrivalStation;

                    journeyDate =
                        result.getDate("journey_date");

                    baseFare =
                        result.getBigDecimal("base_fare");
                }
            }

            sourceStationId = findStationId(
                connection,
                stationSql,
                departureStation
            );

            destinationStationId = findStationId(
                connection,
                stationSql,
                arrivalStation
            );

            customerLabel.setText(customerName);
            trainLabel.setText(trainDetails);
            routeLabel.setText(route);
            dateLabel.setText(journeyDate.toString());

            passengerCountLabel.setText(
                String.valueOf(passengers.size())
            );

            totalFareLabel.setText(
                "Rs. " + calculateTotalFare()
            );

        } catch (SQLException exception) {
            showDatabaseError(
                "Could not load booking details:\n"
                    + exception.getMessage()
            );
            confirmButton.setEnabled(false);
        }
    }

    private long findStationId(
        Connection connection,
        String stationSql,
        String stationName
    ) throws SQLException {
        try (
            PreparedStatement statement =
                connection.prepareStatement(stationSql)
        ) {
            statement.setString(1, stationName);

            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) {
                    throw new SQLException(
                        "Station '"
                            + stationName
                            + "' is missing from the stations table."
                    );
                }

                return result.getLong("station_id");
            }
        }
    }

    private void loadPassengers() {
        tableModel.setRowCount(0);

        for (
            PassengerDetailsFrame.Passenger passenger
                : passengers
        ) {
            tableModel.addRow(new Object[]{
                passenger.getName(),
                passenger.getNic(),
                passenger.getAge(),
                passenger.getGender(),
                passenger.getSeatClass()
            });
        }
    }

    private BigDecimal calculateTotalFare() {
        BigDecimal total = BigDecimal.ZERO;

        for (
            PassengerDetailsFrame.Passenger passenger
                : passengers
        ) {
            BigDecimal classCharge;

            if (
                "First Class".equals(
                    passenger.getSeatClass()
                )
            ) {
                classCharge = new BigDecimal("150.00");
            } else {
                classCharge = new BigDecimal("200.00");
            }

            total = total.add(
                baseFare.add(classCharge)
            );
        }

        return total;
    }

    private void confirmBooking() {
        int answer = JOptionPane.showConfirmDialog(
            this,
            "Confirm this booking?",
            "Confirm Booking",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        confirmButton.setEnabled(false);
        confirmButton.setText("BOOKING...");

        String seatSql = """
            SELECT available_seats
            FROM schedules
            WHERE schedule_id = ?
              AND status = 'SCHEDULED'
            FOR UPDATE
            """;

        String bookingSql = """
            INSERT INTO bookings (
                pnr,
                user_id,
                schedule_id,
                source_station_id,
                destination_station_id,
                passenger_count,
                total_fare,
                status
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, 'CONFIRMED')
            """;

        String updateSeatsSql = """
            UPDATE schedules
            SET available_seats = available_seats - ?
            WHERE schedule_id = ?
            """;

        Connection connection = null;

        try {
            connection =
                DatabaseConnection.getConnection();

            connection.setAutoCommit(false);

            try (
                PreparedStatement seatStatement =
                    connection.prepareStatement(seatSql)
            ) {
                seatStatement.setLong(1, scheduleId);

                try (
                    ResultSet result =
                        seatStatement.executeQuery()
                ) {
                    if (!result.next()) {
                        throw new SQLException(
                            "The selected schedule is unavailable."
                        );
                    }

                    int availableSeats =
                        result.getInt("available_seats");

                    if (availableSeats < passengers.size()) {
                        throw new SQLException(
                            "Only "
                                + availableSeats
                                + " seat(s) are available."
                        );
                    }
                }
            }

            String pnr = generatePnr();

            try (
                PreparedStatement bookingStatement =
                    connection.prepareStatement(bookingSql)
            ) {
                bookingStatement.setString(1, pnr);
                bookingStatement.setLong(2, customerId);
                bookingStatement.setLong(3, scheduleId);
                bookingStatement.setLong(4, sourceStationId);
                bookingStatement.setLong(
                    5,
                    destinationStationId
                );
                bookingStatement.setInt(
                    6,
                    passengers.size()
                );
                bookingStatement.setBigDecimal(
                    7,
                    calculateTotalFare()
                );

                bookingStatement.executeUpdate();
            }

            try (
                PreparedStatement updateStatement =
                    connection.prepareStatement(updateSeatsSql)
            ) {
                updateStatement.setInt(
                    1,
                    passengers.size()
                );
                updateStatement.setLong(2, scheduleId);
                updateStatement.executeUpdate();
            }

            connection.commit();

            JOptionPane.showMessageDialog(
                this,
                "Booking confirmed successfully.\n"
                    + "PNR: " + pnr + "\n"
                    + "Total Fare: Rs. "
                    + calculateTotalFare(),
                "Booking Successful",
                JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

            new CustomerDashboard(
                customerId,
                customerName
            ).setVisible(true);

        } catch (Exception exception) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ignored) {
                }
            }

            showDatabaseError(
                "Could not complete the booking:\n"
                    + exception.getMessage()
            );

        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException ignored) {
                }
            }

            confirmButton.setEnabled(true);
            confirmButton.setText("CONFIRM BOOKING");
        }
    }

    private String generatePnr() {
        return "PNR"
            + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10)
                .toUpperCase();
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

