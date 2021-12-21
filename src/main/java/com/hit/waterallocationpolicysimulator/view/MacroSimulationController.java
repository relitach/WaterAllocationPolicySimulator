package com.hit.waterallocationpolicysimulator.view;

import com.hit.waterallocationpolicysimulator.SimulatorMain;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import animatefx.animation.FadeIn;
import java.io.IOException;

public class MacroSimulationController extends Pane
{
    private static MacroSimulationController instance = null;

    public static MacroSimulationController getInstance() {
        if (instance == null) {
            instance = new MacroSimulationController();
        }
        return instance;
    }

    private MacroSimulationController()
    {
        loadView();
    }

    private Pane loadView() {
        FXMLLoader loader = new FXMLLoader(SimulatorMain.class.getResource("fxml/macro-simulation.fxml"));
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
