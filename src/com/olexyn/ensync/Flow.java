package com.olexyn.ensync;

import com.olexyn.ensync.artifacts.SyncDirectory;
import com.olexyn.ensync.artifacts.SyncMap;

import java.io.File;
import java.util.Map;

public class Flow {


    public Flow(){

    }




    public void start() {
        File asdf = new File("/home/user/");
        System.out.println(asdf.lastModified());

        Tools tools = new Tools();
        Execute x = new Execute();


        SyncMap syncMap = new SyncMap("test");
        syncMap.addDirectory("/home/user/test/a");
        syncMap.addDirectory("/home/user/test/b");
        //syncMap.addDirectory("/home/user/test/c");


        for (Map.Entry<String, SyncDirectory> entry : syncMap.syncDirectories.entrySet()) {
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

            for (Map.Entry<String, SyncDirectory> entry : syncMap.syncDirectories.entrySet()) {

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
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }
}
