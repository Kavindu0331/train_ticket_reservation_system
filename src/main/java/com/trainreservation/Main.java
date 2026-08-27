package com.trainreservation;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminLoginFrame().setVisible(true);
        });
    }
}