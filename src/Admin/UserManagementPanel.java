/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Admin;

import User.User;
import User.UserDataBaseConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;

public class UserManagementPanel extends JPanel {
    private JTable userTable;
    private DefaultTableModel tableModel;
    private UserDataBaseConnection userDB;

    public UserManagementPanel() {
        userDB = new UserDataBaseConnection();
        setLayout(new BorderLayout());
        
        // Create table model
        String[] columns = {"ID", "First Name", "Last Name", "ID Card", "Email", "Admin"};
        tableModel = new DefaultTableModel(columns, 0);
        
        // Create table with increased row height
        userTable = new JTable(tableModel);
        userTable.setFont(new Font("Arial", Font.PLAIN, 18));
        userTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 18));
        userTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(userTable);
        
        // Create buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton addButton = new JButton("Add User");
        JButton editButton = new JButton("Edit User");
        JButton deleteButton = new JButton("Delete User");
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
        addButton.addActionListener(e -> showAddUserDialog());
        editButton.addActionListener(e -> editSelectedUser());
        deleteButton.addActionListener(e -> deleteSelectedUser());
        refreshButton.addActionListener(e -> refreshUserTable());
        
        // Add components to panel
        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Load initial data
        refreshUserTable();
    }
    
    private void refreshUserTable() {
        tableModel.setRowCount(0);
        List<User> users = userDB.getAllUsers();
        
        for (User user : users) {
            Object[] row = {
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getIdCardNumber(),
                user.getEmail(),
                user.isAdmin() ? "Yes" : "No"
            };
            tableModel.addRow(row);
        }
    }
    
    private void showAddUserDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Add New User");
        dialog.setSize(600, 500);
        dialog.setModal(true);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField idCardField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JCheckBox adminCheckbox = new JCheckBox("Is Admin");

        // Set fonts for form components
        Font formFont = new Font("Arial", Font.PLAIN, 18);
        firstNameField.setFont(formFont);
        lastNameField.setFont(formFont);
        idCardField.setFont(formFont);
        emailField.setFont(formFont);
        passwordField.setFont(formFont);
        adminCheckbox.setFont(formFont);

        formPanel.add(new JLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("ID Card Number:"));
        formPanel.add(idCardField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);
        formPanel.add(new JLabel("Admin Privileges:"));
        formPanel.add(adminCheckbox);

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
                Date registrationDate = new Date();

                User newUser = new User(
                    0,
                    firstNameField.getText(),
                    lastNameField.getText(),
                    idCardField.getText(),
                    emailField.getText(),
                    registrationDate,
                    new String(passwordField.getPassword()),
                    adminCheckbox.isSelected()
                );

                if (userDB.addUser(newUser)) {
                    JOptionPane.showMessageDialog(dialog, "User added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshUserTable();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(saveButton, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void editSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            User user = userDB.getUserById(userId);

            if (user != null) {
                JDialog dialog = new JDialog();
                dialog.setTitle("Edit User");
                dialog.setSize(600, 500);
                dialog.setModal(true);

                JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

                JTextField firstNameField = new JTextField(user.getFirstName());
                JTextField lastNameField = new JTextField(user.getLastName());
                JTextField idCardField = new JTextField(user.getIdCardNumber());
                JTextField emailField = new JTextField(user.getEmail());
                JPasswordField passwordField = new JPasswordField();
                JCheckBox adminCheckbox = new JCheckBox("Is Admin", user.isAdmin());

                // Set fonts for form components
                Font formFont = new Font("Arial", Font.PLAIN, 18);
                firstNameField.setFont(formFont);
                lastNameField.setFont(formFont);
                idCardField.setFont(formFont);
                emailField.setFont(formFont);
                passwordField.setFont(formFont);
                adminCheckbox.setFont(formFont);

                formPanel.add(new JLabel("First Name:"));
                formPanel.add(firstNameField);
                formPanel.add(new JLabel("Last Name:"));
                formPanel.add(lastNameField);
                formPanel.add(new JLabel("ID Card Number:"));
                formPanel.add(idCardField);
                formPanel.add(new JLabel("Email:"));
                formPanel.add(emailField);
                formPanel.add(new JLabel("Password (leave blank to keep current):"));
                formPanel.add(passwordField);
                formPanel.add(new JLabel("Admin Privileges:"));
                formPanel.add(adminCheckbox);

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
                        user.setFirstName(firstNameField.getText());
                        user.setLastName(lastNameField.getText());
                        user.setIdCardNumber(idCardField.getText());
                        user.setEmail(emailField.getText());
                        if (passwordField.getPassword().length > 0) {
                            user.setPassword(new String(passwordField.getPassword()));
                        }
                        user.setAdmin(adminCheckbox.isSelected());

                        if (userDB.updateUser(user)) {
                            JOptionPane.showMessageDialog(dialog, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                            refreshUserTable();
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "Failed to update user.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });

                dialog.add(formPanel, BorderLayout.CENTER);
                dialog.add(saveButton, BorderLayout.SOUTH);
                dialog.setVisible(true);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow >= 0) {
            int userId = (int) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this user?", 
                    "Confirm Deletion", 
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (userDB.deleteUser(userId)) {
                    refreshUserTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
        }
    }
}