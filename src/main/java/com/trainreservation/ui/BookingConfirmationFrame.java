package com.trainreservation.ui;

import com.trainreservation.util.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;

public class BookingConfirmationFrame extends JFrame {

    public BookingConfirmationFrame() {
        setTitle("Booking Confirmation");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel title = new JLabel(
            "Booking Confirmation",
            SwingConstants.CENTER
        );
        title.setFont(new Font("Arial", Font.BOLD, 26));

        add(title, BorderLayout.NORTH);
    }

    private Connection getDatabaseConnection() throws Exception {
        return DatabaseConnection.getConnection();
    }
}