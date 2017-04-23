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
public class PersonExtract extends Thread {
    private String fname;
    
    public PersonExtract(String s) {
        this.fname = s;
    }
    
    public void run() {
        ArrayList al = personByLine();
        for (Iterator it=al.iterator(); it.hasNext(); ) {
            //System.out.println("Record: "+it.next());
            System.out.println(it.next());
        }
    }
    
    private ArrayList personByLine() {
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
                //System.out.println("Line data("+count+"): "+line);
                // skip these lines
                String[] skips = {"PERSON [I! ].+", "THE CHINESE REC.+", ".+cont.", "[0-9][0-9][0-9]"};
                boolean skipfound = false;
                for (int i=0; i<skips.length; i++) {
                    if (Pattern.matches(skips[i], line))
                        skipfound = true;
                }
                if (skipfound) continue;
                
                if (collecting) {
                    String[] regex = {"[A-Z].+-.+", "1[0-9][0-9][0-9].*-.+"};
                    for (int i=0; i<regex.length; i++) {
                        if (Pattern.matches(regex[i], line)) { 
                            // if next record start while collecting
                            // record finished, final polish
                            // 1. change all "." to "," except the final one
                            record.replace(".", ",");
                            record = record.substring(0, record.length()-1)+".";
                            result.add(record);
                            // next record continues
                            record = line;
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
                    }
                    
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
                    String[] regex = {"[A-Z].+-.+", "1[0-9][0-9][0-9].*-.+"};
                    boolean found = false;
                    for (int i=0; i<regex.length; i++) {
                        if (Pattern.matches(regex[i], line)) { 
                            found = true;
                            // if next record start while collecting
                            // record finished, final polish
                            if (!"".equals(record)) {
                                record.replace(".", ",");
                                record = record.substring(0, record.length()-1)+".";
                                result.add(record);
                            }
                            collecting = true;
                            // next record continues
                            record = line;
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
                    }
                    if (!found) {
                        System.out.println("Orphan line("+count+"): "+line);                        
                    }
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Person by line error: "+e);
            result = new ArrayList();
        }
        return result;
    }
    
    public static void main(String[] args) {
        PersonExtract pe = new PersonExtract(args[0]);
        pe.start();
    }

}
