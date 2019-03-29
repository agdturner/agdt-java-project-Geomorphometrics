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
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Dimensions;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridIntFactory;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridDoubleFactory;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_AbstractGridNumber;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridDouble;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.grids.core.grid.chunk.Grids_GridChunkDoubleArrayFactory;
import uk.ac.leeds.ccg.andyt.grids.core.grid.chunk.Grids_GridChunkIntArrayFactory;
import uk.ac.leeds.ccg.andyt.grids.io.Grids_ImageExporter;
import uk.ac.leeds.ccg.andyt.grids.io.Grids_ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.andyt.grids.process.Grids_ProcessorDEM;
import uk.ac.leeds.ccg.andyt.grids.utilities.Grids_Utilities;

/**
 * Originally this class was for processing some data from Sweden, but it has
 * also been used to process data from the Himalayan region.
 *
 * @author geoagdt
 */
public class Molly extends Grids_ProcessorDEM {

    private long Time;
    boolean HOOME;
    String Filename;

    protected Molly() {
    }

    /**
     * Creates a new RoofGeneralisation using specified Directory. WARNING:
     * Files in the specified Directory may get overwritten.
     *
     * @param ge
     */
    public Molly(Grids_Environment ge) {
        super(ge);
        Time = System.currentTimeMillis();
        HOOME = true;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //File Directory = new File("/nfs/see-fs-02_users/geoagdt/scratch01/Work/people/Scott Watson/test/Workspace/");
        //File Directory = new File("/nfs/see-fs-02_users/geoagdt/scratch01/Work/people/Scott Watson/Workspace/");
        File dir = new File("/nfs/see-fs-02_users/geoagdt/scratch01/Work/people/Molly/Workspace/");
        //File Directory = new File("C:/Temp/Owen/Workspace");
        //File Directory = new File("C:/Temp/Owen/Workspace2");
        System.out.print("" + dir.toString());
        if (dir.exists()) {
            System.out.println(" exists.");
            dir.mkdirs();
        } else {
            System.out.println(" does not exist.");
        }
        Grids_Environment ge = new Grids_Environment(dir);
        Molly t = new Molly(ge);
        t.run();
    }

