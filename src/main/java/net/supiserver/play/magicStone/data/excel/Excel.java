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

    public Excel(String filePath)throws IOException{this(filePath,"Sheet1");}
    public Excel(String filePath,String default_sheet)throws IOException{
        Workbook workbook = null;
        FileInputStream fis = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fis);

        this.workbook = workbook;
        this.sheet = workbook.getSheet(default_sheet);
    }

    public void setSheet(String sheet_name){
        this.sheet = workbook.getSheet(sheet_name);
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

    public static int[] getCellIndex(String cellName){
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

    public static String getCellName(int[] cellIndex) {
        if (cellIndex == null || cellIndex.length != 2) {
            throw new IllegalArgumentException("cellIndex must be an array of two integers.");
        }

        int rowIndex = cellIndex[1];
        int columnIndex = cellIndex[0];

        if (rowIndex < 0 || columnIndex < 0) {
            throw new IllegalArgumentException("Row and column indices must be non-negative.");
        }

        StringBuilder columnName = new StringBuilder();
        columnIndex += 1;
        while (columnIndex > 0) {
            int remainder = (columnIndex - 1) % 26;
            columnName.insert(0, (char) (remainder + 'A'));
            columnIndex = (columnIndex - 1) / 26;
        }

        return columnName.toString() + rowIndex;
    }

}
