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
public class LocationDataBaseConnection {
    private final BD bd;
    
    public LocationDataBaseConnection(){
        this.bd = new BD();
    }

    /**
     *
     * @param userId
     * @return
     */
    /*public List<Location> getRentalsByUserId(int userId) {
    List<Location> rentals = new ArrayList<>();
    String query = "SELECT * FROM rentals WHERE user_id = ? ORDER BY start_date DESC";
    
    try (Connection conn = bd.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        
        stmt.setInt(1, userId);
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                rentals.add(extractRentalFromResultSet(rs));
            }
        }
    } catch (SQLException e) {
        System.err.println("Error retrieving rentals: " + e.getMessage());
    }
    
    return rentals;
}*/
    public boolean markAsReturned(int rentalId) {
        try {
            BD bd = new BD();
            Connection conn = bd.getConnection();
            String query = "UPDATE rentals SET returned = true WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, rentalId);
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
     // Create a rental
    public boolean addRental(Location location) {
        String query = "INSERT INTO rentals (user_id, car_id, start_date, end_date, total_cost, returned) " +
                       "VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = bd.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, location.getUserId());
            stmt.setInt(2, location.getCarId());
            stmt.setDate(3, new java.sql.Date(location.getStartDate().getTime()));
            stmt.setDate(4, new java.sql.Date(location.getEndDate().getTime()));
            stmt.setDouble(5, location.getTotalCost());
            stmt.setBoolean(6, location.isReturned());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                return false;
            }
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    location.setId(generatedKeys.getInt(1));
                }
            }
            
            // Update car availability
            VoitureDataBaseConnection voitureDataBaseConnection = new VoitureDataBaseConnection();
            voitureDataBaseConnection.updateCarAvailability(location.getCarId(), false);
            
            return true;
        } catch (SQLException e) {
            System.err.println("Error adding rental: " + e.getMessage());
            return false;
        }
    }
    
    // Get rental by ID
    public Location getRentalById(int id) {
        String query = "SELECT * FROM rentals WHERE id = ?";
        
        try (Connection conn = bd.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extractRentalFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving rental: " + e.getMessage());
        }
        
        return null;
    }
    
    // Get rentals by user ID
    public List<Location> getRentalsByUserId(int userId) {
        List<Location> rentals = new ArrayList<>();
        String query = "SELECT * FROM rentals WHERE user_id = ?";
        
        try (Connection conn = bd.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rentals.add(extractRentalFromResultSet(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving rentals: " + e.getMessage());
        }
        
        return rentals;
    }
    
    // Get all rentals
    public List<Location> getAllRentals() {
        List<Location> rentals = new ArrayList<>();
        String query = "SELECT * FROM rentals";
        
        try (Connection conn = bd.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                rentals.add(extractRentalFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving rentals: " + e.getMessage());
        }
        
        return rentals;
    }
       public boolean updateCarAvailability(int carId, boolean available) {
        String query = "UPDATE cars SET available = ? WHERE id = ?";
        try (Connection conn = bd.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query))
            {
            stmt.setBoolean(1, available);
            stmt.setInt(2, carId);
            int rowsAffected = stmt.executeUpdate();
            stmt.close();
            conn.close();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    // Update rental
    public boolean updateRental(Location location) {
        String query = "UPDATE rentals SET user_id = ?, car_id = ?, start_date = ?, " +
                       "end_date = ?, total_cost = ?, returned = ? WHERE id = ?";
        
        try (Connection conn = bd.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, location.getUserId());
            stmt.setInt(2, location.getCarId());
            stmt.setDate(3, new java.sql.Date(location.getStartDate().getTime()));
            stmt.setDate(4, new java.sql.Date(location.getEndDate().getTime()));
            stmt.setDouble(5, location.getTotalCost());
            stmt.setBoolean(6, location.isReturned());
            stmt.setInt(7, location.getId());
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error updating rental: " + e.getMessage());
            return false;
        }
    }
    
    // Return a car
    public boolean returnCar(int rentalId) {
        String query = "UPDATE rentals SET returned = true WHERE id = ?";
        
        try (Connection conn = bd.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, rentalId);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                // Get the rental to update car availability
                Location location = getRentalById(rentalId);
                if (location != null) {
                    VoitureDataBaseConnection voitureDataBaseConnection = new VoitureDataBaseConnection();
                    voitureDataBaseConnection.updateCarAvailability(location.getCarId(), true);
                    return true;
                }
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Error returning car: " + e.getMessage());
            return false;
        }
    }
    
    // Delete rental
    public boolean deleteRental(int id) {
        String query = "DELETE FROM rentals WHERE id = ?";
        
        try (Connection conn = bd.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, id);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting rental: " + e.getMessage());
            return false;
        }
    }
    
    // Helper method to map ResultSet to Rental object
    private Location extractRentalFromResultSet(ResultSet rs) throws SQLException {
        Location location = new Location();
        location.setId(rs.getInt("id"));
        location.setUserId(rs.getInt("user_id"));
        location.setCarId(rs.getInt("car_id"));
        location.setStartDate(rs.getDate("start_date"));
        location.setEndDate(rs.getDate("end_date"));
        location.setTotalCost(rs.getDouble("total_cost"));
        location.setReturned(rs.getBoolean("returned"));
        return location;
    }
}
