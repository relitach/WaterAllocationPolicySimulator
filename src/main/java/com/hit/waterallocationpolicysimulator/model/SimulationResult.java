package com.hit.waterallocationpolicysimulator.model;

public class SimulationResult
{
    private String year;
    private String Q;

    public SimulationResult(String year, String Q) {
        this.year = year;
        this.Q = Q;
    }

    public String getYear() {
        return year;
    }

    public String getQ() {
        return Q;
    }


}
