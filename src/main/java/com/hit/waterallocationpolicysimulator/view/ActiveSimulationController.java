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
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;


public class ActiveSimulationController extends Pane
{
    @FXML
    private Button btnSelectFolderActive;

    @FXML
    private Button btnRunActive;

    @FXML
    private Button btnClearActive;

    @FXML
    private TextField outputFolderPathTextFieldActive;

    @FXML
    private TextField wTextFieldActive;

    @FXML
    private TextField qTextFieldActive;

    @FXML
    private TextField nTextFieldActive;

//    @FXML
//    private TextField numOfPairsTextFieldActive;

    @FXML
    private TableView activeTable;




    private static ActiveSimulationController instance = null;
    private Stage myStage;
    private ArrayList<User> userList = new ArrayList<User>();;
    private int numberOfRun = 0;

    double w; // Price
    double Q; // Aggregate quantity of the country + other sources
    double N; // Number of users
    int numOfPairs; // Number of pairs to try make a deal

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
        initDefaultParams();
        String defaultOutputFolder = "C:\\Temp\\";
        outputFolderPathTextFieldActive.setText(defaultOutputFolder);
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
        btnSelectFolderActive.setOnAction(this::openFileLocation);
        btnRunActive.setOnAction(this::runSimulate);
        btnClearActive.setOnAction(this::clear);
    }

    private void initTable()
    {
        TableColumn yearColumn = new TableColumn("Year");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("Year"));
        yearColumn.prefWidthProperty().bind(activeTable.widthProperty().multiply(0.2));

        TableColumn qColumn = new TableColumn("Q");
        qColumn.setCellValueFactory(new PropertyValueFactory<>("Q"));

        TableColumn newQColumn = new TableColumn("New Q");
        newQColumn.setCellValueFactory(new PropertyValueFactory<>("NewQ"));

        TableColumn wColumn = new TableColumn("w");
        wColumn.setCellValueFactory(new PropertyValueFactory<>("w"));

        TableColumn newWColumn = new TableColumn("New w");
        newWColumn.setCellValueFactory(new PropertyValueFactory<>("NewW"));

        TableColumn amountOfDealsColumn = new TableColumn("Amount Of Deals");
        amountOfDealsColumn.setCellValueFactory(new PropertyValueFactory<>("amountOfDeals"));
        amountOfDealsColumn.prefWidthProperty().bind(activeTable.widthProperty().multiply(0.15));

        TableColumn AverageCostColumn = new TableColumn("No deals");
        AverageCostColumn.setCellValueFactory(new PropertyValueFactory<>("AverageCost"));
        AverageCostColumn.prefWidthProperty().bind(activeTable.widthProperty().multiply(0.15));


        activeTable.getColumns().addAll(yearColumn, qColumn, newQColumn, wColumn, newWColumn, amountOfDealsColumn, AverageCostColumn);

    }

    private void initDefaultParams()
    {

        wTextFieldActive.setText("0.7");

        qTextFieldActive.setText("1000");

        nTextFieldActive.setText("100");

//        numOfPairsTextFieldActive.setText("40");

    }



    private void runSimulate(ActionEvent actionEvent) {


        if(numberOfRun == 0)
        {
            DecimalFormat df = new DecimalFormat("####0.000");

            wTextFieldActive.setEditable(false);
            wTextFieldActive.setDisable(true);

//            qTextFieldActive.setEditable(false);
//            qTextFieldActive.setDisable(true);

            nTextFieldActive.setEditable(false);
            nTextFieldActive.setDisable(true);

//            numOfPairsTextFieldActive.setEditable(false);
//            numOfPairsTextFieldActive.setDisable(true);


            String wString = wTextFieldActive.getText();
            String QString = qTextFieldActive.getText();
            String NString = nTextFieldActive.getText();
//            String NumberOfPairs = numOfPairsTextFieldActive.getText();

            w = Double.parseDouble(wString);
            Q = Double.parseDouble(QString);
            N = Double.parseDouble(NString);
//            numOfPairs = Integer.parseInt(NumberOfPairs);


            List<SimCommon.BaseUser> baseUsers = SimCommon.getInstance().getBaseParametersForUsers();

            System.out.println("User list creation started");
            for (int i=0 ; i<baseUsers.size() ; i++)
            {
//                User tempUser = new User(i, Q/N/Q, Double.valueOf(df.format(baseUsers.get(i).a)), Double.valueOf(df.format(baseUsers.get(i).b)), w, Q);
                User tempUser = new User(i, Q/N/Q, baseUsers.get(i).a, baseUsers.get(i).b, w, Q);
                System.out.println("Generated new user: " + tempUser.toString());
                userList.add(tempUser);
            }



//            Q = 1;
//            w = 0.7;
//
//            // יעיל
//            Random r = new Random();
////            double a = 1 + (3 - 1) * r.nextDouble(); // a: [1 - 3]
//            double a = 1;
//            r = new Random();
////            double b = 0.5 + (0.99 - 0.5) * r.nextDouble(); // b: [0.5 - 0.99]
//            double b = 0.5;
//
//            User tempUser = new User(0, 0.8, Double.valueOf(df.format(a)), Double.valueOf(df.format(b)), w, Q);
//            System.out.println("Generated new user: " + tempUser.toString());
//            userList.add(tempUser);
//
//
//            // לא יעיל
//
//            r = new Random();
////            a = 1 + (3 - 1) * r.nextDouble(); // a: [1 - 3]
//            a = 2;
//            r = new Random();
////            b = 0.5 + (0.99 - 0.5) * r.nextDouble(); // b: [0.5 - 0.99]
//            b = 0.6;

//            tempUser = new User(1, 0.2, Double.valueOf(df.format(a)), Double.valueOf(df.format(b)), w, Q);
//            System.out.println("Generated new user: " + tempUser.toString());
//            userList.add(tempUser);

            System.out.println("User list creation finished");
        }
        else
        {
            if(numberOfRun == 1) // Second run
            {
                // Count efficiency users
                int efficiencyUsers = 0;
                for (int i=0 ; i<N ; i++)
                {
                    if(userList.get(i).isUserWasEfficiencyInLastSim)
                    {
                        efficiencyUsers++;
                    }
                }

                // Calculate the total percent for all users
                double totalPercent = 100.0;

                // Update  percent for efficiency users
                double efficiencyPercent = (2 * totalPercent) / (3 * efficiencyUsers); // Twice the percent of non-efficiency users

                // Update percent for non-efficiency users
                double nonEfficiencyPercent = totalPercent / (3 * (N - efficiencyUsers));


                // alpha*2 for efficiency users
                for (int i=0 ; i<N ; i++)
                {
                    if(userList.get(i).isUserWasEfficiencyInLastSim)
                    {
                        userList.get(i).SetParams(efficiencyPercent, w, Q);
                    }
                    else
                    {
                        userList.get(i).SetParams(nonEfficiencyPercent, w, Q);
                    }
                }
            }
            else if(numberOfRun == 2) // Third run
            {
                // alpha*2 for inefficiency users
                // Count non-efficiency users
                int nonEfficiencyUsers = 0;
                for (int i=0 ; i<N ; i++)
                {
                    if(!userList.get(i).isUserWasEfficiencyInLastSim)
                    {
                        nonEfficiencyUsers++;
                    }
                }

                // Calculate the total percent for all users
                double totalPercent = 100.0;

                // Update  percent for non-efficiency users
                double nonEfficiencyPercent = (2 * totalPercent) / (3 * nonEfficiencyUsers); // Twice the percent of efficiency users

                // Update percent for efficiency users
                double efficiencyPercent = totalPercent / (3 * (N - nonEfficiencyUsers));


                // alpha*2 for efficiency users
                for (int i=0 ; i<N ; i++)
                {
                    if(userList.get(i).isUserWasEfficiencyInLastSim)
                    {
                        userList.get(i).SetParams(efficiencyPercent, w, Q);
                    }
                    else
                    {
                        userList.get(i).SetParams(nonEfficiencyPercent, w, Q);
                    }
                }
            }
            else
            {
                // Update params of user
                for (int i=0 ; i<N ; i++)
                {
                    userList.get(i).SetParams(Q/N/Q, w, Q);
                }
            }

        }
        numberOfRun++;
        if(userList != null)
        {
            String QString = qTextFieldActive.getText();
            Q = Double.parseDouble(QString);

            System.out.println("w = " + w + ", Q = " + Q);

            System.out.println("#### Run Active Simulation ####");

            SimulationResult result = SimCommon.getInstance().runSimulation(SimTypes.PolicyType.QUANTITY, userList, w, Q, 0, 0);

            if(result != null)
            {
                activeTable.getItems().add(result);
//                Q = Double.valueOf(result.getNewQ());
                w = Double.valueOf(result.getNewW());
                Utils.writeUserListToCSVFile(userList, outputFolderPathTextFieldActive.getText() + "\\NumberOfRun_" + numberOfRun + "_Active_" + result.getYear()
                        .replace('/','_')
                        .replace(' ','_')
                        .replace(':','_')+ ".csv");


                Utils.writeDealsListToCSVFile(result.dealResults, outputFolderPathTextFieldActive.getText() + "\\NumberOfRun_" + numberOfRun + "_Active_Deals_" + result.getYear()
                        .replace('/','_')
                        .replace(' ','_')
                        .replace(':','_')+ ".csv");


                Utils.printABCDToFile(result.abcdResults, outputFolderPathTextFieldActive.getText() + "\\NumberOfRun_" + numberOfRun + "_Active_ABCD_Result_" + result.getYear()
                        .replace('/','_')
                        .replace(' ','_')
                        .replace(':','_')+ ".txt");

            }
        }
        else
        {
            System.out.println("User List is null");

        }
    }

    private void openFileLocation(ActionEvent actionEvent) {
        System.out.println("Open file location");
//        getTheUserFilePath();
        getTheDirectoryPath();
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
            outputFolderPathTextFieldActive.setText(file.getPath());
            userList = Utils.parseCSVFileToUserList(file);
        }
    }

    public void getTheDirectoryPath() {

        DirectoryChooser directoryChooser = new DirectoryChooser();

        File selectedDirectory = directoryChooser.showDialog(myStage);
        System.out.println("Directory: " + selectedDirectory.toString());


        if (selectedDirectory != null)
        {
            outputFolderPathTextFieldActive.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public void clear(ActionEvent actionEvent)
    {
//        wTextFieldActive.clear();
        wTextFieldActive.setEditable(true);
        wTextFieldActive.setDisable(false);

//        qTextFieldActive.clear();
        qTextFieldActive.setEditable(true);
        qTextFieldActive.setDisable(false);

//        nTextFieldActive.clear();
        nTextFieldActive.setEditable(true);
        nTextFieldActive.setDisable(false);

//        numOfPairsTextFieldActive.clear();
//        numOfPairsTextFieldActive.setEditable(true);
//        numOfPairsTextFieldActive.setDisable(false);



        activeTable.getItems().clear();

        userList.clear();
//        SimCommon.getInstance().baseUsers = null;
        numberOfRun = 0;
    }






}
