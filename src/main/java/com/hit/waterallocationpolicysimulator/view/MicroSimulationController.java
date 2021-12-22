package com.hit.waterallocationpolicysimulator.view;

import com.hit.waterallocationpolicysimulator.SimulatorMain;
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
        if(policyType == SimTypes.PolicyType.QUANTITY)
        {

        }
        else if (policyType == SimTypes.PolicyType.PRICE)
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
