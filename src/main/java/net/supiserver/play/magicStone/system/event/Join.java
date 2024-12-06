package net.supiserver.play.magicStone.system.event;

import net.supiserver.play.magicStone.debug.Error;
import net.supiserver.play.magicStone.system.MainSystem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Join implements Listener {
    public Join(JavaPlugin plugin){
        plugin.getServer().getPluginManager().registerEvents(this,plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if(!p.isOp())return;
        Error.get().forEach(err->p.sendMessage(String.format("&c&l%s",err)));
    }
}
