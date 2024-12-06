package net.supiserver.play.magicStone.model;

import net.supiserver.play.magicStone.types.Rank;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Map;

public class Bonus {
    final Map<Material, Double> block;
    final Map<Rank, Double> rank;
    final Double[] fortune;

    public Bonus(Map<Material, Double> block, Map<Rank, Double> rank, Double[] fortune) {
        this.block = block;
        this.rank = rank;
        this.fortune = fortune;
    }

    public Double calc(int base_weight, Material block, Rank rank, int fortune) {
        double block_bonus = block == null || !this.block.containsKey(block) ? 0 : this.block.get(block);
        double rank_bonus = rank == null ? 0 : this.rank.get(rank);
        double fortune_bonus = fortune < 0 ? 0 : this.fortune[fortune];
        return base_weight * (100 + block_bonus + rank_bonus + fortune_bonus) / 100.0;
    }

    public String toString() {
        return String.format(
                "{block: %s, rank: %s, fortune: %s}",
                block.toString(),
                rank.toString(),
                Arrays.toString(fortune)
        );
    }

}
