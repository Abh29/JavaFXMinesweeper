package com.example.javafxminnergame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayDeque;
import java.util.Queue;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        System.out.println(new File("views/grid.fxml").toURI().toURL());

//        Parent root = FXMLLoader.load(new File("views/grid.fxml").toURI().toURL());
        Parent root = new FXMLLoader(Main.class.getResource("views/grid.fxml")).load();
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 700, 800));
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();

    }

    public static void main(String[] args) {
        Queue<Integer> queue = new ArrayDeque<>();

        launch(args);
    }
}
