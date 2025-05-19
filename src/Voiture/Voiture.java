/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Voiture;

import java.util.Objects;

/**
 *
 * @author Anis
 */
public class Voiture {
    private int id;
    private String make;
    private String model;
    private int year;
    private String licensePlate;
    private String color;
    private double dailyRate;
    private boolean available;
    
    // Constructor
    public Voiture(int id, String make, String model, int year, String licensePlate, 
               String color, double dailyRate, boolean available) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.licensePlate = licensePlate;
        this.color = color;
        this.dailyRate = dailyRate;
        this.available = available;
    }
    
    // Default constructor
    public Voiture() {
    }
    
    // Getters and setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getMake() {
        return make;
    }
    
    public void setMake(String make) {
        this.make = make;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public int getYear() {
        return year;
    }
    
    public void setYear(int year) {
        this.year = year;
    }
    
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public double getDailyRate() {
        return dailyRate;
    }
    
    public void setDailyRate(double dailyRate) {
        this.dailyRate = dailyRate;
    }
    
    public boolean isAvailable() {
        return available;
    }
    
    public void setAvailable(boolean available) {
        this.available = available;
    }
    
    @Override
    public String toString() {
        return "Car{" + "id=" + id + ", make=" + make + ", model=" + model + 
               ", year=" + year + ", licensePlate=" + licensePlate + 
               ", color=" + color + ", dailyRate=" + dailyRate + ", available=" + available + '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Voiture car = (Voiture) o;
        return this.id == car.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
