package com.olexyn.ensync;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class Main {



    public static void main(String[] args) {


        String conf = new Tools().getConf();
        List<String[]> cmdBuffer = new Routines().parseConfToCmdBuffer(conf);

        System.out.println("bar");
        new Execute().executeBatch(cmdBuffer);


        String br = null;

    }
}
