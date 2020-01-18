package com.olexyn.ensync;

import com.olexyn.ensync.artifacts.SyncDirectory;
import com.olexyn.ensync.artifacts.SyncEntity;

import java.io.File;
import java.util.*;

public class Main {


    public static void main(String[] args) {


        Tools tools = new Tools();
        Execute x = new Execute();


        SyncEntity syncEntity = new SyncEntity("test");
        syncEntity.addDirectory("/home/user/test/a");
        syncEntity.addDirectory("/home/user/test/b");
        syncEntity.addDirectory("/home/user/test/c");

        int br1 = 0;

        while (true) {

            for (Map.Entry<String, SyncDirectory> entry : syncEntity.syncDirectories.entrySet()) {

                SyncDirectory syncDirectory = entry.getValue();


                syncDirectory.updateStateFileNew();
                syncDirectory.updatePoolNew();

                //
                for (File createdFile : syncDirectory.getListCreated()) {
                    for (Map.Entry<String, SyncDirectory> otherEntry : syncEntity.syncDirectories.entrySet()) {
                        SyncDirectory otherSyncDirectory = otherEntry.getValue();

                        if (!syncDirectory.equals(otherSyncDirectory)){
                            // Example:
                            //  syncDirectory /foo
                            //  otherSyncDirectory /bar
                            //  createdFile  /foo/hello/created-file.gif
                            //  relativePath /hello/created-file.gif
                            String relativePath = createdFile.getPath().replace(syncDirectory.realPath, "");
                            String targetPath = otherSyncDirectory.realPath + relativePath;
                            String targetParentPath = new File(targetPath).getParent();
                            if (!new File(targetParentPath).exists()){
                                String[] cmd = new String[]{"mkdir",
                                                            "-p",
                                                            targetParentPath};
                                x.execute(cmd);
                            }

                            String[] cmd = new String[]{"cp",
                                                        createdFile.getPath(),
                                                        otherSyncDirectory.realPath + relativePath};
                            x.execute(cmd);
                        }
                    }

                }

                //
                for (File deletedFile : syncDirectory.getListDeleted()) {

                    for (Map.Entry<String, SyncDirectory> otherEntry : syncEntity.syncDirectories.entrySet()) {
                        SyncDirectory otherSyncDirectory = otherEntry.getValue();

                        if (!syncDirectory.equals(otherSyncDirectory)){
                            String relativePath = deletedFile.getPath().replace(syncDirectory.realPath, "");
                            String[] cmd = new String[]{"rm", "-r",
                                                        otherSyncDirectory.realPath + relativePath};
                            x.execute(cmd);
                        }
                    }

                }


                
//
                // WARNING:
                //  Be very carefull when to update the StateFileOld
                //  i.e. you create a File and update StateFileOld without updating
                //   -> create newFile -> update StateFileNew-> getListCreated contains newFile -> addToMapCreated -> create copies as needed -> updateStateFileOld -> OK
                //   -> create newFile -> update StateFileOld -> getListDeletd contains newFile -> VERY BAD
                //
                syncDirectory.updateStateFileBoth();
                syncDirectory.updatePoolBoth();




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
