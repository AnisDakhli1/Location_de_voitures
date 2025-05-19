/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Voiture;

import java.util.Date;

/**
 *
 * @author Anis
 */
public class Location {
    private int id;
    private int userId;
    private int carId;
    private Date startDate;
    private Date endDate;
    private double totalCost;
    private boolean returned;
    
    // Constructor
    public Location(int id, int userId, int carId, Date startDate, Date endDate, 
                  double totalCost, boolean returned) {
        this.id = id;
        this.userId = userId;
        this.carId = carId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.returned = returned;
    }
    
    // Default constructor
    public Location() {
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public int getCarId() {
        return carId;
    }
    
    public void setCarId(int carId) {
        this.carId = carId;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public double getTotalCost() {
        return totalCost;
    }
    
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    
    public boolean isReturned() {
        return returned;
    }
    
    public void setReturned(boolean returned) {
        this.returned = returned;
    }
    
    @Override
    public String toString() {
        return "Rental{" + "id=" + id + ", userId=" + userId + ", carId=" + carId + 
               ", startDate=" + startDate + ", endDate=" + endDate + 
               ", totalCost=" + totalCost + ", returned=" + returned + '}';
    }
}
