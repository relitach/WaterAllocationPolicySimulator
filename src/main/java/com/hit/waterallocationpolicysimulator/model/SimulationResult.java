package com.hit.waterallocationpolicysimulator.model;

public class SimulationResult
{
    private String year;
    private String Q;
    private String NewQ;
    private String AmountOfDeals;
    private String AverageCost;

    public SimulationResult(String year, String Q, String NewQ, String AmountOfDeals, String AverageCost) {
        this.year = year;
        this.Q = Q;
        this.NewQ = NewQ;
        this.AmountOfDeals = AmountOfDeals;
        this.AverageCost = AverageCost;
    }

    public String getYear() {
        return year;
    }

    public String getQ() {
        return Q;
    }

    public String getNewQ() {
        return NewQ;
    }

    public String getAmountOfDeals() {
        return AmountOfDeals;
    }
    public String getAverageCost() {
        return AverageCost;
    }


}
