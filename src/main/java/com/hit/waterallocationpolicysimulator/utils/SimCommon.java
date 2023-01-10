package com.hit.waterallocationpolicysimulator.utils;

import com.hit.waterallocationpolicysimulator.model.SimulationResult;
import com.hit.waterallocationpolicysimulator.model.User;
import com.hit.waterallocationpolicysimulator.view.AboutController;
import com.hit.waterallocationpolicysimulator.view.ConfigurationController;
import com.hit.waterallocationpolicysimulator.view.PassiveSimulationController;
import com.hit.waterallocationpolicysimulator.view.ActiveSimulationController;

import java.text.DecimalFormat;
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

    private DecimalFormat df = new DecimalFormat("####0.000");


    public static SimCommon getInstance() {
        if (instance == null) {
            instance = new SimCommon();
        }

        return instance;
    }

    // w - Water price. need to get from gui
    // Q - Aggregate quantity used by all users
    public SimulationResult runSimulation(SimTypes.PolicyType policyType, List<User> usersList, double w, double Q, double initW, double initQ)
    {
        SimulationResult result = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        // Check which player gonna play in the next simulation
        for (User user : usersList)
        {
            Boolean isPlayNextRound = false;

            // Check if user is buyer or seller
            if(user.getq() < user.getqCurrent())
            {
                // Check as Seller
                double amountOfQuantityUserCanSell = user.getqCurrent();
                for(double x = 0; x <= amountOfQuantityUserCanSell; x += 0.001)
                {
                    // u = f(q)-w*q + x * (w-ac) = efficiency = money
                    // x * (w-ac) => the profit
                    double u = user.utilityFunction(user.getqCurrent() - x) -  w * (user.getqCurrent() - x) + x * user.demandFunction(user.getqCurrent() + x);
                    if(u > user.getu())
                    {
                        isPlayNextRound = true;
                        break;
                    }
                }

            }
            else
            {
                // Check as Buyer
                double amountOfQuantityUserCanBuy = Double.valueOf(df.format(user.getq() - user.getqCurrent()));
                for(double x = 0; x <= amountOfQuantityUserCanBuy; x += 0.001)
                {
                    // u = f(q)-w*q + x * demandfunction = efficiency = money
                    double u = user.utilityFunction(user.getqCurrent() + x) -  w * (user.getqCurrent() + x) + x * user.demandFunction(user.getqCurrent() + x);
                    if(u > user.getu())
                    {
                        isPlayNextRound = true;
                        break;
                    }
                }
            }




            user.setIsParticipatingNextSimulation(isPlayNextRound ? true : false);
        }



        if(policyType == SimTypes.PolicyType.QUANTITY)  // Active market
        {
//            System.out.println("Try to find " + numOfPairs + " pairs of buyer and seller");
//
//            Random rand = new Random();
//            User buyer;
//            User seller;
//            int amountOfDeals = 0;
//
//            for(int i =0; i < numOfPairs ; i++)
//            {
//                System.out.println("#### Pair number: " + i + " ####");
//
//                int buyerId = rand.nextInt(usersList.size());
//                int sellerId;
//                buyer = usersList.get(buyerId);
//                do
//                {
//                    sellerId = rand.nextInt(usersList.size());
//                    seller = usersList.get(sellerId);
//                } while(buyerId == sellerId || !seller.getIsParticipatingNextSimulation());
//
//
//
//                System.out.println("Current quantity of buyer(" + buyer.getId() +"): " + buyer.getqCurrent() + ". The quantity he need is: " + buyer.getq());
//                System.out.println("Current quantity of seller(" + seller.getId() +"): " + seller.getqCurrent() +  ". The quantity he need is: " + seller.getq());
//
//
//                double dealQuantity = 0;
//                double uBuyer;
//                double uSeller;
//                double uBuyerDeal = buyer.getu();
//                double uSellerDeal = seller.getu();
//
//                System.out.println("Current u of buyer(" + buyer.getId() +"): " + uBuyerDeal);
//                System.out.println("Current u of seller(" + seller.getId() +"): " + uSellerDeal);
//
//                // Check if the buyer need to buy and if the seller have to sell more than he need
//                if(buyer.getq() < buyer.getqCurrent() || seller.getq() < seller.getqCurrent())
//                {
//                    System.out.println("Not pair of buyer and seller");
//                    continue;
//                }
//
//                for(int x = 0; x <= seller.getqCurrent(); x++)
//                {
//                    // u = f(q)-w*q = efficiency = money
//                    uBuyer = buyer.utilityFunction(buyer.getqCurrent() + x) -  w * (buyer.getqCurrent() + x);
//                    uSeller = seller.utilityFunction(seller.getqCurrent() - x) -  w * (seller.getqCurrent() - x);
//                    if(uBuyer > uBuyerDeal && uSeller > uSellerDeal)
//                    {
//                        uBuyerDeal = uBuyer;
//                        uSellerDeal = uSeller;
//                        dealQuantity = x;
//                    }
//                }
//                if(dealQuantity > 0)
//                {
//                    System.out.println("Deal");
//                    buyer.setqCurrent(buyer.getqCurrent() + dealQuantity);
//                    seller.setqCurrent(seller.getqCurrent() - dealQuantity);
//
//                    System.out.println("New quantity of buyer(" + buyer.getId() +"): " + buyer.getqCurrent());
//                    System.out.println("New quantity of seller(" + seller.getId() +"): " + seller.getqCurrent());
//                    amountOfDeals++;
//
//                }
//                else
//                {
//                    System.out.println("No Deal");
////                    buyer.setIsParticipatingNextSimulation(false);
////                    seller.setIsParticipatingNextSimulation(false);
//                }
//            }

            int amountOfDeals = 0;

            for (User user : usersList)
            {
                if(user.getIsParticipatingNextSimulation())
                {
                    // Check if user is buyer or seller
                    if(user.getq() < user.getqCurrent())
                    {
                        // User is seller
                        for (User buyer : usersList)
                        {
                            // Check if the buyer is really buyer
                            if(buyer.getq() > buyer.getqCurrent())
                            {
                                if(CheckForDeal(user , buyer, w))
                                {
                                    amountOfDeals++;
                                }
                            }
                        }
                    }
                    else
                    {
                        // User is buyer
                        for (User seller : usersList)
                        {
                            // Check if the seller is really buyer
                            if(seller.getq() > seller.getqCurrent())
                            {
                                if(CheckForDeal(seller , user, w))
                                {
                                    amountOfDeals++;
                                }
                            }
                        }
                    }
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
            double NewQ = 0;
            double cQ = 0;

            // TODO: Ask eyal if in every new year we use the same current q from the last simulation or use Alpha * initQ
            for (User user: usersList)
            {
                int userId = user.getId();
                double qCurrent = user.getqCurrent();
                double qUserNeed = user.getq();
                double initialQuantity = user.getAlpha() * initQ;

                System.out.println("##################");
                System.out.println("Current quantity of user(" + userId +"): " + qCurrent + ". The quantity he need is: " + qUserNeed);



                if(qCurrent > qUserNeed)
                {
                    System.out.println("User " + userId + " is passive seller");
                    System.out.println("User " + userId + " received " + (qCurrent-qUserNeed) * w);
                    user.setqCurrent(qUserNeed);

                    /*

                     Calculate C(Q)
                     C(Q) = sum of  w*q
                     initW = low price
                     w = Full price
                     cQ = cQ + (initial price * initial quantity) + (new w * (quantity he need - initial quantity))

                     */

                    cQ = cQ + (initW * initialQuantity) + (w * (qUserNeed - initialQuantity));

                }
                else
                {
                    System.out.println("User " + userId + " is passive buyer");
                    System.out.println("User " + userId + " bought amount of " + (qUserNeed-qCurrent) + " and paid: " + (qUserNeed-qCurrent)*w);
                    user.setqCurrent(qUserNeed);


                    // Calculate C(Q) for buyer
                    // TODO: ask Eyal about the calculation of the buyer
                    cQ = cQ + (initW * initialQuantity) + (w * (qUserNeed - initialQuantity));

                }

                // Calculate new Q
                NewQ = NewQ + user.getq();




            }

            // Calculate new w by C(Q)
            double NewW = cQ / NewQ;


            result = new SimulationResult(formatter.format(date), Q+"", NewQ+"", w+"", NewW+"", "", "");
        }
        else
        {
            System.out.println("Policy not supported");
        }


        return result;
    }

    private Boolean CheckForDeal(User seller, User buyer, double w)
    {
        double uBuyerDeal = buyer.getu();
        double uSellerDeal = seller.getu();
        double dealQuantity = 0;

        for(double x = 0; x <= seller.getqCurrent(); x += 0.001)
        {
            // u = f(q)-w*q = efficiency = money
            double uBuyer = buyer.utilityFunction(buyer.getqCurrent() + x) -  w * (buyer.getqCurrent() + x) + x * buyer.demandFunction(buyer.getqCurrent() + x);
            double uSeller = seller.utilityFunction(seller.getqCurrent() - x) -  w * (seller.getqCurrent() - x)  + x * buyer.demandFunction(buyer.getqCurrent() + x);
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
            return true;

        }
        else
        {
            System.out.println("No Deal");
            return false;
        }
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
