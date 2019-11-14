/*
 * Copyright 2019 Centre for Computational Geography, University of Leeds.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package uk.ac.leeds.ccg.andyt.projects.pointcloud;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.ac.leeds.ccg.andyt.projects.geomorphometrics.core.G_Environment;
import uk.ac.leeds.ccg.andyt.projects.geomorphometrics.core.G_Object;

/**
 * Old code for clipping some pointcloud data for Mark Smith.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class Clip extends G_Object {

    private Double xmin;
    private Double ymin;
    private Double zmin;
    private Double xmax;
    private Double ymax;
    private Double zmax;

    public Clip( G_Environment e) {
        super(e);
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
            new Clip(new G_Environment()).run();
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
        BufferedReader br = env.env.io.getBufferedReader(parametersFile);
        while (true) {
            String line = br.readLine();
            if (line != null) {
                System.out.println(line);
                if (line.split(" ").length == 2) {
                    if (xmin == null) {
                        xmin = Double.parseDouble(line.split(" ")[1]);
                    } else {
                        if (xmax == null) {
                            xmax = Double.parseDouble(line.split(" ")[1]);
                        } else {
                            if (ymin == null) {
                                ymin = Double.parseDouble(line.split(" ")[1]);
                            } else {
                                if (ymax == null) {
                                    ymax = Double.parseDouble(line.split(" ")[1]);
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
        HashSet<String> reservedFilenames = new HashSet<>();
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

        BufferedReader br = env.env.io.getBufferedReader(input);
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
        PrintWriter pw = env.env.io.getPrintWriter(output, false);
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
