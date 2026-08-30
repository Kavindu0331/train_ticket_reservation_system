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

public class AddTrainFrame extends JFrame {

    private final JFrame dashboard;
    private final JTextField numberField = new JTextField();
    private final JTextField nameField = new JTextField();
    private final JTextField seatsField = new JTextField();
    private final JCheckBox activeBox = new JCheckBox("Active", true);

    public AddTrainFrame(JFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("Add Train");
        setSize(600, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(30, 50, 30, 50));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel(
            "Add New Train",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(new Color(15, 67, 125));

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(5, 5, 20, 5);
        panel.add(title, c);

        numberField.setEditable(false);
        numberField.setBackground(new Color(235, 240, 245));

        addField(panel, c, "Train Number", numberField, 1);
        addField(panel, c, "Train Name", nameField, 2);
        addField(panel, c, "Total Seats", seatsField, 3);

        ((AbstractDocument) seatsField.getDocument())
            .setDocumentFilter(new NumberFilter());

        activeBox.setBackground(Color.WHITE);
        activeBox.setFont(new Font("Arial", Font.PLAIN, 13));
        activeBox.setFocusPainted(false);

        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 1;
        c.insets = new Insets(10, 5, 10, 5);
        panel.add(activeBox, c);

        JButton saveButton = makeButton("SAVE");
        JButton clearButton = makeButton("CLEAR");
        JButton backButton = makeButton("BACK");

        JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 12, 0)
        );

        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        c.insets = new Insets(25, 5, 5, 5);
        panel.add(buttonPanel, c);

        saveButton.addActionListener(event -> saveTrain());
        clearButton.addActionListener(event -> clearFields());
        backButton.addActionListener(event -> returnToDashboard());

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                returnToDashboard();
            }
        });

        setContentPane(panel);
        loadNextTrainNumber();
    }

    private void addField(
        JPanel panel,
        GridBagConstraints c,
        String text,
        JTextField field,
        int row
    ) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(35, 40, 50));

        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(300, 40));

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
        button.setPreferredSize(new Dimension(120, 42));
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

    private void loadNextTrainNumber() {
        String sql = """
            SELECT COALESCE(
                MAX(CAST(SUBSTRING(train_number, 3) AS UNSIGNED)),
                1000
            ) + 1 AS next_number
            FROM trains
            WHERE train_number REGEXP '^TR[0-9]+$'
            """;

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql);

            ResultSet result = statement.executeQuery()
        ) {
            if (result.next()) {
                int nextNumber = result.getInt("next_number");
                numberField.setText("TR" + nextNumber);
            }
        } catch (SQLException exception) {
            numberField.setText("Unavailable");

            JOptionPane.showMessageDialog(
                this,
                "Could not generate the train number:\n"
                    + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void saveTrain() {
        String trainNumber = numberField.getText();
        String trainName = nameField.getText().trim();
        String seatsText = seatsField.getText().trim();

        if (
            trainName.isEmpty()
                || seatsText.isEmpty()
                || trainNumber.equals("Unavailable")
        ) {
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
                JOptionPane.showMessageDialog(
                    this,
                    "Total seats must be greater than zero.",
                    "Invalid Seats",
                    JOptionPane.WARNING_MESSAGE
                );

                return;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Enter a valid number for total seats.",
                "Invalid Seats",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String sql = """
            INSERT INTO trains
                (train_number, train_name, total_seats, active)
            VALUES (?, ?, ?, ?)
            """;

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setString(1, trainNumber);
            statement.setString(2, trainName);
            statement.setInt(3, totalSeats);
            statement.setBoolean(4, activeBox.isSelected());

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                this,
                "Train " + trainNumber + " added successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();
        } catch (SQLIntegrityConstraintViolationException exception) {
            loadNextTrainNumber();

            JOptionPane.showMessageDialog(
                this,
                "A new train number was generated. Please save again.",
                "Train Number Exists",
                JOptionPane.WARNING_MESSAGE
            );
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not add the train:\n"
                    + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void clearFields() {
        nameField.setText("");
        seatsField.setText("");
        activeBox.setSelected(true);
        loadNextTrainNumber();
        nameField.requestFocusInWindow();
    }

    private void returnToDashboard() {
        dispose();

        if (dashboard != null) {
            dashboard.setVisible(true);
            dashboard.toFront();
            dashboard.requestFocus();
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