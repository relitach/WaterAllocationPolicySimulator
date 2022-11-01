package com.hit.waterallocationpolicysimulator.model;

import java.text.DecimalFormat;

public class User
{

    private int id;
    private double alpha;
    private double a; // a: [1 - 3]
    private double b; // b: [0.5 - 0.99]
    private double v; // Produced value of the water
    private double w; // Water price
    private double q; // the quantity of water the user NEEDS !
    private double qCurrent; // the current quantity of water the user HAVE !
    private double u; // u = f(qCurrent) - w * qCurrent

    private DecimalFormat df = new DecimalFormat("####0.000");



    public User(int id, double alpha, double a, double b, double w, double Q) {
        this.id = id;
        this.alpha = alpha;
        this.a = a;
        this.b = b;
        this.w = w;
        this.qCurrent = alpha * Q;
        producedValue();
        inverseDemandFunction();

        this.u = demandFunction(qCurrent) - w * qCurrent;

    }

    public int getId() {
        return id;
    }


    public double getAlpha() {
        return alpha;
    }

    public double getq() {
        return q;
    }

    public double getqCurrent() {
        return qCurrent;
    }

    public void setqCurrent(double qCurrent) {
        this.qCurrent = qCurrent;
    }

    public double getu() {
        return u;
    }



    /**
     * Produced Function
     * Produced value is v = a*q^b
     * @return v
     */
    public void producedValue()
    {
        this.v = Double.valueOf(df.format(a * Math.pow(q, b)));
    }


    /**
     * Demand function
     * The price of water - Dv = a*b*q^(b-1)
     *
     * @return Dv
     */
    public double demandFunction(double quantity)
    {
        double Dv =  Double.valueOf(df.format(a * b * Math.pow(quantity, b-1)));
        return Dv;
    }

    /**
     * Inverse demand function - q = [Dv/(a*b)]^((-1)*(b-1))
     * @return q - the quantity the user NEEDS !
     */
    public void inverseDemandFunction()
    {
        q =  Double.valueOf(df.format(Math.pow((w /(a * b)), (-1)*(b-1))));
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", alpha=" + alpha +
                ", a=" + a +
                ", b=" + b +
                ", v=" + v +
                ", w=" + w +
                ", q=" + q +
                ", qCurrent=" + qCurrent +
                '}';
    }
}
