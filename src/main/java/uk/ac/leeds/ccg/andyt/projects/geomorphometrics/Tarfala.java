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
import java.math.BigDecimal;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Grid2DSquareCellIntFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Grid2DSquareCellDoubleFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_AbstractGrid2DSquareCell;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Grid2DSquareCellDouble;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.grids.exchange.Grids_ImageExporter;
import uk.ac.leeds.ccg.andyt.grids.exchange.Grids_ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.andyt.grids.process.Grid2DSquareCellProcessorDEM;
import uk.ac.leeds.ccg.andyt.grids.utilities.Grids_Utilities;
import uk.ac.leeds.ccg.andyt.grids.utilities.Grids_FileCreator;

/**
 * Originally this class was for processing some data from Sweden, but it has 
 * also been used to process data from the Himalayan region.
 * @author geoagdt
 */
public class Tarfala
        extends Grid2DSquareCellProcessorDEM {

    private long time;
    boolean _HandleOutOfMemoryError;
    String _FileSeparator;
    int _MessageLength;
    int _FilenameLength;
    String _Message0;
    String _Message;
    String _Filename;

    /**
     * Creates a new RoofGeneralisation
     */
    public Tarfala() {
        this(new Grids_Environment(), Grids_FileCreator.createNewFile());
    }

    /**
     * Creates a new RoofGeneralisation using specified _Directory. WARNING:
     * Files in the specified _Directory may get overwritten.
     *
     * @param _Grids_Environment
     * @param workspace
     */
    public Tarfala(
            Grids_Environment _Grids_Environment,
            File workspace) {
        super(_Grids_Environment, workspace);
        this.time = System.currentTimeMillis();
        this._HandleOutOfMemoryError = true;
        this._FileSeparator = System.getProperty("file.separator");
        this._MessageLength = 1000;
        this._FilenameLength = 1000;
        this._Message0 = _Grids_Environment.initString(_MessageLength, _HandleOutOfMemoryError);
        this._Message = _Grids_Environment.initString(_MessageLength, _HandleOutOfMemoryError);
        this._Filename = _Grids_Environment.initString(_FilenameLength, _HandleOutOfMemoryError);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //File _Directory = new File("/nfs/see-fs-02_users/geoagdt/scratch01/Work/people/Scott Watson/test/Workspace/");
        //File _Directory = new File("/nfs/see-fs-02_users/geoagdt/scratch01/Work/people/Scott Watson/Workspace/");
        File _Directory = new File("/nfs/see-fs-02_users/geoagdt/scratch01/Work/people/Owen/Workspace/");
        //File _Directory = new File("C:/Temp/Owen/Workspace");
        //File _Directory = new File("C:/Temp/Owen/Workspace2");
        System.out.print("" + _Directory.toString());
        if (_Directory.exists()) {
            System.out.println(" exists.");
            _Directory.mkdirs();
        } else {
            System.out.println(" does not exist.");
        }
        Grids_Environment _Grids_Environment = new Grids_Environment();
        Tarfala t = new Tarfala(_Grids_Environment, _Directory);
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
            nrows = 1993;
            ncols = 6728;
            //ncols = 6727;

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
            chunkNRows = 1993;
            chunkNCols = 232;
            //chunkNCols = 217;
            NoDataValue = -9999.0d;
            init_Grid2DSquareCellIntFactory(
                    chunkNRows,
                    chunkNCols);
            //this._Grid2DSquareCellIntFactory.set_ChunkNRows(chunkNRows);
            //this._Grid2DSquareCellIntFactory.set_ChunkNCols(chunkNCols);
            init_Grid2DSquareCellDoubleFactory(chunkNRows,
                    chunkNCols,
                    NoDataValue);
            this.env.setGrid2DSquareCellProcessor(this);
            //this._Grid2DSquareCellDoubleFactory.set_ChunkNRows(chunkNRows);
            //this._Grid2DSquareCellDoubleFactory.set_ChunkNCols(chunkNCols);
            //this._Grid2DSquareCellDoubleFactory.set_NoDataValue(NoDataValue);

            boolean swapOutInitialisedFiles = true;
            File inputDirectory = get_Directory(_HandleOutOfMemoryError);
            File[] inputDirectoryFiles = inputDirectory.listFiles();
            String inputFilename;
            String ascString = "asc";
            String inputFilenameWithoutExtension;
            File outputDirectory;
            Grids_ESRIAsciiGridExporter aESRIAsciiGridExporter = new Grids_ESRIAsciiGridExporter(env);
            Grids_ImageExporter aImageExporter = new Grids_ImageExporter(env);
            File workspaceDirectory = new File(inputDirectory + "/Workspace/");
            String[] imageTypes = new String[1];
            imageTypes[0] = "PNG";
            for (File inputDirectoryFile : inputDirectoryFiles) {
                inputFilename = inputDirectoryFile.getName();
                System.out.println("inputFilename " + inputFilename);
                if (inputFilename.endsWith(ascString)) {
                    // Initialisation
                    inputFilenameWithoutExtension = inputFilename.substring(0, inputFilename.length() - 4);
                    outputDirectory = new File(inputDirectory + "/Geomorphometrics/" + inputFilenameWithoutExtension + "/");
                    Grids_Grid2DSquareCellDouble grid2DSquareCellDouble = null;
                    // Load input
                    boolean _NotLoadedAsGrid = true;
                    if (_NotLoadedAsGrid) {
                        File inputFile = env.initFile(
                                inputDirectory,
                                inputFilename,
                                _HandleOutOfMemoryError);

//                        if (inputFilename.equalsIgnoreCase("rastert_c0202_c1.asc")) {
//                            nrows = 7594;
//                            ncols = 8394;
//                            chunkNRows = 3797;
//                            chunkNCols = 1399;
//                        }
                        init_Grid2DSquareCellIntFactory(
                                chunkNRows,
                                chunkNCols);
                        init_Grid2DSquareCellDoubleFactory(chunkNRows,
                                chunkNCols,
                                NoDataValue);

//                        grid2DSquareCellDouble = (Grids_Grid2DSquareCellDouble) this._Grid2DSquareCellDoubleFactory.create(
//                                inputFile);
                        grid2DSquareCellDouble = (Grids_Grid2DSquareCellDouble) this._Grid2DSquareCellDoubleFactory.create(_GridStatistics,
                                _Directory,
                                inputFile,
                                _Grid2DSquareCellDoubleChunkFactory,
                                0,
                                0,
                                nrows - 1,
                                ncols - 1,
                                //chunkNRows - 1,
                                //chunkNCols - 1,
                                env,
                                _HandleOutOfMemoryError);
                        // Cache input
                        boolean _SwapToFileCache = true;
                        grid2DSquareCellDouble.writeToFile(
                                _SwapToFileCache,
                                _HandleOutOfMemoryError);
                        this.env.get_AbstractGrid2DSquareCell_HashSet().add(grid2DSquareCellDouble);
                        System.out.println("<outputImage>");
                        System.out.println("outputDirectory " + outputDirectory);
                        outputImage(
                                grid2DSquareCellDouble,
                                //this,
                                //_Grids_Environment.get_Grid2DSquareCellProcessor(),
                                outputDirectory,
                                aImageExporter,
                                imageTypes,
                                _HandleOutOfMemoryError);
                        System.out.println("</outputImage>");
                    } else {
                        System.out.println("check1");
//                        _Grid2DSquareCellDouble = (Grids_Grid2DSquareCellDouble) _Grid2DSquareCellDoubleFactory.create(
//                                new File(_Input_Directory.toString() + this._FileSeparator + _Input_Filename_WithoutExtension + "uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleFactory_chunkNrows(" + chunkNrows + ")_chunkNcols(" + chunkNcols + ")"));
//                        this._AbstractGrid2DSquareCell_HashSet.add(_Grid2DSquareCellDouble);
                    }
                    System.out.println(grid2DSquareCellDouble.toString(_HandleOutOfMemoryError));
                    // generalise
                    run1(
                            grid2DSquareCellDouble,
                            outputDirectory,
                            workspaceDirectory,
                            aESRIAsciiGridExporter,
                            aImageExporter,
                            imageTypes,
                            swapOutInitialisedFiles,
                            _HandleOutOfMemoryError);
                    _Message = null;
                    _Message = "Processing complete in "
                            + Grids_Utilities._ReportTime(System.currentTimeMillis() - time);
                    _Message = env.println(_Message, _Message0, _HandleOutOfMemoryError);
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
        this._Grid2DSquareCellDoubleFactory = new Grids_Grid2DSquareCellDoubleFactory(
                this._Directory,
                chunkNRows,
                chunkNCols,
                this._Grid2DSquareCellDoubleChunkFactory,
                NoDataValue,
                this.env,
                this._HandleOutOfMemoryError);
    }

    private void init_Grid2DSquareCellIntFactory(
            int chunkNRows,
            int chunkNCols) {
        this._Grid2DSquareCellIntFactory = new Grids_Grid2DSquareCellIntFactory(
                this._Directory,
                chunkNRows,
                chunkNCols,
                this._Grid2DSquareCellIntChunkFactory,
                this.env,
                this._HandleOutOfMemoryError);
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
            Grids_Grid2DSquareCellDouble grid,
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
            long nrows = grid.get_NRows(handleOutOfMemoryError);
            long ncols = grid.get_NCols(handleOutOfMemoryError);
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
                env.clear_MemoryReserve();
                env.swapToFile_Grid2DSquareCellChunks(handleOutOfMemoryError);
                env.init_MemoryReserve(handleOutOfMemoryError);
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
     * @param grid2DSquareCell
     * @param outputDirectory0
     * @param workspaceDirectory0
     * @param aESRIAsciiGridExporter
     * @param aImageExporter
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
            Grids_AbstractGrid2DSquareCell grid2DSquareCell,
            File outputDirectory0,
            File workspaceDirectory0,
            Grids_ESRIAsciiGridExporter aESRIAsciiGridExporter,
            Grids_ImageExporter aImageExporter,
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
            File outputDirectory = env.initFileDirectory(
                    outputDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            File workspaceDirectory = env.initFileDirectory(
                    workspaceDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            _Filename = env.initString(_FilenameLength, handleOutOfMemoryError);
            this.set_Directory(workspaceDirectory);
            int _NameLength = 1000;
            String _Name = env.initString(_NameLength, false);
            BigDecimal[] dimensions = grid2DSquareCell.get_Dimensions(handleOutOfMemoryError);
            double cellsize = Double.valueOf(dimensions[0].toString()).doubleValue();
            double weightIntersect = 1.0d;
            double weightFactor = 1.0d;
            Grids_AbstractGrid2DSquareCell dummyGrid = null;
            long nrows = grid2DSquareCell.get_NRows(handleOutOfMemoryError);
            long ncols = grid2DSquareCell.get_NCols(handleOutOfMemoryError);
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
                Grids_AbstractGrid2DSquareCell[] metrics1 = getMetrics1(
                        grid2DSquareCell,
                        distance,
                        weightIntersect,
                        weightFactor,
                        _Grid2DSquareCellDoubleFactory,
                        _Grid2DSquareCellIntFactory,
                        swapOutInitialisedFiles,
                        swapOutProcessedChunks,
                        handleOutOfMemoryError);
                for (i = int_0; i < metrics1.length; i++) {
//                    maskEdges(
//                            metrics1[i],
//                            distances,
//                            _HandleOutOfMemoryError);
//                    //rescale
//                    metrics1[i] = rescale(
//                            metrics1[i],
//                            null, min, max, handleOutOfMemoryError);
                    // output
                    output(
                            metrics1[i],
                            outputDirectory,
                            aImageExporter,
                            imageTypes,
                            aESRIAsciiGridExporter,
                            handleOutOfMemoryError);
//                    _Name = metrics1[i].get_Name(handleOutOfMemoryError);
//                    if (_Name.startsWith("count_hhhl") || _Name.startsWith("count_hhll")) {
//                        output(
//                                metrics1[i],
//                                outputDirectory,
//                                aImageExporter,
//                                imageTypes,
//                                aESRIAsciiGridExporter,
//                                handleOutOfMemoryError);
////                        _OutputESRIAsciiGrid(
////                                metrics1[i],
////                                _Output_Directory,
////                                _ESRIAsciiGridExporter,
////                                _HandleOutOfMemoryError);
//                    }
                }
            }
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                env.swapToFile_Grid2DSquareCellChunks(handleOutOfMemoryError);
                env.init_MemoryReserve(handleOutOfMemoryError);
                System.out.println("Going round the loop again...");
                do_Metrics1(
                        grid2DSquareCell,
                        outputDirectory0,
                        workspaceDirectory0,
                        aESRIAsciiGridExporter,
                        aImageExporter,
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
            Grids_AbstractGrid2DSquareCell grid,
            File outputDirectory0,
            File workspaceDirectory0,
            boolean handleOutOfMemoryError)
            throws Exception, Error {
        try {
            // Initialistaion
            _Filename = "_Metrics2";
            File outputDirectory = env.initFileDirectory(
                    outputDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            File workspaceDirectory = env.initFileDirectory(
                    workspaceDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            _Filename = env.initString(_FilenameLength, handleOutOfMemoryError);
            this.set_Directory(workspaceDirectory);
            int _NameLength = 1000;
            String _Name = env.initString(_NameLength, handleOutOfMemoryError);
            BigDecimal[] dimensions = grid.get_Dimensions(handleOutOfMemoryError);
            double cellsize = Double.valueOf(dimensions[0].toString()).doubleValue();
            double weightIntersect = 1.0d;
            double weightFactor = 1.0d;
            Grids_AbstractGrid2DSquareCell dummyGrid = null;
            long nrows = grid.get_NRows(handleOutOfMemoryError);
            long ncols = grid.get_NCols(handleOutOfMemoryError);
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
//                Grids_AbstractGrid2DSquareCell roughness = getMetrics2(
//                        grid,
//                        distance,
//                        weightIntersect,
//                        weightFactor,
//                        _NameLength,
//                        _Grid2DSquareCellDoubleFactory,
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
     * @param grid
     * @param outputDirectory0
     * @param workspaceDirectory0
     * @param eSRIAsciiGridExporter
     * @param imageExporter
     * @param imageTypes
     * @param minDistance
     * @param maxDistance
     * @param multiplier
     * @param handleOutOfMemoryError
     * @throws Exception
     * @throws Error
     */
    public void do_SlopeAndAspect(
            Grids_AbstractGrid2DSquareCell grid,
            File outputDirectory0,
            File workspaceDirectory0,
            Grids_ESRIAsciiGridExporter eSRIAsciiGridExporter,
            Grids_ImageExporter imageExporter,
            String[] imageTypes,
            int minDistance,
            int maxDistance,
            int multiplier,
            boolean handleOutOfMemoryError)
            throws Exception, Error {
        try {
            // Initialistaion
            _Filename = "_SlopeAndAspect";
            File outputDirectory = env.initFileDirectory(
                    outputDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            File workspaceDirectory = env.initFileDirectory(
                    workspaceDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            _Filename = env.initString(_FilenameLength, handleOutOfMemoryError);
            this.set_Directory(workspaceDirectory);
            BigDecimal[] dimensions = grid.get_Dimensions(handleOutOfMemoryError);
            double cellsize = Double.valueOf(dimensions[0].toString()).doubleValue();
            double weightIntersect = 1.0d;
            double weightFactor = 1.0d;
            double distance = 0.0d;
            int distances = 2;
            int i = 0;
            for (distances = minDistance; distances <= maxDistance; distances *= multiplier) {
                distance = cellsize * (double) distances;
                Grids_AbstractGrid2DSquareCell[] slopeAndAspect = getSlopeAspect(
                        grid,
                        distance,
                        weightIntersect,
                        weightFactor,
                        handleOutOfMemoryError);
                for (i = 0; i < slopeAndAspect.length; i++) {
//                    mask(
//                            _SlopeAndAspect[i],
//                            distances,
//                            _HandleOutOfMemoryError);
//                    _OutputESRIAsciiGrid(
//                            _SlopeAndAspect[i],
//                            _Output_Directory,
//                            _ESRIAsciiGridExporter,
//                            _HandleOutOfMemoryError);
//                    outputImage(
//                            _SlopeAndAspect[i],
//                            _Output_Directory,
//                            _ImageExporter,
//                            imageTypes,
//                            _HandleOutOfMemoryError);
                    output(slopeAndAspect[i],
                            //_Grids_Environment.get_Grid2DSquareCellProcessor(),
                            outputDirectory,
                            imageExporter,
                            imageTypes,
                            eSRIAsciiGridExporter,
                            handleOutOfMemoryError);
                    slopeAndAspect[i] = null;
                    this.env.get_AbstractGrid2DSquareCell_HashSet().remove(slopeAndAspect[i]);
                }
            }
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (handleOutOfMemoryError) {
                env.clear_MemoryReserve();
                env.swapToFile_Grid2DSquareCellChunks(handleOutOfMemoryError);
                env.init_MemoryReserve(handleOutOfMemoryError);
                do_SlopeAndAspect(
                        grid,
                        outputDirectory0,
                        workspaceDirectory0,
                        eSRIAsciiGridExporter,
                        imageExporter,
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
//            Grids_AbstractGrid2DSquareCell _Grid2DSquareCell,
//            int distances,
//            boolean _HandleOutOfMemoryError) {
//        try {
//            System.out.println("Masking Edges");
//            long nrows = _Grid2DSquareCell.get_NRows(_HandleOutOfMemoryErrorFalse);
//            long ncols = _Grid2DSquareCell.get_NCols(_HandleOutOfMemoryErrorFalse);
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
//                    _HandleOutOfMemoryError);
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
//                    _HandleOutOfMemoryError);
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
//                    _HandleOutOfMemoryError);
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
//                    _HandleOutOfMemoryError);
//        } catch (OutOfMemoryError _OutOfMemoryError) {
//            if (_HandleOutOfMemoryError) {
//                clear_MemoryReserve();
////                swapToFile_Grid2DSquareCellChunk_AccountDetail();
//                _SwapToFileGrid2DSquareCellChunksExcept(_Grid2DSquareCell);
//                init_MemoryReserve(_Grid2DSquareCell, _HandleOutOfMemoryError);
//                maskEdges(
//                        _Grid2DSquareCell,
//                        distances,
//                        _HandleOutOfMemoryError);
//            } else {
//                throw _OutOfMemoryError;
//            }
//        }
//    }

    public long getTime() {
        return time;
    }
}
