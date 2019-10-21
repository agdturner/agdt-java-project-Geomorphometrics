/**
 * <one line to give the library's name and a brief idea of what it does.>
 * Copyright (C) 2005 Andy Turner, CCG, University of Leeds, UK
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package uk.ac.leeds.ccg.andyt.projects.geomorphometrics;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import uk.ac.leeds.ccg.andyt.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_AbstractGridNumber;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridDouble;
import uk.ac.leeds.ccg.andyt.grids.io.Grids_ImageExporter;
import uk.ac.leeds.ccg.andyt.grids.io.Grids_ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.andyt.grids.process.Grids_ProcessorDEM;
import uk.ac.leeds.ccg.andyt.grids.utilities.Grids_Utilities;
import uk.ac.leeds.ccg.andyt.grids.io.Grids_Files;

public class RoofGeneralisation extends Grids_ProcessorDEM {

    private long time;
    boolean Hoome;
    String FileSeparator;
    String Filename;

//    /**
//     * Creates a new RoofGeneralisation
//     */
//    protected RoofGeneralisation() throws IOException {
//        
//    }

    public RoofGeneralisation(Grids_Environment ge) throws IOException {
        super(ge);
        this.time = System.currentTimeMillis();
        this.Hoome = true;
        this.FileSeparator = System.getProperty("file.separator");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
        // _NotLoadedAsGrid = true?
        // Output images?
        // VM parameters: -Xmx1200m
        runOnPC();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    public static void runOnPC() throws IOException {
        //File dir = new File( "D:/Work/Projects/Geomorphometrics/Workspace/" );
        File dir = new File("C:/Work/People/Sadhvi Selvaraj/Roofs/data/synthetic/");
        //File dir = new File("C:/Work/People/Sadhvi Selvaraj/Roofs/data/NeuralNetwork/Input/");
        //File dir = new File( "C:/temp/GEOG5060/Geomorphometrics/" );
        System.out.print("" + dir.toString());
        if (dir.exists()) {
            System.out.println(" exists.");
        } else {
            System.out.println(" does not exist.");
        }
        Grids_Environment ge = new Grids_Environment(new Generic_Environment(dir), dir);
        RoofGeneralisation aRoofGeneralisation = new RoofGeneralisation(ge);
        aRoofGeneralisation.run();
    }

    public void run() {
        try {
            File inDir = env.files.getDir();
            File[] files = inDir.listFiles();
            String inputFilename;
            String asc = "asc";
            String inputFilenameWithoutExtension;
            File outDir;
            Grids_ImageExporter ie = new Grids_ImageExporter(env);
            File workspaceDirectory = new File(inDir + "/Workspace/");
            String[] imageTypes = new String[1];
            imageTypes[0] = "PNG";
            Grids_Files gf = env.files;
            File dir;
            for (int i = 0; i < files.length; i++) {
                inputFilename = files[i].getName();
                if (inputFilename.endsWith(asc)) {
                    // Initialisation
                    inputFilenameWithoutExtension = inputFilename.substring(
                            0, inputFilename.length() - 4);
                    outDir = new File(inDir + "/Geomorphometrics/"
                            + inputFilenameWithoutExtension + "/");
                    Grids_GridDouble g = null;
                    // Load input
                    boolean notLoadedAsGrid = true;
                    if (notLoadedAsGrid) {
                        File inputFile;
                        inputFile = new File(inDir, inputFilename);
                        dir = env.env.io.createNewFile(gf.getGeneratedGridDoubleDir());
                        g = (Grids_GridDouble) GridDoubleFactory.create(
                                dir, inputFile);
                        // Cache input
                        g.writeToFile();
                        env.getGrids().add(g);
//                        outputImage(g, outDirectory, ie, imageTypes);
                    } else {
                        System.out.println("check1");
//                        g = (Grids_GridDouble) GridDoubleFactory.create(
//                                new File(_Input_Directory.toString() + this.FileSeparator + _Input_Filename_WithoutExtension + "uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleFactory_chunkNrows(" + chunkNrows + ")_chunkNcols(" + chunkNcols + ")"));
//                        this._AbstractGrid2DSquareCell_HashSet.add(_Grid2DSquareCellDouble);
                    }

                    // generalise
                    run1(g, outDir, workspaceDirectory, ie);
                }
            }
        } catch (Exception | Error e) {
            e.printStackTrace(System.err);
        }
    }

    public void run1(
            Grids_GridDouble g,
            File outputDir,
            File workspaceDir,
            Grids_ImageExporter ie)
            throws Exception, Error {
        // Initialise
        log(0, "run1();");
        doMetrics1(g, outputDir, workspaceDir, ie);
        log(0, "Processing complete in "
                + Grids_Utilities.getTime(System.currentTimeMillis() - time));
    }

    public void doMetrics1(Grids_AbstractGridNumber g, File outDir0, 
            File workspaceDir0, Grids_ImageExporter ie) throws IOException {
        // Initialistaion
        Grids_ESRIAsciiGridExporter eage = new Grids_ESRIAsciiGridExporter(env);
        Filename = "Metrics1";
        File outDir;
        outDir = new File(outDir0, Filename);
        outDir.mkdirs();
        boolean swapOutInitialisedFiles = false;
        boolean swapOutProcessedChunks = false;
        String name;
        //Grids_Dimensions dimensions = g.getDimensions();
        double cellsize = g.getCellsizeDouble();
        double weightIntersect = 1.0d;
        double weightFactor = 1.0d;
        double distance;
        int distances;
        int i;
        double min = 0;
        double max = 1;
        for (distances = 2; distances <= 4; distances *= 2) {
            distance = cellsize * (double) distances;
            Grids_AbstractGridNumber[] metrics1 = getMetrics1(g, distance,
                    weightIntersect, weightFactor, GridDoubleFactory,
                    GridIntFactory, swapOutInitialisedFiles,
                    swapOutProcessedChunks);
            for (i = 0; i < metrics1.length; i++) {
                //rescale
                metrics1[i] = rescale(metrics1[i], null, min, max);
                // output
                name = metrics1[i].getName();
                if (name.startsWith("count_hhhl")
                        || name.startsWith("count_hhll")) {
                    outputESRIAsciiGrid(metrics1[i], outDir, eage, Hoome);
                }
            }
        }
    }

    public long getTime() {
        return time;
    }
}
