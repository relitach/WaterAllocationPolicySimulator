package com.hit.waterallocationpolicysimulator.utils;

import com.hit.waterallocationpolicysimulator.model.SimulationResult;
import com.hit.waterallocationpolicysimulator.model.User;
import com.hit.waterallocationpolicysimulator.view.AboutController;
import com.hit.waterallocationpolicysimulator.view.ConfigurationController;
import com.hit.waterallocationpolicysimulator.view.PassiveSimulationController;
import com.hit.waterallocationpolicysimulator.view.ActiveSimulationController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class SimCommon
{
    private static SimCommon instance = null;

    private ActiveSimulationController activeSimulationController = ActiveSimulationController.getInstance();
    private PassiveSimulationController passiveSimulationController = PassiveSimulationController.getInstance();
    private ConfigurationController configurationController = ConfigurationController.getInstance();
    private AboutController aboutController = AboutController.getInstance();



    public static SimCommon getInstance() {
        if (instance == null) {
            instance = new SimCommon();
        }

        return instance;
    }

    // w - Water price. need to get from gui
    // Q - Aggregate quantity used by all users
    public SimulationResult runSimulation(SimTypes.PolicyType policyType, List<User> usersList, double w, double Q, int numOfPairs)
    {
        SimulationResult result = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();


        if(policyType == SimTypes.PolicyType.QUANTITY)  // Active market
        {
            System.out.println("Try to find " + numOfPairs + " pairs of buyer and seller");

            Random rand = new Random();
            User buyer;
            User seller;
            int amountOfDeals = 0;

            for(int i =0; i < numOfPairs ; i++)
            {
                System.out.println("#### Pair number: " + i + " ####");

                int buyerId = rand.nextInt(usersList.size());
                int sellerId;
                buyer = usersList.get(buyerId);
                do
                {
                    sellerId = rand.nextInt(usersList.size());
                    seller = usersList.get(sellerId);
                } while(buyerId == sellerId || !seller.getIsParticipatingNextSimulation());



                System.out.println("Current quantity of buyer(" + buyer.getId() +"): " + buyer.getqCurrent() + ". The quantity he need is: " + buyer.getq());
                System.out.println("Current quantity of seller(" + seller.getId() +"): " + seller.getqCurrent() +  ". The quantity he need is: " + seller.getq());


                double dealQuantity = 0;
                double uBuyer;
                double uSeller;
                double uBuyerDeal = buyer.getu();
                double uSellerDeal = seller.getu();

                System.out.println("Current u of buyer(" + buyer.getId() +"): " + uBuyerDeal);
                System.out.println("Current u of seller(" + seller.getId() +"): " + uSellerDeal);

                // Check if the buyer need to buy and if the seller have to sell more than he need
                if(buyer.getq() < buyer.getqCurrent() || seller.getq() < seller.getqCurrent())
                {
                    System.out.println("Not pair of buyer and seller");
                    continue;
                }

                for(int x = 0; x <= seller.getqCurrent(); x++)
                {
                    // u = f(q)-w*q = efficiency = money
                    uBuyer = buyer.utilityFunction(buyer.getqCurrent() + x) -  w * (buyer.getqCurrent() + x);
                    uSeller = seller.utilityFunction(seller.getqCurrent() - x) -  w * (seller.getqCurrent() - x);
                    if(uBuyer > uBuyerDeal && uSeller > uSellerDeal)
                    {
                        uBuyerDeal = uBuyer;
                        uSellerDeal = uSeller;
                        dealQuantity = x;
                    }
                }
                if(dealQuantity > 0)
                {
                    System.out.println("Deal");
                    buyer.setqCurrent(buyer.getqCurrent() + dealQuantity);
                    seller.setqCurrent(seller.getqCurrent() - dealQuantity);

                    System.out.println("New quantity of buyer(" + buyer.getId() +"): " + buyer.getqCurrent());
                    System.out.println("New quantity of seller(" + seller.getId() +"): " + seller.getqCurrent());
                    amountOfDeals++;

                }
                else
                {
                    System.out.println("No Deal");
                    buyer.setIsParticipatingNextSimulation(false);
                    seller.setIsParticipatingNextSimulation(false);
                }
            }

            // Calculate new Q
            double NewQ = 0;
            for (User user: usersList)
            {
                NewQ = NewQ + user.getq();
            }

            // Calculate C(Q)
            // C(Q) = sum of w*q
            double cQ = 0;
            for (User user: usersList)
            {
                cQ = cQ + (w * user.getqCurrent()); // TODO: CHECK IF NEED w * q (The amount he need)
            }

            // Calculate new w by C(Q)
            double NewW = cQ / NewQ;


            result = new SimulationResult(formatter.format(date), Q+"", NewQ+"", w+"", NewW+"", amountOfDeals+"", "");
        }
        else if (policyType == SimTypes.PolicyType.PRICE) // Passive market
        {
            double AC = w;
            double MC = w*2;
            for(int i =0; i < usersList.size() ; i++)
            {
                int userId = usersList.get(i).getId();
                double qCurrent = usersList.get(i).getqCurrent();
                double qUserNeed = usersList.get(i).getq();

                System.out.println("##################");
                System.out.println("Current quantity of user(" + userId +"): " + qCurrent + ". The quantity he need is: " + qUserNeed);
                if(qCurrent > qUserNeed)
                {
                    System.out.println("User " + userId + " is passive seller");
                    System.out.println("User " + userId + " received " + (qCurrent-qUserNeed)*w);
                    usersList.get(i).setqCurrent(qUserNeed);
                }
                else
                {
                    System.out.println("User " + userId + " is passive buyer");
                    System.out.println("User " + userId + " bought amount of " + (qUserNeed-qCurrent) + " and paid: " + (qUserNeed-qCurrent)*w);
                    usersList.get(i).setqCurrent(qUserNeed);
                }

                // Calculate new Q
                double NewQ = 0;
                for (User user: usersList)
                {
                    NewQ = NewQ + user.getq();
                }

                // Calculate C(Q)
                // C(Q) = sum of w*q
                double cQ = 0;
                for (User user: usersList)
                {
                    cQ = cQ + (w * user.getqCurrent()); // TODO: CHECK IF NEED w * q (The amount he need)
                }

                // Calculate new w by C(Q)
                double NewW = cQ / NewQ;


                result = new SimulationResult(formatter.format(date), Q+"", NewQ+"", w+"", NewW+"", "", "");
            }
        }
        else
        {
            System.out.println("Policy not supported");
        }


        return result;
    }

    // Function that describes the benefit that user derived from the water
    private double ParseFunction(String function, double quantity)
    {

        return 0;
    }

    public static void setInstance(SimCommon instance) {
        SimCommon.instance = instance;
    }

    public ActiveSimulationController getActiveSimulationController() {
        return activeSimulationController;
    }

    public void setActiveSimulationController(ActiveSimulationController activeSimulationController) {
        this.activeSimulationController = activeSimulationController;
    }

    public PassiveSimulationController getPassiveSimulationController() {
        return passiveSimulationController;
    }

    public void setPassiveSimulationController(PassiveSimulationController passiveSimulationController) {
        this.passiveSimulationController = passiveSimulationController;
    }

    public ConfigurationController getConfigurationController() {
        return configurationController;
    }

    public void setConfigurationController(ConfigurationController configurationController) {
        this.configurationController = configurationController;
    }

    public AboutController getAboutController() {
        return aboutController;
    }

    public void setAboutController(AboutController aboutController) {
        this.aboutController = aboutController;
    }



}
