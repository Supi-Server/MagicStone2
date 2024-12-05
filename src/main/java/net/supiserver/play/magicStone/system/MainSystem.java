package net.supiserver.play.magicStone.system;

import net.supiserver.play.magicStone.MagicStone;
import net.supiserver.play.magicStone.data.BlockData;
import net.supiserver.play.magicStone.data.excel.ExcelManager;
import net.supiserver.play.magicStone.debug.Error;
import net.supiserver.play.magicStone.model.Probability;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static net.supiserver.play.magicStone.Settings.TARGET_BLOCKS;

public class MainSystem {
    private Map<Material,BlockData> blockDataMap = null;
    private Set<Probability> probabilitySet = null;
    public MainSystem(){
        Error.reset();
        this.reloadData();
    }

    public boolean reloadData(){
        JavaPlugin plugin = MagicStone.getInstance();
        FileConfiguration config = plugin.getConfig();
        ExcelManager em =null;
        try{
            em=new ExcelManager(config.getString("data_table_path","plugins/MagicStone/data_table.xlsx"));
        }catch(IOException e){
            e.printStackTrace();
            Error.puts("[重要] データファイルが見つからないため、データを構築できません");
            return false;
        }
        Set<Probability> probabilitySet = em.createProbabilities();

        this.probabilitySet = probabilitySet;
        Map<Material,BlockData> blockDataMap = new HashMap<>();
        TARGET_BLOCKS.forEach(block->{
            BlockData blockData = new BlockData(block,probabilitySet);
            blockData.setup();
            blockDataMap.put(block,blockData);
        });
        this.blockDataMap = blockDataMap;
        return true;
    };

    public void printProbabilities(){
        StringBuilder msgBuilder = new StringBuilder();
        probabilitySet.forEach(probability -> {
            msgBuilder.append(probability.toString()).append(",");
        });
        if (msgBuilder.length() > 0) {
            msgBuilder.setLength(msgBuilder.length() - 1);
        }
        String msg = msgBuilder.toString();
        System.out.println(String.format("[%s]",msg));
    }
}
