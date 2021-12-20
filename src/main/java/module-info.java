module com.hit.ariel.waterallocationpolicysimulator {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.hit.ariel.waterallocationpolicysimulator to javafx.fxml;
    exports com.hit.ariel.waterallocationpolicysimulator;
}