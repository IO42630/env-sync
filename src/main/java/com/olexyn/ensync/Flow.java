package com.olexyn.ensync;

import com.olexyn.ensync.artifacts.MapOfSyncMaps;
import com.olexyn.ensync.artifacts.SyncDirectory;
import com.olexyn.ensync.artifacts.SyncMap;

import java.io.File;
import java.util.Map.Entry;



public class Flow implements Runnable {


    Tools tools = new Tools();

    public long pollingPause = 200;


    private String state;


    public void run() {

        while (true) {

            synchronized (MapOfSyncMaps.get()) {

                readOrMakeStateFile();

                for (Entry<String, SyncMap> syncMapEntry : MapOfSyncMaps.get().entrySet()) {

                    for (Entry<String, SyncDirectory> SDEntry : syncMapEntry.getValue().syncDirectories.entrySet()) {

                        doSyncDirectory(SDEntry.getValue());
                    }
                }
            }
            try {
                System.out.println("Pausing... for " + pollingPause + "ms.");
                Thread.sleep(pollingPause);
            } catch (InterruptedException ignored) {}
        }
    }


    private void doSyncDirectory(SyncDirectory SD) {
        state = "READ";
        SD.readStateFromFS();

        SD.listCreated = SD.makeListOfLocallyCreatedFiles();
        SD.listDeleted = SD.makeListOfLocallyDeletedFiles();
        SD.listModified = SD.makeListOfLocallyModifiedFiles();

        SD.doCreateOpsOnOtherSDs();
        SD.doDeleteOpsOnOtherSDs();
        SD.doModifyOpsOnOtherSDs();

        SD.writeStateFile(SD.path);
    }


    public String getState() {
        return state == null ? "NONE" : state;
    }


    /**
     * For every single SyncDirectory try to read it's StateFile. <p>
     * If the StateFile is missing, then create a StateFile.
     */
    private void readOrMakeStateFile() {
        for (var syncMapEntry : MapOfSyncMaps.get().entrySet()) {
            SyncMap syncMap = syncMapEntry.getValue();
            state = syncMap.toString();

            for (var stringSyncDirectoryEntry : syncMap.syncDirectories.entrySet()) {
                SyncDirectory SD = stringSyncDirectoryEntry.getValue();
                String path = SD.path;
                String stateFilePath = tools.stateFilePath(path);

                if (new File(stateFilePath).exists()) {
                    state = "READ-STATE-FILE-" + SD.readStateFile();
                } else {
                    SD.writeStateFile(path);
                }
            }

        }
    }
}
