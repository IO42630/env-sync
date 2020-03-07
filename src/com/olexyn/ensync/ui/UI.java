package com.olexyn.ensync.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class UI extends Application {







    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
        Scene scene = new Scene(root, 500, 500);





        primaryStage.setTitle("EnSync");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        UI.launch(args);

    }



}
