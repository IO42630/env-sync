package com.olexyn.ensync.artifacts;

import java.io.File;

public class SyncFile extends File {


    // Very IMPORTANT field. Allows to store lastModified as it is stored in the StateFile.
    private long stateFileTime = 0;

    public String relativePath;
    private SyncDirectory sd;



    public SyncFile(SyncDirectory sd , String pathname) {

        super(pathname);
        this.sd = sd;
        relativePath = this.getPath().replace(sd.path, "");
    }



    public long stateFileTime(){
        return stateFileTime;
    }

    public void setStateFileTime(long value){
        stateFileTime = value;
    }


    public long getFileTimeModified(SyncDirectory otherSD){
        if (this.exists()){
            return lastModified();
        }

        if (sd.readStateFile().get(this.getPath())!=null){

        }


        return  0;
    }


}
