package org.example.info;

import java.time.LocalDate;

public class Meta {
    private int id;
    private String name;
    private double targetAmount;
    private double savedAmount;
    private LocalDate deadline;
    private String color;
    private String category;
    private LocalDate creationDate; // Para FIFO/LIFO

    public Meta(int id, String name, double targetAmount, double savedAmount, LocalDate deadline, String color, String category, LocalDate creationDate) {
        this.id = id;
        this.name = name;
        this.targetAmount = targetAmount;
        this.savedAmount = savedAmount;
        this.deadline = deadline;
        this.color = color;
        this.category = category;
        this.creationDate = creationDate;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getTargetAmount() { return targetAmount; }
    public double getSavedAmount() { return savedAmount; }
    public LocalDate getDeadline() { return deadline; }
    public String getColor() { return color; }
    public String getCategory() { return category; }
    public LocalDate getCreationDate() { return creationDate; }

    // Setters
    public void setSavedAmount(double savedAmount) {
        this.savedAmount = savedAmount;
    }
}