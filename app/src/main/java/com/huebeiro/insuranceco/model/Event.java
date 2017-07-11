package com.huebeiro.insuranceco.model;

import java.io.Serializable;

/**
 * Created by Adilson on 24/04/2016.
 */
public class Event implements Serializable{

    private int id;
    private int carId;
    private String date;
    private int totalAttachments;
    private int totalCauses;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getTotalAttachments() {
        return totalAttachments;
    }

    public void setTotalAttachments(int totalAttachments) {
        this.totalAttachments = totalAttachments;
    }

    public int getTotalCauses() {
        return totalCauses;
    }

    public void setTotalCauses(int totalCauses) {
        this.totalCauses = totalCauses;
    }
}
