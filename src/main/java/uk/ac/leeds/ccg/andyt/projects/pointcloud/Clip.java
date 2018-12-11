/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.leeds.ccg.andyt.projects.pointcloud;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.generic.io.Generic_IO;

/**
 * Start programming time 25 19:05. End programming time.
 *
 * @author geoagdt
 */
public class Clip {

    private Double xmin;
    private Double ymin;
    private Double zmin;
    private Double xmax;
    private Double ymax;
    private Double zmax;

    public Clip() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
//            // TODO code application logic here
//            if (args.length < 4) {
//                //throw new Exception("Expected 4 arguments: xmin xmax ymin ymax");
//                args = new String[4];
//                args[0] = "-322.7";
//                args[1] = "-316.7";
//                args[2] = "394.8";
//                args[3] = "401.2";
//            }
//            new Clip().run(args);
            new Clip().run();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            Logger.getLogger(Clip.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run( //String[] args
            )
            throws Exception {
        File cwd = new File(System.getProperty("user.dir"));
        System.out.println("System.getProperty(\"user.dir\") " + cwd);
        //setParameters(args, cwd);
        setParameters(cwd);
        cwd = cwd.getParentFile();
        File[] files = cwd.listFiles();
        File outputDir = new File(cwd, "ClippedData");
        HashSet<String> reservedFilenames = getReservedFilenames();
        outputDir.mkdir();
        for (File file : files) {
            //if (!file.isDirectory()) {
            String filename = file.getName();
            if (!reservedFilenames.contains(filename)) {
                processFile(outputDir, file);
            }
            //}
        }
    }

    public void setParameters(
            //String[] args, 
            File cwd)
            throws Exception {
//        xmin = new Double(args[0]);
//        xmax = new Double(args[1]);
//        ymin = new Double(args[2]);
//        ymax = new Double(args[3]);
        File parametersFile = new File(cwd, "parameters.txt");
        BufferedReader br = Generic_IO.getBufferedReader(parametersFile);
        while (true) {
            String line = br.readLine();
            if (line != null) {
                System.out.println(line);
                if (line.split(" ").length == 2) {
                    if (xmin == null) {
                        xmin = new Double(line.split(" ")[1]).doubleValue();
                    } else {
                        if (xmax == null) {
                            xmax = new Double(line.split(" ")[1]).doubleValue();
                        } else {
                            if (ymin == null) {
                                ymin = new Double(line.split(" ")[1]).doubleValue();
                            } else {
                                if (ymax == null) {
                                    ymax = new Double(line.split(" ")[1]).doubleValue();
                                }
                            }
                        }
                    }
                }
            } else {
                break;
            }
        }
        System.out.println("Using:");
        System.out.println("xmin " + xmin);
        System.out.println("xmax " + xmax);
        System.out.println("ymin " + ymin);
        System.out.println("ymax " + ymax);
    }

    public HashSet<String> getReservedFilenames() {
        HashSet<String> reservedFilenames = new HashSet<String>();
        reservedFilenames.add("geomorphometrics.jar");
        reservedFilenames.add("geomorphometrics.zip");
        reservedFilenames.add("lib");
        reservedFilenames.add("run.bat");
        reservedFilenames.add("run.sh");
        reservedFilenames.add("ClippedData");
        reservedFilenames.add("clip");
        reservedFilenames.add("clip.zip");
        reservedFilenames.add("README.txt");
        reservedFilenames.add("licence.txt");
        reservedFilenames.add("parameters.txt");
        return reservedFilenames;
    }

    public void processFile(
            File outputDir,
            File input) throws Exception {
        int n = 9;
        double xminObserved = Double.POSITIVE_INFINITY;
        double xmaxObserved = Double.NEGATIVE_INFINITY;
        double yminObserved = Double.POSITIVE_INFINITY;
        double ymaxObserved = Double.NEGATIVE_INFINITY;

        BufferedReader br = Generic_IO.getBufferedReader(input);
        String name = input.getName();
        String[] nameSplit = name.split("\\.");
        File output = new File(outputDir, name);
        boolean outputFileCreated;
        outputFileCreated = createFile(output);
        int i = 0;
        while (!outputFileCreated) {
            String newName = name;
            if (nameSplit.length == 2) {
                newName = nameSplit[0] + i + "." + nameSplit[1];
            } else {
                newName += i;
            }
            output = new File(outputDir, newName);
            outputFileCreated = createFile(output);
            i++;
            if (i > n) {
                throw new Exception("Unable to create output file write after " + n + " attempts.");
            }
        }
        PrintWriter pw = null;
        try {
            pw = new PrintWriter(output);
        } catch (FileNotFoundException ex) {
            System.err.println("Unable to write to file " + output);
            Logger.getLogger(Clip.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (pw != null) {
            int lineCount = 0;
            int recordsIn = 0;
            int recordsOut = 0;
            try {
                while (true) {
                    String line = br.readLine();
//                    if (!line.isEmpty()) {
                    if (line != null) {
                        String[] fields = line.split(" ");
                        double x = Double.valueOf(fields[0]);
                        double y = Double.valueOf(fields[1]);
                        double z = Double.valueOf(fields[2]);
                        int r = Integer.valueOf(fields[3]);
                        int g = Integer.valueOf(fields[4]);
                        int b = Integer.valueOf(fields[5]);
                        if (x >= xmin) {
                            if (x <= xmax) {
                                if (y >= ymin) {
                                    if (y <= ymax) {
                                        pw.println(fields[0] + " " + fields[1] + " " + fields[2] + " " + fields[3] + " " + fields[4] + " " + fields[5]);
                                        recordsIn++;
                                    } else {
                                        recordsOut++;
                                    }
                                } else {
                                    recordsOut++;
                                }
                            } else {
                                recordsOut++;
                            }
                        } else {
                            recordsOut++;
                        }
                        xminObserved = Math.min(xminObserved, x);
                        xmaxObserved = Math.max(xmaxObserved, x);
                        yminObserved = Math.min(yminObserved, y);
                        ymaxObserved = Math.max(ymaxObserved, y);
                        lineCount++;
                        if (lineCount % 1000000 == 0) {
                            reportProgress(lineCount, recordsIn, recordsOut, xminObserved, xmaxObserved, yminObserved, ymaxObserved);
                        }
//                        }
                    } else {
                        break;
                    }
                }
            } catch (IOException ex) {
                // Presume we get here once we've read all the lines of input
                Logger.getLogger(Clip.class.getName()).log(Level.SEVERE, null, ex);
            }
            pw.close();
            reportProgress(lineCount, recordsIn, recordsOut, xminObserved, xmaxObserved, yminObserved, ymaxObserved);
        }
    }

    public void reportProgress(
            int lineCount,
            int recordsIn,
            int recordsOut,
            double xminObserved,
            double xmaxObserved,
            double yminObserved,
            double ymaxObserved) {
        System.out.print("Processed " + lineCount + " lines:");
        System.out.print(" Records in " + recordsIn);
        System.out.print(" Records out " + recordsOut);
        System.out.print(" xminObserved " + xminObserved);
        System.out.print(" xmaxObserved " + xmaxObserved);
        System.out.print(" yminObserved " + yminObserved);
        System.out.println(" ymaxObserved " + ymaxObserved);

    }

    public boolean createFile(File f) {
        boolean result = false;
        try {
            result = f.createNewFile();
        } catch (IOException ex) {
            System.err.println("Unable to create file " + f);
            Logger.getLogger(Clip.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
