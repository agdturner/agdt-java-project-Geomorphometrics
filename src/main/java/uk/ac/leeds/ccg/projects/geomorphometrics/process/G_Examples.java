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
package uk.ac.leeds.ccg.projects.geomorphometrics.process;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import uk.ac.leeds.ccg.generic.core.Generic_Environment;
import uk.ac.leeds.ccg.generic.io.Generic_Defaults;
import uk.ac.leeds.ccg.generic.io.Generic_Path;
import uk.ac.leeds.ccg.grids.d2.grid.Grids_GridNumber;
import uk.ac.leeds.ccg.grids.d2.grid.d.Grids_GridDouble;
import uk.ac.leeds.ccg.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.grids.io.Grids_ImageExporter;
import uk.ac.leeds.ccg.grids.io.Grids_ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.grids.process.Grids_ProcessorDEM;
import uk.ac.leeds.ccg.grids.d2.util.Grids_Utilities;

/**
 * For processing some data for Ilkley moor.
 *
 * @author Andy Turner
 * @version 1.0.0
 */
public class G_Examples extends Grids_ProcessorDEM {

    private static final long serialVersionUID = 1L;

    // time
    private final long t;

    // hoome
    boolean hoome;

    // filename
    String filename;

    /**
     * Creates a new RoofGeneralisation using specified Directory.WARNING: Files
     * in the specified Directory may get overwritten.
     *
     * @param env
     * @throws java.io.IOException
     */
    public G_Examples(Grids_Environment env) throws IOException, Exception {
        super(env);
        t = System.currentTimeMillis();
        hoome = true;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
//            Path dir = Paths.get("/nfs/see-fs-02_users/geoagdt/scratch01/Work/"
//                    + "people/Molly/Workspace/");
            //Path dir = Paths.get("/nfs/see-fs-02_users/geoagdt/scratch01/Work/people/Scott Watson/test/Workspace/");
            //Path dir = Paths.get("/nfs/see-fs-02_users/geoagdt/scratch01/Work/people/Scott Watson/Workspace/");
            Path dir = Paths.get("/nfs/see-fs-02_users/geoagdt/scratch01/Work/people/Owen/Workspace/");
            //Path dir = Paths.get("C:/Temp/Owen/Workspace");
            //Path dir = Paths.get("C:/Temp/Owen/Workspace2");
            System.out.print("" + dir.toString());
            if (Files.exists(dir)) {
                System.out.println(" exists.");
                Files.createDirectories(dir);
            } else {
                System.out.println(" does not exist.");
            }
            G_Examples t = new G_Examples(new Grids_Environment(
                    new Generic_Environment(new Generic_Defaults(dir))));
            t.run();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    public void run() {
        try {
            //boolean swapOutInitialisedFiles = true;
            boolean swapOutInitialisedFiles = false;
            Path inDir = env.files.getDir().getParent();
            List<Path> ins = Files.list(inDir).collect(Collectors.toList());
            String ascString = "asc";
            Grids_ESRIAsciiGridExporter eage = new Grids_ESRIAsciiGridExporter(env);
            Grids_ImageExporter ie = new Grids_ImageExporter(env);
            Path workspaceDirectory = Paths.get(inDir + "/Workspace/");

            //String[] imageTypes = new String[0];
            String[] imageTypes = new String[1];
            imageTypes[0] = "PNG";

            for (Path in : ins) {
                String fn = in.getFileName().toString();
                System.out.println("input filename " + fn);
                if (fn.endsWith(ascString)) {
                    // Initialisation
                    String pfx = fn.substring(0, fn.length() - 4);
                    Path outDir = Paths.get(inDir + "/Geomorphometrics/" + pfx + "/");
                    Grids_GridDouble g = null;
                    // Load input
                    Path dir = Paths.get(env.files.getGeneratedGridDoubleDir().toString(),
                            pfx);
                    if (Files.exists(dir)) {
                        g = gridFactoryDouble.create(new Generic_Path(dir));
                    } else {
                        Path inputFile = Paths.get(inDir.toString(), fn);
                        g = gridFactoryDouble.create(new Generic_Path(inputFile));
                        env.getGrids().add(g);
                        System.out.println("<outputImage>");
                        System.out.println("outputDirectory " + outDir);
                        g.setName(pfx);
                        outputImage(g, new Generic_Path(outDir), ie, imageTypes, hoome);
                        System.out.println("</outputImage>");
                    }
                    System.out.println(g.toString());
                    // generalise
                    run1(g, outDir, workspaceDirectory, eage, ie,
                            imageTypes, swapOutInitialisedFiles);
                    System.out.println("Processing complete in "
                            + Grids_Utilities.getTime(System.currentTimeMillis() - t));
                }
            }
        } catch (Exception | Error e) {
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
    public void run1(Grids_GridDouble grid, Path outDir, Path workDir,
            Grids_ESRIAsciiGridExporter eage, Grids_ImageExporter ie,
            String[] imageTypes, boolean swapOutInitialisedFiles)
            throws Exception, Error {
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
                minDistance, maxDistance, multiplier, hoome);
        doMetrics2(grid, outDir, workDir, eage, ie, imageTypes, minDistance,
                maxDistance, multiplier, swapOutInitialisedFiles,
                swapOutProcessedChunks);
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
    public void doMetrics1(Grids_GridNumber g, Path outDir0,
            Path workDir0, Grids_ESRIAsciiGridExporter eage,
            Grids_ImageExporter ie, String[] imageTypes, int minDistance,
            int maxDistance, int multiplier, boolean swapOutInitialisedFiles,
            boolean swapOutProcessedChunks) throws Exception, Error {
        // Initialistaion
        env.checkAndMaybeFreeMemory();
        filename = "Metrics1";
        Path outputDirectory = Paths.get(outDir0.toString(), filename);
        Path workspaceDirectory = Paths.get(workDir0.toString(), filename);
        env.files.setDir(workspaceDirectory);
        double cellsize = g.getCellsize().doubleValue();
        double weightIntersect = 1.0d;
        double weightFactor = 1.0d;
        double distance;
        int d;
        int i;
        for (d = minDistance; d <= maxDistance; d *= multiplier) {
            env.checkAndMaybeFreeMemory();
            distance = cellsize * (double) d;
            Grids_GridNumber[] metrics1 = getMetrics1(g, distance,
                    weightIntersect, weightFactor, gridFactoryDouble,
                    gridFactoryInt, swapOutInitialisedFiles,
                    swapOutProcessedChunks);
            env.checkAndMaybeFreeMemory();
            for (i = 0; i < metrics1.length; i++) {
                env.checkAndMaybeFreeMemory();
                output(metrics1[i], outputDirectory, ie, imageTypes, eage);
            }
        }
    }

    public void doMetrics2(Grids_GridNumber g, Path outDir0,
            Path workDir0, Grids_ESRIAsciiGridExporter eage,
            Grids_ImageExporter ie, String[] imageTypes, int minDistance,
            int maxDistance, int multiplier, boolean swapOutInitialisedFiles,
            boolean swapOutProcessedChunks) throws IOException, Exception {
        // Initialistaion
        env.checkAndMaybeFreeMemory();
        filename = "Metrics1";
        Path outputDirectory = Paths.get(outDir0.toString(), filename);
        Path workspaceDirectory = Paths.get(workDir0.toString(), filename);
        env.files.setDir(workspaceDirectory);
        double cellsize = g.getCellsize().doubleValue();
        double weightIntersect = 1.0d;
        double weightFactor = 1.0d;
        double distance;
        int d;
        int i;
        int samplingDensity = 1;
        for (d = minDistance; d <= maxDistance; d *= multiplier) {
            env.checkAndMaybeFreeMemory();
            distance = cellsize * (double) d;
//            Grids_GridNumber[] metrics2 = getMetrics2(
//                    (Grids_GridDouble) g, distance,
//                    weightIntersect, weightFactor, samplingDensity,
//                    gridFactoryDouble, true);
//            env.checkAndMaybeFreeMemory();
//            for (i = 0; i < metrics2.length; i++) {
//                env.checkAndMaybeFreeMemory();
//                output(metrics2[i], outputDirectory, ie, imageTypes, eage);
//            }
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
    public void doSlopeAndAspect(Grids_GridNumber g, Path outDir0,
            Path workDir0, Grids_ESRIAsciiGridExporter eage,
            Grids_ImageExporter ie, String[] imageTypes, int minDistance,
            int maxDistance, int multiplier, boolean hoome)
            throws Exception, Error {
        // Initialistaion
        filename = "SlopeAndAspect";
        //File outDir = env.initFileDirectory(outDir0, filename);
        Path outDir = env.files.getOutputDir();
        //File workDir = env.initFileDirectory(workDir0, filename);
        //File workDir = env.files.getGeneratedDir();
        double cellsize = g.getCellsize().doubleValue();
        double weightIntersect = 1.0d;
        double weightFactor = 1.0d;
        BigDecimal distance;
        int distances;
        int dp = 3;
        RoundingMode rm = RoundingMode.HALF_UP;
        for (distances = minDistance; distances <= maxDistance; distances *= multiplier) {
            distance = BigDecimal.valueOf(cellsize * (double) distances);
            Grids_GridNumber[] slopeAndAspect = getSlopeAspect(g, distance,
                    weightIntersect, weightFactor, dp, rm, hoome);
            for (int i = 0; i < slopeAndAspect.length; i++) {
//                    mask(
//                            SlopeAndAspect[i],
//                            distances,
//                            hoome);
//                    outputESRIAsciiGrid(
//                            SlopeAndAspect[i],
//                            outputDirectory,
//                            _ESRIAsciiGridExporter,
//                            hoome);
//                    outputImage(
//                            SlopeAndAspect[i],
//                            outputDirectory,
//                            _ImageExporter,
//                            imageTypes,
//                            hoome);
                output(slopeAndAspect[i],
                        outDir,
                        ie,
                        imageTypes,
                        eage);
                slopeAndAspect[i] = null;
                env.getGrids().remove(slopeAndAspect[i]);
            }
        }
    }
//    /**
//     *
//     */
//    public void maskEdges(
//            Grids_AbstractGridNumber g,
//            int distances,
//            boolean hoome) {
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
//                    hoome);
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
//                    hoome);
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
//                    hoome);
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
//                    hoome);
//        } catch (OutOfMemoryError e) {
//            if (hoome) {
//                clearMemoryReserve();
////                swapChunk_AccountDetail();
//                _SwapToFileGrid2DSquareCellChunksExcept(g);
//                initMemoryReserve(g, hoome);
//                maskEdges(
//                        g,
//                        distances,
//                        hoome);
//            } else {
//                throw e;
//            }
//        }
//    }

    public long getT() {
        return t;
    }
}
