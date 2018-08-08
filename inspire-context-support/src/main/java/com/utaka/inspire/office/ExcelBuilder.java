/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.office;

import com.google.common.base.Strings;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author XINEN
 */
public final class ExcelBuilder {

    private final HSSFWorkbook book = new HSSFWorkbook();
    private HSSFSheet sheet;
    private HSSFRow row;
    private HSSFFont font;
    private HSSFCellStyle style;
    private int maxColumnIndex = 0;
    private AtomicInteger columnIndex = new AtomicInteger(0);
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();


    public InputStream build() throws IOException {
        try {
            for (int i = 0; i < maxColumnIndex; i++) {
                sheet.autoSizeColumn((short) i);
            }
            book().write(output);
        } finally {
            output.close();

        }
        return new ByteArrayInputStream(output.toByteArray());
    }

    public ExcelBuilder newSheet() {
        sheet = book().createSheet();
        return this;
    }

    public ExcelBuilder newSheet(String name) {
        sheet = book().createSheet(name);
        return this;
    }

    public ExcelBuilder newRow(int rownum) {
        if (columnIndex.get() > maxColumnIndex) {
            maxColumnIndex = columnIndex.get();
        }
        columnIndex = new AtomicInteger(0);
        row = sheet().createRow(rownum);
        return this;
    }

    public ExcelBuilder newEmptyCell() {
        HSSFCell cell = row.createCell(columnIndex.getAndIncrement());
        cell.setCellStyle(style);
        cell.setCellValue("");
        return this;
    }

    public ExcelBuilder newCell(int value) {
        HSSFCell cell = row.createCell(columnIndex.getAndIncrement());
        cell.setCellStyle(style);
        cell.setCellValue(value);
        return this;
    }

    public ExcelBuilder newCell(BigDecimal value) {
        HSSFCell cell = row.createCell(columnIndex.getAndIncrement());
        cell.setCellStyle(style);
        if (value == null)
            cell.setCellValue("");
        else
            cell.setCellValue(value.doubleValue());
        return this;
    }

    public ExcelBuilder newCell(String value) {
        HSSFCell cell = row.createCell(columnIndex.getAndIncrement());
        cell.setCellStyle(style);
        cell.setCellValue(Strings.nullToEmpty(value));
        return this;
    }

    public ExcelBuilder newCell(Date value) {
        HSSFCell cell = row.createCell(columnIndex.getAndIncrement());
        cell.setCellStyle(style);
        if (value == null)
            cell.setCellValue("");
        else
            cell.setCellValue(value);
        return this;
    }

    public ExcelBuilder newCell(Date value, DateFormat format) {
        HSSFCell cell = row.createCell(columnIndex.getAndIncrement());
        cell.setCellStyle(style);
        if (format == null || value == null)
            cell.setCellValue("");
        else
            cell.setCellValue(format.format(value));
        return this;
    }

    public ExcelBuilder newCell(String value, HSSFCellStyle style) {
        HSSFCell cell = row.createCell(columnIndex.getAndIncrement());
        cell.setCellStyle(style);
        cell.setCellValue(Strings.nullToEmpty(value));
        return this;
    }

    public ExcelBuilder newRangeCell(CellRangeAddress region) {
        int index = sheet().addMergedRegion(region);
        columnIndex.set(region.getLastColumn());
        return this;
    }


    public ExcelBuilder newRangeCell(int offset) {
        int rownum = this.row.getRowNum();
        sheet().addMergedRegion(new CellRangeAddress(rownum, rownum, columnIndex.get(), columnIndex.addAndGet(offset)));
        return this;
    }

    public ExcelBuilder newRangeCell(String value, int offset) {
        int rownum = this.row.getRowNum();
        HSSFCell cell = row.createCell(columnIndex.get());
        cell.setCellStyle(style);
        cell.setCellValue(Strings.nullToEmpty(value));
        sheet().addMergedRegion(new CellRangeAddress(rownum, rownum, columnIndex.get(), columnIndex.addAndGet(offset)));
        columnIndex.incrementAndGet();
        return this;
    }

    private ExcelBuilder() {
        font = book().createFont();
        font.setFontName("Verdana");
        // font.setBoldweight((short) 100);
        // font.setFontHeight((short) 300);

        // 创建单元格样式
        style = book().createCellStyle();
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

        // style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);
        style.setFillPattern(HSSFCellStyle.NO_FILL);

        // 设置边框
        style.setBottomBorderColor(HSSFColor.RED.index);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);

        style.setFont(font);// 设置字体

    }

    private HSSFWorkbook book() {
        return book;
    }

    private HSSFSheet sheet() {
        if (this.sheet == null) {
            this.sheet = book.createSheet();
        }
        return this.sheet;
    }

    public AtomicInteger getCellIndex() {
        return this.columnIndex;
    }

    public HSSFSheet currentSheet() {
        return this.sheet();
    }

    public HSSFRow currentRow() {
        return this.row;
    }

    public static final ExcelBuilder newBuilder() {
        return new ExcelBuilder();
    }

}
