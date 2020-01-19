package com.olexyn.ensync.artifacts;

import java.io.File;

public class SyncFile extends File {


    // Very IMPORTANT field. Allows to store lastModified as it is stored in the StateFile.
    private long lastModifiedOld = 0;


    public SyncFile(String pathname) {
        super(pathname);
    }



    public long lastModifiedOld(){
        return lastModifiedOld;
    }

    public void setLastModifiedOld(long value){
        lastModifiedOld = value;
    }

}
