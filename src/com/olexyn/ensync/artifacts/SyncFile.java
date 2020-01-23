package com.olexyn.ensync.artifacts;

import java.io.File;

public class SyncFile extends File {


    // Very IMPORTANT field. Allows to store lastModified as it is stored in the StateFile.
    private long stateFileTime = 0;



    public SyncFile(String pathname) {
        super(pathname);
    }



    public long stateFileTime(){
        return stateFileTime;
    }

    public void setStateFileTime(long value){
        stateFileTime = value;
    }





}
