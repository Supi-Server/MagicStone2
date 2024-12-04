package net.supiserver.play.magicStone.data.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Excel {
    private final Workbook workbook;
    private Sheet sheet;

    public Excel(String filePath){this(filePath,"Sheet1");}
    public Excel(String filePath,String default_sheet){
        Workbook workbook = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fis);
        }catch (IOException e){
            e.printStackTrace();
        }
        this.workbook = workbook;
        assert workbook != null;
        this.sheet = workbook.getSheet(default_sheet);
    }

    public String read(String cellName){return this.read(cellName,"");}
    public String read(String cellName, String default_value){
        int[] cellIdx = getCellIndex(cellName);
        Row row = sheet.getRow(cellIdx[0]);
        Cell cell = row.getCell(cellIdx[1]);
        String result = switch (cell.getCellType()){
            case _NONE, BLANK, BOOLEAN, ERROR -> null;
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case STRING -> cell.getStringCellValue();
            case FORMULA -> {
                CreationHelper ch = workbook.getCreationHelper();
                FormulaEvaluator evaluator = ch.createFormulaEvaluator();
                CellValue value = evaluator.evaluate(cell);
                yield value.getStringValue();
            }
        };
        return (result != null && !result.isEmpty()) ? result : default_value;
    }

    private int[] getCellIndex(String cellName){
        Pattern pattern = Pattern.compile("([A-Za-z]+)([0-9]+)");
        Matcher matcher = pattern.matcher(cellName);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid cellName format: " + cellName);
        }

        String columnPart = matcher.group(1);
        int rowIndex = Integer.parseInt(matcher.group(2)) - 1;

        int columnIndex = 0;
        for (int i = 0; i < columnPart.length(); i++) {
            columnIndex = columnIndex * 26 + (columnPart.charAt(i) - 'A' + 1);
        }
        columnIndex -= 1;
        return new int[]{rowIndex,columnIndex};
    }
}
