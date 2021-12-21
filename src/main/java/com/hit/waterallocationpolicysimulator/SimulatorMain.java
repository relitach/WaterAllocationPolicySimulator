package com.hit.waterallocationpolicysimulator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;




public class SimulatorMain extends Application
{
    private Stage rootStage;
    private FXMLLoader fxmlLoader;
    private SplitPane rootPane;
    private double x, y;

    @Override
    public void start(Stage stage) throws IOException
    {
        rootStage = stage;
        fxmlLoader = new FXMLLoader(SimulatorMain.class.getResource("fxml/simulator-view.fxml"));
        rootPane = fxmlLoader.load();
//        Scene scene = new Scene(fxmlLoader.load(), 500, 300);
        Scene scene = new Scene(rootPane);
        rootStage.setTitle("Water Allocation Policy Simulator");
        rootStage.initStyle(StageStyle.UNDECORATED);   // set stage borderless
        makeStageDraggable();
        rootStage.setScene(scene);
        rootStage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }


    private void makeStageDraggable() {
        this.rootPane.addEventHandler(MouseEvent.MOUSE_PRESSED , event -> {
            x = event.getSceneX();
            y = event.getSceneY();
//            closePopupIfOpened(event);
        });

        this.rootPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            rootStage.setX(event.getScreenX() - x);
            rootStage.setY(event.getScreenY() - y);
        });
    }
}