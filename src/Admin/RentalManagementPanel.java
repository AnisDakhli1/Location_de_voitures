/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin;

import Voiture.Location;
import Voiture.LocationDataBaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RentalManagementPanel extends JPanel {
    private JTable rentalTable;
    private DefaultTableModel tableModel;
    private LocationDataBaseConnection rentalDB;

    public RentalManagementPanel() {
        rentalDB = new LocationDataBaseConnection();
        setLayout(new BorderLayout());
        
        // Create table model
        String[] columns = {"ID", "User ID", "Car ID", "Start Date", "End Date", "Total Cost", "Returned"};
        tableModel = new DefaultTableModel(columns, 0);
        
        // Create table with increased row height
        rentalTable = new JTable(tableModel);
        rentalTable.setFont(new Font("Arial", Font.PLAIN, 18));
        rentalTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        rentalTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(rentalTable);
        
        // Create buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addButton = new JButton("Add Rental");
        JButton returnButton = new JButton("Mark as Returned");
        JButton deleteButton = new JButton("Delete Rental");
        JButton refreshButton = new JButton("Refresh");
        
        // Set button fonts
        Font buttonFont = new Font("Arial", Font.PLAIN, 18);
        addButton.setFont(buttonFont);
        returnButton.setFont(buttonFont);
        deleteButton.setFont(buttonFont);
        refreshButton.setFont(buttonFont);
        
        buttonPanel.add(addButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        // Add action listeners
        addButton.addActionListener(e -> showAddRentalDialog());
        returnButton.addActionListener(e -> markAsReturned());
        deleteButton.addActionListener(e -> deleteSelectedRental());
        refreshButton.addActionListener(e -> refreshRentalTable());
        
        // Add components to panel
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load initial data
        refreshRentalTable();
    }
    
    private void refreshRentalTable() {
        tableModel.setRowCount(0);
        List<Location> rentals = rentalDB.getAllRentals();
        
        for (Location rental : rentals) {
            Object[] row = {
                rental.getId(),
                rental.getUserId(),
                rental.getCarId(),
                rental.getStartDate(),
                rental.getEndDate(),
                rental.getTotalCost(),
                rental.isReturned() ? "Yes" : "No"
            };
            tableModel.addRow(row);
        }
    }
    
    private void showAddRentalDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add New Rental");
        dialog.setSize(600, 500);
        dialog.setModal(true);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        JTextField userIdField = new JTextField();
        JTextField carIdField = new JTextField();
        JTextField startDateField = new JTextField();
        JTextField endDateField = new JTextField();
        JTextField totalCostField = new JTextField();

        // Set fonts for form components
        Font formFont = new Font("Arial", Font.PLAIN, 18);
        userIdField.setFont(formFont);
        carIdField.setFont(formFont);
        startDateField.setFont(formFont);
        endDateField.setFont(formFont);
        totalCostField.setFont(formFont);

        formPanel.add(new JLabel("User ID:"));
        formPanel.add(userIdField);
        formPanel.add(new JLabel("Car ID:"));
        formPanel.add(carIdField);
        formPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        formPanel.add(startDateField);
        formPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        formPanel.add(endDateField);
        formPanel.add(new JLabel("Total Cost:"));
        formPanel.add(totalCostField);

        // Set label fonts
        for (Component comp : formPanel.getComponents()) {
            if (comp instanceof JLabel) {
                ((JLabel) comp).setFont(formFont);
            }
        }

        JButton saveButton = new JButton("Save");
        saveButton.setFont(formFont);
        saveButton.addActionListener(e -> {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date startDate = dateFormat.parse(startDateField.getText());
                Date endDate = dateFormat.parse(endDateField.getText());

                Location newRental = new Location(
                    0,
                    Integer.parseInt(userIdField.getText()),
                    Integer.parseInt(carIdField.getText()),
                    startDate,
                    endDate,
                    Double.parseDouble(totalCostField.getText()),
                    false
                );

                if (rentalDB.addRental(newRental)) {
                    JOptionPane.showMessageDialog(dialog, "Rental added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshRentalTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add rental.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for user ID, car ID, and total cost.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
    
    private void markAsReturned() {
        int selectedRow = rentalTable.getSelectedRow();
        if (selectedRow >= 0) {
            int rentalId = (int) tableModel.getValueAt(selectedRow, 0);
            if (rentalDB.returnCar(rentalId)) {
                refreshRentalTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to mark rental as returned.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a rental to mark as returned.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteSelectedRental() {
        int selectedRow = rentalTable.getSelectedRow();
        if (selectedRow >= 0) {
            int rentalId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this rental?", 
                    "Confirm Deletion", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (rentalDB.deleteRental(rentalId)) {
                    refreshRentalTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete rental.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a rental to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
}