/**
 * <one line to give the library's name and a brief idea of what it does.>
 * Copyright (C) 2005 Andy Turner, CCG, University of Leeds, UK
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package uk.ac.leeds.ccg.andyt.projects.geomorphometrics;

import java.io.File;
import java.math.BigDecimal;
import uk.ac.leeds.ccg.andyt.grids.core.Grids_Environment;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_AbstractGridNumber;
import uk.ac.leeds.ccg.andyt.grids.core.grid.Grids_GridDouble;
import uk.ac.leeds.ccg.andyt.grids.exchange.Grids_ImageExporter;
import uk.ac.leeds.ccg.andyt.grids.exchange.Grids_ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.andyt.grids.process.Grids_ProcessorDEM;
import uk.ac.leeds.ccg.andyt.grids.utilities.Grids_Utilities;
import uk.ac.leeds.ccg.andyt.grids.utilities.Grids_FileCreator;

public class RoofGeneralisation
        extends Grids_ProcessorDEM {

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
    protected RoofGeneralisation() {}

    public RoofGeneralisation(
            Grids_Environment ge) {
        super(ge);
        this.time = System.currentTimeMillis();
        this._HandleOutOfMemoryError = true;
        this._FileSeparator = System.getProperty("file.separator");
        this._MessageLength = 1000;
        this._FilenameLength = 1000;
        this._Message0 = ge.initString(_MessageLength, _HandleOutOfMemoryError);
        this._Message = ge.initString(_MessageLength, _HandleOutOfMemoryError);
        this._Filename = ge.initString(_FilenameLength, _HandleOutOfMemoryError);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // _NotLoadedAsGrid = true?
        // Output images?
        // VM parameters: -Xmx1200m
        runOnPC();
    }

    public static void runOnPC() {
        //File Directory = new File( "D:/Work/Projects/Geomorphometrics/Workspace/" );
        File _Directory = new File("C:/Work/People/Sadhvi Selvaraj/Roofs/data/synthetic/");
        //File Directory = new File("C:/Work/People/Sadhvi Selvaraj/Roofs/data/NeuralNetwork/Input/");
        //File Directory = new File( "C:/temp/GEOG5060/Geomorphometrics/" );
        System.out.print("" + _Directory.toString());
        if (_Directory.exists()) {
            System.out.println(" exists.");
        } else {
            System.out.println(" does not exist.");
        }
        Grids_Environment ge;
        ge = new Grids_Environment(_Directory);
        RoofGeneralisation aRoofGeneralisation = new RoofGeneralisation(ge);
        aRoofGeneralisation.run();
    }

    public void run() {
        try {
            File _Input_Directory = getDirectory(_HandleOutOfMemoryError);
            File[] _Files = _Input_Directory.listFiles();
            String _Input_Filename;
            String _ASC = "asc";
            String _Input_Filename_WithoutExtension;
            File _Output_Directory;
            Grids_ImageExporter _ImageExporter = new Grids_ImageExporter(ge);
            File _Workspace_Directory = new File(_Input_Directory + "/Workspace/");
            String[] _ImageTypes = new String[1];
            _ImageTypes[0] = "PNG";
            for (int i = 0; i < _Files.length; i++) {
                _Input_Filename = _Files[i].getName();
                if (_Input_Filename.endsWith(_ASC)) {
                    // Initialisation
                    _Input_Filename_WithoutExtension = _Input_Filename.substring(0, _Input_Filename.length() - 4);
                    _Output_Directory = new File(_Input_Directory + "/Geomorphometrics/" + _Input_Filename_WithoutExtension + "/");
                    Grids_GridDouble _Grid2DSquareCellDouble = null;
                    // Load input
                    boolean _NotLoadedAsGrid = true;
                    if (_NotLoadedAsGrid) {
                        File _Input_File = ge.initFile(
                                _Input_Directory,
                                _Input_Filename,
                                _HandleOutOfMemoryError);
                        _Grid2DSquareCellDouble = (Grids_GridDouble) this.Grid2DSquareCellDoubleFactory.create(_Input_File);
                        // Cache input
                        boolean _SwapToFileCache = true;
                        _Grid2DSquareCellDouble.writeToFile(
                                _SwapToFileCache,
                                _HandleOutOfMemoryError);
                        ge.getGrids().add(_Grid2DSquareCellDouble);
//                        outputImage(
//                                _Grid2DSquareCellDouble,
//                                _Output_Directory,
//                                _ImageExporter,
//                                _ImageTypes,
//                                _HandleOutOfMemoryError);
                    } else {
                        System.out.println("check1");
//                        _Grid2DSquareCellDouble = (Grids_GridDouble) Grid2DSquareCellDoubleFactory.create(
//                                new File(_Input_Directory.toString() + this._FileSeparator + _Input_Filename_WithoutExtension + "uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleFactory_chunkNrows(" + chunkNrows + ")_chunkNcols(" + chunkNcols + ")"));
//                        this._AbstractGrid2DSquareCell_HashSet.add(_Grid2DSquareCellDouble);
                    }

                    // generalise
                    run1(
                            _Grid2DSquareCellDouble,
                            _Output_Directory,
                            _Workspace_Directory,
                            _ImageExporter,
                            _HandleOutOfMemoryError);
                    _Message = null;
                    _Message = "Processing complete in " +
                            Grids_Utilities._ReportTime(System.currentTimeMillis() - time);
                    _Message = ge.println(_Message, _Message0, _HandleOutOfMemoryError);
                // output

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    public void run1(
            Grids_GridDouble _Grid2DSquareCellDouble,
            File _Output_Directory,
            File _Workspace_Directory,
            Grids_ImageExporter _ImageExporter,
            boolean _HandleOutOfMemoryError)
            throws Exception, Error {
        try {
            // Initialise
            log("run1();",
                    _HandleOutOfMemoryError);
            int maxIterations = 2;
            long nrows = _Grid2DSquareCellDouble.getNRows(_HandleOutOfMemoryError);
            long ncols = _Grid2DSquareCellDouble.getNCols(_HandleOutOfMemoryError);
            long distances = 16;

//        maxIterations = 2000;
//        do_HollowFilledDEM(
//                _Grid2DSquareCellDouble,
//                maxIterations,
//                _Output_Directory,
//                _Workspace_Directory,
//                _HandleOutOfMemoryError );

//        do_SlopeAndAspect(
//                _Grid2DSquareCellDouble,
//                _Output_Directory,
//                _Workspace_Directory,
//                _HandleOutOfMemoryError );

            do_Metrics1(
                    _Grid2DSquareCellDouble,
                    _Output_Directory,
                    _Workspace_Directory,
                    _ImageExporter,
                    _HandleOutOfMemoryError);

            log("Processing complete in " + Grids_Utilities._ReportTime(System.currentTimeMillis() - time),
                    _HandleOutOfMemoryError);
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (_HandleOutOfMemoryError) {
                ge.clearMemoryReserve();
                ge.swapChunks(_HandleOutOfMemoryError);
                ge.initMemoryReserve(_HandleOutOfMemoryError);
                run1(
                        _Grid2DSquareCellDouble,
                        _Output_Directory,
                        _Workspace_Directory,
                        _ImageExporter,
                        _HandleOutOfMemoryError);
            } else {
                throw _OutOfMemoryError;
            }
        }
    }

    public void do_Metrics1(
            Grids_AbstractGridNumber g,
            File _Output_Directory0,
            File _Workspace_Directory0,
            Grids_ImageExporter ie,
            boolean handleOutOfMemoryError)
            throws Exception, Error {
        try {
            // Initialistaion
            Grids_ESRIAsciiGridExporter eage = new Grids_ESRIAsciiGridExporter(ge);
            _Filename = "_Metrics1";
            File _Output_Directory = ge.initFileDirectory(
                    _Output_Directory0,
                    _Filename,
                    handleOutOfMemoryError);
            File _Workspace_Directory = ge.initFileDirectory(
                    _Workspace_Directory0,
                    _Filename,
                    handleOutOfMemoryError);
            _Filename = ge.initString(_FilenameLength, handleOutOfMemoryError);
            this.setDirectory(_Workspace_Directory);
            boolean swapOutInitialisedFiles = false;
            boolean swapOutProcessedChunks = false;
            int _NameLength = 1000;
            String _Name = ge.initString(_NameLength, false);
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
            double min = 0;
            double max = 1;
            for (distances = 2; distances <= 4; distances *= 2) {
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
                for (i = _int_0; i < metrics1.length; i++) {
//                    maskEdges(
//                            metrics1[i],
//                            distances,
//                            _HandleOutOfMemoryError);
                    //rescale
                    metrics1[i] = rescale(
                            metrics1[i],
                            null, min, max, handleOutOfMemoryError);
                    // output
                    _Name = metrics1[i].getName(handleOutOfMemoryError);
                    if (_Name.startsWith("count_hhhl") || _Name.startsWith("count_hhll")) {
//                        output(
//                                metrics1[i],
//                                _Output_Directory,
//                                _ImageExporter,
//                                null,
//                                _ESRIAsciiGridExporter,
//                                _HandleOutOfMemoryError);
                        outputESRIAsciiGrid(
                                metrics1[i],
                                _Output_Directory,
                                eage, 
                                handleOutOfMemoryError);
                    }
                }
            }
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (handleOutOfMemoryError) {
                ge.clearMemoryReserve();
                ge.swapChunks(handleOutOfMemoryError);
                ge.initMemoryReserve(handleOutOfMemoryError);
                do_Metrics1(
                        g,
                        _Output_Directory0,
                        _Workspace_Directory0,
                        ie,
                        handleOutOfMemoryError);
            } else {
                throw _OutOfMemoryError;
            }
        }
    }

    public void do_Roughness(
            Grids_AbstractGridNumber _Grid2DSquareCell,
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
//            this.setDirectory( _Workspace_Directory );
//            int _NameLength = 1000;
//            String _Name = initString( _NameLength, _HandleOutOfMemoryErrorFalse );
//            BigDecimal[] dimensions = _Grid2DSquareCell.getDimensions( _HandleOutOfMemoryError );
//            double cellsize = Double.valueOf( dimensions[ 0 ].toString() ).doubleValue();
//            double weightIntersect = 1.0d;
//            double weightFactor = 1.0d;
//            Grids_AbstractGridNumber dummyGrid = null;
//            long nrows = _Grid2DSquareCell.getNRows( _HandleOutOfMemoryError );
//            long ncols = _Grid2DSquareCell.getNCols( _HandleOutOfMemoryError );
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
//                Grids_AbstractGridNumber _Roughness = get_Roughness(
//                        _Grid2DSquareCell,
//                        distance,
//                        weightIntersect,
//                        weightFactor,
//                        Grid2DSquareCellDoubleFactory,
//                        Grid2DSquareCellIntFactory,
//                        _HandleOutOfMemoryError );
//                maskEdges(
//                            _Roughness,
//                            distances,
//                            _HandleOutOfMemoryError );
////                    _Name = _Roughness.getName( _HandleOutOfMemoryError ) + "_" + distances;
////                    metrics1[ i ].setName( _Name, _HandleOutOfMemoryError );
////                    _Name = initString( _NameLength, _HandleOutOfMemoryError );
////                    output(
////                            _Roughness,
////                            _Output_Directory,
////                            _HandleOutOfMemoryError );
//                    outputESRIAsciiGrid(
//                            _Roughness,
//                            _Output_Directory,
//                            _HandleOutOfMemoryError );
//             }
//        } catch ( OutOfMemoryError _OutOfMemoryError ) {
//            if ( _HandleOutOfMemoryError ) {
//                clearMemoryReserve();
//                swapChunks();
//                initMemoryReserve( _HandleOutOfMemoryError );
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

//    public void do_SlopeAndAspect(
//            Grids_AbstractGridNumber _Grid2DSquareCell,
//            File _Output_Directory0,
//            File _Workspace_Directory0,
//            boolean _HandleOutOfMemoryError)
//            throws Exception, Error {
//        try {
//            // Initialistaion
//            _Filename = new String("_SlopeAndAspect");
//            File _Output_Directory = initFileDirectory(
//                    _Output_Directory0,
//                    _Filename,
//                    _HandleOutOfMemoryError);
//            File _Workspace_Directory = initFileDirectory(
//                    _Workspace_Directory0,
//                    _Filename,
//                    _HandleOutOfMemoryError);
//            _Filename = initString(_FilenameLength, _HandleOutOfMemoryError);
//            this.setDirectory(_Workspace_Directory);
//            BigDecimal[] dimensions = _Grid2DSquareCell.getDimensions(_HandleOutOfMemoryError);
//            double cellsize = Double.valueOf(dimensions[ 0].toString()).doubleValue();
//            double weightIntersect = 1.0d;
//            double weightFactor = 1.0d;
//            Grids_AbstractGridNumber dummyGrid = null;
//            double distance = 0.0d;
//            int distances = 2;
//            int i = 0;
//            for (distances = 2; distances <= 16; distances *= 2) {
//                distance = cellsize * (double) distances;
//                Grids_AbstractGridNumber[] _SlopeAndAspect = getSlopeAspect(
//                        _Grid2DSquareCell,
//                        distance,
//                        weightIntersect,
//                        weightFactor,
//                        _HandleOutOfMemoryError);
//                for (i = 0; i < _SlopeAndAspect.length; i++) {
//                    maskEdges(
//                            _SlopeAndAspect[i],
//                            distances,
//                            _HandleOutOfMemoryError);
//                    output(
//                            _SlopeAndAspect[i],
//                            _Output_Directory,
//                            _HandleOutOfMemoryError);
//                    this._AbstractGrid2DSquareCell_HashSet.remove(_SlopeAndAspect[i]);
//                }
//            }
//        } catch (OutOfMemoryError _OutOfMemoryError) {
//            if (_HandleOutOfMemoryError) {
//                clearMemoryReserve();
//                swapChunks();
//                initMemoryReserve(_HandleOutOfMemoryError);
//                do_SlopeAndAspect(
//                        _Grid2DSquareCell,
//                        _Output_Directory0,
//                        _Workspace_Directory0,
//                        _HandleOutOfMemoryError);
//            } else {
//                throw _OutOfMemoryError;
//            }
//        }
//    }
//    /**
//     *
//     */
//    public void maskEdges(
//            Grids_AbstractGridNumber _Grid2DSquareCell,
//            int distances,
//            boolean _HandleOutOfMemoryError) {
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
//                clearMemoryReserve();
////                swapChunk_AccountDetail();
//                _SwapToFileGrid2DSquareCellChunksExcept(_Grid2DSquareCell);
//                initMemoryReserve(_Grid2DSquareCell, _HandleOutOfMemoryError);
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
