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
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridIntFactory;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridDoubleFactory;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_AbstractGridNumber;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridDouble;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.grids.io.Grids_ImageExporter;
import uk.ac.leeds.ccg.andyt.grids.io.Grids_ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.andyt.grids.process.Grids_ProcessorDEM;
import uk.ac.leeds.ccg.andyt.grids.utilities.Grids_Utilities;

/**
 * Originally this class was for processing some data from Sweden, but it has 
 * also been used to process data from the Himalayan region.
 * @author geoagdt
 */
public class Tarfala
        extends Grids_ProcessorDEM {

    private long time;
    boolean HandleOutOfMemoryError;
    String _FileSeparator;
    int _MessageLength;
    int _FilenameLength;
    String _Message0;
    String _Message;
    String _Filename;

    protected Tarfala() {}

    /**
     * Creates a new RoofGeneralisation using specified Directory. WARNING:
 Files in the specified Directory may get overwritten.
     *
     * @param ge
     */
    public Tarfala(Grids_Environment ge) {
        super(ge);
        this.time = System.currentTimeMillis();
        this.HandleOutOfMemoryError = true;
        this._FileSeparator = System.getProperty("file.separator");
        this._MessageLength = 1000;
        this._FilenameLength = 1000;
        this._Message0 = ge.initString(_MessageLength, HandleOutOfMemoryError);
        this._Message = ge.initString(_MessageLength, HandleOutOfMemoryError);
        this._Filename = ge.initString(_FilenameLength, HandleOutOfMemoryError);
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
            int nrows;
            int ncols;
            int chunkNRows;
            int chunkNCols;
            double NoDataValue;
            // Init nrows, ncols, chunkNRows, chunkNCols, NoDataValue
            //nrows = 4415;
            //ncols = 4838;
//            nrows = 6192;
//            ncols = 7224;
            //nrows = 774;
            //ncols = 903;
//            nrows = 7110;
//            ncols = 6260;
//            nrows = 7426;
//            ncols = 8304;
//            nrows = 1993;
//            ncols = 6728;
            //ncols = 6727;
//nrows         = 7142;
nrows         = 7142;
ncols         = 5200;
//            chunkNRows = 200;
//            chunkNCols = 883;
//            chunkNRows = 64;
//            chunkNCols = 64;
//            chunkNRows = 774;
//            chunkNCols = 903;
//            chunkNRows = 711;
//            chunkNCols = 626;
//            chunkNRows = 3713;
//            chunkNCols = 519;
//            chunkNRows = 1993;
//            chunkNCols = 232;
            chunkNRows = 3571;
            chunkNCols = 16;
            //chunkNCols = 217;
            NoDataValue = -9999.0d;
            init_Grid2DSquareCellIntFactory(
                    chunkNRows,
                    chunkNCols);
            //this.Grid2DSquareCellIntFactory.setChunkNRows(chunkNRows);
            //this.Grid2DSquareCellIntFactory.setChunkNCols(chunkNCols);
            init_Grid2DSquareCellDoubleFactory(
                    chunkNRows,
                    chunkNCols,
                    NoDataValue);
            ge.setProcessor(this);
            //this.Grid2DSquareCellDoubleFactory.setChunkNRows(chunkNRows);
            //this.Grid2DSquareCellDoubleFactory.setChunkNCols(chunkNCols);
            //this.Grid2DSquareCellDoubleFactory.set_NoDataValue(NoDataValue);

            boolean swapOutInitialisedFiles = true;
            File inputDirectory = getDirectory(HandleOutOfMemoryError);
            File[] inputDirectoryFiles = inputDirectory.listFiles();
            String inputFilename;
            String ascString = "asc";
            String inputFilenameWithoutExtension;
            File outputDirectory;
            Grids_ESRIAsciiGridExporter aESRIAsciiGridExporter = new Grids_ESRIAsciiGridExporter(ge);
            Grids_ImageExporter aImageExporter = new Grids_ImageExporter(ge);
            File workspaceDirectory = new File(inputDirectory + "/Workspace/");
            
            
            
            
            
            String[] imageTypes = new String[0];
//            String[] imageTypes = new String[1];
//            imageTypes[0] = "PNG";
            
            
            
            for (File inputDirectoryFile : inputDirectoryFiles) {
                inputFilename = inputDirectoryFile.getName();
                System.out.println("inputFilename " + inputFilename);
                if (inputFilename.endsWith(ascString)) {
                    // Initialisation
                    inputFilenameWithoutExtension = inputFilename.substring(0, inputFilename.length() - 4);
                    outputDirectory = new File(inputDirectory + "/Geomorphometrics/" + inputFilenameWithoutExtension + "/");
                    Grids_GridDouble grid2DSquareCellDouble = null;
                    Grids_GridDouble g = null;
                    // Load input
                    boolean _NotLoadedAsGrid = true;
                    if (_NotLoadedAsGrid) {
                        File inputFile = ge.initFile(inputDirectory,
                                inputFilename,
                                HandleOutOfMemoryError);

//                        if (inputFilename.equalsIgnoreCase("rastert_c0202_c1.asc")) {
//                            nrows = 7594;
//                            ncols = 8394;
//                            chunkNRows = 3797;
//                            chunkNCols = 1399;
//                        }
                        init_Grid2DSquareCellIntFactory(
                                chunkNRows,
                                chunkNCols);
                        init_Grid2DSquareCellDoubleFactory(
                                chunkNRows,
                                chunkNCols,
                                NoDataValue);

//                        grid2DSquareCellDouble = (Grids_GridDouble) this.Grid2DSquareCellDoubleFactory.create(
//                                inputFile);
                        grid2DSquareCellDouble = (Grids_GridDouble) this.Grid2DSquareCellDoubleFactory.create(_GridStatistics,
                                Directory,
                                inputFile,
                                _Grid2DSquareCellDoubleChunkFactory,
                                0,
                                0,
                                nrows - 1,
//                                ncols - 3,
                                ncols - 1,
                                //chunkNRows - 1,
                                //chunkNCols - 1,
                                ge,
                                HandleOutOfMemoryError);
                        
                        // clip grid2DSquareCellDouble
                        nrows         = 7140;
                        g = (Grids_GridDouble) this.Grid2DSquareCellDoubleFactory.create(
                                grid2DSquareCellDouble,
                                0,
                                0,
                                nrows -1,
                                ncols - 1);
                        grid2DSquareCellDouble = g;
                        chunkNRows = 340;
                        chunkNCols = 400;
            init_Grid2DSquareCellIntFactory(
                                chunkNRows,
                                chunkNCols);
                        init_Grid2DSquareCellDoubleFactory(
                                chunkNRows,
                                chunkNCols,
                                NoDataValue);
                        // Cache input
                        boolean _SwapToFileCache = true;
                        grid2DSquareCellDouble.writeToFile(_SwapToFileCache,
                                HandleOutOfMemoryError);
                        ge.getGrids().add(grid2DSquareCellDouble);
                        System.out.println("<outputImage>");
                        System.out.println("outputDirectory " + outputDirectory);
                        grid2DSquareCellDouble.setName(inputFilenameWithoutExtension, HandleOutOfMemoryError);
                        outputImage(g,// grid2DSquareCellDouble,
                                //this,
                                //_Grids_Environment.getProcessor(),
                                outputDirectory,
                                aImageExporter,
                                imageTypes,
                                HandleOutOfMemoryError);
//                        output(grid2DSquareCellDouble,
//                                outputDirectory,
//                                aImageExporter,
//                                imageTypes,
//                                aESRIAsciiGridExporter,
//                                HandleOutOfMemoryError);
                        System.out.println("</outputImage>");
                        
                        
                    } else {
                        System.out.println("check1");
//                        _Grid2DSquareCellDouble = (Grids_GridDouble) Grid2DSquareCellDoubleFactory.create(
//                                new File(_Input_Directory.toString() + this._FileSeparator + _Input_Filename_WithoutExtension + "uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleFactory_chunkNrows(" + chunkNrows + ")_chunkNcols(" + chunkNcols + ")"));
//                        this._AbstractGrid2DSquareCell_HashSet.add(_Grid2DSquareCellDouble);
                    }
                    System.out.println(grid2DSquareCellDouble.toString(HandleOutOfMemoryError));
                    // generalise
                    run1(grid2DSquareCellDouble,
                            outputDirectory,
                            workspaceDirectory,
                            aESRIAsciiGridExporter,
                            aImageExporter,
                            imageTypes,
                            swapOutInitialisedFiles,
                            HandleOutOfMemoryError);
                    _Message = null;
                    _Message = "Processing complete in "
                            + Grids_Utilities._ReportTime(System.currentTimeMillis() - time);
                    _Message = ge.println(_Message, _Message0, HandleOutOfMemoryError);
                    // output

                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        } catch (Error e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void init_Grid2DSquareCellDoubleFactory(
            int chunkNRows,
            int chunkNCols,
            double NoDataValue) {
        this.Grid2DSquareCellDoubleFactory = new Grids_GridDoubleFactory(
                this.Directory,
                chunkNRows,
                chunkNCols,
                this._Grid2DSquareCellDoubleChunkFactory,
                NoDataValue,
                ge,
                this.HandleOutOfMemoryError);
    }

    private void init_Grid2DSquareCellIntFactory(
            int chunkNRows,
            int chunkNCols) {
        this.Grid2DSquareCellIntFactory = new Grids_GridIntFactory(
                this.Directory,
                chunkNRows,
                chunkNCols,
                this.GridChunkIntFactory,
                ge,
                this.HandleOutOfMemoryError);
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
     * @param handleOutOfMemoryError
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
            boolean swapOutInitialisedFiles,
            boolean handleOutOfMemoryError)
            throws Exception, Error {
        try {
            // Initialise
            log("run1();",
                    handleOutOfMemoryError);
            int maxIterations = 16;
            long nrows = grid.getNRows(handleOutOfMemoryError);
            long ncols = grid.getNCols(handleOutOfMemoryError);
//            long distances = 16;
            int minDistance = 2;
            int maxDistance = 16;
            int multiplier = 2;

//        maxIterations = 2000;
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
                    swapOutProcessedChunks,
                    handleOutOfMemoryError);
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

            log("Processing complete in " + Grids_Utilities._ReportTime(System.currentTimeMillis() - time),
                    handleOutOfMemoryError);
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (handleOutOfMemoryError) {
                ge.clearMemoryReserve();
                ge.swapChunks(handleOutOfMemoryError);
                ge.initMemoryReserve(handleOutOfMemoryError);
                run1(
                        grid,
                        outputDirectory,
                        workspaceDirectory,
                        aESRIAsciiGridExporter,
                        aImageExporter,
                        imageTypes,
                        swapOutInitialisedFiles,
                        handleOutOfMemoryError);
            } else {
                throw _OutOfMemoryError;
            }
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
        try {
            // Initialistaion
            _Filename = "_Metrics1";
            File outputDirectory = ge.initFileDirectory(
                    outputDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            File workspaceDirectory = ge.initFileDirectory(
                    workspaceDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            _Filename = ge.initString(_FilenameLength, handleOutOfMemoryError);
            this.setDirectory(workspaceDirectory);
            int _NameLength = 1000;
            String _Name = ge.initString(_NameLength, false);
            //Grids_Dimensions dimensions = grid2DSquareCell.getDimensions(handleOutOfMemoryError);
            double cellsize = g.getCellsizeDouble(handleOutOfMemoryError);
            double weightIntersect = 1.0d;
            double weightFactor = 1.0d;
            Grids_AbstractGridNumber dummyGrid = null;
            long nrows = g.getNRows(handleOutOfMemoryError);
            long ncols = g.getNCols(handleOutOfMemoryError);
//            long _StartRowIndexLong = 0L;
//            long _StartColIndexLong = 0L;
//            long _EndRowIndexLong = 0L;
//            long _EndColIndexLong = 0L;
//            long _long_0 = 0L;
//            long _long_1 = 1L;
            double distance = 0.0d;
            int distances = 2;
            int i = 0;
//            int _int_2 = 2;
            int int_0 = 0;
            double min = 0;
            double max = 1;
            for (distances = minDistance; distances <= maxDistance; distances *= multiplier) {
                distance = cellsize * (double) distances;
                Grids_AbstractGridNumber[] metrics1 = getMetrics1(g,
                        distance,
                        weightIntersect,
                        weightFactor,
                        Grid2DSquareCellDoubleFactory,
                        Grid2DSquareCellIntFactory,
                        swapOutInitialisedFiles,
                        swapOutProcessedChunks,
                        handleOutOfMemoryError);
                for (i = int_0; i < metrics1.length; i++) {
//                    maskEdges(
//                            metrics1[i],
//                            distances,
//                            HandleOutOfMemoryError);
//                    //rescale
//                    metrics1[i] = rescale(
//                            metrics1[i],
//                            null, min, max, handleOutOfMemoryError);
                    // output
                    output(
                            metrics1[i],
                            outputDirectory,
                            ie,
                            imageTypes,
                            eage,
                            handleOutOfMemoryError);
//                    _Name = metrics1[i].getName(handleOutOfMemoryError);
//                    if (_Name.startsWith("count_hhhl") || _Name.startsWith("count_hhll")) {
//                        output(
//                                metrics1[i],
//                                outputDirectory,
//                                aImageExporter,
//                                imageTypes,
//                                aESRIAsciiGridExporter,
//                                handleOutOfMemoryError);
////                        outputESRIAsciiGrid(
////                                metrics1[i],
////                                _Output_Directory,
////                                _ESRIAsciiGridExporter,
////                                HandleOutOfMemoryError);
//                    }
                }
            }
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (handleOutOfMemoryError) {
                ge.clearMemoryReserve();
                ge.swapChunks(handleOutOfMemoryError);
                ge.initMemoryReserve(handleOutOfMemoryError);
                System.out.println("Going round the loop again...");
                do_Metrics1(
                        g,
                        outputDirectory0,
                        workspaceDirectory0,
                        eage,
                        ie,
                        imageTypes,
                        minDistance,
                        maxDistance,
                        multiplier,
                        swapOutInitialisedFiles,
                        swapOutProcessedChunks,
                        handleOutOfMemoryError);
            } else {
                throw _OutOfMemoryError;
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
            _Filename = "_Metrics2";
            File outputDirectory = ge.initFileDirectory(
                    outputDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            File workspaceDirectory = ge.initFileDirectory(
                    workspaceDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            _Filename = ge.initString(_FilenameLength, handleOutOfMemoryError);
            this.setDirectory(workspaceDirectory);
            int _NameLength = 1000;
            String _Name = ge.initString(_NameLength, handleOutOfMemoryError);
            //Grids_Dimensions dimensions = grid2DSquareCell.getDimensions(handleOutOfMemoryError);
            double cellsize = g.getCellsizeDouble(handleOutOfMemoryError);
            double weightIntersect = 1.0d;
            double weightFactor = 1.0d;
            Grids_AbstractGridNumber dummyGrid = null;
            long nrows = g.getNRows(handleOutOfMemoryError);
            long ncols = g.getNCols(handleOutOfMemoryError);
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
//                        Grid2DSquareCellDoubleFactory,
//                        handleOutOfMemoryError);
//                output(roughness,
//                            outputDirectory,
//                            handleOutOfMemoryError );
            }
        } catch (OutOfMemoryError _OutOfMemoryError) {
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
            _Filename = "_SlopeAndAspect";
            File outputDirectory = ge.initFileDirectory(
                    outputDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            File workspaceDirectory = ge.initFileDirectory(
                    workspaceDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            _Filename = ge.initString(_FilenameLength, handleOutOfMemoryError);
            this.setDirectory(workspaceDirectory);
            //Grids_Dimensions dimensions = grid2DSquareCell.getDimensions(handleOutOfMemoryError);
            double cellsize = g.getCellsizeDouble(handleOutOfMemoryError);
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
//                            _Output_Directory,
//                            _ESRIAsciiGridExporter,
//                            HandleOutOfMemoryError);
//                    outputImage(
//                            _SlopeAndAspect[i],
//                            _Output_Directory,
//                            _ImageExporter,
//                            imageTypes,
//                            HandleOutOfMemoryError);
                    output(slopeAndAspect[i],
                            //_Grids_Environment.getProcessor(),
                            outputDirectory,
                            ie,
                            imageTypes,
                            eage,
                            handleOutOfMemoryError);
                    slopeAndAspect[i] = null;
                    ge.getGrids().remove(slopeAndAspect[i]);
                }
            }
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (handleOutOfMemoryError) {
                ge.clearMemoryReserve();
                ge.swapChunks(handleOutOfMemoryError);
                ge.initMemoryReserve(handleOutOfMemoryError);
                do_SlopeAndAspect(
                        g,
                        outputDirectory0,
                        workspaceDirectory0,
                        eage,
                        ie,
                        imageTypes,
                        minDistance,
                        maxDistance,
                        multiplier,
                        handleOutOfMemoryError);
            } else {
                throw _OutOfMemoryError;
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
//        } catch (OutOfMemoryError _OutOfMemoryError) {
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
//                throw _OutOfMemoryError;
//            }
//        }
//    }

    public long getTime() {
        return time;
    }
}
