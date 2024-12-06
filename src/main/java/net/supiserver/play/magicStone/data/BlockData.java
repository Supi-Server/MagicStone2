package net.supiserver.play.magicStone.data;

import net.supiserver.play.magicStone.model.Probability;
import net.supiserver.play.magicStone.types.Rank;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BlockData {
    private final Material block;
    private final Set<Probability> probabilities;
    private final List<Map<Rank, TreeMap<Double, ItemStack>>> drop_table = new ArrayList<>();

    public BlockData(Material block, Set<Probability> probabilities) {
        this.block = block;
        this.probabilities = probabilities;
        for (int i = 0; i < Settings.getMaxFortuneLevel() + 1; i++) drop_table.add(null); //nullで埋めておく
        this.setup();
    }

    public void setup() {
        for (int i = 0; i < Settings.getMaxFortuneLevel() + 1; i++) {
            Map<Rank, TreeMap<Double, ItemStack>> rank_data = new HashMap<>();
            for (Rank rank : Rank.values()) {
                double weight = 0;
                TreeMap<Double, ItemStack> item_data = new TreeMap<>();
                for (Probability p : probabilities) {
                    weight += p.getWeight(block, rank, i);
                    item_data.put(weight, p.getItem());
                }
                rank_data.put(rank, item_data);
            }
            drop_table.set(i, rank_data);
        }
    }

    public ItemStack run_lottery(Rank rank, int fortune) {
        return run_lottery(rank, fortune, -1);
    }

    public ItemStack run_lottery(Rank rank, int fortune, double hit) {
        TreeMap<Double, ItemStack> table = drop_table.get(fortune).get(rank);
        hit = hit < 0 ? Math.random() * Settings.getMaxWeight() : hit;
        for (Map.Entry<Double, ItemStack> entry : table.entrySet()) {
            double weight = entry.getKey();
            ItemStack item = entry.getValue();
            if (hit <= weight) {
                return item;
            }
        }
        return null;
    }

    public String toString(Rank rank, int fortune) {
        return drop_table.get(fortune).get(rank).toString();
    }
}
