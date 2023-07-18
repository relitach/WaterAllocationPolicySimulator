package com.hit.waterallocationpolicysimulator.utils;

import com.hit.waterallocationpolicysimulator.model.DealResult;
import com.hit.waterallocationpolicysimulator.model.User;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class Utils
{

    private static ScheduledFuture<?> timer;

    // Tooltips
    public static final String EXIT = "Exit";
    public static final String MINIMIZE = "Minimize";

    public static void createTooltipListener(Node node, String msg, SimTypes.ShowNodeFrom direction) {
        Tooltip tooltip = new Tooltip();
        final double deltaX = direction == SimTypes.ShowNodeFrom.LEFT ? -2 : 2;
        node.setOnMouseEntered(event -> {
            timer = ThreadManager.getInstance().getScheduledThreadPool().schedule(() -> {
                Platform.runLater(() -> {
                    tooltip.setText(msg);
                    tooltip.setFont(Font.font(12));
                    tooltip.show(node.getScene().getWindow(), event.getScreenX() + deltaX, event.getScreenY() + 8);
                });
            }, 500, TimeUnit.MILLISECONDS);
        });
        node.setOnMousePressed(event -> {
            cancelTimer();
            tooltip.hide();
        });
        node.setOnMouseExited(event -> {
            cancelTimer();
            tooltip.hide();
        });
    }

    private static void cancelTimer() {
        if (timer != null) {
            timer.cancel(true);
            timer = null;
        }
    }


    public static ArrayList<User> parseCSVFileToUserList(File file)
    {
        ArrayList<User> usersList = new ArrayList<User>();

        try (CSVReader reader = new CSVReader(new FileReader(file)))
        {
            List<String[]> csvList = reader.readAll();
            for (String[] row : csvList)
            {
                if(row[0].contains("id"))
                {
                    continue;
                }
                System.out.println(Arrays.toString(row));
                int id = Integer.parseInt(row[0]);
                int amountOfMoney = Integer.parseInt(row[0]);;
                int alpha = Integer.parseInt(row[0]);;
                int demandFunction = Integer.parseInt(row[0]);;
                int slopeOfDemandFunction = Integer.parseInt(row[0]);;
                int interceptOfDemandFunction = Integer.parseInt(row[0]);;
//                usersList.add(new User(id, ));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }

        return usersList;
    }

    public static void writeUserListToCSVFile(ArrayList<User> userList, String filePath)
    {
        // first create file object for file placed at location
        // specified by filepath
        File file = new File(filePath);

        try {
            file.createNewFile();
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = { "id",
                    "alpha",
                    "a",
                    "b",
                    "v",
                    "w",
                    "q",
                    "qCurrent",
                    "u",
                    "IsParticipatingNextSimulation" };
            writer.writeNext(header);

            // add data to csv
            for (int i = 0; i <userList.size(); i++ )
            {
                String[] dataRow = {String.valueOf(userList.get(i).getId()),
                        String.valueOf(userList.get(i).getAlpha()),
                        String.valueOf(userList.get(i).getA()),
                        String.valueOf(userList.get(i).getB()),
                        String.valueOf(userList.get(i).getV()),
                        String.valueOf(userList.get(i).getW()),
                        String.valueOf(userList.get(i).getq()),
                        String.valueOf(userList.get(i).getqCurrent()),
                        String.valueOf(userList.get(i).getu()),
                        String.valueOf(userList.get(i).getIsParticipatingNextSimulation())};
                writer.writeNext(dataRow);
            }


            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void writeDealsListToCSVFile(ArrayList<DealResult> dealList, String filePath)
    {
        // first create file object for file placed at location
        // specified by filepath
        File file = new File(filePath);

        try {
            file.createNewFile();
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);

            // adding header to csv
            String[] header = { "result",
                    "buyerId",
                    "sellerId",
                    "priceOfDeal",
                    "amountOfQuantityInDeal"};
            writer.writeNext(header);

            // add data to csv
            for (int i = 0; i <dealList.size(); i++ )
            {
                String[] dataRow = {
                        String.valueOf(dealList.get(i).result),
                        String.valueOf(dealList.get(i).buyerId),
                        String.valueOf(dealList.get(i).sellerId),
                        String.valueOf(dealList.get(i).priceOfDeal),
                        String.valueOf(dealList.get(i).amountOfQuantityInDeal)
                };
                writer.writeNext(dataRow);
            }


            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
