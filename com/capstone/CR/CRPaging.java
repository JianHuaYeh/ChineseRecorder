/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.capstone.CR;

import java.io.*;
import java.util.*;
import java.util.regex.*;

/**
 *
 * @author jhyeh
 */
public class CRPaging {
    
    private Hashtable pagePool;
    private String fname;
    
    public CRPaging(String s) {
        pagePool = new Hashtable();
        fname = s;
    }
    
    public boolean doPaging() {
      boolean modified = true;
      try {
        //System.out.print("Processing "+fname+"...");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fname), "utf-8"));
        String line="";
        int count = 0;
        BufferedWriter bw = null;
        while ((line=br.readLine()) != null) {
            if (bw == null) {
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fname+"."+count), "utf-8"));
            }
            bw.write(line);
            bw.newLine();
            // try to match paging mark
            //String regex = "[0-9]+CHI.+REC.+";
            // rule 1: standard text line
            // left page
            String[] regex = {"[0-9]+ T[A-Z]+ CH[A-Z]+ RE[A-Z]+.+",
                              "[A-Za-z0-9]+ T[A-Za-z]+ CH[A-Z~�,]+ RE[A-Z&~]+.+",
                              "T[A-Za-z]+.CH[A-Z~�,]+ [A-Z&~]+.+",
                              "[0-9]+ T[a-z]+ C[a-z]+ R[a-z]+.+\\[.+",
            // right page
                              "[a-z1][89][0-9][0-9].[\\]J1] [A-Z]+.+[0-9]+",
                              "[a-z1][89][0-9][0-9].[ ][\\]J1] [A-Z]+.+", 
                              "[a-z1][89][0-9][0-9]. [A-Z' ]+ [0-9]+",
                              "[a-z1][89][0-9][0-9].[\\]J1] .+",
                              "[a-z1][89][0-9][0-9][\\]J1] .+[0-9]+"};
            for (int i=0; i<regex.length; i++) {
                if (Pattern.matches(regex[i], line)) { // new page found
                    //System.out.println(line);
                    // close the current file, increase counter
                    bw.flush();
                    bw.close();
                    System.out.println(fname+"."+count+" generated.");
                    bw = null;
                    count++;
                    break;
                }
            }
        }
        br.close();
      } catch (Exception e) {
          System.out.println(e);
      }
      return modified;
    }
    
    public static void main(String[] args) {
        CRPaging crp = new CRPaging(args[0]);
        crp.doPaging();
        
    }
}
