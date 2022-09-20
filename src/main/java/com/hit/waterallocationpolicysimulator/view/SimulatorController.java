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
    private ActiveSimulationController activeSimulationController;
    @FXML
    private PassiveSimulationController passiveSimulationController;
    @FXML
    private ConfigurationController configurationController;
    @FXML
    private AboutController aboutController;
    @FXML
    private StackPane stackPaneAllScreens;

    @FXML
    private Button btnActiveSimulation;
    @FXML
    private Button btnPassiveSimulation;
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
        passiveSimulationController.setVisible(false);
        configurationController.setVisible(false);
        aboutController.setVisible(false);
        activeSimulationController.showPane();

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
        activeSimulationController = simCommon.getActiveSimulationController();
        passiveSimulationController = simCommon.getPassiveSimulationController();
        configurationController = simCommon.getConfigurationController();
        aboutController = simCommon.getAboutController();
        stackPaneAllScreens.getChildren().addAll(activeSimulationController, passiveSimulationController, configurationController, aboutController);
    }

    private void loadButtons()
    {
        btnActiveSimulation.setOnAction(this::handleClicks);
        btnPassiveSimulation.setOnAction(this::handleClicks);
        btnConfiguration.setOnAction(this::handleClicks);
        btnAbout.setOnAction(this::handleClicks);
    }

    public void handleClicks(ActionEvent actionEvent) {

        if (actionEvent.getSource() == btnActiveSimulation) {
            activeSimulationController.showPane();
            passiveSimulationController.setVisible(false);
            configurationController.setVisible(false);
            aboutController.setVisible(false);
        } else if (actionEvent.getSource() == btnPassiveSimulation) {
            activeSimulationController.setVisible(false);
            passiveSimulationController.showPane();
            configurationController.setVisible(false);
            aboutController.setVisible(false);
        } else if (actionEvent.getSource() == btnConfiguration) {
            activeSimulationController.setVisible(false);
            passiveSimulationController.setVisible(false);
            configurationController.showPane();
            aboutController.setVisible(false);
        } else if (actionEvent.getSource() == btnAbout) {
            activeSimulationController.setVisible(false);
            passiveSimulationController.setVisible(false);
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