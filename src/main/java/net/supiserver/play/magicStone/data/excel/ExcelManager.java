package net.supiserver.play.magicStone.data.excel;

import net.supiserver.play.magicStone.data.Settings;
import net.supiserver.play.magicStone.debug.Error;
import net.supiserver.play.magicStone.model.Bonus;
import net.supiserver.play.magicStone.model.Item;
import net.supiserver.play.magicStone.model.Probability;
import net.supiserver.play.magicStone.types.Rank;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;


public class ExcelManager {
    private static final String DEVELOP_SHEET_NAME = "develop_info";
    private static final String EOF_V = "EOF_V";
    private static final String EOF_H = "EOF_H";
    private static final String EOF = "EOF";

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

    private final String BASIC_INFO_SHEET_NAME;
    private final String BASIC_INFO_MAX_WEIGHT_CELL;

    private final Excel excel;

    public ExcelManager(String filePath)throws IOException{

        excel = new Excel(filePath, DEVELOP_SHEET_NAME);
        excel.open();
        ITEM_DATA_SHEET_NAME = excel.read("D2","item_data");
        ITEM_DATA_START_ROW = (int)Double.parseDouble(excel.read("D3","3"));
        ITEM_DATA_END_ROW = (int)Double.parseDouble(excel.read("D4","19"));
        ITEM_DATA_ID_COL = excel.read("D5","B");
        ITEM_DATA_MATERIAL_COL = excel.read("D6","D");
        ITEM_DATA_NAME_COL = excel.read("D7","E");
        ITEM_DATA_LORE_COL = excel.read("D8","F");
        ITEM_DATA_CUSTOM_MODEL_COL = excel.read("D9","G");

        DROP_TABLE_SHEET_NAME = excel.read("D10","drop_table");
        DROP_TABLE_START_ROW = (int)Double.parseDouble(excel.read("D11","4"));
        DROP_TABLE_END_ROW = (int)Double.parseDouble(excel.read("D12","19"));
        DROP_TABLE_ID_COL = excel.read("D13","B");
        DROP_TABLE_WEIGHT_COL = excel.read("D14","C");
        DROP_TABLE_BLOCK_BONUS_START_COL = excel.read("D15","F");
        DROP_TABLE_BLOCK_BONUS_END_COL = excel.read("D16","L");
        DROP_TABLE_RANK_BONUS_START_COL = excel.read("D17","M");
        DROP_TABLE_RANK_BONUS_END_COL = excel.read("D18","U");
        DROP_TABLE_FORTUNE_BONUS_START_COL = excel.read("D19","V");
        DROP_TABLE_FORTUNE_BONUS_END_COL = excel.read("D20","Z");

        BASIC_INFO_SHEET_NAME = excel.read("D21","basic_info");
        BASIC_INFO_MAX_WEIGHT_CELL = excel.read("D22","C2");
        excel.close();
    }

    public Map<BasicData,String> reloadBasicData() throws IOException{
        Map<BasicData,String> result = new HashMap<>();
        result.put(BasicData.MAX_FORTUNE_LEVEL,
            String.valueOf(
            Excel.getCellIndex(DROP_TABLE_FORTUNE_BONUS_END_COL+"1")[1] -
                Excel.getCellIndex(DROP_TABLE_FORTUNE_BONUS_START_COL+"1")[1]
            )
        );
        excel.open();
        excel.setSheet(BASIC_INFO_SHEET_NAME);
        result.put(BasicData.MAX_WEIGHT,excel.read(BASIC_INFO_MAX_WEIGHT_CELL));
        excel.close();
        return result;
    }

    public Map<String,ItemStack> readItems() throws IOException{
        excel.open();
        excel.setSheet(ITEM_DATA_SHEET_NAME);
        Map<String,ItemStack> result = new HashMap<>();
        for(int row = ITEM_DATA_START_ROW; row<=ITEM_DATA_END_ROW;row++){
            try {
                String id = excel.read(ITEM_DATA_ID_COL + row);
                if (id.isEmpty() || id.equals(EOF_V)) break;
                Material material = Material.getMaterial(excel.read(ITEM_DATA_MATERIAL_COL + row));
                String name = excel.read(ITEM_DATA_NAME_COL + row);
                String[] lore = excel.read(ITEM_DATA_LORE_COL + row).split(",");
                int custom_model = (int)Double.parseDouble(excel.read(ITEM_DATA_CUSTOM_MODEL_COL + row));
                result.put(id,new Item(id,material,name,List.of(lore),custom_model).get());
            }catch (Exception e){
                e.printStackTrace();
                Error.puts(String.format("ITEM_DATA:『%d行』のアイテムの読み込みに失敗しました。",row));
            }
        }
        excel.close();
        return result;
    }

