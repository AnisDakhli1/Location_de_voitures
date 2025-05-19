/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Voiture;

import BD.BD;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Anis
 */
public class VoitureDataBaseConnection {
    private final BD bd;
    
    public VoitureDataBaseConnection() {
        this.bd = new BD();
    }
   // Create a car
    public boolean addCar(Voiture voiture) {
        String query = "INSERT INTO cars (make, model, year, license_plate, color, daily_rate, available) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = bd.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, voiture.getMake());
            stmt.setString(2, voiture.getModel());
            stmt.setInt(3, voiture.getYear());
            stmt.setString(4, voiture.getLicensePlate());
            stmt.setString(5, voiture.getColor());
            stmt.setDouble(6, voiture.getDailyRate());
            stmt.setBoolean(7, voiture.isAvailable());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    voiture.setId(generatedKeys.getInt(1));
                }
            }
            
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding car: " + e.getMessage());
            return false;
        }
    }
    
    // Get car by ID
    public Voiture getCarById(int id) {
        String query = "SELECT * FROM cars WHERE id = ?";
        
        try (Connection conn = bd.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractCarFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving car: " + e.getMessage());
        }
        
        return null;
    }
    
    // Get all cars
    public List<Voiture> getAllCars() {
        List<Voiture> voitures = new ArrayList<>();
        String query = "SELECT * FROM cars";
        
        try (Connection conn = bd.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                voitures.add(extractCarFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving cars: " + e.getMessage());
        }
        
        return voitures;
    }
    
    // Get available cars
    public List<Voiture> getAvailableCars() {
        List<Voiture> cars = new ArrayList<>();
        String query = "SELECT * FROM cars WHERE available = true";
        
        try (Connection conn = bd.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                cars.add(extractCarFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving available cars: " + e.getMessage());
        }
        
        return cars;
    }
    
    // Update car
    public boolean updateCar(Voiture voiture) {
        String query = "UPDATE cars SET make = ?, model = ?, year = ?, license_plate = ?, " +
                       "color = ?, daily_rate = ?, available = ? WHERE id = ?";
        
        try (Connection conn = bd.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, voiture.getMake());
            stmt.setString(2, voiture.getModel());
            stmt.setInt(3, voiture.getYear());
            stmt.setString(4, voiture.getLicensePlate());
            stmt.setString(5, voiture.getColor());
            stmt.setDouble(6, voiture.getDailyRate());
            stmt.setBoolean(7, voiture.isAvailable());
            stmt.setInt(8, voiture.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating car: " + e.getMessage());
            return false;
        }
    }
    
    // Delete car
    public boolean deleteCar(int id) {
        String query = "DELETE FROM cars WHERE id = ?";
        
        try (Connection conn = bd.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;    
        } catch (SQLException e) {
            System.err.println("Error deleting car: " + e.getMessage());
            return false;
        }
    }
    
    // Update car availability
    public boolean updateCarAvailability(int carId, boolean available) {
        String query = "UPDATE cars SET available = ? WHERE id = ?";
        
        try (Connection conn = bd.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setBoolean(1, available);
            stmt.setInt(2, carId);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating car availability: " + e.getMessage());
            return false;
        }
    }
    
    // Helper method to map ResultSet to Car object
    private Voiture extractCarFromResultSet(ResultSet rs) throws SQLException {
        Voiture voiture = new Voiture();
        voiture.setId(rs.getInt("id"));
        voiture.setMake(rs.getString("make"));
        voiture.setModel(rs.getString("model"));
        voiture.setYear(rs.getInt("year"));
        voiture.setLicensePlate(rs.getString("license_plate"));
        voiture.setColor(rs.getString("color"));
        voiture.setDailyRate(rs.getDouble("daily_rate"));
        voiture.setAvailable(rs.getBoolean("available"));
        return voiture;
    }
 
}
