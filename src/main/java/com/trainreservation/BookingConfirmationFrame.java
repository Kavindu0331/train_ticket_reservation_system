package com.trainreservation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class BookingConfirmationFrame extends JFrame {

    private final JFrame previousFrame;
    private final long customerId;
    private final long scheduleId;

    private final List<
        PassengerDetailsFrame.Passenger
    > passengers;

    private String customerName;
    private String trainDetails;
    private String route;

    private Date journeyDate;

    private BigDecimal baseFare =
        BigDecimal.ZERO;

    private long sourceStationId;
    private long destinationStationId;

    private final JLabel customerLabel =
        new JLabel("-");

    private final JLabel trainLabel =
        new JLabel("-");

    private final JLabel routeLabel =
        new JLabel("-");

    private final JLabel dateLabel =
        new JLabel("-");

    private final JLabel passengerCountLabel =
        new JLabel("-");

    private final JLabel totalFareLabel =
        new JLabel("-");

    private final JButton confirmButton =
        makeButton("CONFIRM BOOKING");

    private final DefaultTableModel tableModel =
        new DefaultTableModel(
            new String[] {
                "Passenger Name",
                "NIC / Passport",
                "Age",
                "Gender",
                "Travel Class"
            },
            0
        ) {
            @Override
            public boolean isCellEditable(
                int row,
                int column
            ) {
                return false;
            }
        };

    public BookingConfirmationFrame(
        JFrame previousFrame,
        long customerId,
        long scheduleId,
        List<
            PassengerDetailsFrame.Passenger
        > passengers
    ) {
        this.previousFrame = previousFrame;
        this.customerId = customerId;
        this.scheduleId = scheduleId;
        this.passengers = passengers;

        setTitle("Booking Confirmation");
        setSize(950, 680);
        setLocationRelativeTo(null);

        setDefaultCloseOperation(
            DO_NOTHING_ON_CLOSE
        );

        setResizable(false);

        createInterface();
        loadBookingDetails();
        loadPassengers();

        addWindowListener(
            new java.awt.event.WindowAdapter() {
                @Override
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
            new JPanel(
                new BorderLayout(15, 15)
            );

        mainPanel.setBackground(
            new Color(238, 244, 250)
        );

        mainPanel.setBorder(
            new EmptyBorder(
                25,
                30,
                25,
                30
            )
        );

        JLabel title =
            new JLabel(
                "Confirm Your Booking",
                SwingConstants.CENTER
            );

        title.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                28
            )
        );

        title.setForeground(
            new Color(15, 75, 140)
        );

        JPanel detailsPanel =
            createDetailsPanel();

        JTable passengerTable =
            createPassengerTable();

        JScrollPane scrollPane =
            new JScrollPane(
                passengerTable
            );

        scrollPane.setBorder(
            BorderFactory.createLineBorder(
                new Color(165, 185, 208)
            )
        );

        scrollPane.getViewport()
            .setBackground(Color.WHITE);

        JButton backButton =
            makeButton("BACK");

        JPanel buttonPanel =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT,
                    12,
                    0
                )
            );

        buttonPanel.setOpaque(false);
        buttonPanel.add(backButton);
        buttonPanel.add(confirmButton);

        JPanel centerPanel =
            new JPanel(
                new BorderLayout(12, 12)
            );

        centerPanel.setOpaque(false);

        centerPanel.add(
            detailsPanel,
            BorderLayout.NORTH
        );

        centerPanel.add(
            scrollPane,
            BorderLayout.CENTER
        );

        centerPanel.add(
            buttonPanel,
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

        backButton.addActionListener(
            event -> returnToPreviousPage()
        );

        confirmButton.addActionListener(
            event -> confirmBooking()
        );
    }

    private JPanel createDetailsPanel() {
        JPanel detailsPanel =
            new JPanel(
                new GridBagLayout()
            );

        detailsPanel.setBackground(
            Color.WHITE
        );

        detailsPanel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(210, 220, 232)
                ),
                new EmptyBorder(
                    15,
                    25,
                    15,
                    25
                )
            )
        );

        GridBagConstraints constraints =
            new GridBagConstraints();

        constraints.fill =
            GridBagConstraints.HORIZONTAL;

        constraints.insets =
            new Insets(6, 10, 6, 10);

        addDetail(
            detailsPanel,
            constraints,
            "Customer",
            customerLabel,
            0
        );

        addDetail(
            detailsPanel,
            constraints,
            "Train",
            trainLabel,
            1
        );

        addDetail(
            detailsPanel,
            constraints,
            "Route",
            routeLabel,
            2
        );

        addDetail(
            detailsPanel,
            constraints,
            "Journey Date",
            dateLabel,
            3
        );

        addDetail(
            detailsPanel,
            constraints,
            "Passenger Count",
            passengerCountLabel,
            4
        );

        addDetail(
            detailsPanel,
            constraints,
            "Total Fare",
            totalFareLabel,
            5
        );

        return detailsPanel;
    }

    private JTable createPassengerTable() {
        JTable passengerTable =
            new JTable(tableModel);

        passengerTable.setRowHeight(34);

        passengerTable.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                13
            )
        );

        passengerTable.setForeground(
            new Color(30, 40, 55)
        );

        passengerTable.setBackground(
            Color.WHITE
        );

        passengerTable.setSelectionBackground(
            new Color(35, 125, 205)
        );

        passengerTable.setSelectionForeground(
            Color.WHITE
        );

        passengerTable.setGridColor(
            new Color(200, 215, 230)
        );

        passengerTable.setShowGrid(true);
        passengerTable.setFillsViewportHeight(true);
        passengerTable.setAutoCreateRowSorter(true);

        passengerTable.setAutoResizeMode(
            JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        passengerTable
            .getColumnModel()
            .getColumn(0)
            .setPreferredWidth(180);

        passengerTable
            .getColumnModel()
            .getColumn(1)
            .setPreferredWidth(180);

        passengerTable
            .getColumnModel()
            .getColumn(2)
            .setPreferredWidth(80);

        passengerTable
            .getColumnModel()
            .getColumn(3)
            .setPreferredWidth(120);

        passengerTable
            .getColumnModel()
            .getColumn(4)
            .setPreferredWidth(140);

        JTableHeader tableHeader =
            passengerTable.getTableHeader();

        tableHeader.setReorderingAllowed(false);

        tableHeader.setPreferredSize(
            new Dimension(
                tableHeader
                    .getPreferredSize()
                    .width,
                42
            )
        );

        tableHeader.setBackground(
            new Color(15, 85, 155)
        );

        tableHeader.setForeground(
            Color.WHITE
        );

        tableHeader.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                13
            )
        );

        tableHeader.setOpaque(true);

        tableHeader.setDefaultRenderer(
            new DefaultTableCellRenderer() {

                @Override
                public Component
                    getTableCellRendererComponent(
                        JTable table,
                        Object value,
                        boolean isSelected,
                        boolean hasFocus,
                        int row,
                        int column
                    ) {

                    JLabel headerLabel =
                        (JLabel) super
                            .getTableCellRendererComponent(
                                table,
                                value,
                                isSelected,
                                hasFocus,
                                row,
                                column
                            );

                    headerLabel.setText(
                        value == null
                            ? ""
                            : value.toString()
                    );

                    headerLabel.setHorizontalAlignment(
                        SwingConstants.CENTER
                    );

                    headerLabel.setFont(
                        new Font(
                            "Arial",
                            Font.BOLD,
                            13
                        )
                    );

                    headerLabel.setBackground(
                        new Color(15, 85, 155)
                    );

                    headerLabel.setForeground(
                        Color.WHITE
                    );

                    headerLabel.setOpaque(true);

                    headerLabel.setBorder(
                        BorderFactory
                            .createMatteBorder(
                                0,
                                0,
                                0,
                                1,
                                new Color(
                                    210,
                                    225,
                                    240
                                )
                            )
                    );

                    return headerLabel;
                }
            }
        );

        DefaultTableCellRenderer
            centerRenderer =
                new DefaultTableCellRenderer();

        centerRenderer.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        passengerTable
            .getColumnModel()
            .getColumn(2)
            .setCellRenderer(centerRenderer);

        passengerTable
            .getColumnModel()
            .getColumn(3)
            .setCellRenderer(centerRenderer);

        passengerTable
            .getColumnModel()
            .getColumn(4)
            .setCellRenderer(centerRenderer);

        return passengerTable;
    }

    private void addDetail(
        JPanel panel,
        GridBagConstraints constraints,
        String text,
        JLabel valueLabel,
        int row
    ) {
        JLabel label =
            new JLabel(text);

        label.setFont(
            new Font(
                "Arial",
                Font.BOLD,
                13
            )
        );

        valueLabel.setFont(
            new Font(
                "Arial",
                Font.PLAIN,
                13
            )
        );

        constraints.gridy = row;
        constraints.gridx = 0;
        constraints.weightx = 0;

        panel.add(
            label,
            constraints
        );

        constraints.gridx = 1;
        constraints.weightx = 1;

        panel.add(
            valueLabel,
            constraints
        );
    }

    private static JButton makeButton(
        String text
    ) {
        JButton button =
            new JButton(text);

        button.setUI(
            new BasicButtonUI()
        );

        button.setPreferredSize(
            new Dimension(175, 42)
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
                12
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

    private void loadBookingDetails() {
        if (
            passengers == null
                || passengers.isEmpty()
        ) {
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
            WHERE LOWER(TRIM(station_name))
                = LOWER(TRIM(?))
            LIMIT 1
            """;

        try (
            Connection connection =
                DatabaseConnection
                    .getConnection()
        ) {
            try (
                PreparedStatement
                    customerStatement =
                        connection
                            .prepareStatement(
                                customerSql
                            )
            ) {
                customerStatement.setLong(
                    1,
                    customerId
                );

                try (
                    ResultSet result =
                        customerStatement
                            .executeQuery()
                ) {
                    if (!result.next()) {
                        throw new SQLException(
                            "The logged-in customer "
                                + "was not found. "
                                + "Received customer ID: "
                                + customerId
                                + ". Please log out "
                                + "and log in again."
                        );
                    }

                    customerName =
                        result.getString(
                            "full_name"
                        );
                }
            }

            String departureStation;
            String arrivalStation;

            try (
                PreparedStatement
                    scheduleStatement =
                        connection
                            .prepareStatement(
                                scheduleSql
                            )
            ) {
                scheduleStatement.setLong(
                    1,
                    scheduleId
                );

                try (
                    ResultSet result =
                        scheduleStatement
                            .executeQuery()
                ) {
                    if (!result.next()) {
                        throw new SQLException(
                            "The selected active "
                                + "schedule was not found. "
                                + "Received schedule ID: "
                                + scheduleId
                                + "."
                        );
                    }

                    trainDetails =
                        result.getString(
                            "train_number"
                        )
                            + " - "
                            + result.getString(
                                "train_name"
                            );

                    departureStation =
                        result.getString(
                            "departure_station"
                        );

                    arrivalStation =
                        result.getString(
                            "arrival_station"
                        );

                    route =
                        departureStation
                            + " to "
                            + arrivalStation;

                    journeyDate =
                        result.getDate(
                            "journey_date"
                        );

                    baseFare =
                        result.getBigDecimal(
                            "base_fare"
                        );
                }
            }

            sourceStationId =
                findStationId(
                    connection,
                    stationSql,
                    departureStation
                );

            destinationStationId =
                findStationId(
                    connection,
                    stationSql,
                    arrivalStation
                );

            customerLabel.setText(
                customerName
            );

            trainLabel.setText(
                trainDetails
            );

            routeLabel.setText(route);

            dateLabel.setText(
                journeyDate.toString()
            );

            passengerCountLabel.setText(
                String.valueOf(
                    passengers.size()
                )
            );

            totalFareLabel.setText(
                "Rs. "
                    + calculateTotalFare()
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
                connection.prepareStatement(
                    stationSql
                )
        ) {
            statement.setString(
                1,
                stationName
            );

            try (
                ResultSet result =
                    statement.executeQuery()
            ) {
                if (!result.next()) {
                    throw new SQLException(
                        "Station '"
                            + stationName
                            + "' is missing from "
                            + "the stations table."
                    );
                }

                return result.getLong(
                    "station_id"
                );
            }
        }
    }

    private void loadPassengers() {
        tableModel.setRowCount(0);

        if (passengers == null) {
            return;
        }

        for (
            PassengerDetailsFrame.Passenger
                passenger : passengers
        ) {
            tableModel.addRow(
                new Object[] {
                    passenger.getName(),
                    passenger.getNic(),
                    passenger.getAge(),
                    passenger.getGender(),
                    passenger.getSeatClass()
                }
            );
        }
    }

    private BigDecimal calculateTotalFare() {
        BigDecimal total =
            BigDecimal.ZERO;

        for (
            PassengerDetailsFrame.Passenger
                passenger : passengers
        ) {
            BigDecimal classCharge;

            if (
                "First Class".equalsIgnoreCase(
                    passenger.getSeatClass()
                )
            ) {
                classCharge =
                    new BigDecimal("150.00");
            } else {
                classCharge =
                    new BigDecimal("200.00");
            }

            total =
                total.add(
                    baseFare.add(
                        classCharge
                    )
                );
        }

        return total;
    }

    private void confirmBooking() {
        int answer =
            JOptionPane.showConfirmDialog(
                this,
                "Confirm this booking?",
                "Confirm Booking",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );

        if (
            answer
                != JOptionPane.YES_OPTION
        ) {
            return;
        }

        confirmButton.setEnabled(false);

        confirmButton.setText(
            "BOOKING..."
        );

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
            VALUES (
                ?, ?, ?, ?, ?, ?, ?,
                'CONFIRMED'
            )
            """;

        String updateSeatsSql = """
            UPDATE schedules
            SET available_seats =
                available_seats - ?
            WHERE schedule_id = ?
            """;

        Connection connection = null;

        try {
            connection =
                DatabaseConnection
                    .getConnection();

            connection.setAutoCommit(false);

            try (
                PreparedStatement seatStatement =
                    connection.prepareStatement(
                        seatSql
                    )
            ) {
                seatStatement.setLong(
                    1,
                    scheduleId
                );

                try (
                    ResultSet result =
                        seatStatement
                            .executeQuery()
                ) {
                    if (!result.next()) {
                        throw new SQLException(
                            "The selected schedule "
                                + "is unavailable."
                        );
                    }

                    int availableSeats =
                        result.getInt(
                            "available_seats"
                        );

                    if (
                        availableSeats
                            < passengers.size()
                    ) {
                        throw new SQLException(
                            "Only "
                                + availableSeats
                                + " seat(s) "
                                + "are available."
                        );
                    }
                }
            }

            String pnr = generatePnr();

            try (
                PreparedStatement
                    bookingStatement =
                        connection
                            .prepareStatement(
                                bookingSql
                            )
            ) {
                bookingStatement.setString(
                    1,
                    pnr
                );

                bookingStatement.setLong(
                    2,
                    customerId
                );

                bookingStatement.setLong(
                    3,
                    scheduleId
                );

                bookingStatement.setLong(
                    4,
                    sourceStationId
                );

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
                PreparedStatement
                    updateStatement =
                        connection
                            .prepareStatement(
                                updateSeatsSql
                            )
            ) {
                updateStatement.setInt(
                    1,
                    passengers.size()
                );

                updateStatement.setLong(
                    2,
                    scheduleId
                );

                updateStatement.executeUpdate();
            }

            connection.commit();

            JOptionPane.showMessageDialog(
                this,
                "Booking confirmed successfully.\n"
                    + "PNR: "
                    + pnr
                    + "\nTotal Fare: Rs. "
                    + calculateTotalFare(),
                "Booking Successful",
                JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

            CustomerDashboard dashboard =
                new CustomerDashboard(
                    customerId,
                    customerName
                );

            dashboard.setVisible(true);

        } catch (Exception exception) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (
                    SQLException rollbackError
                ) {
                    rollbackError
                        .printStackTrace();
                }
            }

            showDatabaseError(
                "Could not complete "
                    + "the booking:\n"
                    + exception.getMessage()
            );

        } finally {
            if (connection != null) {
                try {
                    connection
                        .setAutoCommit(true);

                    connection.close();

                } catch (
                    SQLException closeError
                ) {
                    closeError
                        .printStackTrace();
                }
            }

            confirmButton.setEnabled(true);

            confirmButton.setText(
                "CONFIRM BOOKING"
            );
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