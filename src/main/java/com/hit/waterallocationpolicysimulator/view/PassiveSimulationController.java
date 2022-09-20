package com.hit.waterallocationpolicysimulator.view;

import com.hit.waterallocationpolicysimulator.SimulatorMain;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import animatefx.animation.FadeIn;
import java.io.IOException;

public class PassiveSimulationController extends Pane
{
    private static PassiveSimulationController instance = null;

    public static PassiveSimulationController getInstance() {
        if (instance == null) {
            instance = new PassiveSimulationController();
        }
        return instance;
    }

    private void runSimulation()
    {

    }

    private PassiveSimulationController()
    {
        loadView();
    }

    private Pane loadView() {
        FXMLLoader loader = new FXMLLoader(SimulatorMain.class.getResource("fxml/passive-simulation.fxml"));
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
