package net.kingdomsofarden.andrew2060.invasion.rewards;

import net.kingdomsofarden.crafty.Crafty;
import net.kingdomsofarden.crafty.api.CraftyItem;
import net.kingdomsofarden.crafty.api.Module;
import net.kingdomsofarden.crafty.api.ModuleRegistrar;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class Item {

    private final Material mat;
    private final String name;
    private final Map<Enchantment, Integer> enchants;
    private final List<String> lore;
    private final Map<String, String> modules;

    public Item(Material mat, String name, Map<Enchantment, Integer> enchants, List<String> lore,
                Map<String,String> modules) {
        this.mat = mat;
        this.name = name;
        this.enchants = enchants;
        this.lore = lore;
        this.modules = modules;
    }


    public ItemStack toItemStack(int amount) {
        ItemStack item = new ItemStack(this.mat, amount);
        ItemMeta meta = item.getItemMeta();
        if (this.name != null) {
            meta.setDisplayName(this.name);
        }
        if (this.lore != null) {
            meta.setLore(this.lore);
        }
        item.setItemMeta(meta);
        if (this.enchants != null) {
            item.addUnsafeEnchantments(this.enchants);
        }
        if (this.modules != null) {
            ModuleRegistrar registrar = Crafty.getInstance().getModuleRegistrar();
            item = Crafty.getInstance().getItemManager().createCraftyItem(item);
            CraftyItem cItem = Crafty.getInstance().getItemManager().getCraftyItem(item);
            for (Map.Entry<String,String> moduleData : this.modules.entrySet()) {
                Module m = registrar.createFromData(moduleData.getKey(), moduleData.getValue(), item);
                cItem.addModule(m);
            }
            cItem.updateItem();
        }
        return item;
    }

}
