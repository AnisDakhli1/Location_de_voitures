/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Anis
 */
package Admin;

import User.User;
import Voiture.VoitureDataBaseConnection;
import User.UserDataBaseConnection;
import Voiture.Location;
import Voiture.LocationDataBaseConnection;
import Voiture.Voiture;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Comparator;
import javax.swing.table.DefaultTableModel;

public class DashboardPanel extends JPanel {
    private VoitureDataBaseConnection carDB;
    private UserDataBaseConnection userDB;
    private LocationDataBaseConnection rentalDB;
    
    public DashboardPanel() {
        carDB = new VoitureDataBaseConnection();
        userDB = new UserDataBaseConnection();
        rentalDB = new LocationDataBaseConnection();
        
        setLayout(new BorderLayout());
        
        // Create welcome label
        JLabel welcomeLabel = new JLabel("Admin Dashboard", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        // Create main panel with statistics and top lists
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        
        // Create statistics panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        
        // Get data from database
        int totalUsers = userDB.getAllUsers().size();
        int availableCars = carDB.getAvailableCars().size();
        int activeRentals = getActiveRentalsCount();
        double totalRevenue = getTotalRevenue();
        
        // Add statistic cards
            statsPanel.add(createStatCard("Total Users", String.valueOf(totalUsers)));
            statsPanel.add(createStatCard("Available Cars", String.valueOf(availableCars)));
            statsPanel.add(createStatCard("Active Rentals", String.valueOf(activeRentals)));
            statsPanel.add(createStatCard("Total Revenue", String.format("$%.2f", totalRevenue)));
        
        // Create top lists panel
        JPanel topListsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        
        // Add top users panel
        topListsPanel.add(createTopUsersPanel());
        
        // Add top cars panel
        topListsPanel.add(createTopCarsPanel());
        
        // Add components to main panel
        mainPanel.add(statsPanel);
        mainPanel.add(topListsPanel);
        
        // Add components to panel
        add(welcomeLabel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private int getActiveRentalsCount() {
        try {
            List<Location> rentals = rentalDB.getAllRentals();
            int count = 0;
            for (Location rental : rentals) {
                if (rental != null && !rental.isReturned()) {
                    count++;
                }
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    private double getTotalRevenue() {
        try {
            List<Location> rentals = rentalDB.getAllRentals();
            double total = 0;
            for (Location rental : rentals) {
                if (rental != null) {
                    total += rental.getTotalCost();
                }
            }
            return total;
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }
    private Map<Voiture, Integer> calculateCarRentals() {
        Map<Voiture, Integer> carRentals = new HashMap<>();
        try {
            List<Location> rentals = rentalDB.getAllRentals();
            List<Voiture> cars = carDB.getAllCars();
            
            // Initialize all cars with 0 rentals
            for (Voiture car : cars) {
                if (car != null) {
                    carRentals.put(car, 0);
                }
            }
            
            // Count rentals for each car
            for (Location rental : rentals) {
                if (rental != null) {
                    Voiture car = carDB.getCarById(rental.getCarId());
                    if (car != null) {
                        carRentals.merge(car, 1, Integer::sum);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return carRentals;
    }
    
private Map<User, Double> calculateUserSpending() {
    Map<User, Double> userSpending = new HashMap<>();
    try {
        List<Location> rentals = rentalDB.getAllRentals();
        
        // Calculate total spending for each user
        for (Location rental : rentals) {
            if (rental != null) {
                User user = userDB.getUserById(rental.getUserId());
                if (user != null) {
                    // Sum up all rentals for this user
                    double currentTotal = userSpending.getOrDefault(user, 0.0);
                    userSpending.put(user, currentTotal + rental.getTotalCost());
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return userSpending;
}

private JPanel createTopUsersPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Top Spenders"));

    try {
        Map<User, Double> userSpending = calculateUserSpending();
        
        // Convert to list and sort by total spent (descending)
        List<Map.Entry<User, Double>> sortedUsers = new ArrayList<>(userSpending.entrySet());
        sortedUsers.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // Create table
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Rank", "User", "Total Spent"}, 0);

        int rank = 1;
        for (Map.Entry<User, Double> entry : sortedUsers) {
            User user = entry.getKey();
            if (user != null && entry.getValue() > 0) {
                model.addRow(new Object[]{
                    rank++,
                    user.getFirstName() + " " + user.getLastName(),
                    String.format("$%.2f", entry.getValue())
                });
            }
        }

        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

    } catch (Exception e) {
        e.printStackTrace();
        panel.add(new JLabel("Error loading data", SwingConstants.CENTER));
    }
    return panel;
}
private Map<Voiture, Double> calculateCarRevenue() {
    Map<Voiture, Double> carRevenue = new HashMap<>();
    try {
        List<Location> rentals = rentalDB.getAllRentals();
        
        // Calculate total revenue for each car
        for (Location rental : rentals) {
            if (rental != null) {
                Voiture car = carDB.getCarById(rental.getCarId());
                if (car != null) {
                    // Sum up all rentals for this car
                    double currentTotal = carRevenue.getOrDefault(car, 0.0);
                    carRevenue.put(car, currentTotal + rental.getTotalCost());
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return carRevenue;
}

private JPanel createTopCarsPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBorder(BorderFactory.createTitledBorder("Top Revenue Cars"));

    try {
        Map<Voiture, Double> carRevenue = calculateCarRevenue();
        
        // Convert to list and sort by revenue (descending)
        List<Map.Entry<Voiture, Double>> sortedCars = new ArrayList<>(carRevenue.entrySet());
        sortedCars.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // Create table
        DefaultTableModel model = new DefaultTableModel(
            new String[]{"Car", "Total Revenue"}, 0);

        for (Map.Entry<Voiture, Double> entry : sortedCars) {
            Voiture car = entry.getKey();
            if (car != null && entry.getValue() > 0) {
                model.addRow(new Object[]{
                    car.getMake() + " " + car.getModel(),
                    String.format("$%.2f", entry.getValue())
                });
            }
        }

        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);

    } catch (Exception e) {
        e.printStackTrace();
        panel.add(new JLabel("Error loading data", SwingConstants.CENTER));
    }
    return panel;
}
    
    private JPanel createStatCard(String title, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        card.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 24));
        valueLabel.setForeground(new Color(0, 100, 200));
        
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        
        return card;
    }
}