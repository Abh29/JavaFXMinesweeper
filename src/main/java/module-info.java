module com.example.javafxminnergame {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.jfoenix;

    opens com.example.javafxminnergame.controllers to javafx.fxml;
    exports com.example.javafxminnergame.controllers;
    exports com.example.javafxminnergame;
}