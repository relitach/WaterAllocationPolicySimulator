package com.hit.waterallocationpolicysimulator.view;

import com.hit.waterallocationpolicysimulator.SimulatorMain;
import com.hit.waterallocationpolicysimulator.model.SimulationResult;
import com.hit.waterallocationpolicysimulator.model.User;
import com.hit.waterallocationpolicysimulator.utils.SimCommon;
import com.hit.waterallocationpolicysimulator.utils.SimTypes;
import com.hit.waterallocationpolicysimulator.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import animatefx.animation.FadeIn;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class PassiveSimulationController extends Pane
{

    @FXML
    private Button btnSelectFolderPassive;

    @FXML
    private Button btnRunPassive;

    @FXML
    private Button btnClearPassive;

    @FXML
    private TextField openCsvFileTextFieldPassive;

    @FXML
    private TextField wTextFieldPassive;

    @FXML
    private TextField qTextFieldPassive;

    @FXML
    private TextField nTextFieldPassive;

    @FXML
    private TextField owTextFieldPassive;

    @FXML
    private TextField oqTextFieldPassive;

    @FXML
    private TableView passiveTable;

    private static PassiveSimulationController instance = null;
    private Stage myStage;
    private ArrayList<User> userList = new ArrayList<User>();;
    private boolean isFirstRun = true;

    double w; // Price of local water
    double ow; // Price of outside water
    double initW; // Price - The initial price - Never change
    double Q; // Aggregate quantity of the country
    double OQ; // Aggregate quantity of other sources
    double initQ; // Aggregate quantity of the country - Never change
    double N; // Number of users
    int marginalCost; // Number of pairs to try make a deal


    public static PassiveSimulationController getInstance() {
        if (instance == null) {
            instance = new PassiveSimulationController();
        }
        return instance;
    }


    private PassiveSimulationController()
    {
        loadView();
        loadButtons();
        initTable();
        initDefaultParams();
        String defaultOutputFolder = "C:\\Temp\\";
        openCsvFileTextFieldPassive.setText(defaultOutputFolder);
    }

    private Pane loadView() {
        FXMLLoader loader = new FXMLLoader(SimulatorMain.class.getResource("fxml/passive-simulation.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        PassiveSimulationController controller = loader.getController();
        controller.setStage(this.myStage);

        try {
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void setStage(Stage stage) {
        myStage = stage;
    }

    public void showPane() {
//        logger.debug("showPane");
        this.setVisible(true);
        this.toFront();
        new FadeIn(this).setSpeed(1.5).play();
    }

    private void loadButtons()
    {
        btnSelectFolderPassive.setOnAction(this::openFileLocation);
        btnRunPassive.setOnAction(this::runSimulate);
        btnClearPassive.setOnAction(this::clear);
    }

    private void initTable()
    {
        TableColumn yearColumn = new TableColumn("Year");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("Year"));
        yearColumn.prefWidthProperty().bind(passiveTable.widthProperty().multiply(0.2));

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
        amountOfDealsColumn.prefWidthProperty().bind(passiveTable.widthProperty().multiply(0.15));

        TableColumn AverageCostColumn = new TableColumn("Average Cost");
        AverageCostColumn.setCellValueFactory(new PropertyValueFactory<>("AverageCost"));
        AverageCostColumn.prefWidthProperty().bind(passiveTable.widthProperty().multiply(0.15));


        passiveTable.getColumns().addAll(yearColumn, qColumn, newQColumn, wColumn, newWColumn, amountOfDealsColumn, AverageCostColumn);

    }

    private void initDefaultParams()
    {

        wTextFieldPassive.setText("0.7");

        qTextFieldPassive.setText("200");

        owTextFieldPassive.setText("3");

        oqTextFieldPassive.setText("400");

        nTextFieldPassive.setText("100");

    }



    private void runSimulate(ActionEvent actionEvent) {

        // Validate textfield
//        if()
//        {
//
//        }
//        else
//        {
//
//        }

        if(isFirstRun)
        {
            isFirstRun = false;
            DecimalFormat df = new DecimalFormat("####0.000");

            wTextFieldPassive.setEditable(false);
            wTextFieldPassive.setDisable(true);

            qTextFieldPassive.setEditable(false);
            qTextFieldPassive.setDisable(true);

//            owTextFieldPassive.setEditable(false);
//            owTextFieldPassive.setDisable(true);
//
//            oqTextFieldPassive.setEditable(false);
//            oqTextFieldPassive.setDisable(true);

            nTextFieldPassive.setEditable(false);
            nTextFieldPassive.setDisable(true);


            String wString = wTextFieldPassive.getText();
            String QString = qTextFieldPassive.getText();
            String owString = owTextFieldPassive.getText();
            String OQString = oqTextFieldPassive.getText();
            String NString = nTextFieldPassive.getText();

            w = Double.parseDouble(wString);
            initW = w;
            Q = Double.parseDouble(QString);
            initQ = Q;
            ow = Double.parseDouble(owString);
            OQ = Double.parseDouble(OQString);

            N = Double.parseDouble(NString);


            System.out.println("User list creation started");
            for (int i=0 ; i<N ; i++)
            {
                Random r = new Random();
                double a = 1 + (3 - 1) * r.nextDouble(); // a: [1 - 3]
                r = new Random();
                double b = 0.5 + (0.99 - 0.5) * r.nextDouble(); // b: [0.5 - 0.99]

                User tempUser = new User(i, Q/N/Q, Double.valueOf(df.format(a)), Double.valueOf(df.format(b)), w, Q);
                System.out.println("Generated new user: " + tempUser.toString());
                userList.add(tempUser);
            }
            System.out.println("User list creation finished");

        }
        else
        {
            String owString = owTextFieldPassive.getText();
            String OQString = oqTextFieldPassive.getText();
            ow = Double.parseDouble(owString);
            OQ = Double.parseDouble(OQString);

            // Update params of user
            for (int i=0 ; i<N ; i++)
            {
                userList.get(i).SetParams(Q/N/Q, w, Q);
            }
        }

        if(userList != null)
        {
            System.out.println("w = " + w + ", Q = " + Q);

            System.out.println("#### Run Passive Simulation ####");
            SimulationResult result = SimCommon.getInstance().runSimulation(SimTypes.PolicyType.PRICE, userList, w, Q, ow, OQ);

            if(result != null)
            {
                passiveTable.getItems().add(result);
//                Q = Double.valueOf(result.getNewQ());
//                w = Double.valueOf(result.getNewW());
                Utils.writeUserListToCSVFile(userList, openCsvFileTextFieldPassive.getText() + "\\Passive_" + result.getYear()
                        .replace('/','_')
                        .replace(' ','_')
                        .replace(':','_')+ ".csv");

                Utils.writeDealsListToCSVFile(result.dealResults, openCsvFileTextFieldPassive.getText() + "\\Passive_Deals_" + result.getYear()
                        .replace('/','_')
                        .replace(' ','_')
                        .replace(':','_')+ ".csv");
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
            openCsvFileTextFieldPassive.setText(file.getPath());
            userList = Utils.parseCSVFileToUserList(file);
        }
    }

    public void getTheDirectoryPath() {

        DirectoryChooser directoryChooser = new DirectoryChooser();

        File selectedDirectory = directoryChooser.showDialog(myStage);
        System.out.println("Directory: " + selectedDirectory.toString());


        if (selectedDirectory != null)
        {
            openCsvFileTextFieldPassive.setText(selectedDirectory.getAbsolutePath());
        }
    }

    public void clear(ActionEvent actionEvent)
    {
//        wTextFieldPassive.clear();
        wTextFieldPassive.setEditable(true);
        wTextFieldPassive.setDisable(false);

//        qTextFieldPassive.clear();
        qTextFieldPassive.setEditable(true);
        qTextFieldPassive.setDisable(false);

//        owTextFieldPassive.clear();
        owTextFieldPassive.setEditable(true);
        owTextFieldPassive.setDisable(false);

//        oqTextFieldPassive.clear();
        oqTextFieldPassive.setEditable(true);
        oqTextFieldPassive.setDisable(false);


//        nTextFieldPassive.clear();
        nTextFieldPassive.setEditable(true);
        nTextFieldPassive.setDisable(false);



        passiveTable.getItems().clear();

        isFirstRun = true;
    }
}
