package com.huebeiro.insuranceco.model;

import java.io.Serializable;

/**
 * Created by Adilson on 19/04/2016.
 */
public class Car implements Serializable {
    private int id;
    private String plate;
    private int fipeId;
    private String brand;
    private String model;
    private int year;
    private int totalEvents;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public String getFormattedPlate() {
        if(plate != null){
            if(plate.length() == 7){
                return plate.substring(0,3) + "-" + plate.substring(3,7);
            }
        }
        return null;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public int getFipeId() {
        return fipeId;
    }

    public void setFipeId(int fipeId) {
        this.fipeId = fipeId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public int getTotalEvents() {
        return totalEvents;
    }

    public void setTotalEvents(int totalEvents) {
        this.totalEvents = totalEvents;
    }
}
