package com.hit.waterallocationpolicysimulator.model;

public class SimulationResult
{
    private String year;
    private String Q;
    private String NewQ;
    private String w;
    private String NewW;
    private String AmountOfDeals;
    private String AverageCost;

    public SimulationResult(String year, String Q, String NewQ, String w, String NewW, String AmountOfDeals, String AverageCost) {
        this.year = year;
        this.Q = Q;
        this.NewQ = NewQ;
        this.w = w;
        this.NewW = NewW;
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

    public String getW() {
        return w;
    }

    public String getNewW() {
        return NewW;
    }

    public String getAmountOfDeals() {
        return AmountOfDeals;
    }
    public String getAverageCost() {
        return AverageCost;
    }


}
