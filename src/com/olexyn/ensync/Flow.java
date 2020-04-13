package com.olexyn.ensync;

import com.olexyn.ensync.artifacts.SyncDirectory;
import com.olexyn.ensync.artifacts.SyncMap;

import java.io.File;

public class Flow implements Runnable {


    private String state;


    public void run() {




        while (true) {

            initialize();

            for (var syncMapEntry : Main.SYNC.entrySet()) {


                for (var syncDirectoryEntry : syncMapEntry.getValue().syncDirectories.entrySet()) {

                    SyncDirectory syncDirectory = syncDirectoryEntry.getValue();

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


            }
            try {
                System.out.println("Pausing...");
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {

            }

        }
    }


    public String getState() {
        return state == null ? "NONE" : state;
    }


    /**
     * For every single SyncDirectory try to read it's StateFile. <p>
     * If the StateFile is missing, then create a StateFile.
     */
    private void initialize() {
        for (var syncMapEntry : Main.SYNC.entrySet()) {
            SyncMap syncMap = syncMapEntry.getValue();
            state = syncMap.toString();

            for (var stringSyncDirectoryEntry : syncMap.syncDirectories.entrySet()) {
                SyncDirectory syncDirectory = stringSyncDirectoryEntry.getValue();
                String path = syncDirectory.path;
                String stateFilePath = syncDirectory.stateFilePath(path);

                if (new File(stateFilePath).exists()) {
                    state = "READ-STATE-FILE-" + syncDirectory.readStateFile();
                } else {
                    syncDirectory.writeStateFile(path);
                }
            }

        }
    }
}
