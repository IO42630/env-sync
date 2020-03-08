package com.olexyn.ensync;

import com.olexyn.ensync.artifacts.SyncDirectory;
import com.olexyn.ensync.artifacts.SyncMap;

import java.io.File;
import java.util.Map;

public class Flow implements Runnable{



    private String state;


    public void run() {





        for (Map.Entry<String, SyncMap> mapEntry : Main.sync.syncMaps.entrySet()) {
            SyncMap syncMap = mapEntry.getValue();
            state = syncMap.toString();

            for (Map.Entry<String, SyncDirectory> entry : syncMap.syncDirectories.entrySet()) {
                SyncDirectory syncDirectory = entry.getValue();
                String path = syncDirectory.path;
                String stateFilePath = syncDirectory.stateFilePath(path);

                if (new File(stateFilePath).exists()) {
                    state = "READ-STATE-FILE-"+
                    syncDirectory.readStateFile();
                } else {
                    syncDirectory.writeStateFile(path);
                }
            }


            while (true) {

                for (Map.Entry<String, SyncDirectory> entry : mapEntry.getValue().syncDirectories.entrySet()) {

                    SyncDirectory syncDirectory = entry.getValue();

                    String path = syncDirectory.path;

                    state = "READ";
                    syncDirectory.readState();

                    syncDirectory.makeListCreated();
                    syncDirectory.makeListDeleted();
                    syncDirectory.makeListModified();

                    syncDirectory.doCreate();
                    syncDirectory.doDelete();
                    syncDirectory.doModify();


                    syncDirectory.writeStateFile(path);




                }

                try {
                    System.out.println("Pausing...");
                    Main.flowThread.sleep(2000);
                } catch (InterruptedException e) {

                }
            }
        }
    }


    public String getState() {
        return state==null ? "NONE" : state;
    }
}
