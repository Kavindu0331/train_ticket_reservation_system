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

public class ViewTrainsFrame extends JFrame {

    private final JFrame dashboard;
    private final DefaultTableModel tableModel;
    private final JTable trainTable;

    public ViewTrainsFrame(JFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("View Trains");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 20));
        mainPanel.setBackground(new Color(240, 245, 250));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JPanel headingPanel = new JPanel(new BorderLayout());
        headingPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Train List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(new Color(15, 67, 125));

        JLabel descriptionLabel = new JLabel(
            "All trains available in the system"
        );

        descriptionLabel.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        descriptionLabel.setForeground(
            new Color(90, 100, 110)
        );

        headingPanel.add(titleLabel, BorderLayout.NORTH);
        headingPanel.add(descriptionLabel, BorderLayout.SOUTH);

        String[] columns = {
            "Train ID",
            "Train Number",
            "Train Name",
            "Total Seats",
            "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        trainTable = new JTable(tableModel);
        trainTable.setFont(new Font("Arial", Font.PLAIN, 14));
        trainTable.setRowHeight(34);
        trainTable.setFillsViewportHeight(true);
        trainTable.setShowVerticalLines(false);
        trainTable.setGridColor(new Color(220, 228, 238));

        trainTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );

        trainTable.setSelectionBackground(
            new Color(205, 225, 245)
        );

        trainTable.setSelectionForeground(
            new Color(25, 35, 45)
        );

        trainTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        trainTable.getTableHeader().setBackground(
            new Color(15, 67, 125)
        );

        trainTable.getTableHeader().setForeground(Color.WHITE);

        trainTable.getTableHeader().setPreferredSize(
            new Dimension(0, 38)
        );

        DefaultTableCellRenderer center =
            new DefaultTableCellRenderer();

        center.setHorizontalAlignment(SwingConstants.CENTER);

        trainTable.getColumnModel()
            .getColumn(0)
            .setCellRenderer(center);

        trainTable.getColumnModel()
            .getColumn(1)
            .setCellRenderer(center);

        trainTable.getColumnModel()
            .getColumn(3)
            .setCellRenderer(center);

        trainTable.getColumnModel()
            .getColumn(4)
            .setCellRenderer(center);

        trainTable.getColumnModel()
            .getColumn(0)
            .setPreferredWidth(70);

        trainTable.getColumnModel()
            .getColumn(1)
            .setPreferredWidth(120);

        trainTable.getColumnModel()
            .getColumn(2)
            .setPreferredWidth(250);

        trainTable.getColumnModel()
            .getColumn(3)
            .setPreferredWidth(100);

        trainTable.getColumnModel()
            .getColumn(4)
            .setPreferredWidth(90);

        JScrollPane scrollPane = new JScrollPane(trainTable);

        scrollPane.setBorder(
            BorderFactory.createLineBorder(
                new Color(210, 220, 232)
            )
        );

        JButton refreshButton = makeButton("REFRESH");
        JButton backButton = makeButton("BACK");

        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 12, 0)
        );

        buttonPanel.setOpaque(false);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        mainPanel.add(headingPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        refreshButton.addActionListener(
            event -> loadTrains()
        );

        backButton.addActionListener(
            event -> returnToDashboard()
        );

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                returnToDashboard();
            }
        });

        loadTrains();
    }

    private JButton makeButton(String text) {
        JButton button = new JButton(text);

        button.setUI(new BasicButtonUI());
        button.setPreferredSize(new Dimension(125, 42));
        button.setBackground(new Color(21, 101, 192));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);

        button.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        return button;
    }

    private void loadTrains() {
        String sql = """
            SELECT train_id, train_number, train_name,
                   total_seats, active
            FROM trains
            ORDER BY train_id DESC
            """;

        tableModel.setRowCount(0);

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery()
        ) {
            while (result.next()) {
                String status;

                if (result.getBoolean("active")) {
                    status = "Active";
                } else {
                    status = "Inactive";
                }

                Object[] row = {
                    result.getLong("train_id"),
                    result.getString("train_number"),
                    result.getString("train_name"),
                    result.getInt("total_seats"),
                    status
                };

                tableModel.addRow(row);
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not load trains:\n"
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