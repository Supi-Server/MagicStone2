package net.supiserver.play.magicStone.system;

import net.supiserver.play.magicStone.MagicStone;
import net.supiserver.play.magicStone.data.BlockData;
import net.supiserver.play.magicStone.data.Settings;
import net.supiserver.play.magicStone.data.excel.ExcelManager;
import net.supiserver.play.magicStone.debug.Error;
import net.supiserver.play.magicStone.model.Probability;
import net.supiserver.play.magicStone.system.command.AdminCommand;
import net.supiserver.play.magicStone.system.event.BlockBreak;
import net.supiserver.play.magicStone.system.event.Join;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class MainSystem {
    private Map<Material, BlockData> blockDataMap = null;
    private Set<Probability> probabilitySet = null;
    private Map<String, ItemStack> magicStones = null;

    public MainSystem() {
        JavaPlugin plugin = MagicStone.getInstance();
        Error.reset();
        this.reloadData();

        new BlockBreak(plugin, this);
        new Join(plugin);
        new AdminCommand(this);
    }

    public void reloadData() {
        JavaPlugin plugin = MagicStone.getInstance();
        FileConfiguration config = plugin.getConfig();

        ExcelManager em;
        Map<String, ItemStack> items;
        Set<Probability> probabilitySet;
        try {
            em = new ExcelManager(config.getString("data_table_path", "plugins/MagicStone/data_table.xlsx"));
            items = em.readItems();
            probabilitySet = em.createProbabilities();
        } catch (IOException e) {
            e.printStackTrace();
            Error.puts("[重要] データファイルが見つからないため、データを構築できません");
            return;
        }
        Settings.reload(plugin, em);

        this.magicStones = items;
        this.probabilitySet = probabilitySet;
        Map<Material, BlockData> blockDataMap = new HashMap<>();
        Settings.getTargetBlocks().forEach(block -> {
            BlockData blockData = new BlockData(block, probabilitySet);
            blockData.setup();
            blockDataMap.put(block, blockData);
        });
        this.blockDataMap = blockDataMap;
    }

    ;

    public String printProbabilities() {
        StringBuilder msgBuilder = new StringBuilder();
        probabilitySet.forEach(probability -> {
            msgBuilder.append(probability.toString()).append(",");
        });
        msgBuilder.append(Settings.getAsString());
        String msg = msgBuilder.toString();
        return String.format("[%s]\n", msg);
    }

    public BlockData getBlockData(Material mate) {
        return blockDataMap.get(mate);
    }

    public Map<String, ItemStack> getMagicStones() {
        return magicStones;
    }


}
