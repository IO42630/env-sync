package com.olexyn.ensync;

import com.olexyn.ensync.artifacts.SyncDirectory;
import com.olexyn.ensync.artifacts.SyncEntity;

import java.util.*;

public class Main {


    public static void main(String[] args) {


        Tools tools = new Tools();
        Execute x = new Execute();


        SyncEntity syncEntity = new SyncEntity("test");
        syncEntity.addDirectory("/home/user/test/a");
        syncEntity.addDirectory("/home/user/test/b");
        //syncEntity.addDirectory("/home/user/test/c");

        int br1 = 0;

        while (true) {

            for (Map.Entry<String, SyncDirectory> entry : syncEntity.syncDirectories.entrySet()) {

                SyncDirectory syncDirectory = entry.getValue();


                syncDirectory.updateStateFileNew();
                syncDirectory.updatePoolNew();

                syncDirectory.getListCreated();
                syncDirectory.getListDeleted();


                //

                 syncDirectory.doSyncOps();


//
                // WARNING:
                //  Be very carefull when to update the StateFileOld
                //  i.e. you create a File and update StateFileOld without updating
                //   -> create newFile -> update StateFileNew-> getListCreated contains newFile -> addToMapCreated -> create copies as needed -> updateStateFileOld -> OK
                //   -> create newFile -> update StateFileOld -> getListDeletd contains newFile -> VERY BAD
                //
                syncDirectory.updateStateFileNew();
                syncDirectory.updatePoolNew();

                syncDirectory.updateStateFileOld();
                syncDirectory.updatePoolOld();




            }
            //Map<String,File> mapCreated = syncEntity.getMapCreated();
            //Map<String,File> mapDeleted = syncEntity.getMapDeleted();

            int br = 0;
            try {Thread.sleep(1000);}
            catch (InterruptedException e){

            }
        }
    }
}
