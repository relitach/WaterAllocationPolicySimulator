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
            user.CheckIsUserPlayNextRound();
            if (!user.getIsParticipatingNextSimulation())
            {
                System.out.println("User " + user.getId() + " is not play in the next simulation");
            }
        }



        if(policyType == SimTypes.PolicyType.QUANTITY)  // Active market
        {
            for (User user : usersList)
            {
                user.setqCurrent(user.getAlpha() * Q);;
            }


            int amountOfDeals = 0;
            int amonutOfBuyersWithNoDeal = 0;

            for (User user : usersList)
            {
                System.out.println("#### Looking for deal with user id: " + user.getId() + " ####");

                if(user.getIsParticipatingNextSimulation())
                {
                    // Check if user is buyer or seller
                    if(user.getq() < user.getqCurrent())
                    {
                        System.out.println("User id: " + user.getId() + " is seller");


                        // User is seller
                        for (User buyer : usersList)
                        {
                            // Validate user is not do deal with him self
                            if(user.getId() == buyer.getId() || !buyer.getIsParticipatingNextSimulation())
                            {
                                continue;
                            }

                            // Check if the buyer is really buyer
                            if(buyer.getq() > buyer.getqCurrent())
                            {
                                if(CheckForDeal(user , buyer, w))
                                {
                                    amountOfDeals++;
                                }
                                else
                                {
//
                                }
                            }
                        }
                    }
                    else
                    {
                        System.out.println("User id: " + user.getId() + " is buyer");
                        boolean isBuyerFoundDeal = false;
                        // User is buyer
                        for (User seller : usersList)
                        {
                            // Validate user is not do deal with him self
                            if(user.getId() == seller.getId() || !seller.getIsParticipatingNextSimulation())
                            {
                                continue;
                            }

                            // Check if the seller is really seller
                            if(seller.getq() < seller.getqCurrent())
                            {
                                if(CheckForDeal(seller , user, w))
                                {
                                    amountOfDeals++;
                                    isBuyerFoundDeal = true;
                                }
                                else
                                {

                                }
                            }
                        }

                        if(!isBuyerFoundDeal)
                        {
                            amonutOfBuyersWithNoDeal++;
                        }
                    }
                }
                else
                {
                    System.out.println("User id: " + user.getId() + " is not play in the simulation");
                }
            }

            // Calculate new Q - its Active no need to calculate new Q. the manager decided the new Q
            double NewQ = 0;
//            for (User user: usersList)
//            {
//                NewQ = NewQ + user.getqCurrent();
//            }
            NewQ = Q;

            // Calculate C(Q)
            // C(Q) = sum of w*q
            double cQ = 0;
            for (User user: usersList)
            {
                cQ = cQ + (w * user.getqCurrent());
            }

            // Calculate new w by C(Q)
            double NewW = cQ / NewQ;


            result = new SimulationResult(formatter.format(date), Q+"", NewQ+"", w+"", NewW+"", amountOfDeals+"", amonutOfBuyersWithNoDeal+"");
        }
        else if (policyType == SimTypes.PolicyType.PRICE) // Passive market
        {
            double NewQ = 0;
            double cQ = 0;

            // TODO: Ask eyal if in every new year we use the same current q from the last simulation or use Alpha * initQ
            for (User user: usersList)
            {
                 // TODO: CHECK IF NEED - because if its negative they should get money from the country (As passive buyer)
                if(user.getu() < 0)
                {
                    break;
                }

                int userId = user.getId();
                double qCurrent = user.getqCurrent();
                double qUserNeed = user.getq();
                double initialQuantity = user.getAlpha() * initQ;

                System.out.println("##################");
                System.out.println("Current quantity of user(" + userId +"): " + qCurrent + ". The quantity he need is: " + qUserNeed);


                // TODO: Ask eyal if its ok that we just compare the quantity and not computing the
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

                    cQ = cQ + (initW * initialQuantity) - (w * (initialQuantity - qUserNeed));

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

            double costOfAllWater = (initW * initQ) + (NewW * cQ);

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
            // before - uBuyerDeal = f(q)-w*q
            // after - uBuyer = f(q)-w*q
            double uBuyer = buyer.utilityFunction(buyer.getqCurrent() + x) - x * buyer.demandFunction(buyer.getqCurrent() + x);

            double uSeller = seller.utilityFunction(seller.getqCurrent() - x) + x * seller.demandFunction(seller.getqCurrent() - x);
            if(uBuyer > uBuyerDeal && uSeller > uSellerDeal)
            {
                uBuyerDeal = uBuyer;
                uSellerDeal = uSeller;
                dealQuantity = x;
            }
        }
        if(dealQuantity > 0)
        {
            System.out.println("Buyer user: " + buyer.toString());
            System.out.println("Seller user: " + seller.toString());
            System.out.println("Deal ! Quantity of water in the deal: " + dealQuantity);
            buyer.setqCurrent(buyer.getqCurrent() + dealQuantity);
            seller.setqCurrent(seller.getqCurrent() - dealQuantity);

            System.out.println("New quantity of buyer(" + buyer.getId() +"): " + buyer.getqCurrent());
            System.out.println("New quantity of seller(" + seller.getId() +"): " + seller.getqCurrent());
            return true;

        }
        else
        {
            //System.out.println("No Deal");
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
