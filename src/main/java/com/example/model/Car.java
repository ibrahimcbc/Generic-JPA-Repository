package com.example.model;

import javax.persistence.Embeddable;

@Embeddable
public class Car {
    private String plateNo;

    public Car() {}

    public Car(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    @Override
    public String toString() {
        return "Car{plateNo='" + plateNo + "'}";
    }
}
