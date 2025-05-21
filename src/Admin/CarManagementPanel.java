/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin;

import Voiture.Voiture;
import Voiture.VoitureDataBaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CarManagementPanel extends JPanel {
    private JTable carTable;
    private DefaultTableModel tableModel;
    private VoitureDataBaseConnection carDB;

    public CarManagementPanel() {
        carDB = new VoitureDataBaseConnection();
        setLayout(new BorderLayout());
        
        // Create table model
        String[] columns = {"ID", "Make", "Model", "Year", "License Plate", "Color", "Daily Rate", "Available"};
        tableModel = new DefaultTableModel(columns, 0);
        
        // Create table with increased row height
        carTable = new JTable(tableModel);
        carTable.setFont(new Font("Arial", Font.PLAIN, 18));
        carTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        carTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(carTable);
        
        // Create buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addButton = new JButton("Add Car");
        JButton editButton = new JButton("Edit Car");
        JButton deleteButton = new JButton("Delete Car");
        JButton refreshButton = new JButton("Refresh");
        
        // Set button fonts
        Font buttonFont = new Font("Arial", Font.PLAIN, 18);
        addButton.setFont(buttonFont);
        editButton.setFont(buttonFont);
        deleteButton.setFont(buttonFont);
        refreshButton.setFont(buttonFont);
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        
        // Add action listeners
        addButton.addActionListener(e -> showAddCarDialog());
        editButton.addActionListener(e -> editSelectedCar());
        deleteButton.addActionListener(e -> deleteSelectedCar());
        refreshButton.addActionListener(e -> refreshCarTable());
        
        // Add components to panel
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load initial data
        refreshCarTable();
    }
    
    private void refreshCarTable() {
        tableModel.setRowCount(0);
        List<Voiture> cars = carDB.getAllCars();
        
        for (Voiture car : cars) {
            Object[] row = {
                car.getId(),
                car.getMake(),
                car.getModel(),
                car.getYear(),
                car.getLicensePlate(),
                car.getColor(),
                car.getDailyRate(),
                car.isAvailable() ? "Yes" : "No"
            };
            tableModel.addRow(row);
        }
    }
    
    private void showAddCarDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add New Car");
        dialog.setSize(600, 500);
        dialog.setModal(true);
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        
        JTextField makeField = new JTextField();
        JTextField modelField = new JTextField();
        JTextField yearField = new JTextField();
        JTextField licenseField = new JTextField();
        JTextField colorField = new JTextField();
        JTextField rateField = new JTextField();
        JCheckBox availableCheckbox = new JCheckBox("Available");
        
        // Set fonts for form components
        Font formFont = new Font("Arial", Font.PLAIN, 18);
        makeField.setFont(formFont);
        modelField.setFont(formFont);
        yearField.setFont(formFont);
        licenseField.setFont(formFont);
        colorField.setFont(formFont);
        rateField.setFont(formFont);
        availableCheckbox.setFont(formFont);
        
        formPanel.add(new JLabel("Make:"));
        formPanel.add(makeField);
        formPanel.add(new JLabel("Model:"));
        formPanel.add(modelField);
        formPanel.add(new JLabel("Year:"));
        formPanel.add(yearField);
        formPanel.add(new JLabel("License Plate:"));
        formPanel.add(licenseField);
        formPanel.add(new JLabel("Color:"));
        formPanel.add(colorField);
        formPanel.add(new JLabel("Daily Rate:"));
        formPanel.add(rateField);
        formPanel.add(new JLabel("Available:"));
        formPanel.add(availableCheckbox);
        
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
                Voiture newCar = new Voiture(
                    0,
                    makeField.getText(),
                    modelField.getText(),
                    Integer.parseInt(yearField.getText()),
                    licenseField.getText(),
                    colorField.getText(),
                    Double.parseDouble(rateField.getText()),
                    availableCheckbox.isSelected()
                );
                
                if (carDB.addCar(newCar)) {
                    JOptionPane.showMessageDialog(dialog, "Car added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshCarTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add car.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for year and daily rate.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void editSelectedCar() {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow >= 0) {
            int carId = (int) tableModel.getValueAt(selectedRow, 0);
            Voiture car = carDB.getCarById(carId);
            
            if (car != null) {
                JDialog dialog = new JDialog();
                dialog.setTitle("Edit Car");
                dialog.setSize(600, 500);
                dialog.setModal(true);
                
                JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
                
                JTextField makeField = new JTextField(car.getMake());
                JTextField modelField = new JTextField(car.getModel());
                JTextField yearField = new JTextField(String.valueOf(car.getYear()));
                JTextField licenseField = new JTextField(car.getLicensePlate());
                JTextField colorField = new JTextField(car.getColor());
                JTextField rateField = new JTextField(String.valueOf(car.getDailyRate()));
                JCheckBox availableCheckbox = new JCheckBox("Available", car.isAvailable());
                
                // Set fonts for form components
                Font formFont = new Font("Arial", Font.PLAIN, 18);
                makeField.setFont(formFont);
                modelField.setFont(formFont);
                yearField.setFont(formFont);
                licenseField.setFont(formFont);
                colorField.setFont(formFont);
                rateField.setFont(formFont);
                availableCheckbox.setFont(formFont);
                
                formPanel.add(new JLabel("Make:"));
                formPanel.add(makeField);
                formPanel.add(new JLabel("Model:"));
                formPanel.add(modelField);
                formPanel.add(new JLabel("Year:"));
                formPanel.add(yearField);
                formPanel.add(new JLabel("License Plate:"));
                formPanel.add(licenseField);
                formPanel.add(new JLabel("Color:"));
                formPanel.add(colorField);
                formPanel.add(new JLabel("Daily Rate:"));
                formPanel.add(rateField);
                formPanel.add(new JLabel("Available:"));
                formPanel.add(availableCheckbox);
                
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
                        car.setMake(makeField.getText());
                        car.setModel(modelField.getText());
                        car.setYear(Integer.parseInt(yearField.getText()));
                        car.setLicensePlate(licenseField.getText());
                        car.setColor(colorField.getText());
                        car.setDailyRate(Double.parseDouble(rateField.getText()));
                        car.setAvailable(availableCheckbox.isSelected());
                        
                        if (carDB.updateCar(car)) {
                            JOptionPane.showMessageDialog(dialog, "Car updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            refreshCarTable();
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Failed to update car.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for year and daily rate.", "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
                
                dialog.add(formPanel, BorderLayout.CENTER);
                dialog.add(saveButton, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a car to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteSelectedCar() {
        int selectedRow = carTable.getSelectedRow();
        if (selectedRow >= 0) {
            int carId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this car?", 
                    "Confirm Deletion", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (carDB.deleteCar(carId)) {
                    refreshCarTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete car.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a car to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
}