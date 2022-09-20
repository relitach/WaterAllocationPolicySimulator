package com.hit.waterallocationpolicysimulator.utils;

import com.hit.waterallocationpolicysimulator.model.User;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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


    public static List<User> parseCSVFileToUserList(File file)
    {
        List<User> usersList = new ArrayList<User>();

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

}
