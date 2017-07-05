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
import uk.ac.leeds.ccg.andyt.grids.core.Grids_AbstractGrid2DSquareCell;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDouble;
import uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellInt;
import uk.ac.leeds.ccg.andyt.grids.exchange.ImageExporter;
import uk.ac.leeds.ccg.andyt.grids.exchange.ESRIAsciiGridExporter;
import uk.ac.leeds.ccg.andyt.grids.process.Grid2DSquareCellProcessorDEM;
import uk.ac.leeds.ccg.andyt.grids.utilities.Utilities;
import uk.ac.leeds.ccg.andyt.grids.utilities.FileCreator;

public class SplitIntoDistinctGrids
        extends Grid2DSquareCellProcessorDEM {

    private long time;
    boolean _HandleOutOfMemoryError;
    String _FileSeparator;
    int _MessageLength;
    int _FilenameLength;
    String _Message0;
    String _Message;
    String _Filename;

    public SplitIntoDistinctGrids() {
        this(FileCreator.createNewFile());
    }

    public SplitIntoDistinctGrids(File workspace) {
        super(workspace);
        this.time = System.currentTimeMillis();
        this._HandleOutOfMemoryError = true;
        this._FileSeparator = System.getProperty("file.separator");
        this._MessageLength = 1000;
        this._FilenameLength = 1000;
        this._Message0 = env.initString(_MessageLength, _HandleOutOfMemoryError);
        this._Message = env.initString(_MessageLength, _HandleOutOfMemoryError);
        this._Filename = env.initString(_FilenameLength, _HandleOutOfMemoryError);
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

    public static void runOnMaxima() {
        //File _Directory = new File( "/home/maxima01_a/geoagdt/projects/geomorphometrics/" );
        File _Directory = new File("/nobackup/geoagdt/");
        System.out.print("" + _Directory.toString());
        if (_Directory.exists()) {
            System.out.println(" exists.");
        } else {
            System.out.println(" does not exist.");
        }
        new SplitIntoDistinctGrids(_Directory).run();
    }

    public static void runOnPC() {
        File _Directory = new File("D:/Work/Projects/Geomorphometrics/Workspace/");
        System.out.print("" + _Directory.toString());
        if (_Directory.exists()) {
            System.out.println(" exists.");
        } else {
            System.out.println(" does not exist.");
        }
        new SplitIntoDistinctGrids(_Directory).run();
    }

    public void run() {
        try {
            String _Filename;
            String _InitialDirectory = get_Directory(_HandleOutOfMemoryError).toString();
            String _Input_Filename;
            File _Input_Directory;

            _Input_Filename = "roughness_10.0.asc";
            _Input_Directory = new File(_InitialDirectory + "/data/NextMap/Output/atdemcnp/_Metrics1/");

//            _Input_Filename = "atdemcnp.asc";
//            _Input_Directory = new File( _InitialDirectory + "/data/NextMap/" );

//            _Input_Filename = "roughness_20.0.asc";
//            _Input_Directory = new File( _InitialDirectory + "/data/NextMap/Output/cnp/_Metrics1/" );

            String _Input_Filename_WithoutExtension = _Input_Filename.substring(0, _Input_Filename.length() - 4);
            File _Output_Directory = new File(_Input_Directory + "/Output/" + _Input_Filename_WithoutExtension + "/");
            File _Workspace_Directory = new File(_Input_Directory + "/Workspace/");

            int chunkNrows = 512; //64, 128, 256, 512, 1024, 2048
            int chunkNcols = 512; //64, 128, 256, 512, 1024, 2048

            this._Grid2DSquareCellDoubleFactory.set_ChunkNRows(chunkNrows);
            this._Grid2DSquareCellDoubleFactory.set_ChunkNCols(chunkNcols);
            this._Grid2DSquareCellDoubleFactory.setHandleOutOfMemoryError(_HandleOutOfMemoryError);
            //this._Grid2DSquareCellDoubleFactory.set_Grid2DSquareCells(env.get_AbstractGrid2DSquareCell_HashSet());

            this._Grid2DSquareCellIntFactory.set_ChunkNRows(chunkNrows);
            this._Grid2DSquareCellIntFactory.set_ChunkNCols(chunkNcols);
            this._Grid2DSquareCellIntFactory.setHandleOutOfMemoryError(_HandleOutOfMemoryError);
            //this._Grid2DSquareCellIntFactory.set_Grid2DSquareCells(this._AbstractGrid2DSquareCell_HashSet);

            this.set_Directory(_Workspace_Directory);

//            Grid2DSquareCellInt _Grid2DSquareCellInt = null;
            Grid2DSquareCellDouble _Grid2DSquareCellDouble = null;

            // Load input
            //boolean _NotLoadedAsGrid = false;
            boolean _NotLoadedAsGrid = true;
            if (_NotLoadedAsGrid) {
                File _Input_File = env.initFile(
                        _Input_Directory,
                        _Input_Filename,
                        _HandleOutOfMemoryError);
                _Grid2DSquareCellDouble = (Grid2DSquareCellDouble) this._Grid2DSquareCellDoubleFactory.create(_Input_File);
                // Cache input
                boolean _SwapToFileCache = true;
                _Grid2DSquareCellDouble.writeToFile(
                        _SwapToFileCache,
                        _HandleOutOfMemoryError);
                env.get_AbstractGrid2DSquareCell_HashSet().add(_Grid2DSquareCellDouble);
//                _OutputImagePNG(
//                        _Grid2DSquareCellDouble,
//                        _Output_Directory,
//                        _HandleOutOfMemoryError );
            } else {
                _Grid2DSquareCellDouble = (Grid2DSquareCellDouble) _Grid2DSquareCellDoubleFactory.create(
                        new File(_Input_Directory.toString() + this._FileSeparator + _Input_Filename_WithoutExtension + "uk.ac.leeds.ccg.andyt.grids.core.Grid2DSquareCellDoubleFactory_chunkNrows(" + chunkNrows + ")_chunkNcols(" + chunkNcols + ")"));
                 env.get_AbstractGrid2DSquareCell_HashSet().add(_Grid2DSquareCellDouble);
            }

            // Run
            run1(
                    _Grid2DSquareCellDouble,
                    _Output_Directory,
                    _Workspace_Directory,
                    _HandleOutOfMemoryError);
            _Message = null;
            _Message = "Processing complete in "
                    + Utilities._ReportTime(System.currentTimeMillis() - time);
            _Message = env.println(_Message, _Message0, _HandleOutOfMemoryError);

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

            int _NumberOfRowsOfGrids = 2;
            int _NumberOfColsOfGrids = 1;
            int i = 0;

            // _SplitIntoDistinctGrids
            Grid2DSquareCellDouble[] Grid2DSquareCellDoubles = _SplitIntoDistinctGrids(
                    _NumberOfRowsOfGrids,
                    _NumberOfColsOfGrids,
                    _Grid2DSquareCellDouble,
                    _Output_Directory,
                    _Workspace_Directory,
                    _HandleOutOfMemoryError);

            // Write out results
            for (i = 0; i < Grid2DSquareCellDoubles.length; i++) {
                _OutputESRIAsciiGrid(Grid2DSquareCellDoubles[i], _Output_Directory, _HandleOutOfMemoryError);
            }

            log("Processing complete in " + Utilities._ReportTime(System.currentTimeMillis() - time),
                    _HandleOutOfMemoryError);
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (_HandleOutOfMemoryError) {
                 env.clear_MemoryReserve();
                env.swapToFile_Grid2DSquareCellChunks(_HandleOutOfMemoryError);
                env.init_MemoryReserve(_HandleOutOfMemoryError);
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

    public void _Output(
            Grids_AbstractGrid2DSquareCell _Grid2DSquareCell,
            File _Output_Directory,
            boolean _HandleOutOfMemoryError)
            throws IOException {
        try {
            System.out.println("_Output " + _Grid2DSquareCell.toString(false));
            ImageExporter _ImageExporter = new ImageExporter(env);
            ESRIAsciiGridExporter _ESRIAsciiGridExporter = new ESRIAsciiGridExporter(env);
            //int _StringLength = 1000;
            String _DotASC = ".asc";
            String _DotPNG = ".png";
            String _PNG = "PNG";
            BigDecimal _BigDecimal_Minus9999Point0 = new BigDecimal("-9999.0");
            String _String;
            File _File;
            _String = env.initString(
                    _Grid2DSquareCell.get_Name(_HandleOutOfMemoryError),
                    _DotPNG,
                    _HandleOutOfMemoryError);
            _File = env.initFile(
                    _Output_Directory,
                    _String,
                    _HandleOutOfMemoryError);
            _ImageExporter.toGreyScaleImage(
                    _Grid2DSquareCell,
                    env.get_Grid2DSquareCellProcessor(),
                    _File,
                    _PNG,
                    true);
            _String = env.initString(
                    _Grid2DSquareCell.get_Name(_HandleOutOfMemoryError),
                    _DotASC,
                    _HandleOutOfMemoryError);
            _File = env.initFile(
                    _Output_Directory,
                    _String,
                    _HandleOutOfMemoryError);
            _ESRIAsciiGridExporter.toAsciiFile(
                    _Grid2DSquareCell,
                    _File,
                    _BigDecimal_Minus9999Point0,
                    _HandleOutOfMemoryError);
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (_HandleOutOfMemoryError) {
                env.clear_MemoryReserve();
                env.swapToFile_Grid2DSquareCellChunks(_HandleOutOfMemoryError);
                env.init_MemoryReserve(_HandleOutOfMemoryError);
                _Output(
                        _Grid2DSquareCell,
                        _Output_Directory,
                        _HandleOutOfMemoryError);
            } else {
                throw _OutOfMemoryError;
            }
        }
    }

    public void _OutputImagePNG(
            Grids_AbstractGrid2DSquareCell _Grid2DSquareCell,
            File _Output_Directory,
            boolean _HandleOutOfMemoryError)
            throws IOException {
        try {
            System.out.println("_Output " + _Grid2DSquareCell.toString(false));
            ImageExporter _ImageExporter = new ImageExporter(env);
            //int _StringLength = 1000;
            String _DotPNG = ".png";
            String _PNG = "PNG";
            BigDecimal _BigDecimal_Minus9999Point0 = new BigDecimal("-9999.0");
            String _String;
            File _File;
            _String = env.initString(
                    _Grid2DSquareCell.get_Name(_HandleOutOfMemoryError),
                    _DotPNG,
                    _HandleOutOfMemoryError);
            _File = env.initFile(
                    _Output_Directory,
                    _String,
                    _HandleOutOfMemoryError);
            _ImageExporter.toGreyScaleImage(
                    _Grid2DSquareCell,
                    env.get_Grid2DSquareCellProcessor(),
                    _File,
                    _PNG,
                    false);
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (_HandleOutOfMemoryError) {
                env.clear_MemoryReserve();
                env.swapToFile_Grid2DSquareCellChunks(_HandleOutOfMemoryError);
                env.init_MemoryReserve(_HandleOutOfMemoryError);
                _Output(
                        _Grid2DSquareCell,
                        _Output_Directory,
                        _HandleOutOfMemoryError);
            } else {
                throw _OutOfMemoryError;
            }
        }
    }

    public void _OutputESRIAsciiGrid(
            Grids_AbstractGrid2DSquareCell _Grid2DSquareCell,
            File _Output_Directory,
            boolean _HandleOutOfMemoryError)
            throws IOException {
        try {
            System.out.println("_Output " + _Grid2DSquareCell.toString(false));
            ESRIAsciiGridExporter _ESRIAsciiGridExporter = new ESRIAsciiGridExporter(env);
            //int _StringLength = 1000;
            String _DotASC = ".asc";
            BigDecimal _BigDecimal_Minus9999Point0 = new BigDecimal("-9999.0");
            String _String;
            File _File;
            _String = env.initString(
                    _Grid2DSquareCell.get_Name(_HandleOutOfMemoryError),
                    _DotASC,
                    _HandleOutOfMemoryError);
            _File = env.initFile(
                    _Output_Directory,
                    _String,
                    _HandleOutOfMemoryError);
            _ESRIAsciiGridExporter.toAsciiFile(
                    _Grid2DSquareCell,
                    _File,
                    _BigDecimal_Minus9999Point0,
                    _HandleOutOfMemoryError);
        } catch (OutOfMemoryError _OutOfMemoryError) {
            if (_HandleOutOfMemoryError) {
                env.clear_MemoryReserve();
                env.swapToFile_Grid2DSquareCellChunks(_HandleOutOfMemoryError);
                env.init_MemoryReserve(_HandleOutOfMemoryError);
                _OutputESRIAsciiGrid(
                        _Grid2DSquareCell,
                        _Output_Directory,
                        _HandleOutOfMemoryError);
            } else {
                throw _OutOfMemoryError;
            }
        }
    }

    public Grid2DSquareCellDouble[] _SplitIntoDistinctGrids(
            int _NumberOfRowsOfGrids,
            int _NumberOfColsOfGrids,
            Grids_AbstractGrid2DSquareCell _Grid2DSquareCell,
            File _Output_Directory0,
            File _Workspace_Directory0,
            boolean _HandleOutOfMemoryError)
            throws Exception, Error {
        // Initialistaion
        _Filename = "_SplitIntoDistinctGrids";
        Grid2DSquareCellDouble[] result = new Grid2DSquareCellDouble[_NumberOfRowsOfGrids * _NumberOfColsOfGrids];
        File _Output_Directory = env.initFileDirectory(
                _Output_Directory0,
                _Filename,
                _HandleOutOfMemoryError);
        File _Workspace_Directory = env.initFileDirectory(
                _Workspace_Directory0,
                _Filename,
                _HandleOutOfMemoryError);
        _Filename = env.initString(_FilenameLength, _HandleOutOfMemoryError);
        this.set_Directory(_Workspace_Directory);
        int _NameLength = 1000;
        String _Name = env.initString(_NameLength, false);
        BigDecimal[] dimensions = _Grid2DSquareCell.get_Dimensions(_HandleOutOfMemoryError);
        double cellsize = Double.valueOf(dimensions[ 0].toString()).doubleValue();
        long nrows = _Grid2DSquareCell.get_NRows(_HandleOutOfMemoryError);
        long ncols = _Grid2DSquareCell.get_NCols(_HandleOutOfMemoryError);
        long _StartRowIndexLong = 0L;
        long _StartColIndexLong = 0L;
        long _EndRowIndexLong = -1L;
        long _EndColIndexLong = -1L;
        long _long_0 = 0L;
        long _long_1 = 1L;
        if (_Grid2DSquareCell instanceof Grid2DSquareCellDouble) {
            _Grid2DSquareCellDoubleFactory.set_NoDataValue(((Grid2DSquareCellDouble) _Grid2DSquareCell).get_NoDataValue(_HandleOutOfMemoryError));
        } else {
            //_Grid2DSquareCell instanceof Grid2DSquareCellInt
            _Grid2DSquareCellDoubleFactory.set_NoDataValue(((Grid2DSquareCellInt) _Grid2DSquareCell).getNoDataValue(_HandleOutOfMemoryError));
        }
        long _RowIncrement = nrows / _NumberOfRowsOfGrids;
        long _ColIncrement = ncols / _NumberOfColsOfGrids;
        _EndRowIndexLong += _RowIncrement;
        _EndColIndexLong += _ColIncrement;
        int i = 0;
        int j = 0;
        int k = 0;
        for (i = 0; i < _NumberOfRowsOfGrids; i++) {
            for (j = 0; j < _NumberOfColsOfGrids; j++) {
                System.out.println("_StartRowIndexLong " + _StartRowIndexLong);
                System.out.println("_StartColIndexLong " + _StartColIndexLong);
                System.out.println("_EndRowIndexLong " + _EndRowIndexLong);
                System.out.println("_EndColIndexLong " + _EndColIndexLong);
                result[ k] = (Grid2DSquareCellDouble) _Grid2DSquareCellDoubleFactory.create(
                        _Grid2DSquareCell, _StartRowIndexLong, _StartColIndexLong,
                        _EndRowIndexLong, _EndColIndexLong);
                k++;
                _StartColIndexLong += _ColIncrement;
                _EndColIndexLong += _ColIncrement;
            }
            _StartColIndexLong = 0;
            _EndColIndexLong = _ColIncrement - 1;
            _StartRowIndexLong += _RowIncrement;
            _EndRowIndexLong += _RowIncrement;
        }
        return result;
    }

    public long getTime() {
        return time;
    }
}
