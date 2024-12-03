package net.supiserver.play.magicStone.data;

import net.supiserver.play.magicStone.model.Probability;
import net.supiserver.play.magicStone.types.Rank;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Block {
    private final Material block;
    private final Set<Probability> probabilities;
    private final List<Map<Rank, TreeMap<Double, ItemStack>>> drop_table = new ArrayList<>(6);

    public Block(Material block, Set<Probability> probabilities) {
        this.block = block;
        this.probabilities = probabilities;
        this.setup();
    }

    public void setup() {
        int max_fortune = 6;
        for (int i = 0; i < max_fortune; i++) {
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
        TreeMap<Double, ItemStack> table = drop_table.get(fortune).get(rank);
        double max_weight = table.lastKey();
        double hit = Math.random() * max_weight;
        for (Map.Entry<Double, ItemStack> entry : table.entrySet()) {
            double weight = entry.getKey();
            ItemStack item = entry.getValue();
            if (hit <= weight) return item;
        }
        return null;
    }
}
