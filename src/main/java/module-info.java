module com.hit.ariel.waterallocationpolicysimulator {
    requires javafx.controls;
    requires javafx.fxml;
    requires AnimateFX;
    requires opencsv;

    opens com.hit.waterallocationpolicysimulator to javafx.fxml;
    exports com.hit.waterallocationpolicysimulator;
    exports com.hit.waterallocationpolicysimulator.view;
    opens com.hit.waterallocationpolicysimulator.view to javafx.fxml;
    opens com.hit.waterallocationpolicysimulator.model to javafx.fxml, javafx.base;
}