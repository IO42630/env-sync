package com.olexyn.ensync;

import com.olexyn.ensync.artifacts.SyncMap;
import com.olexyn.ensync.ui.UI;

import java.util.HashMap;


public class Main{

    final public static Thread UI_THREAD = new Thread(new UI(), "ui");
    final public static Thread FLOW_THREAD = new Thread(new Flow(), "flow");
    final public static HashMap<String, SyncMap> MAP_OF_SYNCMAPS = new HashMap<>();

    public static void main(String[] args) {

        UI_THREAD.start();
        FLOW_THREAD.start();
    }
}
