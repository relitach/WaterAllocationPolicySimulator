package com.hit.waterallocationpolicysimulator.model;

public class User
{

    private int id;
    private double amountOfMoney;
    private double alpha;
    private String demandFunction; // Bikush Function
    private String slopeOfDemandFunction; // Shipua
    private String interceptOfDemandFunction; // Hituh


    public User(int id, double amountOfMoney, double alpha, String demandFunction) {
        this.id = id;
        this.amountOfMoney = amountOfMoney;
        this.alpha = alpha;
        this.demandFunction = demandFunction; // Dv
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

    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double amountOfQuantity) {
        this.alpha = amountOfQuantity;
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
