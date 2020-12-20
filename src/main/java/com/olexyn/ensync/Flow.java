package com.olexyn.ensync;

import com.olexyn.ensync.artifacts.SyncDirectory;
import com.olexyn.ensync.artifacts.SyncMap;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import static com.olexyn.ensync.Main.MAP_OF_SYNCMAPS;

public class Flow implements Runnable {


    Tools tools = new Tools();


    private String state;


    public void run() {

        while (true) {

            synchronized (MAP_OF_SYNCMAPS) {

                readOrMakeStateFile();

                for (Entry<String, SyncMap> syncMapEntry : MAP_OF_SYNCMAPS.entrySet()) {

                    for (Entry<String, SyncDirectory> SDEntry : syncMapEntry.getValue().syncDirectories.entrySet()) {

                        doSyncDirectory(SDEntry.getValue());
                    }
                }
            }
            try {
                long pause = 2000;
                System.out.println("Pausing... for " + pause + "ms.");
                Thread.sleep(pause);
            } catch (InterruptedException ignored) {}
        }
    }


    private void doSyncDirectory(SyncDirectory SD) {
        state = "READ";
        SD.readFreshState();

        SD.listCreated = SD.makeListCreated();
        SD.listDeleted = SD.makeListDeleted();
        SD.listModified = SD.makeListModified();

        SD.doCreate();
        SD.doDelete();
        SD.doModify();

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
        for (var syncMapEntry : MAP_OF_SYNCMAPS.entrySet()) {
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
