package com.hit.waterallocationpolicysimulator.model;

public class User
{

    private int id;
    private double amountOfMoney;
    private double amountOfQuantity;
    private String demandFunction; // Bikush Function
    private String slopeOfDemandFunction; // Shipua
    private String interceptOfDemandFunction; // Hituh


    public User(int id, double amountOfMoney, double amountOfQuantity, String demandFunction, String slopeOfDemandFunction, String interceptOfDemandFunction) {
        this.id = id;
        this.amountOfMoney = amountOfMoney;
        this.amountOfQuantity = amountOfQuantity;
        this.demandFunction = demandFunction;
        this.slopeOfDemandFunction = slopeOfDemandFunction;
        this.interceptOfDemandFunction = interceptOfDemandFunction;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmountOfMoney() {
        return amountOfMoney;
    }

    public void setAmountOfMoney(double amountOfMoney) {
        this.amountOfMoney = amountOfMoney;
    }

    public double getAmountOfQuantity() {
        return amountOfQuantity;
    }

    public void setAmountOfQuantity(double amountOfQuantity) {
        this.amountOfQuantity = amountOfQuantity;
    }

    public String getDemandFunction() {
        return demandFunction;
    }

    public void setDemandFunction(String demandFunction) {
        this.demandFunction = demandFunction;
    }

    public String getSlopeOfDemandFunction() {
        return slopeOfDemandFunction;
    }

    public void setSlopeOfDemandFunction(String slopeOfDemandFunction) {
        this.slopeOfDemandFunction = slopeOfDemandFunction;
    }

    public String getInterceptOfDemandFunction() {
        return interceptOfDemandFunction;
    }

    public void setInterceptOfDemandFunction(String interceptOfDemandFunction) {
        this.interceptOfDemandFunction = interceptOfDemandFunction;
    }
}
