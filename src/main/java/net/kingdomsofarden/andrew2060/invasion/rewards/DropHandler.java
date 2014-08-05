package net.kingdomsofarden.andrew2060.invasion.rewards;

import java.util.Collection;

import net.kingdomsofarden.andrew2060.invasion.dimensions.DimensionManager.DimensionType;

import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class DropHandler {
    
    private static DropHandler instance;
    
    public static DropHandler getInstance() {
        return instance;
    }

    public Collection<ItemStack> getMonsterDrops(EntityType type, String world, int tier) {
        return null;
    }
    
    public Collection<ItemStack> getBossDrops(EntityType type, String world, int tier) {
        return null;
    }
    
    public Collection<ItemStack> getDimensionalStabilizerDrops(DimensionType type, int tier) {
        return null;
    }
    
    public Collection<ItemStack> getDimensionDrops(DimensionType type, int tier) {
        return null;
    }
    
}
