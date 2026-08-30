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

public class CancelScheduleFrame extends JFrame {

    private final JFrame dashboard;
    private final DefaultTableModel tableModel;
    private final JTable scheduleTable;

    public CancelScheduleFrame(JFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("Cancel Schedule");
        setSize(1050, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(
            new BorderLayout(0, 20)
        );

        mainPanel.setBackground(
            new Color(240, 245, 250)
        );

        mainPanel.setBorder(
            new EmptyBorder(25, 30, 25, 30)
        );

        JPanel headingPanel = new JPanel(
            new BorderLayout()
        );

        headingPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(
            "Cancel Schedule"
        );

        titleLabel.setFont(
            new Font("Arial", Font.BOLD, 28)
        );

        titleLabel.setForeground(
            new Color(15, 67, 125)
        );

        JLabel descriptionLabel = new JLabel(
            "Select a scheduled journey to cancel"
        );

        descriptionLabel.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        descriptionLabel.setForeground(
            new Color(90, 100, 110)
        );

        headingPanel.add(
            titleLabel,
            BorderLayout.NORTH
        );

        headingPanel.add(
            descriptionLabel,
            BorderLayout.SOUTH
        );

        String[] columns = {
            "Schedule ID",
            "Train Number",
            "Train Name",
            "Departure",
            "Arrival",
            "Journey Date",
            "Available Seats",
            "Fare",
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

        scheduleTable = new JTable(tableModel);

        scheduleTable.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        scheduleTable.setRowHeight(34);
        scheduleTable.setFillsViewportHeight(true);
        scheduleTable.setShowVerticalLines(false);

        scheduleTable.setGridColor(
            new Color(220, 228, 238)
        );

        scheduleTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        scheduleTable.setSelectionBackground(
            new Color(205, 225, 245)
        );

        scheduleTable.setSelectionForeground(
            new Color(25, 35, 45)
        );

        scheduleTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        scheduleTable.getTableHeader().setBackground(
            new Color(15, 67, 125)
        );

        scheduleTable.getTableHeader()
            .setForeground(Color.WHITE);

        scheduleTable.getTableHeader()
            .setPreferredSize(
                new Dimension(0, 38)
            );

        DefaultTableCellRenderer center =
            new DefaultTableCellRenderer();

        center.setHorizontalAlignment(
            SwingConstants.CENTER
        );

        scheduleTable.getColumnModel()
            .getColumn(0)
            .setCellRenderer(center);

        scheduleTable.getColumnModel()
            .getColumn(1)
            .setCellRenderer(center);

        scheduleTable.getColumnModel()
            .getColumn(5)
            .setCellRenderer(center);

        scheduleTable.getColumnModel()
            .getColumn(6)
            .setCellRenderer(center);

        scheduleTable.getColumnModel()
            .getColumn(7)
            .setCellRenderer(center);

        scheduleTable.getColumnModel()
            .getColumn(8)
            .setCellRenderer(center);

        scheduleTable.getColumnModel()
            .getColumn(0)
            .setPreferredWidth(75);

        scheduleTable.getColumnModel()
            .getColumn(1)
            .setPreferredWidth(90);

        scheduleTable.getColumnModel()
            .getColumn(2)
            .setPreferredWidth(140);

        scheduleTable.getColumnModel()
            .getColumn(3)
            .setPreferredWidth(120);

        scheduleTable.getColumnModel()
            .getColumn(4)
            .setPreferredWidth(120);

        scheduleTable.getColumnModel()
            .getColumn(5)
            .setPreferredWidth(95);

        scheduleTable.getColumnModel()
            .getColumn(6)
            .setPreferredWidth(95);

        scheduleTable.getColumnModel()
            .getColumn(7)
            .setPreferredWidth(75);

        scheduleTable.getColumnModel()
            .getColumn(8)
            .setPreferredWidth(90);

        JScrollPane scrollPane =
            new JScrollPane(scheduleTable);

        scrollPane.setBorder(
            BorderFactory.createLineBorder(
                new Color(210, 220, 232)
            )
        );

        JButton cancelButton =
            makeButton("CANCEL SCHEDULE");

        JButton refreshButton =
            makeButton("REFRESH");

        JButton backButton =
            makeButton("BACK");

        cancelButton.setPreferredSize(
            new Dimension(170, 42)
        );

        cancelButton.setBackground(
            new Color(190, 45, 45)
        );

        JPanel buttonPanel = new JPanel(
            new FlowLayout(
                FlowLayout.RIGHT,
                12,
                0
            )
        );

        buttonPanel.setOpaque(false);
        buttonPanel.add(cancelButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(
            headingPanel,
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

        cancelButton.addActionListener(
            event -> cancelSchedule()
        );

        refreshButton.addActionListener(
            event -> loadSchedules()
        );

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

        loadSchedules();
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

    private void loadSchedules() {
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
            WHERE s.status = 'SCHEDULED'
            ORDER BY s.journey_date, t.train_number
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

    private void cancelSchedule() {
        int selectedRow =
            scheduleTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Select a schedule first.",
                "No Schedule Selected",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        long scheduleId = Long.parseLong(
            tableModel.getValueAt(
                selectedRow,
                0
            ).toString()
        );

        String trainNumber =
            tableModel.getValueAt(
                selectedRow,
                1
            ).toString();

        String journeyDate =
            tableModel.getValueAt(
                selectedRow,
                5
            ).toString();

        int answer = JOptionPane.showConfirmDialog(
            this,
            "Cancel schedule " + scheduleId
                + " for train " + trainNumber
                + " on " + journeyDate + "?",
            "Confirm Cancellation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = """
            UPDATE schedules
            SET status = 'CANCELLED'
            WHERE schedule_id = ?
              AND status = 'SCHEDULED'
            """;

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setLong(1, scheduleId);

            int changedRows =
                statement.executeUpdate();

            if (changedRows > 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Schedule cancelled successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                loadSchedules();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "The schedule was already changed.",
                    "Cancellation Failed",
                    JOptionPane.WARNING_MESSAGE
                );

                loadSchedules();
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not cancel the schedule:\n"
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
