package net.supiserver.play.magicStone.system.command;

import net.supiserver.play.magicStone.MagicStone;
import net.supiserver.play.magicStone.data.BlockData;
import net.supiserver.play.magicStone.data.Settings;
import net.supiserver.play.magicStone.debug.Error;
import net.supiserver.play.magicStone.system.MainSystem;
import net.supiserver.play.magicStone.types.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AdminCommand implements CommandExecutor {
    private static final String HEADER = "§e§l----- [MagicStone] -----";
    private static final String LABEL = "magicstone";
    private static final JavaPlugin plugin = MagicStone.getInstance();
    private final MainSystem system;
    public AdminCommand(MainSystem system){
        this.system = system;
        PluginCommand command = plugin.getCommand(LABEL);
        assert command != null;
        command.setExecutor(this);
        command.setTabCompleter(new Tab());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp())return true;
        if(args.length==0){
            sendHelp(sender);
            return true;
        }
        switch (args[0]){
            case "reload" ->{
                sender.sendMessage(HEADER);
                sender.sendMessage("§a§lリロード開始");
                system.reloadData();
                List<String> error = Error.get();
                if(error.isEmpty())sender.sendMessage("§a§lエラーなし");
                else error.forEach(err->sender.sendMessage(String.format("&c&l%s",err)));
                sender.sendMessage("§a§lリロード終了");
            }
            case "error" ->{
                sender.sendMessage(HEADER);
                List<String> error = Error.get();
                if(error.isEmpty())sender.sendMessage("§a§lエラーなし");
                else error.forEach(err->sender.sendMessage(String.format("&c&l%s",err)));
            }
            case "print" ->{
                sender.sendMessage(HEADER);
                if(args.length<4)sender.sendMessage(system.printProbabilities());
                else{
                    try {
                        Material mate = Material.valueOf(args[1]);
                        Rank rank = Rank.fromValue(args[2]);
                        int fortune = Integer.parseInt(args[3]);
                        if(fortune<0||fortune>5)throw new RuntimeException();
                        sender.sendMessage(system.getBlockData(mate).toString(rank,fortune));
                    }catch (Exception e){
                        sender.sendMessage("§c引数が不正です");
                    }

                }
            }
            case "challenge" ->{
                sender.sendMessage(HEADER);
                if(args.length<4)sender.sendMessage(system.printProbabilities());
                else{
                    try {
                        Material mate = Material.valueOf(args[1]);
                        Rank rank = Rank.fromValue(args[2]);
                        int fortune = Integer.parseInt(args[3]);
                        if(fortune<0||fortune>5)throw new RuntimeException();
                        BlockData bd = system.getBlockData(mate);
                        ItemStack item = bd.run_lottery(rank,fortune,args.length == 5 ? Double.parseDouble(args[4]) : -1);
                        if(item==null)sender.sendMessage("§cはずれ");
                        else sender.sendMessage(item.toString());
                    }catch (Exception e){
                        sender.sendMessage("§c引数が不正です");
                    }

                }
            }
            case "get" ->{
                if(args.length<2)sendHelp(sender);
                else{
                    ItemStack item = system.getMagicStones().get(args[1]);
                    if(item==null)sender.sendMessage("§cItemが見つかりません");
                    else {
                        if(sender instanceof Player)((Player)sender).getInventory().addItem(item);
                        else sender.sendMessage(item.toString());
                    }
                }
            }
            default -> {
                sendHelp(sender);
            }
        }
        return true;
    };

    private void sendHelp(CommandSender sender){
        sender.sendMessage(HEADER);
        sender.sendMessage("§a/magicstone reload§r: §d全データを再読み込みします");
        sender.sendMessage("§a/magicstone error§r: §dエラー内容を確認します");
        sender.sendMessage("§a/magicstone print [<Material>] [<Rank>] [<FortuneLevel>]§r: §d登録データを確認します");
        sender.sendMessage("§a/magicstone challenge <Material> <Rank> <FortuneLevel> [<hit_number>]§r: §dテスト抽選");
        sender.sendMessage("§a/magicstone get <id>§r: §d指定したIDの魔法石を取得します");
    }

    private class Tab implements TabCompleter{
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
            return switch (args.length){
                case 1->List.of("reload","error","print","get","challenge");
                case 2-> switch (args[0]){
                    case "print","challenge"-> Settings.getTargetBlocks().stream().map(Objects::toString).collect(Collectors.toList());
                    case "get"-> new ArrayList<>(system.getMagicStones().keySet());
                    default -> null;
                };
                case 3-> switch (args[0]){
                    case "print","challenge"-> Arrays.stream(Rank.values()).map(Rank::getValue).collect(Collectors.toList());
                    default -> null;
                };
                default -> null;
            };
        }
    }
}

