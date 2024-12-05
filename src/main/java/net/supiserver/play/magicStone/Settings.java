package net.supiserver.play.magicStone;

import org.bukkit.Material;

import java.util.List;

public class Settings {
    public static final int MAX_FORTUNE_LEVEL = 5;
    public static final double MAX_WEIGHT = 300000;
    public static final List<Material> TARGET_BLOCKS = List.of(
        Material.STONE,
        Material.ANDESITE,
        Material.DIORITE,
        Material.GRANITE,
        Material.TUFF,
        Material.CALCITE,
        Material.DEEPSLATE
    );
}
