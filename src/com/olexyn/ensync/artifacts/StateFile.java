package com.olexyn.ensync.artifacts;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class StateFile {

    private final String[] types = new String[]{ "OLD" , "NEW"};

    public String stateFilePath;
    public File stateFileOld;
    private Map<String, File> poolOld = new HashMap<>();


    public StateFile(){

    }




    public void update(){

    }



}
