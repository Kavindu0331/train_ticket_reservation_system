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

public class TrainSchedulesFrame extends JFrame {

    private final JFrame previousFrame;

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
                "Base Fare",
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

    private final JTable scheduleTable =
        new JTable(tableModel);

    public TrainSchedulesFrame(
        JFrame previousFrame
    ) {
        this.previousFrame = previousFrame;

        setTitle("View Train Schedules");
        setSize(1400, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(
            DO_NOTHING_ON_CLOSE
        );
        setResizable(true);

        createInterface();
        loadSchedules();

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
            "Available Train Schedules",
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
            new JScrollPane(scheduleTable);

        scrollPane.setBorder(
            BorderFactory.createLineBorder(
                new Color(190, 210, 230)
            )
        );

        JButton refreshButton =
            makeButton("REFRESH");

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
        buttonPanel.add(refreshButton);
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
            event -> loadSchedules()
        );

        backButton.addActionListener(
            event -> returnToPreviousPage()
        );
    }

    private void configureTable() {
        scheduleTable.setRowHeight(31);

        scheduleTable.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        scheduleTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        scheduleTable.setFillsViewportHeight(true);

        scheduleTable.setAutoResizeMode(
            JTable.AUTO_RESIZE_ALL_COLUMNS
        );

        scheduleTable.getTableHeader()
            .setPreferredSize(
                new Dimension(0, 46)
            );

        scheduleTable.getTableHeader()
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
            column < scheduleTable.getColumnCount();
            column++
        ) {
            scheduleTable.getColumnModel()
                .getColumn(column)
                .setHeaderRenderer(renderer);
        }
    }

    private JButton makeButton(String text) {
        JButton button =
            new JButton(text);

        button.setUI(new BasicButtonUI());

        button.setPreferredSize(
            new Dimension(180, 45)
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

    private void loadSchedules() {
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
                s.base_fare,
                s.status
            FROM schedules s
            JOIN trains t
                ON t.train_id = s.train_id
            WHERE s.journey_date >= CURRENT_DATE
            ORDER BY
                s.journey_date,
                s.departure_time,
                t.train_number
            """;

        tableModel.setRowCount(0);

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql);

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
                            ),
                        result.getString(
                            "status"
                        )
                    }
                );
            }

            if (
                tableModel.getRowCount() == 0
            ) {
                JOptionPane.showMessageDialog(
                    this,
                    "No upcoming schedules were found.",
                    "No Schedules",
                    JOptionPane.INFORMATION_MESSAGE
                );
            }

        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not load schedules:\n"
                    + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
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

    private void returnToPreviousPage() {
        dispose();

        if (previousFrame != null) {
            previousFrame.setVisible(true);
            previousFrame.toFront();
        }
    }
}