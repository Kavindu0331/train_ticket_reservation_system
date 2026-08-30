package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.*;

public class UpdateTrainFrame extends JFrame {

    private final JFrame dashboard;
    private final JComboBox<TrainItem> trainBox = new JComboBox<>();
    private final JTextField numberField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField seatsField = new JTextField();
    private final JCheckBox activeBox = new JCheckBox("Active");

    public UpdateTrainFrame(JFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("Update Train");
        setSize(630, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel(
            "Update Train",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(15, 67, 125));

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(5, 5, 22, 5);
        panel.add(title, c);

        addField(panel, c, "Select Train", trainBox, 1);
        addField(panel, c, "Train Number", numberField, 2);
        addField(panel, c, "Train Name", nameField, 3);
        addField(panel, c, "Total Seats", seatsField, 4);

        numberField.setEditable(false);
        numberField.setBackground(new Color(235, 240, 245));

        ((AbstractDocument) seatsField.getDocument())
            .setDocumentFilter(new NumberFilter());

        activeBox.setBackground(Color.WHITE);
        activeBox.setFont(new Font("Arial", Font.PLAIN, 14));
        activeBox.setFocusPainted(false);

        c.gridx = 1;
        c.gridy = 5;
        c.gridwidth = 1;
        c.insets = new Insets(10, 5, 10, 5);
        panel.add(activeBox, c);

        JButton updateButton = makeButton("UPDATE");
        JButton refreshButton = makeButton("REFRESH");
        JButton backButton = makeButton("BACK");

        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 12, 0)
        );

        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(updateButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(backButton);

        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 2;
        c.insets = new Insets(25, 5, 5, 5);
        panel.add(buttonPanel, c);

        setContentPane(panel);

        trainBox.addActionListener(event ->
            showSelectedTrain()
        );

        updateButton.addActionListener(event ->
            updateTrain()
        );

        refreshButton.addActionListener(event ->
            loadTrains()
        );

        backButton.addActionListener(event ->
            returnToDashboard()
        );

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                returnToDashboard();
            }
        });

        loadTrains();
    }

    private void addField(
        JPanel panel,
        GridBagConstraints c,
        String text,
        JComponent field,
        int row
    ) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(35, 40, 50));

        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(320, 40));

        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.weightx = 0;
        c.insets = new Insets(10, 5, 10, 20);
        panel.add(label, c);

        c.gridx = 1;
        c.weightx = 1;
        c.insets = new Insets(10, 5, 10, 5);
        panel.add(field, c);
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
            ORDER BY train_number
            """;

        trainBox.removeAllItems();

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery()
        ) {
            while (result.next()) {
                trainBox.addItem(
                    new TrainItem(
                        result.getLong("train_id"),
                        result.getString("train_number"),
                        result.getString("train_name"),
                        result.getInt("total_seats"),
                        result.getBoolean("active")
                    )
                );
            }

            if (trainBox.getItemCount() > 0) {
                trainBox.setSelectedIndex(0);
                showSelectedTrain();
            } else {
                clearFields();

                JOptionPane.showMessageDialog(
                    this,
                    "No trains were found.",
                    "No Trains",
                    JOptionPane.WARNING_MESSAGE
                );
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

    private void showSelectedTrain() {
        TrainItem train =
            (TrainItem) trainBox.getSelectedItem();

        if (train == null) {
            clearFields();
            return;
        }

        numberField.setText(train.number);
        nameField.setText(train.name);
        seatsField.setText(String.valueOf(train.totalSeats));
        activeBox.setSelected(train.active);
    }

    private void updateTrain() {
        TrainItem train =
            (TrainItem) trainBox.getSelectedItem();

        String trainName = nameField.getText().trim();
        String seatsText = seatsField.getText().trim();

        if (train == null) {
            JOptionPane.showMessageDialog(
                this,
                "Select a train first.",
                "No Train Selected",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (trainName.isEmpty() || seatsText.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Enter the train name and total seats.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int totalSeats;

        try {
            totalSeats = Integer.parseInt(seatsText);

            if (totalSeats <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Total seats must be greater than zero.",
                "Invalid Seats",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int answer = JOptionPane.showConfirmDialog(
            this,
            "Update train " + train.number + "?",
            "Confirm Update",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        String sql = """
            UPDATE trains
            SET train_name = ?,
                total_seats = ?,
                active = ?
            WHERE train_id = ?
            """;

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setString(1, trainName);
            statement.setInt(2, totalSeats);
            statement.setBoolean(3, activeBox.isSelected());
            statement.setLong(4, train.id);

            int changedRows = statement.executeUpdate();

            if (changedRows > 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "Train updated successfully.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );

                loadTrains();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "The train could not be found.",
                    "Update Failed",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not update the train:\n"
                    + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearFields() {
        numberField.setText("");
        nameField.setText("");
        seatsField.setText("");
        activeBox.setSelected(false);
    }

    private void returnToDashboard() {
        dispose();

        if (dashboard != null) {
            dashboard.setVisible(true);
            dashboard.toFront();
            dashboard.requestFocus();
        }
    }

    private static class TrainItem {

        private final long id;
        private final String number;
        private final String name;
        private final int totalSeats;
        private final boolean active;

        private TrainItem(
            long id,
            String number,
            String name,
            int totalSeats,
            boolean active
        ) {
            this.id = id;
            this.number = number;
            this.name = name;
            this.totalSeats = totalSeats;
            this.active = active;
        }

        public String toString() {
            return number + " - " + name;
        }
    }

    private static class NumberFilter extends DocumentFilter {

        public void insertString(
            FilterBypass filter,
            int offset,
            String text,
            AttributeSet attributes
        ) throws BadLocationException {
            if (text != null && text.matches("\\d+")) {
                filter.insertString(offset, text, attributes);
            }
        }

        public void replace(
            FilterBypass filter,
            int offset,
            int length,
            String text,
            AttributeSet attributes
        ) throws BadLocationException {
            if (
                text == null
                    || text.isEmpty()
                    || text.matches("\\d+")
            ) {
                filter.replace(
                    offset,
                    length,
                    text,
                    attributes
                );
            }
        }
    }
}