package net.supiserver.play.magicStone.system.event;

import net.supiserver.play.magicStone.data.Settings;
import net.supiserver.play.magicStone.system.MainSystem;
import net.supiserver.play.magicStone.types.Rank;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;


public class BlockBreak implements Listener {
    private  final MainSystem system;
    public BlockBreak(JavaPlugin plugin, MainSystem system){
        this.system = system;
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Location loc = e.getBlock().getLocation();
        World world = loc.getWorld();
        if(world==null)return;
        String worldName = world.getName();
        Player p = e.getPlayer();
        if(!Settings.getTargetWorlds().contains(worldName) || p.getGameMode() != GameMode.SURVIVAL )return;
        Material mate = e.getBlock().getType();
        int fortune = p.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.FORTUNE);
        Rank maxRank = Rank.BEGINNER;
        for(Rank rank : Rank.values())maxRank = p.hasPermission(String.format("rank.%s",rank.getValue())) ? rank : maxRank;
        ItemStack item = system.getBlockData(mate).run_lottery(maxRank,fortune);

        if (item != null && item.getType() != Material.AIR)world.dropItemNaturally(loc, item);
    }
}
