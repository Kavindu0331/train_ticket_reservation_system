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

public class MyBookingsFrame extends JFrame {

    private final JFrame previousFrame;
    private final long customerId;

    private final DefaultTableModel tableModel =
        new DefaultTableModel(
            new String[]{
                "Booking ID",
                "PNR",
                "Train",
                "Route",
                "Journey Date",
                "Passengers",
                "Total Fare",
                "Status"
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

    private final JTable bookingTable =
        new JTable(tableModel);

    public MyBookingsFrame(
        JFrame previousFrame,
        long customerId
    ) {
        this.previousFrame = previousFrame;
        this.customerId = customerId;

        setTitle("My Bookings");
        setSize(1150, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        createInterface();
        loadBookings();

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
            new EmptyBorder(25, 30, 25, 30)
        );

        JLabel title =
            new JLabel(
                "My Bookings",
                SwingConstants.CENTER
            );

        title.setFont(
            new Font("Arial", Font.BOLD, 30)
        );

        title.setForeground(
            new Color(15, 75, 140)
        );

        configureTable();

        JScrollPane scrollPane =
            new JScrollPane(bookingTable);

        scrollPane.setBorder(
            BorderFactory.createLineBorder(
                new Color(195, 208, 225)
            )
        );

        JButton refreshButton =
            makeButton("REFRESH");

        JButton cancelButton =
            makeButton("CANCEL BOOKING");

        JButton backButton =
            makeButton("BACK");

        cancelButton.setBackground(
            new Color(190, 55, 55)
        );

        JPanel buttonPanel =
            new JPanel(
                new FlowLayout(
                    FlowLayout.RIGHT,
                    12,
                    0
                )
            );

        buttonPanel.setOpaque(false);
        buttonPanel.add(refreshButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(backButton);

        mainPanel.add(
            title,
            BorderLayout.NORTH
        );

        mainPanel.add(
            scrollPane,
            BorderLayout.CENTER
        );

        mainPanel.add(
            buttonPanel,
            BorderLayout.SOUTH
        );

        setContentPane(mainPanel);

        refreshButton.addActionListener(
            event -> loadBookings()
        );

        cancelButton.addActionListener(
            event -> cancelSelectedBooking()
        );

        backButton.addActionListener(
            event -> returnToPreviousPage()
        );
    }

    private void configureTable() {
        bookingTable.setRowHeight(32);

        bookingTable.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        bookingTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        bookingTable.setAutoCreateRowSorter(true);
        bookingTable.setFillsViewportHeight(true);

        bookingTable.setGridColor(
            new Color(205, 215, 228)
        );

        bookingTable.setSelectionBackground(
            new Color(205, 225, 250)
        );

        bookingTable.setSelectionForeground(
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
            column < bookingTable.getColumnCount();
            column++
        ) {
            bookingTable
                .getColumnModel()
                .getColumn(column)
                .setHeaderRenderer(headerRenderer);
        }

        bookingTable
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
            new Dimension(180, 44)
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

    private void loadBookings() {
       String sql = """
    SELECT
        b.booking_id,
        b.pnr,
        t.train_number,
        t.train_name,
        s.departure_station,
        s.arrival_station,
        s.journey_date,
        b.passenger_count,
        b.total_fare,
        b.status
    FROM bookings b
    JOIN schedules s
        ON s.schedule_id = b.schedule_id
    JOIN trains t
        ON t.train_id = s.train_id
    WHERE b.user_id = ?
      AND UPPER(b.status) <> 'CANCELLED'
    ORDER BY b.booking_id DESC
    """;

        tableModel.setRowCount(0);

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setLong(1, customerId);

            try (
                ResultSet result =
                    statement.executeQuery()
            ) {
                while (result.next()) {
                    String train =
                        result.getString(
                            "train_number"
                        )
                            + " - "
                            + result.getString(
                                "train_name"
                            );

                    String route =
                        result.getString(
                            "departure_station"
                        )
                            + " to "
                            + result.getString(
                                "arrival_station"
                            );

                    tableModel.addRow(
                        new Object[]{
                            result.getLong(
                                "booking_id"
                            ),

                            result.getString("pnr"),

                            train,

                            route,

                            result.getDate(
                                "journey_date"
                            ),

                            result.getInt(
                                "passenger_count"
                            ),

                            "Rs. "
                                + result.getBigDecimal(
                                    "total_fare"
                                ),

                            result.getString("status")
                        }
                    );
                }
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "You do not have any bookings.",
                    "No Bookings",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (Exception exception) {
            showDatabaseError(
                "Could not load bookings:\n"
                    + exception.getMessage()
            );
        }
    }

    private void cancelSelectedBooking() {
        int selectedViewRow =
            bookingTable.getSelectedRow();

        if (selectedViewRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Select a booking from the table.",
                "No Booking Selected",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int selectedModelRow =
            bookingTable.convertRowIndexToModel(
                selectedViewRow
            );

        long bookingId =
            Long.parseLong(
                tableModel.getValueAt(
                    selectedModelRow,
                    0
                ).toString()
            );

        String pnr =
            tableModel.getValueAt(
                selectedModelRow,
                1
            ).toString();

        String status =
            tableModel.getValueAt(
                selectedModelRow,
                7
            ).toString();

        if ("CANCELLED".equalsIgnoreCase(status)) {
            JOptionPane.showMessageDialog(
                this,
                "This booking is already cancelled.",
                "Already Cancelled",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int answer =
            JOptionPane.showConfirmDialog(
                this,
                "Cancel booking " + pnr + "?",
                "Confirm Cancellation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        String selectSql = """
            SELECT
                schedule_id,
                passenger_count,
                status
            FROM bookings
            WHERE booking_id = ?
              AND user_id = ?
            FOR UPDATE
            """;

        String cancelSql = """
            UPDATE bookings
            SET status = 'CANCELLED'
            WHERE booking_id = ?
              AND user_id = ?
            """;

        String restoreSeatsSql = """
            UPDATE schedules
            SET available_seats =
                available_seats + ?
            WHERE schedule_id = ?
            """;

        Connection connection = null;

        try {
            connection =
                DatabaseConnection.getConnection();

            connection.setAutoCommit(false);

            long scheduleId;
            int passengerCount;

            try (
                PreparedStatement statement =
                    connection.prepareStatement(
                        selectSql
                    )
            ) {
                statement.setLong(1, bookingId);
                statement.setLong(2, customerId);

                try (
                    ResultSet result =
                        statement.executeQuery()
                ) {
                    if (!result.next()) {
                        throw new SQLException(
                            "The booking could not be found."
                        );
                    }

                    String currentStatus =
                        result.getString("status");

                    if (
                        "CANCELLED".equalsIgnoreCase(
                            currentStatus
                        )
                    ) {
                        throw new SQLException(
                            "The booking is already cancelled."
                        );
                    }

                    scheduleId =
                        result.getLong(
                            "schedule_id"
                        );

                    passengerCount =
                        result.getInt(
                            "passenger_count"
                        );
                }
            }

            try (
                PreparedStatement statement =
                    connection.prepareStatement(
                        cancelSql
                    )
            ) {
                statement.setLong(1, bookingId);
                statement.setLong(2, customerId);

                int updatedRows =
                    statement.executeUpdate();

                if (updatedRows != 1) {
                    throw new SQLException(
                        "The booking was not cancelled."
                    );
                }
            }

            try (
                PreparedStatement statement =
                    connection.prepareStatement(
                        restoreSeatsSql
                    )
            ) {
                statement.setInt(
                    1,
                    passengerCount
                );

                statement.setLong(
                    2,
                    scheduleId
                );

                statement.executeUpdate();
            }

            connection.commit();

            JOptionPane.showMessageDialog(
                this,
                "Booking cancelled successfully.\n"
                    + "PNR: "
                    + pnr,
                "Cancellation Successful",
                JOptionPane.INFORMATION_MESSAGE
            );

            loadBookings();

        } catch (Exception exception) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ignored) {
                }
            }

            showDatabaseError(
                "Could not cancel booking:\n"
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
        }
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