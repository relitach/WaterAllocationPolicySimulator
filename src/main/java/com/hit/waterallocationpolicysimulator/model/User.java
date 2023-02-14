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
    private boolean isParticipatingNextSimulation;

    private DecimalFormat df = new DecimalFormat("####0.000");



    public User(int id, double alpha, double a, double b, double w, double Q) {
        this.id = id;
        this.alpha = alpha;
        this.a = a;
        this.b = b;
        this.w = w;
        this.qCurrent = alpha * Q;
        inverseDemandFunction();
        producedValue();
        this.u = utilityFunction(qCurrent);
        CheckIsUserPlayNextRound();

//        if(w != demandFunction(q))
//        {
//            System.out.println("###### Failed ######");
//        }
//        this.u = demandFunction(qCurrent) - w * qCurrent;

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


    public void setIsParticipatingNextSimulation(boolean isParticipatingNextSimulation) {
        this.isParticipatingNextSimulation = isParticipatingNextSimulation;
    }

    public boolean getIsParticipatingNextSimulation() {
        return isParticipatingNextSimulation;
    }

    public double getu() {
        return u;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getV() {
        return v;
    }

    public double getW() {
        return w;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setW(double w) {
        this.w = w;
    }



    /**
     * Produced Function
     * Produced value is v = a*q^b
     *  פונקצית ייצור
     * @return v
     */
    public void producedValue()
    {
        if(qCurrent == 0)
        {
            this.v = 0;
        }
        else
        {
            this.v = Double.valueOf(df.format(a * Math.pow(qCurrent, b)));
        }
    }


    /**
     * Demand function
     * The price of water - Dv = a*b*q^(b-1)
     *   פונקצית ביקוש = ערך תפוקה שולית
     * @return Dv
     */
    public double demandFunction(double quantity)
    {
        if(quantity == 0)
        {
            return 0;
        }

        double Dv =  a * b * Math.pow(quantity, b-1);
        return Double.valueOf(df.format(Dv));
    }

    /**
     * Utility Function
     * u = a * q^b
     *
     * @return u
     */
    public double utilityFunction(double quantity)
    {
        if(quantity == 0)
        {
            return 0;
        }
    // a*q^b - w*q
    //        this.u =  a * Math.pow(quantity, b);
        return Double.valueOf(df.format(a * Math.pow(quantity, b) - w*quantity));
    }

    /**
     * Inverse demand function - q = [Dv/(a*b)]^((-1)*(b-1))
     * @return q - the quantity the user NEEDS !
     */
    public void inverseDemandFunction()
    {

        double base = (w /(a * b));
        double exponent = 1/(b-1);
        q =  Double.valueOf(df.format(Math.pow(base, exponent)));
    }

    public void CheckIsUserPlayNextRound()
    {
        Boolean isPlayNextRound = false;

        // Check if user is buyer or seller
        if(getq() < getqCurrent())
        {
            // Check as Seller
            double amountOfQuantityUserCanSell = getqCurrent();
            for(double x = 0; x <= amountOfQuantityUserCanSell; x += 0.001)
            {
                // u = f(q)   -   w*q   + x*(w-ac) = efficiency = money
                // x * (w-ac) => the profit

                double u = utilityFunction(getqCurrent() - x) - x * demandFunction(getqCurrent() - x);
                if(u > getu())
                {
                    isPlayNextRound = true;
                    break;
                }
            }

        }
        else
        {
            // Check as Buyer
            double amountOfQuantityUserCanBuy = Double.valueOf(df.format(getq() - getqCurrent()));
            for(double x = 0; x <= amountOfQuantityUserCanBuy; x += 0.001)
            {
                // u = f(q)-w*q + x * demandfunction = efficiency = money
                double u = utilityFunction(getqCurrent() + x) + x * demandFunction(getqCurrent() + x);
                if(u > getu())
                {
                    isPlayNextRound = true;
                    break;
                }
            }
        }

        setIsParticipatingNextSimulation(isPlayNextRound ? true : false);
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
                ", u=" + u +
                ", q=" + q +
                ", qCurrent=" + qCurrent +
                ", isParticipatingNextSimulation=" + isParticipatingNextSimulation +
                '}';
    }
}
