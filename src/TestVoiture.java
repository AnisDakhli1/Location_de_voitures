import BD.BD;
import Voiture.Voiture;
import Menu_SingUp.LogIn;
import Admin.AdminPanel;
import User.User;
import MainInterface.UserInterface;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.sql.Connection;
import java.sql.ResultSet; 


import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class TestVoiture {
    public static void main(String[] args) {
        // First show the login screen
        SwingUtilities.invokeLater(() -> {
            LogIn login = new LogIn();
            login.setVisible(true);
            
            // Set up login success listener
            login.setLoginSuccessListener(authenticatedUser -> {
                if (authenticatedUser.isAdmin()) {
                    SwingUtilities.invokeLater(() -> {
                        new AdminPanel().setVisible(true);
                    });
                } else {
                    SwingUtilities.invokeLater(() -> {
                        try {
                            new UserInterface().setVisible(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(null, 
                                "Error loading interface: " + e.getMessage(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        }
                    });
                }
            });
            
        });
    }
}