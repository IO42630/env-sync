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

    final private static Flow flow = new Flow();

    final private static UI ui = new UI();







    public static void main(String[] args) {

        sync.syncMaps.put("test", new SyncMap("test"));


        Thread uiThread = new Thread(ui, "ui");
        uiThread.start();


        Thread flowThread = new Thread(flow, "flow");
        flowThread.start();







    }




}
