package com.hit.waterallocationpolicysimulator.view;

import com.hit.waterallocationpolicysimulator.utils.SimCommon;
import com.hit.waterallocationpolicysimulator.utils.SimTypes;
import com.hit.waterallocationpolicysimulator.utils.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SimulatorController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private ImageView logoImageView;

    @FXML
    private StackPane minimizeSimStackPane;

    @FXML
    private StackPane exitSimStackPane;

    @FXML
    private MicroSimulationController microSimulationController;
    @FXML
    private MacroSimulationController macroSimulationController;
    @FXML
    private ConfigurationController configurationController;
    @FXML
    private AboutController aboutController;
    @FXML
    private StackPane stackPaneAllScreens;

    @FXML
    private Button btnMicroSimulation;
    @FXML
    private Button btnMacroSimulation;
    @FXML
    private Button btnConfiguration;
    @FXML
    private Button btnAbout;



    @FXML
    private void initialize()
    {
        System.out.println("SimulatorController - init");
        loadLogo();
        loadScreensInstances();
//        microSimulationController.setVisible(false);
        macroSimulationController.setVisible(false);
        configurationController.setVisible(false);
        aboutController.setVisible(false);
        microSimulationController.showPane();

        loadButtons();
        Utils.createTooltipListener(minimizeSimStackPane, Utils.MINIMIZE, SimTypes.ShowNodeFrom.RIGHT);
        Utils.createTooltipListener(exitSimStackPane, Utils.EXIT, SimTypes.ShowNodeFrom.RIGHT);

    }


    private void loadLogo()
    {
        Image logoImage = new Image(getClass().getResourceAsStream("/images/HIT_logo_50_years_c.jpg"));
        logoImageView.setImage(logoImage);
    }




    private void loadScreensInstances()
    {
        SimCommon simCommon = SimCommon.getInstance();
        microSimulationController = simCommon.getMicroSimulationController();
        macroSimulationController = simCommon.getMacroSimulationController();
        configurationController = simCommon.getConfigurationController();
        aboutController = simCommon.getAboutController();
        stackPaneAllScreens.getChildren().addAll(microSimulationController, macroSimulationController, configurationController, aboutController);
    }

    private void loadButtons()
    {
        btnMicroSimulation.setOnAction(this::handleClicks);
        btnMacroSimulation.setOnAction(this::handleClicks);
        btnConfiguration.setOnAction(this::handleClicks);
        btnAbout.setOnAction(this::handleClicks);
    }

    public void handleClicks(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnMicroSimulation) {
            microSimulationController.showPane();
            macroSimulationController.setVisible(false);
            configurationController.setVisible(false);
            aboutController.setVisible(false);
        } else if (actionEvent.getSource() == btnMacroSimulation) {
            microSimulationController.setVisible(false);
            macroSimulationController.showPane();
            configurationController.setVisible(false);
            aboutController.setVisible(false);
        } else if (actionEvent.getSource() == btnConfiguration) {
            microSimulationController.setVisible(false);
            macroSimulationController.setVisible(false);
            configurationController.showPane();
            aboutController.setVisible(false);
        } else if (actionEvent.getSource() == btnAbout) {
            microSimulationController.setVisible(false);
            macroSimulationController.setVisible(false);
            configurationController.setVisible(false);
            aboutController.showPane();
        }
    }

    public void exitSimHandler(MouseEvent mouseEvent) {
        Platform.exit();
    }

    public void minimizeSimHandler(MouseEvent mouseEvent) {
        Stage stage = (Stage) minimizeSimStackPane.getScene().getWindow();
        // is stage minimizable into task bar. (true | false)
        stage.setIconified(true);
    }
}