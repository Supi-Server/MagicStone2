package net.supiserver.play.magicStone.model;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Item {
    private final String id;
    private final Material material;
    private final String name;
    private final List<String> lore;
    private ItemStack item;

    public Item(String id, Material material, String name, List<String> lore){
        this.id=id;
        this.material=material;
        this.name=name;
        this.lore = lore;
        this.reload();
    }

    public void reload(){
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setItemName(name);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        this.item=item;
    }

    public ItemStack get(){
        return this.item;
    }
}
