package com.trainreservation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PassengerDetailsFrame extends JFrame {

    private final JFrame previousFrame;
    private final long customerId;
    private final long scheduleId;

    private final JTextField nameField = new JTextField();
    private final JTextField nicField = new JTextField();
    private final JTextField ageField = new JTextField();

    private final JComboBox<String> genderBox =
        new JComboBox<>(new String[]{
            "Male",
            "Female",
            "Other"
        });

    private final JComboBox<String> classBox =
        new JComboBox<>(new String[]{
            "First Class",
            "Second Class"
        });

    private final DefaultTableModel tableModel =
        new DefaultTableModel(
            new String[]{
                "Passenger Name",
                "NIC / Passport",
                "Age",
                "Gender",
                "Class"
            },
            0
        ) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

    private final JTable passengerTable =
        new JTable(tableModel);

    public PassengerDetailsFrame(
        JFrame previousFrame,
        long customerId,
        long scheduleId
    ) {
        this.previousFrame = previousFrame;
        this.customerId = customerId;
        this.scheduleId = scheduleId;

        setTitle("Passenger Details");
        setSize(950, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setResizable(false);

        createInterface();

        ((AbstractDocument) ageField.getDocument())
            .setDocumentFilter(new NumberFilter());

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(
                java.awt.event.WindowEvent event
            ) {
                returnToPreviousPage();
            }
        });
    }

    private void createInterface() {
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBackground(new Color(238, 244, 250));
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel(
            "Passenger Details",
            SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(15, 75, 140));

        mainPanel.add(title, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(
                new Color(210, 220, 232)
            ),
            new EmptyBorder(18, 25, 18, 25)
        ));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(7, 8, 7, 8);

        addInput(formPanel, c, "Passenger Name", nameField, 0);
        addInput(formPanel, c, "NIC / Passport", nicField, 1);
        addInput(formPanel, c, "Age", ageField, 2);
        addInput(formPanel, c, "Gender", genderBox, 3);
        addInput(formPanel, c, "Seat Class", classBox, 4);

        JButton addButton = makeButton("ADD PASSENGER");
        JButton clearButton = makeButton("CLEAR");

        JPanel formButtons = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 12, 0)
        );

        formButtons.setOpaque(false);
        formButtons.add(addButton);
        formButtons.add(clearButton);

        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        c.insets = new Insets(15, 8, 3, 8);
        formPanel.add(formButtons, c);

        passengerTable.setRowHeight(29);
        passengerTable.setSelectionMode(
            ListSelectionModel.SINGLE_SELECTION
        );
        passengerTable.setFont(
            new Font("Arial", Font.PLAIN, 13)
        );

        passengerTable.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );

        passengerTable.getTableHeader().setBackground(
            new Color(15, 75, 140)
        );

        passengerTable.getTableHeader().setForeground(
            Color.WHITE
        );

        JScrollPane scrollPane =
            new JScrollPane(passengerTable);

        JButton removeButton =
            makeButton("REMOVE SELECTED");

        JButton continueButton =
            makeButton("CONTINUE");

        JButton backButton =
            makeButton("BACK");

        JPanel bottomButtons = new JPanel(
            new FlowLayout(FlowLayout.RIGHT, 12, 0)
        );

        bottomButtons.setOpaque(false);
        bottomButtons.add(removeButton);
        bottomButtons.add(backButton);
        bottomButtons.add(continueButton);

        JPanel content = new JPanel(new BorderLayout(12, 12));
        content.setOpaque(false);
        content.add(formPanel, BorderLayout.NORTH);
        content.add(scrollPane, BorderLayout.CENTER);
        content.add(bottomButtons, BorderLayout.SOUTH);

        mainPanel.add(content, BorderLayout.CENTER);
        setContentPane(mainPanel);

        addButton.addActionListener(
            event -> addPassenger()
        );

        clearButton.addActionListener(
            event -> clearFields()
        );

        removeButton.addActionListener(
            event -> removePassenger()
        );

        backButton.addActionListener(
            event -> returnToPreviousPage()
        );

        continueButton.addActionListener(
            event -> continueToConfirmation()
        );
    }

    private void addInput(
        JPanel panel,
        GridBagConstraints c,
        String labelText,
        JComponent input,
        int row
    ) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 13));

        input.setPreferredSize(new Dimension(350, 37));
        input.setFont(new Font("Arial", Font.PLAIN, 13));

        c.gridy = row;
        c.gridwidth = 1;
        c.gridx = 0;
        c.weightx = 0;
        panel.add(label, c);

        c.gridx = 1;
        c.weightx = 1;
        panel.add(input, c);
    }

    private JButton makeButton(String text) {
        JButton button = new JButton(text);

        button.setUI(new BasicButtonUI());
        button.setPreferredSize(new Dimension(165, 40));
        button.setBackground(new Color(25, 105, 195));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(
            Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
        );

        return button;
    }

    private void addPassenger() {
        String name = nameField.getText().trim();
        String nic = nicField.getText().trim();
        String ageText = ageField.getText().trim();

        if (
            name.isEmpty()
                || nic.isEmpty()
                || ageText.isEmpty()
        ) {
            JOptionPane.showMessageDialog(
                this,
                "Complete all passenger details.",
                "Missing Information",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int age = Integer.parseInt(ageText);

        if (age < 1 || age > 120) {
            JOptionPane.showMessageDialog(
                this,
                "Enter a valid passenger age.",
                "Invalid Age",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        for (int row = 0; row < tableModel.getRowCount(); row++) {
            String savedNic =
                tableModel.getValueAt(row, 1).toString();

            if (savedNic.equalsIgnoreCase(nic)) {
                JOptionPane.showMessageDialog(
                    this,
                    "This NIC or passport number is already added.",
                    "Duplicate Passenger",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
        }

        tableModel.addRow(new Object[]{
            name,
            nic,
            age,
            genderBox.getSelectedItem(),
            classBox.getSelectedItem()
        });

        clearFields();
    }

    private void removePassenger() {
        int selectedRow =
            passengerTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Select a passenger from the table.",
                "No Passenger Selected",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        tableModel.removeRow(selectedRow);
    }

    private void clearFields() {
        nameField.setText("");
        nicField.setText("");
        ageField.setText("");
        genderBox.setSelectedIndex(0);
        classBox.setSelectedIndex(0);
        nameField.requestFocus();
    }

    private void continueToConfirmation() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(
                this,
                "Add at least one passenger.",
                "No Passengers",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        List<Passenger> passengers = new ArrayList<>();

        for (int row = 0; row < tableModel.getRowCount(); row++) {
            passengers.add(new Passenger(
                tableModel.getValueAt(row, 0).toString(),
                tableModel.getValueAt(row, 1).toString(),
                Integer.parseInt(
                    tableModel.getValueAt(row, 2).toString()
                ),
                tableModel.getValueAt(row, 3).toString(),
                tableModel.getValueAt(row, 4).toString()
            ));
        }

        JOptionPane.showMessageDialog(
            this,
            passengers.size()
                + " passenger(s) added.\n"
                + "Booking Confirmation page will open next.",
            "Passenger Details Complete",
            JOptionPane.INFORMATION_MESSAGE
        );

        System.out.println(
            "Customer: " + customerId
                + ", Schedule: " + scheduleId
        );

        // BookingConfirmationFrame will be connected next.
    }

    private void returnToPreviousPage() {
        dispose();

        if (previousFrame != null) {
            previousFrame.setVisible(true);
            previousFrame.toFront();
        }
    }

    public static class Passenger {

        private final String name;
        private final String nic;
        private final int age;
        private final String gender;
        private final String seatClass;

        public Passenger(
            String name,
            String nic,
            int age,
            String gender,
            String seatClass
        ) {
            this.name = name;
            this.nic = nic;
            this.age = age;
            this.gender = gender;
            this.seatClass = seatClass;
        }

        public String getName() {
            return name;
        }

        public String getNic() {
            return nic;
        }

        public int getAge() {
            return age;
        }

        public String getGender() {
            return gender;
        }

        public String getSeatClass() {
            return seatClass;
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
}

