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
public class Tarfala extends Grids_ProcessorDEM {

    private long Time;
    boolean HandleOutOfMemoryError;
    String Filename;

    protected Tarfala() {
    }

    /**
     * Creates a new RoofGeneralisation using specified Directory. WARNING:
     * Files in the specified Directory may get overwritten.
     *
     * @param ge
     */
    public Tarfala(Grids_Environment ge) {
        super(ge);
        Time = System.currentTimeMillis();
        HandleOutOfMemoryError = true;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //File Directory = new File("/nfs/see-fs-02_users/geoagdt/scratch01/Work/people/Scott Watson/test/Workspace/");
        //File Directory = new File("/nfs/see-fs-02_users/geoagdt/scratch01/Work/people/Scott Watson/Workspace/");
        File dir = new File("/nfs/see-fs-02_users/geoagdt/scratch01/Work/people/Owen/Workspace/");
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
        Tarfala t = new Tarfala(ge);
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
                    boolean notLoadedAsGrid = true;
                    if (notLoadedAsGrid) {
                        File inputFile = new File(inDir, inputFilename);
                        File dir;
                        dir = new File(ge.getFiles().getGeneratedGridDoubleDir(),
                                inputFilenameWithoutExtension);
                        g = GridDoubleFactory.create(dir, inputFile);
                        // Cache input
                        g.writeToFile();
                        ge.getGrids().add(g);
                        System.out.println("<outputImage>");
                        System.out.println("outputDirectory " + outDir);
                        g.setName(inputFilenameWithoutExtension);
                        outputImage(g, outDir, ie, imageTypes,
                                HandleOutOfMemoryError);
                        System.out.println("</outputImage>");

                    } else {
                        System.out.println("check1");
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
     * @param outputDirectory
     * @param workspaceDirectory
     * @param aESRIAsciiGridExporter
     * @param aImageExporter
     * @param imageTypes
     * @param swapOutInitialisedFiles
     * @throws Exception
     * @throws Error
     */
    public void run1(
            Grids_GridDouble grid,
            File outputDirectory,
            File workspaceDirectory,
            Grids_ESRIAsciiGridExporter aESRIAsciiGridExporter,
            Grids_ImageExporter aImageExporter,
            String[] imageTypes,
            boolean swapOutInitialisedFiles)
            throws Exception, Error {
        // Initialise
        log(0, "run1(...)");
        int minDistance;
        //minDistance = 8; 
        minDistance = 2;
        int maxDistance = 16;
        int multiplier = 2;

//        int maxIterations = 2000;
//            do_SlopeAndAspect(
//                    grid,
//                    outputDirectory,
//                    workspaceDirectory,
//                    aESRIAsciiGridExporter,
//                    aImageExporter,
//                    imageTypes,
//                    minDistance,
//                    maxDistance,
//                    multiplier,
//                    handleOutOfMemoryError);
//            do_HollowFilledDEM(
//                grid,
//                maxIterations,
//                outputDirectory,
//                workspaceDirectory,
//                handleOutOfMemoryError );
        boolean swapOutProcessedChunks = true;
        do_Metrics1(
                grid,
                outputDirectory,
                workspaceDirectory,
                aESRIAsciiGridExporter,
                aImageExporter,
                imageTypes,
                minDistance,
                maxDistance,
                multiplier,
                swapOutInitialisedFiles,
                swapOutProcessedChunks, HandleOutOfMemoryError);
//            do_Metrics2(
//                    grid,
//                    outputDirectory,
//                    workspaceDirectory,
//                    aESRIAsciiGridExporter,
//                    aImageExporter,
//                    imageTypes,
//                    minDistance,
//                    maxDistance,
//                    multiplier,
//                    swapOutInitialisedFiles,
//                    swapOutProcessedChunks,
//                    handleOutOfMemoryError);

        log(0, "Processing complete in "
                + Grids_Utilities.getTime(System.currentTimeMillis() - Time));
    }

    /**
     *
     * @param g
     * @param outputDirectory0
     * @param workspaceDirectory0
     * @param eage
     * @param ie
     * @param imageTypes
     * @param minDistance
     * @param maxDistance
     * @param multiplier
     * @param swapOutInitialisedFiles
     * @param swapOutProcessedChunks
     * @param handleOutOfMemoryError
     * @throws Exception
     * @throws Error
     */
    public void do_Metrics1(
            Grids_AbstractGridNumber g,
            File outputDirectory0,
            File workspaceDirectory0,
            Grids_ESRIAsciiGridExporter eage,
            Grids_ImageExporter ie,
            String[] imageTypes,
            int minDistance,
            int maxDistance,
            int multiplier,
            boolean swapOutInitialisedFiles,
            boolean swapOutProcessedChunks,
            boolean handleOutOfMemoryError)
            throws Exception, Error {
        // Initialistaion
        ge.checkAndMaybeFreeMemory();
        Filename = "Metrics1";
        File outputDirectory = new File(outputDirectory0, Filename);
        File workspaceDirectory = new File(workspaceDirectory0, Filename);
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
            Grids_AbstractGridNumber[] metrics1 = getMetrics1(
                    g,
                    distance,
                    weightIntersect,
                    weightFactor,
                    GridDoubleFactory,
                    GridIntFactory,
                    swapOutInitialisedFiles,
                    swapOutProcessedChunks);
            ge.checkAndMaybeFreeMemory();
            for (i = 0; i < metrics1.length; i++) {
                ge.checkAndMaybeFreeMemory();
                output(
                        metrics1[i],
                        outputDirectory,
                        ie,
                        imageTypes,
                        eage);
            }
        }
    }

    public void do_Metrics2(
            Grids_AbstractGridNumber g,
            File outputDirectory0,
            File workspaceDirectory0,
            boolean handleOutOfMemoryError)
            throws Exception, Error {
        try {
            // Initialistaion
            Filename = "_Metrics2";
            File outputDirectory = new File(outputDirectory0, Filename);
            File workspaceDirectory = new File(workspaceDirectory0, Filename);
            int _NameLength = 1000;
            String name;
            double cellsize = g.getCellsizeDouble();
            double weightIntersect = 1.0d;
            double weightFactor = 1.0d;
            Grids_AbstractGridNumber dummyGrid = null;
            long nrows = g.getNRows();
            long ncols = g.getNCols();
            long _StartRowIndexLong = 0L;
            long _StartColIndexLong = 0L;
            long _EndRowIndexLong = 0L;
            long _EndColIndexLong = 0L;
            long _long_0 = 0L;
            long _long_1 = 1L;
            double distance = 0.0d;
            int distances = 2;
            int i = 0;
            int _int_2 = 2;
            int _int_0 = 0;
            for (distances = 2; distances <= 32; distances *= 2) {
                distance = cellsize * (double) distances;
//                Grids_AbstractGridNumber roughness = getMetrics2(
//                        grid,
//                        distance,
//                        weightIntersect,
//                        weightFactor,
//                        _NameLength,
//                        GridDoubleFactory,
//                        handleOutOfMemoryError);
//                output(roughness,
//                            outputDirectory,
//                            handleOutOfMemoryError );
            }
        } catch (OutOfMemoryError e) {
            System.err.println("OOME fggfd");
        }
    }

    /**
     *
     * @param g
     * @param outputDirectory0
     * @param workspaceDirectory0
     * @param eage
     * @param ie
     * @param imageTypes
     * @param minDistance
     * @param maxDistance
     * @param multiplier
     * @param handleOutOfMemoryError
     * @throws Exception
     * @throws Error
     */
    public void do_SlopeAndAspect(
            Grids_AbstractGridNumber g,
            File outputDirectory0,
            File workspaceDirectory0,
            Grids_ESRIAsciiGridExporter eage,
            Grids_ImageExporter ie,
            String[] imageTypes,
            int minDistance,
            int maxDistance,
            int multiplier,
            boolean handleOutOfMemoryError)
            throws Exception, Error {
        try {
            // Initialistaion
            Filename = "_SlopeAndAspect";
            File outputDirectory = new File(outputDirectory0,                    Filename);
            File workspaceDirectory = new File(workspaceDirectory0,                    Filename);
            double cellsize = g.getCellsizeDouble();
            double weightIntersect = 1.0d;
            double weightFactor = 1.0d;
            double distance = 0.0d;
            int distances = 2;
            int i = 0;
            for (distances = minDistance; distances <= maxDistance; distances *= multiplier) {
                distance = cellsize * (double) distances;
                Grids_AbstractGridNumber[] slopeAndAspect = getSlopeAspect(
                        g,
                        distance,
                        weightIntersect,
                        weightFactor,
                        handleOutOfMemoryError);
                for (i = 0; i < slopeAndAspect.length; i++) {
//                    mask(
//                            _SlopeAndAspect[i],
//                            distances,
//                            HandleOutOfMemoryError);
//                    outputESRIAsciiGrid(
//                            _SlopeAndAspect[i],
//                            outputDirectory,
//                            _ESRIAsciiGridExporter,
//                            HandleOutOfMemoryError);
//                    outputImage(
//                            _SlopeAndAspect[i],
//                            outputDirectory,
//                            _ImageExporter,
//                            imageTypes,
//                            HandleOutOfMemoryError);
                    output(slopeAndAspect[i],
                            //_Grids_Environment.getProcessor(),
                            outputDirectory,
                            ie,
                            imageTypes,
                            eage);
                    slopeAndAspect[i] = null;
                    ge.getGrids().remove(slopeAndAspect[i]);
                }
            }
        } catch (OutOfMemoryError e) {
            if (handleOutOfMemoryError) {
                ge.clearMemoryReserve();
                ge.swapChunks(handleOutOfMemoryError);
                ge.initMemoryReserve();
                do_SlopeAndAspect(g, outputDirectory0, workspaceDirectory0,
                        eage, ie, imageTypes, minDistance, maxDistance,
                        multiplier, handleOutOfMemoryError);
            } else {
                throw e;
            }
        }
    }
//    /**
//     *
//     */
//    public void maskEdges(
//            Grids_AbstractGridNumber _Grid2DSquareCell,
//            int distances,
//            boolean HandleOutOfMemoryError) {
//        try {
//            System.out.println("Masking Edges");
//            long nrows = _Grid2DSquareCell.getNRows(_HandleOutOfMemoryErrorFalse);
//            long ncols = _Grid2DSquareCell.getNCols(_HandleOutOfMemoryErrorFalse);
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
//            mask(_Grid2DSquareCell,
//                    _StartRowIndexLong,
//                    _StartColIndexLong,
//                    _EndRowIndexLong,
//                    _EndColIndexLong,
//                    HandleOutOfMemoryError);
//            // mask right
//            _StartRowIndexLong = _long_0;
//            _StartColIndexLong = ncols - distances;
//            _EndRowIndexLong = nrows - _long_1;
//            _EndColIndexLong = ncols - _long_1;
//            mask(_Grid2DSquareCell,
//                    _StartRowIndexLong,
//                    _StartColIndexLong,
//                    _EndRowIndexLong,
//                    _EndColIndexLong,
//                    HandleOutOfMemoryError);
//            // mask top
//            _StartRowIndexLong = _long_0;
//            _StartColIndexLong = _long_0;
//            _EndRowIndexLong = distances - _long_1;
//            _EndColIndexLong = ncols - _long_1;
//            mask(_Grid2DSquareCell,
//                    _StartRowIndexLong,
//                    _StartColIndexLong,
//                    _EndRowIndexLong,
//                    _EndColIndexLong,
//                    HandleOutOfMemoryError);
//            // mask bottom
//            _StartRowIndexLong = nrows - distances;
//            _StartColIndexLong = _long_0;
//            _EndRowIndexLong = nrows - _long_1;
//            _EndColIndexLong = ncols - _long_1;
//            mask(_Grid2DSquareCell,
//                    _StartRowIndexLong,
//                    _StartColIndexLong,
//                    _EndRowIndexLong,
//                    _EndColIndexLong,
//                    HandleOutOfMemoryError);
//        } catch (OutOfMemoryError e) {
//            if (HandleOutOfMemoryError) {
//                clearMemoryReserve();
////                swapChunk_AccountDetail();
//                _SwapToFileGrid2DSquareCellChunksExcept(_Grid2DSquareCell);
//                initMemoryReserve(_Grid2DSquareCell, HandleOutOfMemoryError);
//                maskEdges(
//                        _Grid2DSquareCell,
//                        distances,
//                        HandleOutOfMemoryError);
//            } else {
//                throw e;
//            }
//        }
//    }

    public long getTime() {
        return Time;
    }
}