    public void run() {
        try {
            ge.setProcessor(this);
            //boolean swapOutInitialisedFiles = true;
            boolean swapOutInitialisedFiles = false;
            File inDir = Files.getDataDir().getParentFile();
            File[] inputDirectoryFiles = inDir.listFiles();
            String inputFilename;
            String ascString = "asc";
            String inputFilenameWithoutExtension;
            File outDir;
            Grids_ESRIAsciiGridExporter eage;
            eage = new Grids_ESRIAsciiGridExporter(ge);
            Grids_ImageExporter ie = new Grids_ImageExporter(ge);
            File workspaceDirectory = new File(inDir + "/Workspace/");

            //String[] imageTypes = new String[0];
            String[] imageTypes = new String[1];
            imageTypes[0] = "PNG";

            for (File inputDirectoryFile : inputDirectoryFiles) {
                inputFilename = inputDirectoryFile.getName();
                System.out.println("inputFilename " + inputFilename);
                if (inputFilename.endsWith(ascString)) {
                    // Initialisation
                    inputFilenameWithoutExtension = inputFilename.substring(0,
                            inputFilename.length() - 4);
                    outDir = new File(inDir
                            + "/Geomorphometrics/"
                            + inputFilenameWithoutExtension + "/");
                    Grids_GridDouble g = null;
                    // Load input
                    File dir;
                    dir = new File(ge.getFiles().getGeneratedGridDoubleDir(),
                            inputFilenameWithoutExtension);
                    if (dir.exists()) {
                        g = GridDoubleFactory.create(dir, dir);
                    } else {
                        File inputFile = new File(inDir, inputFilename);
                        g = GridDoubleFactory.create(dir, inputFile);
                        // Cache input
                        g.writeToFile();
                        ge.getGrids().add(g);
                        System.out.println("<outputImage>");
                        System.out.println("outputDirectory " + outDir);
                        g.setName(inputFilenameWithoutExtension);
                        outputImage(g, outDir, ie, imageTypes, HOOME);
                        System.out.println("</outputImage>");
                    }
                    System.out.println(g.toString());
                    // generalise
                    run1(g, outDir, workspaceDirectory, eage, ie,
                            imageTypes, swapOutInitialisedFiles);
                    System.out.println("Processing complete in "
                            + Grids_Utilities.getTime(System.currentTimeMillis() - Time));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } catch (OutOfMemoryError e) {
            e.printStackTrace(System.err);
        } catch (Error e) {
            e.printStackTrace(System.err);
        }
    }

    /**
     *
     * @param grid
     * @param outDir
     * @param workDir
     * @param eage
     * @param ie
     * @param imageTypes
     * @param swapOutInitialisedFiles
     * @throws Exception
     * @throws Error
     */
    public void run1(Grids_GridDouble grid, File outDir, File workDir,
            Grids_ESRIAsciiGridExporter eage, Grids_ImageExporter ie,
            String[] imageTypes, boolean swapOutInitialisedFiles)
            throws Exception, Error {
        // Initialise
        log(0, "run1(...)");
        int minDistance;
        //minDistance = 8; 
        //minDistance = 2;
        minDistance = 16;
//        int maxDistance = 16;
        int maxDistance = 128;
        int multiplier = 2;
        //int maxIterations = 2000;
//            do_HollowFilledDEM(//                grid,
//                maxIterations,
//                outputDirectory,
//                workspaceDirectory,
//                handleOutOfMemoryError );
        boolean swapOutProcessedChunks = true;
        doMetrics1(grid, outDir, workDir, eage, ie, imageTypes, minDistance,
                maxDistance, multiplier, swapOutInitialisedFiles,
                swapOutProcessedChunks);
        doSlopeAndAspect(grid, outDir, workDir, eage, ie, imageTypes,
                minDistance, maxDistance, multiplier, HOOME);
        doMetrics2(grid, outDir, workDir, eage, ie, imageTypes, minDistance,
                maxDistance, multiplier, swapOutInitialisedFiles,
                swapOutProcessedChunks);
        log(0, "Processing complete in "
                + Grids_Utilities.getTime(System.currentTimeMillis() - Time));
    }

    /**
     *
     * @param g
     * @param outDir0
     * @param workDir0
     * @param eage
     * @param ie
     * @param imageTypes
     * @param minDistance
     * @param maxDistance
     * @param multiplier
     * @param swapOutInitialisedFiles
     * @param swapOutProcessedChunks
     * @throws Exception
     * @throws Error
     */
    public void doMetrics1(Grids_AbstractGridNumber g, File outDir0,
            File workDir0, Grids_ESRIAsciiGridExporter eage,
            Grids_ImageExporter ie, String[] imageTypes, int minDistance,
            int maxDistance, int multiplier, boolean swapOutInitialisedFiles,
            boolean swapOutProcessedChunks) throws Exception, Error {
        // Initialistaion
        ge.checkAndMaybeFreeMemory();
        Filename = "Metrics1";
        File outputDirectory = new File(outDir0, Filename);
        File workspaceDirectory = new File(workDir0, Filename);
        Files.setDataDirectory(workspaceDirectory);
        double cellsize = g.getCellsizeDouble();
        double weightIntersect = 1.0d;
        double weightFactor = 1.0d;
        double distance;
        int d;
        int i;
        for (d = minDistance; d <= maxDistance; d *= multiplier) {
            ge.checkAndMaybeFreeMemory();
            distance = cellsize * (double) d;
            Grids_AbstractGridNumber[] metrics1 = getMetrics1(g, distance,
                    weightIntersect, weightFactor, GridDoubleFactory,
                    GridIntFactory, swapOutInitialisedFiles,
                    swapOutProcessedChunks);
            ge.checkAndMaybeFreeMemory();
            for (i = 0; i < metrics1.length; i++) {
                ge.checkAndMaybeFreeMemory();
                output(metrics1[i], outputDirectory, ie, imageTypes, eage);
            }
        }
    }

    public void doMetrics2(Grids_AbstractGridNumber g, File outDir0,
            File workDir0, Grids_ESRIAsciiGridExporter eage,
            Grids_ImageExporter ie, String[] imageTypes, int minDistance,
            int maxDistance, int multiplier, boolean swapOutInitialisedFiles,
            boolean swapOutProcessedChunks) {
        // Initialistaion
        ge.checkAndMaybeFreeMemory();
        Filename = "Metrics1";
        File outputDirectory = new File(outDir0, Filename);
        File workspaceDirectory = new File(workDir0, Filename);
        Files.setDataDirectory(workspaceDirectory);
        double cellsize = g.getCellsizeDouble();
        double weightIntersect = 1.0d;
        double weightFactor = 1.0d;
        double distance;
        int d;
        int i;
        int samplingDensity = 1;
        for (d = minDistance; d <= maxDistance; d *= multiplier) {
            ge.checkAndMaybeFreeMemory();
            distance = cellsize * (double) d;
            Grids_AbstractGridNumber[] metrics2 = getMetrics2(
                    (Grids_GridDouble) g, distance,
                    weightIntersect, weightFactor, samplingDensity,
                    GridDoubleFactory, true);
            ge.checkAndMaybeFreeMemory();
            for (i = 0; i < metrics2.length; i++) {
                ge.checkAndMaybeFreeMemory();
                output(metrics2[i], outputDirectory, ie, imageTypes, eage);
            }
        }
    }

    /**
     *
     * @param g
     * @param outDir0
     * @param workDir0
     * @param eage
     * @param ie
     * @param imageTypes
     * @param minDistance
     * @param maxDistance
     * @param multiplier
     * @param hoome
     * @throws Exception
     * @throws Error
     */
    public void doSlopeAndAspect(Grids_AbstractGridNumber g, File outDir0,
            File workDir0, Grids_ESRIAsciiGridExporter eage,
            Grids_ImageExporter ie, String[] imageTypes, int minDistance,
            int maxDistance, int multiplier, boolean hoome)
            throws Exception, Error {
        try {
            // Initialistaion
            Filename = "SlopeAndAspect";
            File outDir = ge.initFileDirectory(outDir0, Filename);
            File workDir = ge.initFileDirectory(workDir0, Filename);
            double cellsize = g.getCellsizeDouble();
            double weightIntersect = 1.0d;
            double weightFactor = 1.0d;
            double distance = 0.0d;
            int distances = 2;
            int i = 0;
            for (distances = minDistance; distances <= maxDistance; distances *= multiplier) {
                distance = cellsize * (double) distances;
                Grids_AbstractGridNumber[] slopeAndAspect;
                slopeAndAspect = getSlopeAspect(g, distance, weightIntersect,
                        weightFactor, hoome);
                for (i = 0; i < slopeAndAspect.length; i++) {
//                    mask(
//                            SlopeAndAspect[i],
//                            distances,
//                            HOOME);
//                    outputESRIAsciiGrid(
//                            SlopeAndAspect[i],
//                            outputDirectory,
//                            _ESRIAsciiGridExporter,
//                            HOOME);
//                    outputImage(
//                            SlopeAndAspect[i],
//                            outputDirectory,
//                            _ImageExporter,
//                            imageTypes,
//                            HOOME);
                    output(slopeAndAspect[i],
                            outDir,
                            ie,
                            imageTypes,
                            eage);
                    slopeAndAspect[i] = null;
                    ge.getGrids().remove(slopeAndAspect[i]);
                }
            }
        } catch (OutOfMemoryError e) {
            if (hoome) {
                ge.clearMemoryReserve();
                ge.swapChunks(hoome);
                ge.initMemoryReserve();
                doSlopeAndAspect(g, outDir0, workDir0,
                        eage, ie, imageTypes, minDistance, maxDistance,
                        multiplier, hoome);
            } else {
                throw e;
            }
        }
    }
//    /**
//     *
//     */
//    public void maskEdges(
//            Grids_AbstractGridNumber g,
//            int distances,
//            boolean HOOME) {
//        try {
//            System.out.println("Masking Edges");
//            long nrows = g.getNRows(_HandleOutOfMemoryErrorFalse);
//            long ncols = g.getNCols(_HandleOutOfMemoryErrorFalse);
//            long _StartRowIndexLong = 0L;
//            long _StartColIndexLong = 0L;
//            long _EndRowIndexLong = 0L;
//            long _EndColIndexLong = 0L;
//            long _long_0 = 0L;
//            long _long_1 = 1L;
//            // mask left
//            _StartRowIndexLong = _long_0;
//            _StartColIndexLong = _long_0;
//            _EndRowIndexLong = nrows - _long_1;
//            _EndColIndexLong = distances - _long_1;
//            mask(g,
//                    _StartRowIndexLong,
//                    _StartColIndexLong,
//                    _EndRowIndexLong,
//                    _EndColIndexLong,
//                    HOOME);
//            // mask right
//            _StartRowIndexLong = _long_0;
//            _StartColIndexLong = ncols - distances;
//            _EndRowIndexLong = nrows - _long_1;
//            _EndColIndexLong = ncols - _long_1;
//            mask(g,
//                    _StartRowIndexLong,
//                    _StartColIndexLong,
//                    _EndRowIndexLong,
//                    _EndColIndexLong,
//                    HOOME);
//            // mask top
//            _StartRowIndexLong = _long_0;
//            _StartColIndexLong = _long_0;
//            _EndRowIndexLong = distances - _long_1;
//            _EndColIndexLong = ncols - _long_1;
//            mask(g,
//                    _StartRowIndexLong,
//                    _StartColIndexLong,
//                    _EndRowIndexLong,
//                    _EndColIndexLong,
//                    HOOME);
//            // mask bottom
//            _StartRowIndexLong = nrows - distances;
//            _StartColIndexLong = _long_0;
//            _EndRowIndexLong = nrows - _long_1;
//            _EndColIndexLong = ncols - _long_1;
//            mask(g,
//                    _StartRowIndexLong,
//                    _StartColIndexLong,
//                    _EndRowIndexLong,
//                    _EndColIndexLong,
//                    HOOME);
//        } catch (OutOfMemoryError e) {
//            if (HOOME) {
//                clearMemoryReserve();
////                swapChunk_AccountDetail();
//                _SwapToFileGrid2DSquareCellChunksExcept(g);
//                initMemoryReserve(g, HOOME);
//                maskEdges(
//                        g,
//                        distances,
//                        HOOME);
//            } else {
//                throw e;
//            }
//        }
//    }

    public long getTime() {
        return Time;
    }
}
