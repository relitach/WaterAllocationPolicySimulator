package com.hit.waterallocationpolicysimulator.model;

public class DealResult
{
    public boolean result;
    public int buyerId;
    public int sellerId;
    public double priceOfDeal;
    public double amountOfQuantityInDeal;

    public DealResult(boolean result, int buyerId, int sellerId, double priceOfDeal, double amountOfQuantityInDeal) {
        this.result = result;
        this.buyerId = buyerId;
        this.sellerId = sellerId;
        this.priceOfDeal = priceOfDeal;
        this.amountOfQuantityInDeal = amountOfQuantityInDeal;
    }
}
