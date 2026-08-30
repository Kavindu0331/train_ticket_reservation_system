package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingHistoryFrame extends JFrame {

    private final JFrame dashboard;
    private final JTextField searchField = new JTextField();

    private final JComboBox<String> statusBox =
        new JComboBox<>(
            new String[]{
                "ALL",
                "PENDING",
                "CONFIRMED",
                "CANCELLED"
            }
        );

    private final DefaultTableModel tableModel;
    private final JTable bookingTable;
    private final JLabel countLabel = new JLabel();

    public BookingHistoryFrame(JFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("Booking History");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(
            new BorderLayout(0, 18)
        );

        mainPanel.setBackground(
            new Color(240, 245, 250)
        );

        mainPanel.setBorder(
            new EmptyBorder(25, 30, 25, 30)
        );

        JPanel topPanel = new JPanel(
            new BorderLayout(20, 0)
        );

        topPanel.setOpaque(false);

        JPanel headingPanel = new JPanel();
        headingPanel.setOpaque(false);

        headingPanel.setLayout(
            new BoxLayout(
                headingPanel,
                BoxLayout.Y_AXIS
            )
        );

        JLabel titleLabel =
            new JLabel("Booking History");

        titleLabel.setFont(
            new Font("Arial", Font.BOLD, 28)
        );

        titleLabel.setForeground(
            new Color(15, 67, 125)
        );

        JLabel descriptionLabel = new JLabel(
            "View and search customer bookings"
        );

        descriptionLabel.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        descriptionLabel.setForeground(
            new Color(90, 100, 110)
        );

        headingPanel.add(titleLabel);
        headingPanel.add(
            Box.createVerticalStrut(4)
        );
        headingPanel.add(descriptionLabel);

        JPanel searchPanel = new JPanel(
            new FlowLayout(
                FlowLayout.RIGHT,
                10,
                5
            )
        );

        searchPanel.setOpaque(false);

        searchField.setPreferredSize(
            new Dimension(190, 38)
        );

        searchField.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        searchField.setToolTipText(
            "Search by PNR, customer, train or station"
        );

        statusBox.setPreferredSize(
            new Dimension(125, 38)
        );

        JButton searchButton =
            makeButton("SEARCH");

        searchButton.setPreferredSize(
            new Dimension(105, 38)
        );

        searchPanel.add(new JLabel("Search"));
        searchPanel.add(searchField);
        searchPanel.add(new JLabel("Status"));
        searchPanel.add(statusBox);
        searchPanel.add(searchButton);

        topPanel.add(
            headingPanel,
            BorderLayout.WEST
        );

        topPanel.add(
            searchPanel,
            BorderLayout.EAST
        );

        String[] columns = {
            "Booking ID",
            "PNR",
            "Customer",
            "Email",
            "Train",
            "Departure",
            "Arrival",
            "Journey Date",
            "Booking Date",
            "Passengers",
            "Total Fare",
            "Status"
        };

        tableModel = new DefaultTableModel(
            columns,
            0
        ) {
            public boolean isCellEditable(
                int row,
                int column
            ) {
                return false;
            }
        };

        bookingTable = new JTable(tableModel);

        bookingTable.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        bookingTable.setRowHeight(34);
        bookingTable.setAutoResizeMode(
            JTable.AUTO_RESIZE_OFF
        );

        bookingTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        bookingTable.setSelectionBackground(
            new Color(205, 225, 245)
        );

        bookingTable.setSelectionForeground(
            new Color(25, 35, 45)
        );

        bookingTable.setGridColor(
            new Color(220, 228, 238)
        );

        bookingTable.setShowVerticalLines(false);

        bookingTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        bookingTable.getTableHeader().setBackground(
            new Color(15, 67, 125)
        );

        bookingTable.getTableHeader()
            .setForeground(Color.WHITE);

        bookingTable.getTableHeader()
            .setPreferredSize(
                new Dimension(0, 38)
            );

        int[] widths = {
            80, 100, 140, 180, 170, 120,
            120, 100, 145, 90, 95, 100
        };

        for (int i = 0; i < widths.length; i++) {
            bookingTable.getColumnModel()
                .getColumn(i)
                .setPreferredWidth(widths[i]);
        }

        DefaultTableCellRenderer center =
            new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        int[] centeredColumns = {
            0, 1, 7, 8, 9, 10, 11
        };

        for (int column : centeredColumns) {
            bookingTable.getColumnModel()
                .getColumn(column)
                .setCellRenderer(center);
        }

        JScrollPane scrollPane =
            new JScrollPane(bookingTable);

        scrollPane.setBorder(
            BorderFactory.createLineBorder(
                new Color(210, 220, 232)
            )
        );

        JButton refreshButton =
            makeButton("REFRESH");

        JButton backButton =
            makeButton("BACK");

        countLabel.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        countLabel.setForeground(
            new Color(70, 80, 90)
        );

        JPanel bottomPanel = new JPanel(
            new BorderLayout()
        );

        bottomPanel.setOpaque(false);

        JPanel buttonPanel = new JPanel(
            new FlowLayout(
                FlowLayout.RIGHT,
                12,
                0
            )
        );

        buttonPanel.setOpaque(false);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        bottomPanel.add(
            countLabel,
            BorderLayout.WEST
        );

        bottomPanel.add(
            buttonPanel,
            BorderLayout.EAST
        );

        mainPanel.add(
            topPanel,
            BorderLayout.NORTH
        );

        mainPanel.add(
            scrollPane,
            BorderLayout.CENTER
        );

        mainPanel.add(
            bottomPanel,
            BorderLayout.SOUTH
        );

        setContentPane(mainPanel);

        searchButton.addActionListener(
            event -> loadBookings()
        );

        searchField.addActionListener(
            event -> loadBookings()
        );

        statusBox.addActionListener(
            event -> loadBookings()
        );

        refreshButton.addActionListener(event -> {
            searchField.setText("");
            statusBox.setSelectedItem("ALL");
            loadBookings();
        });

        backButton.addActionListener(
            event -> returnToDashboard()
        );

        addWindowListener(new WindowAdapter() {
            public void windowClosing(
                WindowEvent event
            ) {
                returnToDashboard();
            }
        });

        loadBookings();
    }

    private JButton makeButton(String text) {
        JButton button = new JButton(text);

        button.setUI(new BasicButtonUI());

        button.setPreferredSize(
            new Dimension(125, 42)
        );

        button.setBackground(
            new Color(21, 101, 192)
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

    private void loadBookings() {
        String sql = """
            SELECT
                b.booking_id,
                b.pnr,
                u.full_name,
                u.email,
                t.train_number,
                t.train_name,
                source.station_name AS departure_station,
                destination.station_name AS arrival_station,
                s.journey_date,
                b.booking_date,
                b.passenger_count,
                b.total_fare,
                b.status
            FROM bookings b
            JOIN users u
                ON b.user_id = u.user_id
            JOIN schedules s
                ON b.schedule_id = s.schedule_id
            JOIN trains t
                ON s.train_id = t.train_id
            JOIN stations source
                ON b.source_station_id = source.station_id
            JOIN stations destination
                ON b.destination_station_id =
                   destination.station_id
            WHERE (
                b.pnr LIKE ?
                OR u.full_name LIKE ?
                OR u.email LIKE ?
                OR t.train_number LIKE ?
                OR t.train_name LIKE ?
                OR source.station_name LIKE ?
                OR destination.station_name LIKE ?
            )
            AND (? = 'ALL' OR b.status = ?)
            ORDER BY b.booking_date DESC
            """;

        String search =
            "%" + searchField.getText().trim() + "%";

        String status =
            (String) statusBox.getSelectedItem();

        tableModel.setRowCount(0);

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setString(1, search);
            statement.setString(2, search);
            statement.setString(3, search);
            statement.setString(4, search);
            statement.setString(5, search);
            statement.setString(6, search);
            statement.setString(7, search);
            statement.setString(8, status);
            statement.setString(9, status);

            try (
                ResultSet result =
                    statement.executeQuery()
            ) {
                while (result.next()) {
                    String train =
                        result.getString("train_number")
                            + " - "
                            + result.getString("train_name");

                    Object[] row = {
                        result.getLong("booking_id"),
                        result.getString("pnr"),
                        result.getString("full_name"),
                        result.getString("email"),
                        train,
                        result.getString(
                            "departure_station"
                        ),
                        result.getString(
                            "arrival_station"
                        ),
                        result.getDate("journey_date"),
                        result.getTimestamp("booking_date"),
                        result.getInt("passenger_count"),
                        result.getBigDecimal("total_fare"),
                        result.getString("status")
                    };

                    tableModel.addRow(row);
                }
            }

            countLabel.setText(
                "Bookings: " + tableModel.getRowCount()
            );
        } catch (SQLException exception) {
            countLabel.setText("Bookings: 0");

            JOptionPane.showMessageDialog(
                this,
                "Could not load booking history:\n"
                    + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void returnToDashboard() {
        dispose();

        if (dashboard != null) {
            dashboard.setVisible(true);
            dashboard.toFront();
            dashboard.requestFocus();
        }
    }
}

