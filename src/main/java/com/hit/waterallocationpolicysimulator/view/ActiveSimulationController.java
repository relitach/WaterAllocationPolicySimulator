package com.hit.waterallocationpolicysimulator.view;

import animatefx.animation.FadeIn;
import com.hit.waterallocationpolicysimulator.SimulatorMain;
import com.hit.waterallocationpolicysimulator.model.SimulationResult;
import com.hit.waterallocationpolicysimulator.model.User;
import com.hit.waterallocationpolicysimulator.utils.SimCommon;
import com.hit.waterallocationpolicysimulator.utils.SimTypes;
import com.hit.waterallocationpolicysimulator.utils.Utils;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;


public class ActiveSimulationController extends Pane
{
    @FXML
    private Button btnOpenCsvFile;

    @FXML
    private Button btnRunActive;

    @FXML
    private Button btnClearActive;

    @FXML
    private TextField openCsvFileTextField;

    @FXML
    private TextField wTextField;

    @FXML
    private TextField qTextField;

    @FXML
    private TableView activeTable;




    private static ActiveSimulationController instance = null;
    static HostServices Host;
    private Stage myStage;
    private List<User> userList = null;

    public static ActiveSimulationController getInstance() {
        if (instance == null) {
            instance = new ActiveSimulationController();
        }
        return instance;
    }

    private ActiveSimulationController()
    {
        loadView();
        loadButtons();
        initTable();
        String defaultCsv = "D:\\Ariel\\Projects\\WaterAllocationPolicySimulator\\src\\main\\resources\\com\\hit\\waterallocationpolicysimulator\\csv.files\\distribution_of_property_rights.csv";
        openCsvFileTextField.setText(defaultCsv);
    }

    public void setStage(Stage stage) {
        myStage = stage;
    }
    private Pane loadView() {
        FXMLLoader loader = new FXMLLoader(SimulatorMain.class.getResource("fxml/active-simulation.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        ActiveSimulationController controller = loader.getController();
        controller.setStage(this.myStage);

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


    private void loadButtons()
    {
        btnOpenCsvFile.setOnAction(this::openFileLocation);
        btnRunActive.setOnAction(this::runSimulate);
        btnClearActive.setOnAction(this::openFileLocation);
    }

    private void initTable()
    {
        //TableView tab = new TableView();

        TableColumn yearColumn = new TableColumn("Year");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("Year"));

        TableColumn qColumn = new TableColumn("Q");
        qColumn.setCellValueFactory(new PropertyValueFactory<>("Q"));

        activeTable.getColumns().addAll(yearColumn, qColumn);


        SimulationResult result = new SimulationResult("2022", "500");
        activeTable.getItems().add(result);

    }


    private void runSimulate(ActionEvent actionEvent) {

        if(userList != null)
        {
            String wString = wTextField.getText();
            String QString = qTextField.getText();

            double w = Double.parseDouble(wString);
            double Q = Double.parseDouble(QString);

            System.out.println("w = " + w + ", Q = " + Q);
            SimulationResult result = SimCommon.getInstance().runSimulation(SimTypes.PolicyType.QUANTITY, userList, w, Q);

            if(result != null)
            {

            }
        }
        else
        {
            System.out.println("User List is null");

        }
    }

    private void openFileLocation(ActionEvent actionEvent) {
        System.out.println("Open file location");
        getTheUserFilePath();
    }


    public void getTheUserFilePath() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload File Path");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("ALL FILES", "*.*"),
                new FileChooser.ExtensionFilter("ZIP", "*.zip"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("TEXT", "*.txt"),
                new FileChooser.ExtensionFilter("IMAGE FILES", "*.jpg", "*.png", "*.gif")
        );


        File file = fileChooser.showOpenDialog(myStage);
        System.out.println("File: " + file.toString());


        if (file != null)
        {
            System.out.println("Parsing file to User List");
            openCsvFileTextField.setText(file.getPath());
            userList = Utils.parseCSVFileToUserList(file);
        }
    }





}
