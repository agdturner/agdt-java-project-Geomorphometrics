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
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashSet;
import uk.ac.leeds.ccg.andyt.grids.core.AbstractGrid2DSquareCell;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDouble;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleChunk64CellMapFactory;
import uk.ac.leeds.ccg.andyt.grids.core.AbstractGrid2DSquareCellDoubleChunkFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleChunkArrayFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleChunkJAIFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleChunkMapFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleChunkRAFFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellInt;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellIntChunk64CellMapFactory;
import uk.ac.leeds.ccg.andyt.grids.core.AbstractGrid2DSquareCellIntChunkFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellIntChunkArrayFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellIntChunkJAIFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellIntChunkMapFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellIntChunkRAFFactory;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellIntFactory;
import uk.ac.leeds.ccg.andyt.grids.core.GridStatistics0;
import uk.ac.leeds.ccg.andyt.grids.core.GridStatistics1;
import uk.ac.leeds.ccg.andyt.grids.core.AbstractGridStatistics;
import uk.ac.leeds.ccg.andyt.grids.exchange.ImageExporter;
import uk.ac.leeds.ccg.andyt.grids.exchange.ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.andyt.grids.process.Grid2DSquareCellProcessorDEM;
import uk.ac.leeds.ccg.andyt.grids.utilities.Utilities;
import uk.ac.leeds.ccg.andyt.grids.utilities.FileCreator;

