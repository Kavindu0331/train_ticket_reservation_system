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

public class StationDetailsFrame extends JFrame {

    private final JFrame dashboard;
    private final JTextField searchField = new JTextField();
    private final DefaultTableModel tableModel;
    private final JTable stationTable;

    public StationDetailsFrame(JFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("Station Details");
        setSize(1050, 600);
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

        JLabel titleLabel = new JLabel(
            "Station Details"
        );

        titleLabel.setFont(
            new Font("Arial", Font.BOLD, 28)
        );

        titleLabel.setForeground(
            new Color(15, 67, 125)
        );

        JLabel descriptionLabel = new JLabel(
            "View departure and arrival station details"
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

        JLabel searchLabel = new JLabel("Station");

        searchLabel.setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        searchField.setPreferredSize(
            new Dimension(200, 38)
        );

        searchField.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        JButton searchButton =
            makeButton("SEARCH");

        searchButton.setPreferredSize(
            new Dimension(105, 38)
        );

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
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
            "Schedule ID",
            "Train Number",
            "Train Name",
            "Departure Station",
            "Arrival Station",
            "Journey Date",
            "Available Seats",
            "Base Fare",
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

        stationTable = new JTable(tableModel);

        stationTable.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        stationTable.setRowHeight(34);
        stationTable.setFillsViewportHeight(true);
        stationTable.setShowVerticalLines(false);

        stationTable.setGridColor(
            new Color(220, 228, 238)
        );

        stationTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        stationTable.setSelectionBackground(
            new Color(205, 225, 245)
        );

        stationTable.setSelectionForeground(
            new Color(25, 35, 45)
        );

        stationTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        stationTable.getTableHeader().setBackground(
            new Color(15, 67, 125)
        );

        stationTable.getTableHeader()
            .setForeground(Color.WHITE);

        stationTable.getTableHeader()
            .setPreferredSize(
                new Dimension(0, 38)
            );

        DefaultTableCellRenderer center =
            new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        stationTable.getColumnModel()
            .getColumn(0)
            .setCellRenderer(center);

        stationTable.getColumnModel()
            .getColumn(1)
            .setCellRenderer(center);

        stationTable.getColumnModel()
            .getColumn(5)
            .setCellRenderer(center);

        stationTable.getColumnModel()
            .getColumn(6)
            .setCellRenderer(center);

        stationTable.getColumnModel()
            .getColumn(7)
            .setCellRenderer(center);

        stationTable.getColumnModel()
            .getColumn(8)
            .setCellRenderer(center);

        stationTable.getColumnModel()
            .getColumn(0)
            .setPreferredWidth(70);

        stationTable.getColumnModel()
            .getColumn(1)
            .setPreferredWidth(90);

        stationTable.getColumnModel()
            .getColumn(2)
            .setPreferredWidth(130);

        stationTable.getColumnModel()
            .getColumn(3)
            .setPreferredWidth(130);

        stationTable.getColumnModel()
            .getColumn(4)
            .setPreferredWidth(130);

        stationTable.getColumnModel()
            .getColumn(5)
            .setPreferredWidth(90);

        stationTable.getColumnModel()
            .getColumn(6)
            .setPreferredWidth(90);

        stationTable.getColumnModel()
            .getColumn(7)
            .setPreferredWidth(80);

        stationTable.getColumnModel()
            .getColumn(8)
            .setPreferredWidth(90);

        JScrollPane scrollPane =
            new JScrollPane(stationTable);

        scrollPane.setBorder(
            BorderFactory.createLineBorder(
                new Color(210, 220, 232)
            )
        );

        JButton refreshButton =
            makeButton("REFRESH");

        JButton backButton =
            makeButton("BACK");

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

        mainPanel.add(
            topPanel,
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

        searchButton.addActionListener(
            event -> loadStations()
        );

        searchField.addActionListener(
            event -> loadStations()
        );

        refreshButton.addActionListener(event -> {
            searchField.setText("");
            loadStations();
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

        loadStations();
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

    private void loadStations() {
        String sql = """
            SELECT
                s.schedule_id,
                t.train_number,
                t.train_name,
                s.departure_station,
                s.arrival_station,
                s.journey_date,
                s.available_seats,
                s.base_fare,
                s.status
            FROM schedules s
            JOIN trains t
                ON s.train_id = t.train_id
            WHERE s.departure_station LIKE ?
               OR s.arrival_station LIKE ?
            ORDER BY s.journey_date, t.train_number
            """;

        String search =
            "%" + searchField.getText().trim() + "%";

        tableModel.setRowCount(0);

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setString(1, search);
            statement.setString(2, search);

            try (
                ResultSet result =
                    statement.executeQuery()
            ) {
                while (result.next()) {
                    Object[] row = {
                        result.getLong("schedule_id"),
                        result.getString("train_number"),
                        result.getString("train_name"),
                        result.getString("departure_station"),
                        result.getString("arrival_station"),
                        result.getDate("journey_date"),
                        result.getInt("available_seats"),
                        result.getBigDecimal("base_fare"),
                        result.getString("status")
                    };

                    tableModel.addRow(row);
                }
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not load station details:\n"
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