package com.olexyn.ensync;

import com.olexyn.ensync.artifacts.SyncMap;
import com.olexyn.ensync.ui.UI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Main{

    final public static Thread UI_THREAD = new Thread(new UI(), "ui");
    final public static Thread FLOW_THREAD = new Thread(new Flow(), "flow");
    final public static HashMap<String, SyncMap> MAP_OF_SYNCMAPS = new HashMap<>();
    final private static Tools tools = new Tools();

    public static void main(String[] args) {

        OperationMode operationMode = OperationMode.JSON;

        switch (operationMode) {
            case JAVA_FX:
                UI_THREAD.start();
                break;
            case JSON:
                String configPath = System.getProperty("user.dir") + "/src/main/resources/config.json";
                String configString = tools.fileToString(new File(configPath));
                JSONObject jsonMapOfSyncMaps = new JSONObject(configString).getJSONObject("jsonMapOfSyncMaps");
                for (String key : jsonMapOfSyncMaps.keySet()) {
                    SyncMap syncMap = new SyncMap(key);
                    for (Object jsonSyncDirPath : jsonMapOfSyncMaps.getJSONArray(key).toList()) {
                        syncMap.addDirectory(jsonSyncDirPath.toString());
                    }
                    MAP_OF_SYNCMAPS.put(key, syncMap);
                }
                break;
            default:
        }


        FLOW_THREAD.start();
    }
}
