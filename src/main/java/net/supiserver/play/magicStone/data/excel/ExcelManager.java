package net.supiserver.play.magicStone.data.excel;

public class ExcelManager {
    private static final String DEVELOP_SHEET_NAME = "develop_info";


    private final String ITEM_DATA_SHEET_NAME;
    private final int ITEM_DATA_START_ROW;
    private final int ITEM_DATA_END_ROW;
    private final String ITEM_DATA_ID_COL;
    private final String ITEM_DATA_MATERIAL_COL;
    private final String ITEM_DATA_NAME_COL;
    private final String ITEM_DATA_LORE_COL;
    private final String ITEM_DATA_CUSTOM_MODEL_COL;

    private final String DROP_TABLE_SHEET_NAME;
    private final int DROP_TABLE_START_ROW;
    private final int DROP_TABLE_END_ROW;
    private final String DROP_TABLE_ID_COL;
    private final String DROP_TABLE_WEIGHT_COL;
    private final String DROP_TABLE_BLOCK_BONUS_START_COL;
    private final String DROP_TABLE_BLOCK_BONUS_END_COL;
    private final String DROP_TABLE_RANK_BONUS_START_COL;
    private final String DROP_TABLE_RANK_BONUS_END_COL;
    private final String DROP_TABLE_FORTUNE_BONUS_START_COL;
    private final String DROP_TABLE_FORTUNE_BONUS_END_COL;

    private final Excel excel;

    public ExcelManager(String filePath){
        excel = new Excel(filePath,DEVELOP_SHEET_NAME);

        ITEM_DATA_SHEET_NAME = excel.read("D2","item_data");
        ITEM_DATA_START_ROW = Integer.parseInt(excel.read("D3","3"));
        ITEM_DATA_END_ROW = Integer.parseInt(excel.read("D4","19"));
        ITEM_DATA_ID_COL = excel.read("D5","B");
        ITEM_DATA_MATERIAL_COL = excel.read("D6","D");
        ITEM_DATA_NAME_COL = excel.read("D7","E");
        ITEM_DATA_LORE_COL = excel.read("D8","F");
        ITEM_DATA_CUSTOM_MODEL_COL = excel.read("D9","G");

        DROP_TABLE_SHEET_NAME = excel.read("D10","drop_table");
        DROP_TABLE_START_ROW = Integer.parseInt(excel.read("D11","3"));
        DROP_TABLE_END_ROW = Integer.parseInt(excel.read("D12","19"));
        DROP_TABLE_ID_COL = excel.read("D13","B");
        DROP_TABLE_WEIGHT_COL = excel.read("D14","C");
        DROP_TABLE_BLOCK_BONUS_START_COL = excel.read("D15","F");
        DROP_TABLE_BLOCK_BONUS_END_COL = excel.read("D16","L");
        DROP_TABLE_RANK_BONUS_START_COL = excel.read("D17","M");
        DROP_TABLE_RANK_BONUS_END_COL = excel.read("D18","U");
        DROP_TABLE_FORTUNE_BONUS_START_COL = excel.read("D19","V");
        DROP_TABLE_FORTUNE_BONUS_END_COL = excel.read("D20","Z");
    }
}
