package com.olexyn.ensync;

import com.olexyn.ensync.artifacts.SyncMap;
import com.olexyn.ensync.ui.UI;

import java.util.HashMap;
import java.util.Map;


public class Main{






    final public static Thread uiThread = new Thread(new UI(), "ui");

    final public static Thread flowThread = new Thread(new Flow(), "flow");

    final public static Map<String, SyncMap> SYNC = new HashMap<>();



    public static void main(String[] args) {

        SYNC.put("test", new SyncMap("test"));



        uiThread.start();

        flowThread.start();







    }




}
