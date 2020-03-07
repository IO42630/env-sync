package com.olexyn.ensync;

import com.olexyn.ensync.artifacts.SyncDirectory;
import com.olexyn.ensync.artifacts.SyncMap;

import java.io.File;
import java.util.Map;

public class Flow implements Runnable{






    public void run() {





        for (Map.Entry<String, SyncMap> mapEntry : Main.sync.syncMaps.entrySet()) {


            for (Map.Entry<String, SyncDirectory> entry : mapEntry.getValue().syncDirectories.entrySet()) {
                SyncDirectory syncDirectory = entry.getValue();
                String path = syncDirectory.path;
                String stateFilePath = syncDirectory.stateFilePath(path);

                if (new File(stateFilePath).exists()) {
                    syncDirectory.readStateFile();
                } else {
                    syncDirectory.writeStateFile(path);
                }
            }


            while (true) {

                for (Map.Entry<String, SyncDirectory> entry : mapEntry.getValue().syncDirectories.entrySet()) {

                    SyncDirectory syncDirectory = entry.getValue();

                    String path = syncDirectory.path;

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
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
            }
        }
    }

}
