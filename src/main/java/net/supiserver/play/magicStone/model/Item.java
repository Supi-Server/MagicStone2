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
    private final int custom_model;
    private ItemStack item;

    public Item(String id, Material material, String name, List<String> lore, int custom_model){
        this.id=id;
        this.material=material;
        this.name=name;
        this.lore = lore;
        this.custom_model = custom_model;
        this.reload();
    }

    public void reload(){
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        assert itemMeta != null;
        itemMeta.setItemName(name);
        itemMeta.setLore(lore);
        itemMeta.setCustomModelData(custom_model);
        item.setItemMeta(itemMeta);
        this.item=item;
    }

    public String id(){return this.id;}
    public ItemStack get(){
        return this.item;
    }
}
