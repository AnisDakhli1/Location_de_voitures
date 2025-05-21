/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Anis
 */
package Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Menu_SingUp.LogIn;

public class AdminPanel extends JFrame {
    private JTabbedPane tabbedPane;

    public AdminPanel() {
        setTitle("Car Rental System - Admin Panel");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Set default font sizes
        UIManager.put("Label.font", new Font("Arial", Font.PLAIN, 18));
        UIManager.put("Button.font", new Font("Arial", Font.PLAIN, 18));
        UIManager.put("TabbedPane.font", new Font("Arial", Font.PLAIN, 18));
        UIManager.put("Table.font", new Font("Arial", Font.PLAIN, 18));
        UIManager.put("TableHeader.font", new Font("Arial", Font.BOLD, 18));
        
        // Create tabbed pane
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 18));
        
        // Add tabs
        tabbedPane.addTab("Dashboard", new DashboardPanel());
        tabbedPane.addTab("Users", new UserManagementPanel());
        tabbedPane.addTab("Cars", new CarManagementPanel());
        tabbedPane.addTab("Rentals", new RentalManagementPanel());
        
        // Add logout button to the bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 18));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new LogIn().setVisible(true);
            }
        });
        bottomPanel.add(logoutButton);
        
        // Add components to frame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AdminPanel().setVisible(true);
        });
    }
}