package com.hit.waterallocationpolicysimulator.utils;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;

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
            timer = ThreadManger.getInstance().getScheduledThreadPool().schedule(() -> {
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
}