public class Test
        extends Grid2DSquareCellProcessorDEM {

    private long time;
    boolean _HandleOutOfMemoryError;
    String _FileSeparator;
    int _MessageLength;
    int _FilenameLength;
    String _Message0;
    String _Message;
    String _Filename;

    /** Creates a new Test */
    public Test() {
        this(FileCreator.createNewFile());
    }

    /**
     * Creates a new Test using specified _Directory.
     * WARNING: Files in the specified _Directory may get overwritten.
     * @param workspace
     */
    public Test(File workspace) {
        super(workspace);
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
        // _NotLoadedAsGrid = true?
        // Output images?

        // VM parameters: -Xmx31000m -Xms31000m -XX:-UseGCOverheadLimit
        //runOnMaxima();

        // VM parameters: -Xmx1200m
        runOnPC();

    }

//    public void asciitopng() {
//        long nrows = 100;
//        long ncols = 100;
//        File _Directory = new File("C:/Work/src/jianhuij/MedAction/data/SquareTests/NNInputs/");
//        File _File;
//        boolean _HandleOutOfMemoryError = true;
//        Grid2DSquareCellDouble _Grid2DSquareCellDouble;
//        ImageExporter _ImageExporter = new ImageExporter();
//        for ( int i = 1; i < 5; i ++ ) {
//            _File = new File( _Directory, "1kColumn1kRowX" + i + ".asc" );
//            _Grid2DSquareCellDouble = ( Grid2DSquareCellDouble ) this._Grid2DSquareCellDoubleFactory.create( _Directory, _File, 0L, 0L, nrows - 1L, ncols - 1L );
//            _ImageExporter.toGreyScaleImage( _Grid2DSquareCellDouble, new File( _Directory, "1kColumn1kRowX" + i + ".png" ), "PNG", _HandleOutOfMemoryError );
//        }
//        for ( int i = 1; i < 5; i ++ ) {
//            for ( int j = 1; j < 4; j ++ ) {
//                _File = new File( _Directory, "1kColumn1kRowX" + i + "" + j + ".asc" );
//                try {
//                    _Grid2DSquareCellDouble = ( Grid2DSquareCellDouble ) this._Grid2DSquareCellDoubleFactory.create( _Directory, _File, 0L, 0L, nrows - 1L, ncols - 1L );
//                    _ImageExporter.toGreyScaleImage( _Grid2DSquareCellDouble, new File( _Directory, "1kColumn1kRowX" + i + ".png" ), "PNG", _HandleOutOfMemoryError );
//                } catch ( Exception _Exception ) {
//                    _Exception.printStackTrace();
//                }
//            }
//        }
//    }
    public static void runOnMaxima() {
        //File _Directory = new File( "/home/maxima01_a/geoagdt/projects/geomorphometrics/" );
        File _Directory = new File("/nobackup/geoagdt/");
        System.out.print("" + _Directory.toString());
        if (_Directory.exists()) {
            System.out.println(" exists.");
        } else {
            System.out.println(" does not exist.");
        }
        Test aTest = new Test(_Directory);
        aTest.run();
    }

    public static void runOnPC() {


        //File _Directory = new File( "D:/Work/Projects/Geomorphometrics/Workspace/" );
        File _Directory = new File("C:/Work/People/Sadhvi Selvaraj/Roofs/");
        //File _Directory = new File( "C:/temp/GEOG5060/Geomorphometrics/" );
        System.out.print("" + _Directory.toString());
        if (_Directory.exists()) {
            System.out.println(" exists.");
        } else {
            System.out.println(" does not exist.");
        }
        Test aTest = new Test(_Directory);
        //aTest.run();

        //aTest._CreateSyntheticRoofs();
        long nrows = 120;//14;//6L;//320L;

        long ncols = 80;//18;//4L;//640L;

        long _RowWithRidge = nrows / 2;
        //long _RowWithRidge = ( nrows - 1 ) / 2;
        long _ColWithRidge = ncols / 2;
        //long _ColWithRidge = ( ncols - 1 ) / 2;
//        if ( nrows % 2.0d != 0 ) {
//            _RowWithRidge ++;
////            _RowWithRidge += 1;
////            _RowWithRidge = _RowWithRidge + 1;
//        }
        //if ( ncols % 2.0d != 0 ) {
        double _RowRidgeHeight = 10.0d;
        double _ColRidgeHeight = 5.0d;
    //aTest.asciitopng();

    }

    public void run() {
        try {
            String _Filename;
            String _InitialDirectory = get_Directory(_HandleOutOfMemoryError).toString();
            String _Input_Filename;
            File _Input_Directory;

            //_Input_Filename = "geom_rast.asc";
            _Input_Filename = "rastert_mskd_nd1.asc";
            _Input_Directory = new File(_InitialDirectory + "/data/");

//            //_Input_Filename = "megt88n270hb.asc";
//            //_Input_Filename = "megt88n180hb.asc";
//            _Input_Filename = "megt44n180hb.asc";
//            _Input_Directory = new File( _InitialDirectory + "/data/Mars/" );

//            _Input_Filename = "cnpdem10m.asc";
//            _Input_Directory = new File( _InitialDirectory + "/data/Digimap/10metreCairngorm/" );

            //_Input_Filename = "cnp.asc";
//            _Input_Filename = "atdemcnp.asc";
//            _Input_Directory = new File( _InitialDirectory + "/data/NextMap/" );


//             _Input_Filename = "crete_0.asc";
//             _Input_Directory = new File ( _InitialDirectory + "/data/srtm/crete/" );
//             _Input_Filename = "dem_masked.asc";
//             _Input_Directory = new File ( _InitialDirectory + "/data/srtm/med/" );

//             _Input_Filename = "uk.asc";
//             _Input_Directory = new File( _InitialDirectory + "/data/srtm/uk/" );

//             _Input_Filename = "_HollowFilledDEM_2000.asc";
//             _Input_Directory = new File( _InitialDirectory + "/data/Jose/Output/_HollowFilledDEM/_HollowFilledDEM/_HollowFilledDEM/" );
//             _Input_Filename = "AscBullaque.asc";
//             _Input_Directory = new File( _InitialDirectory + "/data/Jose/" );

//             _Input_Filename = "alice_1.asc";
//             _Input_Directory = new File( _InitialDirectory + "/data/srtm/Greece/alice_1/" );

//             // TEST
//             _Input_Filename = "dhm25.asc";
//             _Input_Directory = new File( _InitialDirectory + "/data/SteveCarver/AustrianAlp/" );

//             _Input_Filename = "sample10m.asc";
//             _Input_Directory = new File( _InitialDirectory + "/data/SteveCarver/sample/sample10/" );
//             _Input_Filename = "sample50m.asc";
//             _Input_Directory = new File( _InitialDirectory + "/data/SteveCarver/sample/sample50/" );


            String _Input_Filename_WithoutExtension = _Input_Filename.substring(0, _Input_Filename.length() - 4);

            File _Output_Directory = new File(_Input_Directory + "/Output/" + _Input_Filename_WithoutExtension + "/");
//            _Filename = new String( "Output" );
//            File _Output_Directory = initFileDirectory(
//                    this._Input_Directory,
//                    _Filename,
//                    _HandleOutOfMemoryError );
//            _Filename = initString( _FilenameLength, _HandleOutOfMemoryError );
//
            File _Workspace_Directory = new File(_Input_Directory + "/Workspace/");
//            _Filename = new String( "Workspace" );
//            File _Workspace_Directory = initFileDirectory(
//                    this._Input_Directory,
//                    _Filename,
//                    _HandleOutOfMemoryError );
//            _Filename = initString( _FilenameLength, _HandleOutOfMemoryError );

            int chunkNrows = 512; //64, 128, 256, 512, 1024, 2048

            int chunkNcols = 512; //64, 128, 256, 512, 1024, 2048

            this._Grid2DSquareCellDoubleFactory.set_ChunkNRows(chunkNrows);
            this._Grid2DSquareCellDoubleFactory.set_ChunkNCols(chunkNcols);
            //this._Grid2DSquareCellDoubleFactory.set_HandleOutOfMemoryError(_HandleOutOfMemoryError);
            //this._Grid2DSquareCellDoubleFactory.set_Grid2DSquareCells(this._AbstractGrid2DSquareCell_HashSet);

            this._Grid2DSquareCellIntFactory.set_ChunkNRows(chunkNrows);
            this._Grid2DSquareCellIntFactory.set_ChunkNCols(chunkNcols);
            //this._Grid2DSquareCellIntFactory.set_HandleOutOfMemoryError(_HandleOutOfMemoryError);
            //this._Grid2DSquareCellIntFactory.set_Grid2DSquareCells(this._AbstractGrid2DSquareCell_HashSet);

            this.set_Directory(_Workspace_Directory);

//            Grid2DSquareCellInt _Grid2DSquareCellInt = null;
            Grid2DSquareCellDouble _Grid2DSquareCellDouble = null;

            // Load input
            //boolean _NotLoadedAsGrid = false;
            boolean _NotLoadedAsGrid = true;
            if (_NotLoadedAsGrid) {
                File _Input_File = _Grids_Environment.initFile(
                        _Input_Directory,
                        _Input_Filename,
                        _HandleOutOfMemoryError);
                _Grid2DSquareCellDouble = (Grid2DSquareCellDouble) this._Grid2DSquareCellDoubleFactory.create(_Input_File);
                // Cache input
                boolean _SwapToFileCache = true;
                _Grid2DSquareCellDouble.writeToFile(
                        _SwapToFileCache,
                        _HandleOutOfMemoryError);
                _Grids_Environment.get_AbstractGrid2DSquareCell_HashSet().add(_Grid2DSquareCellDouble);
//                _OutputImagePNG(
//                        _Grid2DSquareCellDouble,
//                        _Output_Directory,
//                        _HandleOutOfMemoryError );
            } else {
                _Grid2DSquareCellDouble = (Grid2DSquareCellDouble) _Grid2DSquareCellDoubleFactory.create(
                        new File(_Input_Directory.toString() + this._FileSeparator + _Input_Filename_WithoutExtension + "uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleFactory_chunkNrows(" + chunkNrows + ")_chunkNcols(" + chunkNcols + ")"));
                _Grids_Environment.get_AbstractGrid2DSquareCell_HashSet().add(_Grid2DSquareCellDouble);
            }

            // Run
            run1(
                    _Grid2DSquareCellDouble,
                    _Output_Directory,
                    _Workspace_Directory,
                    _HandleOutOfMemoryError);
            _Message = null;
            _Message = "Processing complete in " +
                    Utilities._ReportTime(System.currentTimeMillis() - time);
            _Message = _Grids_Environment.println(_Message, _Message0, _HandleOutOfMemoryError);

        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    public void run1(
            Grid2DSquareCellDouble _Grid2DSquareCellDouble,
            File _Output_Directory,
            File _Workspace_Directory,
            boolean _HandleOutOfMemoryError)
            throws Exception, Error {
        try {
            // Initialise
            log("run1();",
                    _HandleOutOfMemoryError);
            int maxIterations = 2;
            long nrows = _Grid2DSquareCellDouble.get_NRows(_HandleOutOfMemoryError);
            long ncols = _Grid2DSquareCellDouble.get_NCols(_HandleOutOfMemoryError);
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
                    _HandleOutOfMemoryError);

            log("Processing complete in " + Utilities._ReportTime(System.currentTimeMillis() - time),
                    _HandleOutOfMemoryError);
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (_HandleOutOfMemoryError) {
                _Grids_Environment.clear_MemoryReserve();
                _Grids_Environment.swapToFile_Grid2DSquareCellChunks(_HandleOutOfMemoryError);
                _Grids_Environment.init_MemoryReserve(_HandleOutOfMemoryError);
                run1(
                        _Grid2DSquareCellDouble,
                        _Output_Directory,
                        _Workspace_Directory,
                        _HandleOutOfMemoryError);
            } else {
                throw _OutOfMemoryError;
            }
        }
    }

//    
//    
//    public void _OutputImagePNG(
//            AbstractGrid2DSquareCell _Grid2DSquareCell,
//            File _Output_Directory,
//            boolean _HandleOutOfMemoryError )
//            throws IOException {
//        try {
//            System.out.println("output " + _Grid2DSquareCell.toString( _HandleOutOfMemoryErrorFalse ) );
//            ImageExporter _ImageExporter = new ImageExporter();
//            //int _StringLength = 1000;
//            String _DotPNG = ".png";
//            String _PNG = "PNG";
//            BigDecimal _BigDecimal_Minus9999Point0 = new BigDecimal( "-9999.0" );
//            String _String;
//            File _File;
//            _String = initString(
//                    _Grid2DSquareCell.get_Name( _HandleOutOfMemoryError ),
//                    _DotPNG,
//                    _HandleOutOfMemoryError );
//            _File = initFile(
//                    _Output_Directory,
//                    _String,
//                    _HandleOutOfMemoryError );
//            _ImageExporter.toGreyScaleImage(
//                    _Grid2DSquareCell,
//                    _File,
//                    _PNG,
//                    _HandleOutOfMemoryErrorFalse );
//        } catch ( OutOfMemoryError _OutOfMemoryError ) {
//            if ( _HandleOutOfMemoryError ) {
//                clear_MemoryReserve();
//                swapToFile_Grid2DSquareCellChunks();
//                init_MemoryReserve( _HandleOutOfMemoryError );
//                output(
//                        _Grid2DSquareCell,
//                        _Output_Directory,
//                        _HandleOutOfMemoryError );
//            } else {
//                throw _OutOfMemoryError;
//            }
//        }
//    }
//    
//    public void _OutputESRIAsciiGrid(
//            AbstractGrid2DSquareCell _Grid2DSquareCell,
//            File _Output_Directory,
//            boolean _HandleOutOfMemoryError )
//            throws IOException {
//        try {
//            System.out.println("output " + _Grid2DSquareCell.toString( _HandleOutOfMemoryErrorFalse ) );
//            ESRIAsciiGridExporter _ESRIAsciiGridExporter = new ESRIAsciiGridExporter();
//            //int _StringLength = 1000;
//            String _DotASC = ".asc";
//            BigDecimal _BigDecimal_Minus9999Point0 = new BigDecimal( "-9999.0" );
//            String _String;
//            File _File;
//            _String = initString(
//                    _Grid2DSquareCell.get_Name( _HandleOutOfMemoryError ),
//                    _DotASC,
//                    _HandleOutOfMemoryError );
//            _File = initFile(
//                    _Output_Directory,
//                    _String,
//                    _HandleOutOfMemoryError );
//            _ESRIAsciiGridExporter.toAsciiFile(
//                    _Grid2DSquareCell,
//                    _File,
//                    _BigDecimal_Minus9999Point0,
//                    _HandleOutOfMemoryError );
//        } catch ( OutOfMemoryError _OutOfMemoryError ) {
//            if ( _HandleOutOfMemoryError ) {
//                clear_MemoryReserve();
//                swapToFile_Grid2DSquareCellChunks();
//                init_MemoryReserve( _HandleOutOfMemoryError );
//                _OutputESRIAsciiGrid(
//                        _Grid2DSquareCell,
//                        _Output_Directory,
//                        _HandleOutOfMemoryError );
//            } else {
//                throw _OutOfMemoryError;
//            }
//        }
//    }
    public void do_Metrics1(
            AbstractGrid2DSquareCell _Grid2DSquareCell,
            File _Output_Directory0,
            File _Workspace_Directory0,
            boolean _HandleOutOfMemoryError)
            throws Exception, Error {
        try {
            // Initialistaion
            _Filename = "_Metrics1";
            File _Output_Directory = _Grids_Environment.initFileDirectory(
                    _Output_Directory0,
                    _Filename,
                    _HandleOutOfMemoryError);
            File _Workspace_Directory = _Grids_Environment.initFileDirectory(
                    _Workspace_Directory0,
                    _Filename,
                    _HandleOutOfMemoryError);
            _Filename = _Grids_Environment.initString(_FilenameLength, _HandleOutOfMemoryError);
            this.set_Directory(_Workspace_Directory);
            boolean swapOutInitialisedFiles = false;
            boolean swapOutProcessedChunks = false;
            ImageExporter _ImageExporter = new ImageExporter();
            ESRIAsciiGridExporter _ESRIAsciiGridExporter = new ESRIAsciiGridExporter();
            int _NameLength = 1000;
            String _Name = _Grids_Environment.initString(_NameLength, false);
            BigDecimal[] dimensions = _Grid2DSquareCell.get_Dimensions(_HandleOutOfMemoryError);
            double cellsize = Double.valueOf(dimensions[ 0].toString()).doubleValue();
            double weightIntersect = 1.0d;
            double weightFactor = 1.0d;
            AbstractGrid2DSquareCell dummyGrid = null;
            long nrows = _Grid2DSquareCell.get_NRows(_HandleOutOfMemoryError);
            long ncols = _Grid2DSquareCell.get_NCols(_HandleOutOfMemoryError);
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
            for (distances = 1; distances <= 2; distances *= 2) {
                distance = cellsize * (double) distances;
                AbstractGrid2DSquareCell[] metrics1 = getMetrics1(
                        _Grid2DSquareCell,
                        distance,
                        weightIntersect,
                        weightFactor,
                        _Grid2DSquareCellDoubleFactory,
                        _Grid2DSquareCellIntFactory,
                        swapOutInitialisedFiles,
                        swapOutProcessedChunks,
                        _HandleOutOfMemoryError);

                for (i = _int_0; i < metrics1.length; i++) {
                    //if ( i == _int_2 ){
                    maskEdges(
                            metrics1[i],
                            distances,
                            _HandleOutOfMemoryError);
//                    _Name = metrics1[ i ].get_Name( _HandleOutOfMemoryError ) + "_" + distances;
//                    metrics1[ i ].set_Name( _Name, _HandleOutOfMemoryError );
//                    _Name = initString( _NameLength, _HandleOutOfMemoryError );
                    output(
                            metrics1[i],
                            _Grids_Environment.get_Grid2DSquareCellProcessor(),
                            _Output_Directory,
                            _ImageExporter,
                            null,
                            _ESRIAsciiGridExporter,
                            _HandleOutOfMemoryError);
//                    _OutputESRIAsciiGrid(
//                            metrics1[ i ],
//                            _Output_Directory,
//                            _HandleOutOfMemoryError );
                //}
                }
            }
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (_HandleOutOfMemoryError) {
                _Grids_Environment.clear_MemoryReserve();
                _Grids_Environment.swapToFile_Grid2DSquareCellChunks(_HandleOutOfMemoryError);
                _Grids_Environment.init_MemoryReserve(_HandleOutOfMemoryError);
                do_Metrics1(
                        _Grid2DSquareCell,
                        _Output_Directory0,
                        _Workspace_Directory0,
                        _HandleOutOfMemoryError);
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

    public void do_SlopeAndAspect(
            AbstractGrid2DSquareCell _Grid2DSquareCell,
            File _Output_Directory0,
            File _Workspace_Directory0,
            boolean _HandleOutOfMemoryError)
            throws Exception, Error {
        try {
            // Initialistaion
            _Filename = "_SlopeAndAspect";
            File _Output_Directory = _Grids_Environment.initFileDirectory(
                    _Output_Directory0,
                    _Filename,
                    _HandleOutOfMemoryError);
            File _Workspace_Directory = _Grids_Environment.initFileDirectory(
                    _Workspace_Directory0,
                    _Filename,
                    _HandleOutOfMemoryError);
            _Filename = _Grids_Environment.initString(_FilenameLength, _HandleOutOfMemoryError);
            this.set_Directory(_Workspace_Directory);
            ImageExporter _ImageExporter = new ImageExporter();
            ESRIAsciiGridExporter _ESRIAsciiGridExporter = new ESRIAsciiGridExporter();
            BigDecimal[] dimensions = _Grid2DSquareCell.get_Dimensions(_HandleOutOfMemoryError);
            double cellsize = Double.valueOf(dimensions[ 0].toString()).doubleValue();
            double weightIntersect = 1.0d;
            double weightFactor = 1.0d;
            AbstractGrid2DSquareCell dummyGrid = null;
            double distance = 0.0d;
            int distances = 2;
            int i = 0;
            for (distances = 2; distances <= 16; distances *= 2) {
                distance = cellsize * (double) distances;
                AbstractGrid2DSquareCell[] _SlopeAndAspect = getSlopeAspect(
                        _Grid2DSquareCell,
                        distance,
                        weightIntersect,
                        weightFactor,
                        _HandleOutOfMemoryError);
                for (i = 0; i < _SlopeAndAspect.length; i++) {
                    maskEdges(
                            _SlopeAndAspect[i],
                            distances,
                            _HandleOutOfMemoryError);
                    output(
                            _SlopeAndAspect[i],
                            _Grids_Environment.get_Grid2DSquareCellProcessor(),
                            _Output_Directory,
                            _ImageExporter,
                            null,
                            _ESRIAsciiGridExporter,
                            _HandleOutOfMemoryError);
                    _Grids_Environment.get_AbstractGrid2DSquareCell_HashSet().remove(_SlopeAndAspect[i]);
                }
            }
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (_HandleOutOfMemoryError) {
                _Grids_Environment.clear_MemoryReserve();
                _Grids_Environment.swapToFile_Grid2DSquareCellChunks(_HandleOutOfMemoryError);
                _Grids_Environment.init_MemoryReserve(_HandleOutOfMemoryError);
                do_SlopeAndAspect(
                        _Grid2DSquareCell,
                        _Output_Directory0,
                        _Workspace_Directory0,
                        _HandleOutOfMemoryError);
            } else {
                throw _OutOfMemoryError;
            }
        }
    }

    public void do_Difference(
            Grid2DSquareCellDouble _Grid,
            Grid2DSquareCellDouble _GridToSubtract,
            File _Output_Directory0,
            File _Workspace_Directory0,
            boolean _HandleOutOfMemoryError)
            throws Exception, Error {
        try {
            String _Filename = "_Difference";
            double _double_Minus1Point0 = -1.0d;
            File _Output_Directory = _Grids_Environment.initFileDirectory(
                    _Output_Directory0,
                    _Filename,
                    _HandleOutOfMemoryError);
//            File _Workspace_Directory = initFileDirectory(
//                    _Workspace_Directory0,
//                    _Filename,
//                    _HandleOutOfMemoryError );
            ImageExporter _ImageExporter = new ImageExporter();
            ESRIAsciiGridExporter _ESRIAsciiGridExporter = new ESRIAsciiGridExporter();

            addToGrid(
                    _Grid,
                    _GridToSubtract,
                    _double_Minus1Point0,
                    _HandleOutOfMemoryError);
            _Grid.set_Name(
                    _Filename,
                    _HandleOutOfMemoryError);
            output(
                    _Grid,
                    _Grids_Environment.get_Grid2DSquareCellProcessor(),
                    _Output_Directory,
                    _ImageExporter,
                    null,
                    _ESRIAsciiGridExporter,
                    _HandleOutOfMemoryError);
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (_HandleOutOfMemoryError) {
                _Grids_Environment.clear_MemoryReserve();
                _Grids_Environment.swapToFile_Grid2DSquareCellChunks(_HandleOutOfMemoryError);
                _Grids_Environment.init_MemoryReserve(_HandleOutOfMemoryError);
                do_Difference(
                        _Grid,
                        _GridToSubtract,
                        _Output_Directory0,
                        _Workspace_Directory0,
                        _HandleOutOfMemoryError);
            } else {
                throw _OutOfMemoryError;
            }
        }
    }

    public void do_HollowFilledDEM(
            Grid2DSquareCellDouble _Grid2DSquareCellDouble,
            int maxIterations,
            File _Output_Directory0,
            File _Workspace_Directory0,
            boolean _HandleOutOfMemoryError)
            throws Exception, Error {
        try {
            _Filename = "_HollowFilledDEM";
            File _Output_Directory = _Grids_Environment.initFileDirectory(
                    _Output_Directory0,
                    _Filename,
                    _HandleOutOfMemoryError);
            File _Workspace_Directory = _Grids_Environment.initFileDirectory(
                    _Workspace_Directory0,
                    _Filename,
                    _HandleOutOfMemoryError);
            _Filename = _Grids_Environment.initString(_FilenameLength, _HandleOutOfMemoryError);
            this.set_Directory(_Workspace_Directory);
            ImageExporter _ImageExporter = new ImageExporter();
            ESRIAsciiGridExporter _ESRIAsciiGridExporter = new ESRIAsciiGridExporter();
            double outflowHeight = 0;
            double _double_Minus1Point0 = -1.0d;
            //int maxIterations = 1;

            boolean _TreatNoDataValueAsOutflow = true;
            HashSet outflowCellIDsSet = null;
            Grid2DSquareCellDouble result = getHollowFilledDEM(
                    _Grid2DSquareCellDouble,
                    _Grid2DSquareCellDoubleFactory,
                    outflowHeight,
                    maxIterations,
                    outflowCellIDsSet,
                    _TreatNoDataValueAsOutflow,
                    _HandleOutOfMemoryError);
            output(
                    result,
                    _Grids_Environment.get_Grid2DSquareCellProcessor(),
                    _Output_Directory,
                    _ImageExporter,
                    null,
                    _ESRIAsciiGridExporter,
                    _HandleOutOfMemoryError);
            do_Difference(
                    _Grid2DSquareCellDouble,
                    result,
                    _Output_Directory,
                    _Workspace_Directory,
                    _HandleOutOfMemoryError);
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (_HandleOutOfMemoryError) {
                _Grids_Environment.clear_MemoryReserve();
                _Grids_Environment.swapToFile_Grid2DSquareCellChunks(_HandleOutOfMemoryError);
                _Grids_Environment.init_MemoryReserve(_HandleOutOfMemoryError);
                do_HollowFilledDEM(
                        _Grid2DSquareCellDouble,
                        maxIterations,
                        _Output_Directory0,
                        _Workspace_Directory0,
                        _HandleOutOfMemoryError);
            } else {
                throw _OutOfMemoryError;
            }
        }
    }

    /**
     *
     * @param _Grid2DSquareCell
     * @param _HandleOutOfMemoryError
     * @param distances
     */
    public void maskEdges(
            AbstractGrid2DSquareCell _Grid2DSquareCell,
            int distances,
            boolean _HandleOutOfMemoryError) {
        try {
            System.out.println("Masking Edges");
            long nrows = _Grid2DSquareCell.get_NRows(false);
            long ncols = _Grid2DSquareCell.get_NCols(false);
            long _StartRowIndexLong = 0L;
            long _StartColIndexLong = 0L;
            long _EndRowIndexLong = 0L;
            long _EndColIndexLong = 0L;
            long _long_0 = 0L;
            long _long_1 = 1L;
            // mask left
            _StartRowIndexLong = _long_0;
            _StartColIndexLong = _long_0;
            _EndRowIndexLong = nrows - _long_1;
            _EndColIndexLong = distances - _long_1;
            mask(_Grid2DSquareCell,
                    _StartRowIndexLong,
                    _StartColIndexLong,
                    _EndRowIndexLong,
                    _EndColIndexLong,
                    _HandleOutOfMemoryError);
            // mask right
            _StartRowIndexLong = _long_0;
            _StartColIndexLong = ncols - distances;
            _EndRowIndexLong = nrows - _long_1;
            _EndColIndexLong = ncols - _long_1;
            mask(_Grid2DSquareCell,
                    _StartRowIndexLong,
                    _StartColIndexLong,
                    _EndRowIndexLong,
                    _EndColIndexLong,
                    _HandleOutOfMemoryError);
            // mask top
            _StartRowIndexLong = _long_0;
            _StartColIndexLong = _long_0;
            _EndRowIndexLong = distances - _long_1;
            _EndColIndexLong = ncols - _long_1;
            mask(_Grid2DSquareCell,
                    _StartRowIndexLong,
                    _StartColIndexLong,
                    _EndRowIndexLong,
                    _EndColIndexLong,
                    _HandleOutOfMemoryError);
            // mask bottom
            _StartRowIndexLong = nrows - distances;
            _StartColIndexLong = _long_0;
            _EndRowIndexLong = nrows - _long_1;
            _EndColIndexLong = ncols - _long_1;
            mask(_Grid2DSquareCell,
                    _StartRowIndexLong,
                    _StartColIndexLong,
                    _EndRowIndexLong,
                    _EndColIndexLong,
                    _HandleOutOfMemoryError);
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (_HandleOutOfMemoryError) {
                _Grids_Environment.clear_MemoryReserve();
//                swapToFile_Grid2DSquareCellChunk_AccountDetail();
                _Grids_Environment.swapToFile_Grid2DSquareCellChunksExcept_Account(
                        _Grid2DSquareCell, _HandleOutOfMemoryError);
                _Grids_Environment.init_MemoryReserve(_Grid2DSquareCell, _HandleOutOfMemoryError);
                maskEdges(
                        _Grid2DSquareCell,
                        distances,
                        _HandleOutOfMemoryError);
            } else {
                throw _OutOfMemoryError;
            }
        }
    }

    public long getTime() {
        return time;
    }
}
