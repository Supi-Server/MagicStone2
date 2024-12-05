package net.supiserver.play.magicStone.model;

import net.supiserver.play.magicStone.types.Rank;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class Probability {
    private final ItemStack item;
    private final Integer base_weight;
    private final Bonus bonus;

    public Probability(ItemStack item, int base_weight, Bonus bonus){
        this.item = item;
        this.base_weight = base_weight;
        this.bonus = bonus;
    }

    public double getWeight(){
        return getWeight(null,null,-1);
    }

    public double getWeight(Material block, Rank rank, int fortune){
        return this.bonus.calc(this.base_weight,block,rank,fortune);
    }

    public ItemStack getItem(){
        return item;
    }

    public String toString(){
        return String.format("{ItemStack: %s, BaseWeight: %d, Bonus: %s",
                Objects.requireNonNull(item.getItemMeta()).getItemName(),
                base_weight,
                bonus.toString()
        );
    }
}


