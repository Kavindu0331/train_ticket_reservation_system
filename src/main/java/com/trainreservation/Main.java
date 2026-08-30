package com.trainreservation;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
            );
        } catch (Exception ignored) {
        }

        SwingUtilities.invokeLater(
            () -> {
                LoginFrame loginFrame =
                    new LoginFrame();

                loginFrame.setVisible(true);
            }
        );
    }
}