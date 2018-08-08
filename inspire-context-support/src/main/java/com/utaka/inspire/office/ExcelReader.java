/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.office;

import com.utaka.inspire.util.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * {@code EXCEL}读取工具
 *
 * @author XINEN
 */
public final class ExcelReader implements Iterable<ExcelReader.SheetReader> {
    private final Workbook book;


    public static ExcelReader newReader(String fileName) {
        return new ExcelReader(checkNotNull(fileName));
    }

    public static ExcelReader newReader(InputStream input, String extension) {
        return new ExcelReader(checkNotNull(input), "xlsx".equalsIgnoreCase(extension));
    }


    private ExcelReader(String fileName) {
        try {
            FileInputStream inputStream = new FileInputStream(fileName);
            if (fileName.toLowerCase().endsWith("xls")) {
                book = new HSSFWorkbook(inputStream);
            } else {
                book = new XSSFWorkbook(inputStream);
            }

        } catch (IOException ie) {
            throw new RuntimeException(ie);
        }
    }

    private ExcelReader(InputStream input, boolean xlsx) {
        try {
            if (xlsx)
                book = new XSSFWorkbook(input);
            else
                book = new HSSFWorkbook(input);

        } catch (IOException ie) {
            throw new RuntimeException(ie);
        }
    }

    private ExcelReader(Workbook book) {
        this.book = checkNotNull(book);
    }


    /**
     * 根据索引获取Sheet页.
     *
     * @param index Sheet页索引，从0开始.
     * @return 返回一个{@link SheetReader}对象.
     */
    public SheetReader sheet(int index) {
        return new SheetReader(checkNotNull(this.book.getSheetAt(index)));
    }


    /**
     * 根据名称获取Sheet页.
     *
     * @param name Sheet页名称.
     * @return 返回一个{@link SheetReader}对象.
     */
    public SheetReader sheet(String name) {
        return new SheetReader(checkNotNull(this.book.getSheet(name)));
    }

    @Override
    public Iterator<SheetReader> iterator() {
        return new WorkbookReader(this.book);
    }

    private static class WorkbookReader implements Iterator<SheetReader> {
        private final Workbook book;
        private int index = 0;

        private WorkbookReader(Workbook book) {
            this.book = book;
        }


        @Override
        public boolean hasNext() {
            return index <= book.getNumberOfSheets();
        }

        @Override
        public SheetReader next() {
            return new SheetReader(this.book.getSheetAt(index++));
        }

        @Override
        public void remove() {

        }
    }

    /**
     * {@code sheet} 读取工具，支持{@code for} 循环读取行.
     */
    public static class SheetReader implements Iterable<RowReader> {
        private final Sheet sheet;
        private int index = 0;

        /**
         * 构造一个{@code sheet}读取器。
         */
        public SheetReader(Sheet sheet) {
            this.sheet = sheet;
        }

        /**
         * 跳过第一行
         */
        public SheetReader skipNext() {
            index++;
            return this;
        }

        public RowReader row(int rowNum) {
            return new RowReader(this.sheet.getRow(rowNum));
        }

        @Override
        public Iterator<RowReader> iterator() {
            return new Iterator<RowReader>() {
                @Override
                public boolean hasNext() {
                    return index < sheet.getPhysicalNumberOfRows();
                }

                @Override
                public RowReader next() {
                    return new RowReader(SheetReader.this.sheet.getRow(index++));
                }

                @Override
                public void remove() {

                }
            };
        }
    }

    public static class RowReader implements Iterable<String> {
        private final Row row;
        private int index = 0;

        public RowReader(Row row) {
            this.row = row;
        }

        /**
         * 跳过第一行
         */
        public RowReader skipNext() {
            index++;
            return this;
        }

        public String cell(int cellNum) {
            return toString(this.row.getCell(cellNum));
        }

        @Override
        public Iterator<String> iterator() {
            return new Iterator<String>() {
                @Override
                public boolean hasNext() {
                    return index <= RowReader.this.row.getPhysicalNumberOfCells();
                }

                @Override
                public String next() {
                    return RowReader.this.toString(RowReader.this.row.getCell(index++));
                }

                @Override
                public void remove() {

                }

            };
        }

        public String toString(Cell cell) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
                case Cell.CELL_TYPE_BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case Cell.CELL_TYPE_ERROR:
                    return StringUtils.EMPTY;
                default:
                    return cell.getStringCellValue();
            }
        }

    }


}