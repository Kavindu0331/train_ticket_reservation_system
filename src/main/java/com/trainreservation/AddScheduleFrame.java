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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class AddScheduleFrame extends JFrame {

    private final JFrame dashboard;

    private final JComboBox<TrainItem> trainBox =
        new JComboBox<>();

    private final JComboBox<String> departureBox =
        new JComboBox<>();

    private final JComboBox<String> arrivalBox =
        new JComboBox<>();

    private final JTextField seatsField =
        new JTextField();

    private final JTextField fareField =
        new JTextField();

    private final JComboBox<String> statusBox =
        new JComboBox<>(
            new String[]{
                "SCHEDULED",
                "CANCELLED",
                "COMPLETED"
            }
        );

    private final JSpinner journeyDate;

    private final JSpinner departureTime;

    private final JSpinner arrivalTime;

    public AddScheduleFrame(JFrame dashboard) {
        this.dashboard = dashboard;

        setTitle("Add Schedule");
        setSize(700, 790);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        journeyDate = createDateSpinner();
        departureTime = createTimeSpinner(8, 0);
        arrivalTime = createTimeSpinner(11, 0);

        ((AbstractDocument) seatsField.getDocument())
            .setDocumentFilter(new NumberFilter());

        ((AbstractDocument) fareField.getDocument())
            .setDocumentFilter(new PriceFilter());

        JPanel panel = new JPanel(
            new GridBagLayout()
        );

        panel.setBackground(Color.WHITE);

        panel.setBorder(
            new EmptyBorder(25, 55, 25, 55)
        );

        GridBagConstraints c =
            new GridBagConstraints();

        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel(
            "Add New Schedule",
            SwingConstants.CENTER
        );

        title.setFont(
            new Font("Arial", Font.BOLD, 28)
        );

        title.setForeground(
            new Color(15, 67, 125)
        );

        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.insets = new Insets(5, 5, 22, 5);

        panel.add(title, c);

        addField(panel, c, "Train", trainBox, 1);

        addField(
            panel,
            c,
            "Departure Station",
            departureBox,
            2
        );

        addField(
            panel,
            c,
            "Arrival Station",
            arrivalBox,
            3
        );

        addField(
            panel,
            c,
            "Journey Date",
            journeyDate,
            4
        );

        addField(
            panel,
            c,
            "Departure Time",
            departureTime,
            5
        );

        addField(
            panel,
            c,
            "Arrival Time",
            arrivalTime,
            6
        );

        addField(
            panel,
            c,
            "Available Seats",
            seatsField,
            7
        );

        addField(
            panel,
            c,
            "Base Fare",
            fareField,
            8
        );

        addField(
            panel,
            c,
            "Status",
            statusBox,
            9
        );

        JButton saveButton = makeButton("SAVE");
        JButton clearButton = makeButton("CLEAR");
        JButton backButton = makeButton("BACK");

        JPanel buttonPanel = new JPanel(
            new FlowLayout(
                FlowLayout.CENTER,
                12,
                0
            )
        );

        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(saveButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(backButton);

        c.gridx = 0;
        c.gridy = 10;
        c.gridwidth = 2;
        c.insets = new Insets(25, 5, 5, 5);

        panel.add(buttonPanel, c);

        setContentPane(panel);

        trainBox.addActionListener(
            event -> showTrainSeats()
        );

        saveButton.addActionListener(
            event -> saveSchedule()
        );

        clearButton.addActionListener(
            event -> clearFields()
        );

        backButton.addActionListener(
            event -> returnToDashboard()
        );

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                returnToDashboard();
            }
        });

        loadStations();
        loadTrains();
    }

    private JSpinner createDateSpinner() {
        SpinnerDateModel model = new SpinnerDateModel();
        model.setValue(new Date());

        JSpinner spinner = new JSpinner(model);

        spinner.setEditor(
            new JSpinner.DateEditor(
                spinner,
                "yyyy-MM-dd"
            )
        );

        return spinner;
    }

    private JSpinner createTimeSpinner(
        int hour,
        int minute
    ) {
        java.util.Calendar calendar =
            java.util.Calendar.getInstance();

        calendar.set(
            java.util.Calendar.HOUR_OF_DAY,
            hour
        );

        calendar.set(
            java.util.Calendar.MINUTE,
            minute
        );

        calendar.set(
            java.util.Calendar.SECOND,
            0
        );

        SpinnerDateModel model =
            new SpinnerDateModel();

        model.setValue(calendar.getTime());

        JSpinner spinner =
            new JSpinner(model);

        spinner.setEditor(
            new JSpinner.DateEditor(
                spinner,
                "HH:mm"
            )
        );

        return spinner;
    }

    private void addField(
        JPanel panel,
        GridBagConstraints c,
        String text,
        JComponent field,
        int row
    ) {
        JLabel label = new JLabel(text);

        label.setFont(
            new Font("Arial", Font.BOLD, 14)
        );

        label.setForeground(
            new Color(35, 40, 50)
        );

        field.setPreferredSize(
            new Dimension(320, 40)
        );

        field.setFont(
            new Font("Arial", Font.PLAIN, 14)
        );

        c.gridx = 0;
        c.gridy = row;
        c.gridwidth = 1;
        c.weightx = 0;
        c.insets = new Insets(9, 5, 9, 20);

        panel.add(label, c);

        c.gridx = 1;
        c.weightx = 1;
        c.insets = new Insets(9, 5, 9, 5);

        panel.add(field, c);
    }

    private JButton makeButton(String text) {
        JButton button = new JButton(text);

        button.setUI(new BasicButtonUI());

        button.setPreferredSize(
            new Dimension(120, 42)
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
            SELECT station_name
            FROM stations
            WHERE active = TRUE
            ORDER BY station_name
            """;

        departureBox.removeAllItems();
        arrivalBox.removeAllItems();

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql);

            ResultSet result =
                statement.executeQuery()
        ) {
            while (result.next()) {
                String station =
                    result.getString("station_name");

                departureBox.addItem(station);
                arrivalBox.addItem(station);
            }

            if (departureBox.getItemCount() > 0) {
                departureBox.setSelectedIndex(0);
            }

            if (arrivalBox.getItemCount() > 1) {
                arrivalBox.setSelectedIndex(1);
            }

            if (departureBox.getItemCount() == 0) {
                JOptionPane.showMessageDialog(
                    this,
                    "No active stations were found.",
                    "No Stations",
                    JOptionPane.WARNING_MESSAGE
                );
            }
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not load stations:\n"
                    + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadTrains() {
        String sql = """
            SELECT train_id, train_number,
                   train_name, total_seats
            FROM trains
            WHERE active = TRUE
            ORDER BY train_number
            """;

        trainBox.removeAllItems();

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql);

            ResultSet result =
                statement.executeQuery()
        ) {
            while (result.next()) {
                trainBox.addItem(
                    new TrainItem(
                        result.getInt("train_id"),
                        result.getString("train_number"),
                        result.getString("train_name"),
                        result.getInt("total_seats")
                    )
                );
            }

            if (trainBox.getItemCount() > 0) {
                trainBox.setSelectedIndex(0);
                showTrainSeats();
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "No active trains were found.",
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

    private void showTrainSeats() {
        TrainItem train =
            (TrainItem) trainBox.getSelectedItem();

        if (train != null) {
            seatsField.setText(
                String.valueOf(train.totalSeats)
            );
        }
    }

    private void saveSchedule() {
        TrainItem train =
            (TrainItem) trainBox.getSelectedItem();

        String departure =
            (String) departureBox.getSelectedItem();

        String arrival =
            (String) arrivalBox.getSelectedItem();

        String seatsText =
            seatsField.getText().trim();

        String fareText =
            fareField.getText().trim();

        String status =
            (String) statusBox.getSelectedItem();

        if (
            train == null
                || departure == null
                || arrival == null
                || seatsText.isEmpty()
                || fareText.isEmpty()
        ) {
            JOptionPane.showMessageDialog(
                this,
                "Please complete all fields.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        if (departure.equalsIgnoreCase(arrival)) {
            JOptionPane.showMessageDialog(
                this,
                "Departure and arrival stations "
                    + "cannot be the same.",
                "Invalid Stations",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        int availableSeats;

        try {
            availableSeats =
                Integer.parseInt(seatsText);

            if (
                availableSeats <= 0
                    || availableSeats > train.totalSeats
            ) {
                JOptionPane.showMessageDialog(
                    this,
                    "Available seats must be between 1 and "
                        + train.totalSeats + ".",
                    "Invalid Seats",
                    JOptionPane.WARNING_MESSAGE
                );

                return;
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Available seats must be a valid number.",
                "Invalid Seats",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        double baseFare;

        try {
            baseFare = Double.parseDouble(fareText);

            if (baseFare <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Base fare must be a positive number.",
                "Invalid Fare",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        Date selectedDate =
            (Date) journeyDate.getValue();

        LocalDate date = selectedDate
            .toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate();

        if (date.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(
                this,
                "Journey date cannot be in the past.",
                "Invalid Date",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        LocalTime departureTimeValue =
            toLocalTime(
                (Date) departureTime.getValue()
            );

        LocalTime arrivalTimeValue =
            toLocalTime(
                (Date) arrivalTime.getValue()
            );

        if (
            !arrivalTimeValue.isAfter(
                departureTimeValue
            )
        ) {
            JOptionPane.showMessageDialog(
                this,
                "Arrival time must be after departure time.",
                "Invalid Time",
                JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        String sql = """
            INSERT INTO schedules (
                train_id,
                departure_station,
                arrival_station,
                journey_date,
                departure_time,
                arrival_time,
                available_seats,
                base_fare,
                status
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;

        try (
            Connection connection =
                DatabaseConnection.getConnection();

            PreparedStatement statement =
                connection.prepareStatement(sql)
        ) {
            statement.setInt(1, train.id);
            statement.setString(2, departure);
            statement.setString(3, arrival);

            statement.setDate(
                4,
                java.sql.Date.valueOf(date)
            );

            statement.setTime(
                5,
                java.sql.Time.valueOf(
                    departureTimeValue
                )
            );

            statement.setTime(
                6,
                java.sql.Time.valueOf(
                    arrivalTimeValue
                )
            );

            statement.setInt(7, availableSeats);
            statement.setDouble(8, baseFare);
            statement.setString(9, status);

            statement.executeUpdate();

            JOptionPane.showMessageDialog(
                this,
                "Schedule added successfully.",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            clearFields();
        } catch (SQLException exception) {
            JOptionPane.showMessageDialog(
                this,
                "Could not add the schedule:\n"
                    + exception.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private LocalTime toLocalTime(Date value) {
        return value.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalTime()
            .withSecond(0)
            .withNano(0);
    }

    private void clearFields() {
        if (trainBox.getItemCount() > 0) {
            trainBox.setSelectedIndex(0);
        }

        if (departureBox.getItemCount() > 0) {
            departureBox.setSelectedIndex(0);
        }

        if (arrivalBox.getItemCount() > 1) {
            arrivalBox.setSelectedIndex(1);
        }

        journeyDate.setValue(new Date());
        departureTime.setValue(
            createTimeValue(8, 0)
        );
        arrivalTime.setValue(
            createTimeValue(11, 0)
        );
        fareField.setText("");
        statusBox.setSelectedItem("SCHEDULED");

        showTrainSeats();
    }

    private Date createTimeValue(
        int hour,
        int minute
    ) {
        java.util.Calendar calendar =
            java.util.Calendar.getInstance();

        calendar.set(
            java.util.Calendar.HOUR_OF_DAY,
            hour
        );

        calendar.set(
            java.util.Calendar.MINUTE,
            minute
        );

        calendar.set(
            java.util.Calendar.SECOND,
            0
        );

        return calendar.getTime();
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

        private final int id;
        private final String number;
        private final String name;
        private final int totalSeats;

        private TrainItem(
            int id,
            String number,
            String name,
            int totalSeats
        ) {
            this.id = id;
            this.number = number;
            this.name = name;
            this.totalSeats = totalSeats;
        }

        public String toString() {
            return number + " - " + name;
        }
    }

    private static class NumberFilter
        extends DocumentFilter {

        public void insertString(
            FilterBypass filter,
            int offset,
            String text,
            AttributeSet attributes
        ) throws BadLocationException {
            if (
                text != null
                    && text.matches("\\d+")
            ) {
                filter.insertString(
                    offset,
                    text,
                    attributes
                );
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

    private static class PriceFilter
        extends DocumentFilter {

        public void insertString(
            FilterBypass filter,
            int offset,
            String text,
            AttributeSet attributes
        ) throws BadLocationException {
            replace(
                filter,
                offset,
                0,
                text,
                attributes
            );
        }

        public void replace(
            FilterBypass filter,
            int offset,
            int length,
            String text,
            AttributeSet attributes
        ) throws BadLocationException {
            String oldText =
                filter.getDocument().getText(
                    0,
                    filter.getDocument().getLength()
                );

            String insertedText =
                text == null ? "" : text;

            String newText =
                oldText.substring(0, offset)
                    + insertedText
                    + oldText.substring(offset + length);

            if (
                newText.matches(
                    "\\d*(\\.\\d{0,2})?"
                )
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

