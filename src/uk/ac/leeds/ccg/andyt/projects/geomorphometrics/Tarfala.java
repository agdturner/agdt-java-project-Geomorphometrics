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
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellIntFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleFactory;
import uk.ac.leeds.ccg.andyt.grids.core.AbstractGrid2DSquareCell;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDouble;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.grids.exchange.ImageExporter;
import uk.ac.leeds.ccg.andyt.grids.exchange.ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.andyt.grids.process.Grid2DSquareCellProcessorDEM;
import uk.ac.leeds.ccg.andyt.grids.utilities.Utilities;
import uk.ac.leeds.ccg.andyt.grids.utilities.FileCreator;

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
        this(new Grids_Environment(), FileCreator.createNewFile());
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
        File _Directory = new File("/nfs/see-fs-02_users/geoagdt/scratch01/Work/people/Scott Watson/Workspace");
        //File _Directory = new File("/nfs/see-fs-02_users/geoagdt/scratch01/Work/people/Scott Watson/Workspace");
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
            nrows = 6192;
            ncols = 7224;
//            chunkNRows = 200;
//            chunkNCols = 883;
//            chunkNRows = 64;
//            chunkNCols = 64;
            chunkNRows = 774;
            chunkNCols = 903;
            NoDataValue = -9999.0d;
            this._Grid2DSquareCellIntFactory = new Grid2DSquareCellIntFactory(
                    this._Directory,
                    chunkNRows,
                    chunkNCols,
                    this._Grid2DSquareCellIntChunkFactory,
                    this._Grids_Environment,
                    this._HandleOutOfMemoryError);
            this._Grids_Environment._Grid2DSquareCellIntFactory = this._Grid2DSquareCellIntFactory;
            //this._Grid2DSquareCellIntFactory.set_ChunkNRows(chunkNRows);
            //this._Grid2DSquareCellIntFactory.set_ChunkNCols(chunkNCols);
            this._Grid2DSquareCellDoubleFactory = new Grid2DSquareCellDoubleFactory(
                    this._Directory,
                    chunkNRows,
                    chunkNCols,
                    this._Grid2DSquareCellDoubleChunkFactory,
                    NoDataValue,
                    this._Grids_Environment,
                    this._HandleOutOfMemoryError);
            this._Grids_Environment._Grid2DSquareCellDoubleFactory = this._Grid2DSquareCellDoubleFactory;
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
            ESRIAsciiGridExporter aESRIAsciiGridExporter = new ESRIAsciiGridExporter();
            ImageExporter aImageExporter = new ImageExporter();
            File workspaceDirectory = new File(inputDirectory + "/Workspace/");
            String[] imageTypes = new String[1];
            imageTypes[0] = "PNG";
            for (File inputDirectoryFile : inputDirectoryFiles) {
                inputFilename = inputDirectoryFile.getName();
                if (inputFilename.endsWith(ascString)) {
                    // Initialisation
                    inputFilenameWithoutExtension = inputFilename.substring(0, inputFilename.length() - 4);
                    outputDirectory = new File(inputDirectory + "/Geomorphometrics/" + inputFilenameWithoutExtension + "/");
                    Grid2DSquareCellDouble grid2DSquareCellDouble = null;
                    // Load input
                    boolean _NotLoadedAsGrid = true;
                    if (_NotLoadedAsGrid) {
                        File inputFile = _Grids_Environment.initFile(
                                inputDirectory,
                                inputFilename,
                                _HandleOutOfMemoryError);
//                        grid2DSquareCellDouble = (Grid2DSquareCellDouble) this._Grid2DSquareCellDoubleFactory.create(
//                                inputFile);
                        grid2DSquareCellDouble = (Grid2DSquareCellDouble) this._Grid2DSquareCellDoubleFactory.create(
                                _GridStatistics,
                                _Directory,
                                inputFile,
                                _Grid2DSquareCellDoubleChunkFactory,
                                0,
                                0,
                                nrows - 1,
                                ncols - 1,
                                _Grids_Environment,
                                _HandleOutOfMemoryError);
                        // Cache input
                        boolean _SwapToFileCache = true;
                        grid2DSquareCellDouble.writeToFile(
                                _SwapToFileCache,
                                _HandleOutOfMemoryError);
                        this._Grids_Environment.get_AbstractGrid2DSquareCell_HashSet().add(grid2DSquareCellDouble);
                        System.out.println("<outputImage>");
                        System.out.println("outputDirectory " + outputDirectory);                        
                        outputImage(
                                grid2DSquareCellDouble,
                                _Grids_Environment.get_Grid2DSquareCellProcessor(),
                                outputDirectory,
                                aImageExporter,
                                imageTypes,
                                _HandleOutOfMemoryError);
                        System.out.println("</outputImage>");
                    } else {
                        System.out.println("check1");
//                        _Grid2DSquareCellDouble = (Grid2DSquareCellDouble) _Grid2DSquareCellDoubleFactory.create(
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
                            + Utilities._ReportTime(System.currentTimeMillis() - time);
                    _Message = _Grids_Environment.println(_Message, _Message0, _HandleOutOfMemoryError);
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
            Grid2DSquareCellDouble grid,
            File outputDirectory,
            File workspaceDirectory,
            ESRIAsciiGridExporter aESRIAsciiGridExporter,
            ImageExporter aImageExporter,
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
//        do_HollowFilledDEM(
//                _Grid2DSquareCellDouble,
//                maxIterations,
//                _Output_Directory,
//                _Workspace_Directory,
//                _HandleOutOfMemoryError );
//            do_SlopeAndAspect(
//                    grid,
//                    outputDirectory,
//                    workspaceDirectory,
//                    aESRIAsciiGridExporter,
//                    aImageExporter,
//                    imageTypes,
//                    minDistance,
//            maxDistance,
//            multiplier,
//                    handleOutOfMemoryError);
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

            log("Processing complete in " + Utilities._ReportTime(System.currentTimeMillis() - time),
                    handleOutOfMemoryError);
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (handleOutOfMemoryError) {
                _Grids_Environment.clear_MemoryReserve();
                _Grids_Environment.swapToFile_Grid2DSquareCellChunks(handleOutOfMemoryError);
                _Grids_Environment.init_MemoryReserve(handleOutOfMemoryError);
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
            AbstractGrid2DSquareCell grid2DSquareCell,
            File outputDirectory0,
            File workspaceDirectory0,
            ESRIAsciiGridExporter aESRIAsciiGridExporter,
            ImageExporter aImageExporter,
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
            File outputDirectory = _Grids_Environment.initFileDirectory(
                    outputDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            File workspaceDirectory = _Grids_Environment.initFileDirectory(
                    workspaceDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            _Filename = _Grids_Environment.initString(_FilenameLength, handleOutOfMemoryError);
            this.set_Directory(workspaceDirectory);
            int _NameLength = 1000;
            String _Name = _Grids_Environment.initString(_NameLength, false);
            BigDecimal[] dimensions = grid2DSquareCell.get_Dimensions(handleOutOfMemoryError);
            double cellsize = Double.valueOf(dimensions[0].toString()).doubleValue();
            double weightIntersect = 1.0d;
            double weightFactor = 1.0d;
            AbstractGrid2DSquareCell dummyGrid = null;
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
                AbstractGrid2DSquareCell[] metrics1 = getMetrics1(
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
                    //rescale
                    metrics1[i] = rescale(
                            metrics1[i],
                            null, min, max, handleOutOfMemoryError);
                    // output
                    output(
                            metrics1[i],
                            _Grids_Environment.get_Grid2DSquareCellProcessor(),
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
                _Grids_Environment.clear_MemoryReserve();
                _Grids_Environment.swapToFile_Grid2DSquareCellChunks(handleOutOfMemoryError);
                _Grids_Environment.init_MemoryReserve(handleOutOfMemoryError);
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

    public void do_Roughness(
            AbstractGrid2DSquareCell _Grid2DSquareCell,
            File _Output_Directory0,
            File _Workspace_Directory0,
            boolean _HandleOutOfMemoryError)
            throws Exception, Error {
//        try {
//            // Initialistaion
//            _Filename = new String( "_Roughness" );
//            File _Output_Directory = initFileDirectory(
//                    _Output_Directory0,
//                    _Filename,
//                    _HandleOutOfMemoryError );
//            File _Workspace_Directory = initFileDirectory(
//                    _Workspace_Directory0,
//                    _Filename,
//                    _HandleOutOfMemoryError );
//            _Filename = initString( _FilenameLength, _HandleOutOfMemoryError );
//            this.set_Directory( _Workspace_Directory );
//            int _NameLength = 1000;
//            String _Name = initString( _NameLength, _HandleOutOfMemoryErrorFalse );
//            BigDecimal[] dimensions = _Grid2DSquareCell.get_Dimensions( _HandleOutOfMemoryError );
//            double cellsize = Double.valueOf( dimensions[ 0 ].toString() ).doubleValue();
//            double weightIntersect = 1.0d;
//            double weightFactor = 1.0d;
//            AbstractGrid2DSquareCell dummyGrid = null;
//            long nrows = _Grid2DSquareCell.get_NRows( _HandleOutOfMemoryError );
//            long ncols = _Grid2DSquareCell.get_NCols( _HandleOutOfMemoryError );
//            long _StartRowIndexLong = 0L;
//            long _StartColIndexLong = 0L;
//            long _EndRowIndexLong =  0L;
//            long _EndColIndexLong = 0L;
//            long _long_0 = 0L;
//            long _long_1 = 1L;
//            double distance = 0.0d;
//            int distances = 2;
//            int i = 0;
//            int _int_2 = 2;
//            int _int_0 = 0;
//            for ( distances = 2; distances <= 32; distances *=2 ) {
//                distance = cellsize * ( double ) distances;
//                AbstractGrid2DSquareCell _Roughness = get_Roughness(
//                        _Grid2DSquareCell,
//                        distance,
//                        weightIntersect,
//                        weightFactor,
//                        _Grid2DSquareCellDoubleFactory,
//                        _Grid2DSquareCellIntFactory,
//                        _HandleOutOfMemoryError );
//                maskEdges(
//                            _Roughness,
//                            distances,
//                            _HandleOutOfMemoryError );
////                    _Name = _Roughness.get_Name( _HandleOutOfMemoryError ) + "_" + distances;
////                    metrics1[ i ].set_Name( _Name, _HandleOutOfMemoryError );
////                    _Name = initString( _NameLength, _HandleOutOfMemoryError );
////                    output(
////                            _Roughness,
////                            _Output_Directory,
////                            _HandleOutOfMemoryError );
//                    _OutputESRIAsciiGrid(
//                            _Roughness,
//                            _Output_Directory,
//                            _HandleOutOfMemoryError );
//             }
//        } catch ( OutOfMemoryError _OutOfMemoryError ) {
//            if ( _HandleOutOfMemoryError ) {
//                clear_MemoryReserve();
//                swapToFile_Grid2DSquareCellChunks();
//                init_MemoryReserve( _HandleOutOfMemoryError );
//                do_Roughness(
//                        _Grid2DSquareCell,
//                        _Output_Directory0,
//                        _Workspace_Directory0,
//                        _HandleOutOfMemoryError );
//            } else {
//                throw _OutOfMemoryError;
//            }
//        }
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
            AbstractGrid2DSquareCell grid,
            File outputDirectory0,
            File workspaceDirectory0,
            ESRIAsciiGridExporter eSRIAsciiGridExporter,
            ImageExporter imageExporter,
            String[] imageTypes,
            int minDistance,
            int maxDistance,
            int multiplier,
            boolean handleOutOfMemoryError)
            throws Exception, Error {
        try {
            // Initialistaion
            _Filename = "_SlopeAndAspect";
            File outputDirectory = _Grids_Environment.initFileDirectory(
                    outputDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            File workspaceDirectory = _Grids_Environment.initFileDirectory(
                    workspaceDirectory0,
                    _Filename,
                    handleOutOfMemoryError);
            _Filename = _Grids_Environment.initString(_FilenameLength, handleOutOfMemoryError);
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
                AbstractGrid2DSquareCell[] slopeAndAspect = getSlopeAspect(
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
                            _Grids_Environment.get_Grid2DSquareCellProcessor(),
                            outputDirectory,
                            imageExporter,
                            imageTypes,
                            eSRIAsciiGridExporter,
                            handleOutOfMemoryError);
                    slopeAndAspect[i] = null;
                    this._Grids_Environment.get_AbstractGrid2DSquareCell_HashSet().remove(slopeAndAspect[i]);
                }
            }
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (handleOutOfMemoryError) {
                _Grids_Environment.clear_MemoryReserve();
                _Grids_Environment.swapToFile_Grid2DSquareCellChunks(handleOutOfMemoryError);
                _Grids_Environment.init_MemoryReserve(handleOutOfMemoryError);
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
//            AbstractGrid2DSquareCell _Grid2DSquareCell,
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
