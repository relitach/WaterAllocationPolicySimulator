package com.hit.waterallocationpolicysimulator.utils;

import com.hit.waterallocationpolicysimulator.model.DealResult;
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

    public List<BaseUser> baseUsers = null;

    public static SimCommon getInstance() {
        if (instance == null) {
            instance = new SimCommon();
        }

        return instance;
    }

    // w - Water price. need to get from gui
    // Q - Aggregate quantity used by all users
    public SimulationResult runSimulation(SimTypes.PolicyType policyType, List<User> usersList, double localW, double localQ, double outsideW, double outsideQ)
    {
        SimulationResult result = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();

        ArrayList<DealResult> deals = new ArrayList<DealResult>();;

        double totalQ = localQ;


        // Check which player gonna play in the next simulation
        for (User user : usersList)
        {
            user.CheckIsUserPlayNextRound();
            if (!user.getIsParticipatingNextSimulation())
            {
                System.out.println("User " + user.getId() + " is not play in the next simulation");
            }
        }


        /*
        #####################################
        Active market (Quantity)
        #####################################
        */
        if(policyType == SimTypes.PolicyType.QUANTITY)
        {
            // Refresh params of users every simulation
            for (User user : usersList)
            {
                user.SetParams(user.getAlpha(), localW, localQ);
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
                                DealResult dealResult = CheckForDeal(user , buyer, localW);
                                if(dealResult.result == true)
                                {
                                    amountOfDeals++;
                                    deals.add(dealResult);
                                }
                                else
                                {

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
                                DealResult dealResult = CheckForDeal(seller , user, localW);
                                if(dealResult.result == true)
                                {
                                    amountOfDeals++;
                                    isBuyerFoundDeal = true;
                                    deals.add(dealResult);
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
            NewQ = localQ;

            // Calculate C(Q)
            // C(Q) = sum of w*q
            double cQ = 0;
            for (User user: usersList)
            {
                cQ = cQ + (localW * user.getqCurrent());
            }

            // Calculate new w by C(Q)
            double NewW = cQ / NewQ;


            result = new SimulationResult(formatter.format(date), localQ+"", NewQ+"", localW+"", NewW+"", amountOfDeals+"", amonutOfBuyersWithNoDeal+"");

        }

        /*
        #####################################
        Passive market (Price)
        #####################################
        */
        else if (policyType == SimTypes.PolicyType.PRICE)
        {
            double NewQ = 0;
            double cQ = 0;
            double temp1 = 0;
            double temp2 = 0;

            double costOfAllWater = (localW * localQ) + (outsideW * outsideQ);

            for (User user: usersList)
            {
                int userId = user.getId();
                double qCurrent = user.getqCurrent();
                double qUserNeed = user.getq();

                 // TODO: CHECK IF NEED - because if its negative they should get money from the country (As passive seller)
//                if(user.getu() < 0)
//                {
//                    // Calculate only the water that the seller use
//                    cQ = cQ + (localW * qUserNeed) ;
//
//                    // The money that the country need to pay to the seller
//                    cQ = cQ - ((qCurrent-qUserNeed) * localW);
//                    continue;
//                }


                // double initialQuantity = user.getAlpha() * initQ;

                System.out.println("##################");
                System.out.println("Current quantity of user(" + userId +"): " + qCurrent + ". The quantity he need is: " + qUserNeed);


                /*
                 ############# Passive Seller #############
                */
                if(qCurrent > qUserNeed)
                {
                    double quantityToSell = qCurrent - qUserNeed;
                    System.out.println("User " + userId + " is passive seller");
                    System.out.println("User " + userId + " received " + quantityToSell * localW);
                    user.setqCurrent(qUserNeed);

                    DealResult dealResult = new DealResult(true, -1, userId, quantityToSell * localW, quantityToSell);
                    deals.add(dealResult);

                    /*

                     Calculate C(Q)
                     C(Q) = sum of  w*q
                     initW = low price
                     w = Full price
                     cQ = cQ + (initial price * initial quantity) + (new w * (quantity he need - initial quantity))

                     */
//                    cQ = cQ + (initW * initialQuantity) - (localW * (initialQuantity - qUserNeed));


                    temp1 = 0;
                    temp2 = 0;

                    // Calculate only the water that the seller use
                    cQ = cQ + (localW * qUserNeed);


                    // The money that the country need to pay to the seller
                    cQ = cQ - (localW * quantityToSell);

                }
                /*
                 ############# Passive Buyer #############
                */
                else
                {

                    double quantityToBuy = qUserNeed - qCurrent;

                    System.out.println("User " + userId + " is passive buyer");

                    if(outsideQ <= 0)
                    {
                        System.out.println("Not enough water in outside pool !!!");
                        quantityToBuy = 0;
                    }
                    else if(qUserNeed > qCurrent && quantityToBuy >= outsideQ)
                    {
                        System.out.println("Not enough water in outside pool !!! User " + userId + " bought amount of " + (outsideQ) + " and paid: " + outsideQ * localW);
                        user.setqCurrent(qCurrent + outsideQ);
                        quantityToBuy = outsideQ;
                    }
                    else if(qUserNeed > qCurrent && quantityToBuy < outsideQ)
                    {
                        System.out.println("User " + userId + " bought amount of " + quantityToBuy + " and paid: " + quantityToBuy * outsideW);
                        user.setqCurrent(qUserNeed);
                    }
                    else
                    {
                        System.out.println("Unkown");
                    }


                    // Calculate C(Q) for buyer
                    cQ = cQ + (localW * qCurrent) + (outsideW * quantityToBuy);

                    // Allocate quantity from the outside Q
                    outsideQ = outsideQ - quantityToBuy;

                    DealResult dealResult = new DealResult(true, userId, -1, quantityToBuy * localW, quantityToBuy);
                    deals.add(dealResult);

                }

                // Calculate new Q
                NewQ = NewQ + user.getq();

            }


            totalQ = localQ + outsideQ;


            // Calculate new w by C(Q)
            double NewW = cQ / NewQ;




            System.out.println("New Q: " + NewQ);
            System.out.println("New W: " + NewW);
            System.out.println("C(Q): " + cQ);  // It's ok that it's less than the cost of water because we the country pay to seller
            System.out.println("Current outside water allocation (OQ): " + outsideQ);
            System.out.println("Cost of Current outside water allocation (Not Used): " + outsideQ * outsideW);
            System.out.println("CQ + Cost of not used outside water): " + ( cQ + outsideQ * outsideW));
            System.out.println("Cost of all water: " + costOfAllWater);

            result = new SimulationResult(formatter.format(date), localQ+"", NewQ+"", localW+"", NewW+"", "", "");


        }
        else
        {
            System.out.println("Policy not supported");
        }





        result.dealResults = deals;


        /*
        #################################################
        Calculate Cases of property rights and efficiency
        #################################################
        */
        double sumOfEfficiency = 0;
        int sumA = 0;
        int sumB = 0;
        int sumC = 0;
        int sumD = 0;
        for (User user: usersList)
        {
            sumOfEfficiency += user.efficiencyFunction();
        }
        // Average efficiency
        double averageEfficiency = sumOfEfficiency/usersList.size();

        // Alpha i (user.alpha)- Share of user i in the total quantity of property rights.

        // S i (user.qCurrent) - Share of user i in the total quantity actually used.

        // Efficiency of user i (user.u).

        for (User user: usersList)
        {

            if(user.alpha - user.qCurrent/totalQ > 0)
            {
                // A or B (Sellers)
                if(user.lamda - averageEfficiency > 0)
                {
                    // B
                    sumB++;
                    user.isUserWasEfficiencyInLastSim = true;
                }
                else
                {
                    // A
                    sumA++;
                    user.isUserWasEfficiencyInLastSim = false;
                }
            }
            else
            {
                // C or D (Buyers)
                if(user.lamda - averageEfficiency > 0)
                {
                    // D
                    sumD++;
                    user.isUserWasEfficiencyInLastSim = false;
                }
                else
                {
                    // C
                    sumC++;
                    user.isUserWasEfficiencyInLastSim = true;
                }
            }
        }

        System.out.println("Total users in case A: " + sumA);
        System.out.println("Total users in case B: " + sumB);
        System.out.println("Total users in case C: " + sumC);
        System.out.println("Total users in case D: " + sumD);

        return result;
    }

    private DealResult CheckForDeal(User seller, User buyer, double w)
    {
        //System.out.println("Check for deal: Buyer user: " + buyer.id + "Seller user: " + seller.id);
        double uBuyerDeal = buyer.getu();
        double uSellerDeal = seller.getu();
        double dealQuantity = 0;

        for(double x = 0; x <= seller.getqCurrent(); x += 0.001)
        {
            // before - uBuyerDeal = f(q)-w*q
            // after - uBuyer = f(q)-w*q
            double uBuyer = buyer.utilityFunction(buyer.getqCurrent() + x) + x * buyer.demandFunction(buyer.getqCurrent() + x);

            double uSeller = seller.utilityFunction(seller.getqCurrent() - x) - x * seller.demandFunction(seller.getqCurrent() - x);

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

            return new DealResult(true, buyer.getId(), seller.getId(), dealQuantity * 0.7, dealQuantity);



        }
        else
        {
            //System.out.println("No Deal");
            return new DealResult(false, 0, 0,0,0);
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


    public List<BaseUser> getBaseParametersForUsers()
    {
        if(baseUsers == null)
        {
            baseUsers = new ArrayList<BaseUser>();

            int N = 100;

            for (int i=0 ; i<N ; i++)
            {
                Random r = new Random();
                double a = 1 + (3 - 1) * r.nextDouble(); // a: [1 - 3]
                r = new Random();
                double b = 0.5 + (0.99 - 0.5) * r.nextDouble(); // b: [0.5 - 0.99]

                baseUsers.add(new BaseUser(a, b));
            }
        }
        else
        {
            System.out.println("Users Base Parameters already initialized");
        }

        return baseUsers;
    }

    public class BaseUser
    {
        public double a;
        public double b;

        public BaseUser(double a_user, double b_user)
        {
            a = a_user;
            b = b_user;
        }

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
