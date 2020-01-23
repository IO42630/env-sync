package com.olexyn.ensync;

import com.olexyn.ensync.artifacts.SyncDirectory;
import com.olexyn.ensync.artifacts.SyncEntity;
import com.olexyn.ensync.artifacts.SyncFile;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Flow {


    public Flow(){

    }




    public void start() {
        File asdf = new File("/home/user/");
        System.out.println(asdf.lastModified());

        Tools tools = new Tools();
        Execute x = new Execute();


        SyncEntity syncEntity = new SyncEntity("test");
        syncEntity.addDirectory("/home/user/test/a");
        syncEntity.addDirectory("/home/user/test/b");
        //syncEntity.addDirectory("/home/user/test/c");


        for (Map.Entry<String, SyncDirectory> entry : syncEntity.syncDirectories.entrySet()) {
            SyncDirectory syncDirectory = entry.getValue();
            String path = syncDirectory.path;
            String stateFilePath = syncDirectory.stateFilePath(path);
            
            if (new File(stateFilePath).exists()) {
                syncDirectory.readStateFile(syncDirectory.path);
            } else {
                syncDirectory.writeStateFile(path);
            }
        }


        while (true) {

            for (Map.Entry<String, SyncDirectory> entry : syncEntity.syncDirectories.entrySet()) {

                SyncDirectory syncDirectory = entry.getValue();

                String path = syncDirectory.path;

                    syncDirectory.readState(path);

                    syncDirectory.makeListCreated(path);
                    syncDirectory.makeListDeleted(path);
                    syncDirectory.makeListModified(path);

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
