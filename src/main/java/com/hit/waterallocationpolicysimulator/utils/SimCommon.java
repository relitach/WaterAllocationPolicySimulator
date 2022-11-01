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
    public SimulationResult runSimulation(SimTypes.PolicyType policyType, List<User> usersList, double w, double Q)
    {
        SimulationResult result = null;


        if(policyType == SimTypes.PolicyType.QUANTITY)  // Active market
        {

            int numberOfPairs = 3; // Number of a pair of people. need to get from gui
            System.out.println("Try to find " + numberOfPairs + " pairs of buyer and seller");

            Random rand = new Random();
            User buyer;
            User seller;
            int amountOfDeals = 0;

            for(int i =0; i < numberOfPairs ; i++)
            {
                System.out.println("#### Pair number: " + i + " ####");

                int buyerId = rand.nextInt(usersList.size());
                int sellerId;
                buyer = usersList.get(buyerId);
                do { sellerId = rand.nextInt(usersList.size()); } while(buyerId == sellerId);
                seller = usersList.get(sellerId);


                System.out.println("Current quantity of buyer(" + buyer.getId() +"): " + buyer.getqCurrent());
                System.out.println("Current quantity of seller(" + seller.getId() +"): " + seller.getqCurrent());


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
                    System.out.println("No Deal");
                    continue;
                }

                for(int x = 0; x <= seller.getqCurrent(); x++)
                {
                    // u = f(q)-w*q
                    uBuyer = buyer.demandFunction(buyer.getqCurrent() + x) -  w * (buyer.getqCurrent() + x);
                    uSeller = seller.demandFunction(seller.getqCurrent() - x) -  w * (seller.getqCurrent() - x);
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
                }
            }
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();

            // Calculate new Q
            double NewQ = 0;
            for (User user: usersList)
            {
                NewQ = NewQ + user.getq();
            }

            result = new SimulationResult(formatter.format(date), Q+"", NewQ+"", amountOfDeals+"", "");
        }
        else if (policyType == SimTypes.PolicyType.PRICE) // Passive market
        {

            for(int i =0; i < usersList.size() ; i++)
            {

                double qOfUser = usersList.get(i).getAlpha() * Q;
                double uOfUser;

                // what is the max q ?
                for(int j = 0; qOfUser - j < 0; j++)
                {

                }
            }
        }
        else
        {
            System.out.println("NO DEAL");
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