    public Map<String, Bonus> readBonus() throws IOException{
        int MAX_FORTUNE_LEVEL = Settings.getMaxFortuneLevel();
        excel.open();
        excel.setSheet(DROP_TABLE_SHEET_NAME);
        Map<String,Bonus> result = new HashMap<>();
        for(int row = DROP_TABLE_START_ROW; row<=DROP_TABLE_END_ROW;row++){
            String id = excel.read(DROP_TABLE_ID_COL + row,"");
            if (id.isEmpty() || id.equals(EOF_V)) break;

            //blockBonus読み込み
            Map<Material,Double> blockBonus = new HashMap<>();
            try {
                int block_start_column = Excel.getCellIndex(DROP_TABLE_BLOCK_BONUS_START_COL+row)[1];
                int block_end_column = Excel.getCellIndex(DROP_TABLE_BLOCK_BONUS_END_COL+row)[1];
                for(int i = block_start_column;i<=block_end_column;i++){
                    Material mate = Material.getMaterial(excel.read(Excel.getCellName(new int[]{i,DROP_TABLE_START_ROW-1})));
                    if(mate==null){
                        Error.puts(String.format("DROP_TABLE: ブロックIDセル『%s』を読み込めませんでした",Excel.getCellName(new int[]{i,DROP_TABLE_START_ROW-1})));
                        continue;
                    }
                    double cellValue = Double.parseDouble(excel.read(Excel.getCellName(new int[]{i,row})));
                    blockBonus.put(mate,cellValue);
                }
            }catch (Exception e){
                e.printStackTrace();
                Error.puts(String.format("DROP_TABLE:『%d行』のブロックボーナス状況の読み込みに失敗しました。",row));
                continue;
            }

            //rankBonus読み込み
            Map<Rank,Double> rankBonus = new HashMap<>();
            try{
                int rank_start_column = Excel.getCellIndex(DROP_TABLE_RANK_BONUS_START_COL+row)[1];
                int rank_end_column = Excel.getCellIndex(DROP_TABLE_RANK_BONUS_END_COL+row)[1];
                for(int i = rank_start_column;i<=rank_end_column;i++){
                    Rank rank = Rank.fromValue(excel.read(Excel.getCellName(new int[]{i,DROP_TABLE_START_ROW-1})));
                    double cellValue = Double.parseDouble(excel.read(Excel.getCellName(new int[]{i,row})));
                    rankBonus.put(rank,cellValue);
                }
            }catch (Exception e){
                e.printStackTrace();
                Error.puts(String.format("DROP_TABLE:『%d行』のランクボーナス状況の読み込みに失敗しました。",row));
                continue;
            }

            //幸運ボーナス読み込み
            Double[] fortuneBonus = new Double[MAX_FORTUNE_LEVEL+1];
            try{
                int fortune_start_column = Excel.getCellIndex(DROP_TABLE_FORTUNE_BONUS_START_COL+row)[1];
                int fortune_end_column = Excel.getCellIndex(DROP_TABLE_FORTUNE_BONUS_END_COL+row)[1];
                fortune_end_column = Math.max(fortune_end_column,fortune_start_column+MAX_FORTUNE_LEVEL);
                for(int i = fortune_start_column;i<=fortune_end_column;i++){
                    double cellValue = Double.parseDouble(excel.read(Excel.getCellName(new int[]{i,row})));
                    fortuneBonus[i-fortune_start_column] = cellValue;
                }
            }catch (Exception e){
                e.printStackTrace();
                Error.puts(String.format("DROP_TABLE:『%d行』の幸運ボーナス状況の読み込みに失敗しました。",row));
                continue;
            }

            Bonus bonus = new Bonus(blockBonus,rankBonus,fortuneBonus);
            result.put(id,bonus);
        }
        excel.close();
        return result;
    }

    public Map<String, Integer> readBaseWeight() throws IOException{
        excel.open();
        excel.setSheet(DROP_TABLE_SHEET_NAME);
        Map<String, Integer> result = new HashMap<>();
        for(int row = DROP_TABLE_START_ROW; row<=DROP_TABLE_END_ROW;row++) {
            try {
            String id = excel.read(DROP_TABLE_ID_COL + row);
            if (id.isEmpty() || id.equals(EOF_V)) break;
            int weight = (int)Double.parseDouble(excel.read(DROP_TABLE_WEIGHT_COL+row));
            result.put(id,weight);
            } catch (Exception e) {
                e.printStackTrace();
                Error.puts(String.format("DROP_TABLE:『%d行』の基礎比重状況の読み込みに失敗しました。", row));
            }
        }
        excel.close();
        return result;
    }

    public Set<Probability> createProbabilities(){

        Map<String,ItemStack> items;
        Map<String,Bonus> bonus;
        Map<String,Integer> weight;
        try{
            items = readItems();
            bonus = readBonus();
            weight = readBaseWeight();
        }catch (IOException e){
            e.printStackTrace();
            Error.puts("[重要] データファイルが見つからないため、データを構築できません");
            return null;
        }
        if(items.size()!=bonus.size()|| items.size()!=weight.size()){
            Error.puts("登録アイテム数とドロップテーブルの個数が異なります。全てが正常に読み取れていない可能性があります");
        }

        Set<Probability> result = new HashSet<>();
        for(Map.Entry<String, ItemStack> entry : items.entrySet()){
            String id = entry.getKey();
            ItemStack value = entry.getValue();
            result.add(new Probability(value,weight.get(id),bonus.get(id)));
        }
        return result;
    }
}
