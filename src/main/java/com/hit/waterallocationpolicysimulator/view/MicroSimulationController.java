package com.hit.waterallocationpolicysimulator.view;

import com.hit.waterallocationpolicysimulator.SimulatorMain;
import com.hit.waterallocationpolicysimulator.model.User;
import com.hit.waterallocationpolicysimulator.utils.SimTypes;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import animatefx.animation.FadeIn;
import java.io.IOException;


public class MicroSimulationController extends Pane
{
    private static MicroSimulationController instance = null;

    public static MicroSimulationController getInstance() {
        if (instance == null) {
            instance = new MicroSimulationController();
        }
        return instance;
    }

    private MicroSimulationController()
    {
        loadView();
    }

    private void runSimulation(SimTypes.PolicyType policyType)
    {
        // load list of users from file

        if(policyType == SimTypes.PolicyType.QUANTITY)  // Active market
        {
            // u = f(q)-w*q

            int n = 20; // Number of pair of people. need to get from gui
            double w = 10 ; // The price of unit of water. need to get from gui


            for(int i =0; i < n ; i++)
            {

                User userA = new User(1,1,1,"","","");
                User userB = new User(2,1,1,"","","");
            }


        }
        else if (policyType == SimTypes.PolicyType.PRICE) // Passive market
        {

        }
        else
        {
            System.out.println("NO DEAL");
        }
    }

    private Pane loadView() {
        FXMLLoader loader = new FXMLLoader(SimulatorMain.class.getResource("fxml/micro-simulation.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void showPane() {
//        logger.debug("showPane");
        this.setVisible(true);
        this.toFront();
        new FadeIn(this).setSpeed(1.5).play();
    }
}
