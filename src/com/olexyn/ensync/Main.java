package com.olexyn.ensync;

import com.olexyn.ensync.artifacts.Sync;
import com.olexyn.ensync.artifacts.SyncMap;
import com.olexyn.ensync.ui.UI;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;


public class Main{



    final public static Sync sync = new Sync();



    final public static Thread uiThread = new Thread(new UI(), "ui");

    final public static Thread flowThread = new Thread(new Flow(), "flow");





    public static void main(String[] args) {

        sync.syncMaps.put("test", new SyncMap("test"));



        uiThread.start();

        flowThread.start();







    }




}
