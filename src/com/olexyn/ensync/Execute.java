package com.olexyn.ensync;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class Execute {


    /**
     * @param cmd an array representing a shell command
     * @return <i>TwoBr</i>  class, containing two BufferedReaders,
     * <i>output</i> and  <i>error</i>
     * @see <i>output</i>  BufferedReader, corresponds to STDOUT
     * <i>error</i>  BufferedReader, corresponds to STDERR
     */
    public TwoBr execute(String cmd[]) {
        TwoBr twobr = new TwoBr();
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
            twobr.output = new BufferedReader(new InputStreamReader(process.getInputStream()));
            twobr.error = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return twobr;
    }

    public void executeBatch(List<String[]> batch){

        for (int i =0; i<batch.size();i++){
            execute(batch.get(i));
        }

    }




    public class TwoBr {
        public BufferedReader output;
        public BufferedReader error;
    }
}
