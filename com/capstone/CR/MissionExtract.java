/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.capstone.CR;

import java.util.*;
import java.util.regex.*;
import java.io.*;
/**
 *
 * @author jhyeh
 */
public class MissionExtract extends Thread {
    private String fname;
    
    public MissionExtract(String s) {
        this.fname = s;
    }
    
    public void run() {
        ArrayList al = missionByLine();
        for (Iterator it=al.iterator(); it.hasNext(); ) {
            //System.out.println("Record: "+it.next());
            System.out.println(it.next());
        }
    }
    
    private ArrayList missionByLine() {
        ArrayList result = new ArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fname));
            String line="";
            String record="";
            boolean collecting = false;
            int count = 0;
            while ((line=br.readLine()) != null) {
                line = line.trim();
                count++;
                if (collecting) {
                    record += line;
                    if (line.endsWith(".")) {
                        // record finished, final polish
                        // 1. change all "." to "," except the final one
                        record.replace(".", ",");
                        record = record.substring(0, record.length()-1)+".";
                        result.add(record);
                        record = "";
                        collecting = false;
                    }
                }
                else {
                    // find if a record start signature found
                    String regex = "[A-Z].+";
                    if (Pattern.matches(regex, line)) { // record start
                        record = line;
                        collecting = true;
                        if (line.endsWith(".")) {
                            // record finished, final polish
                            // 1. change all "." to "," except the final one
                            record.replace(".", ",");
                            record = record.substring(0, record.length()-1)+".";
                            result.add(record);
                            record = "";
                            collecting = false;
                        } 
                    }
                    else {
                        System.out.println("Orphan line("+count+"): "+line);
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Mission by line error: "+e);
            result = new ArrayList();
        }
        return result;
    }
    
    public static void main(String[] args) {
        MissionExtract me = new MissionExtract(args[0]);
        me.start();
    }

}
