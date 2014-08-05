package net.kingdomsofarden.andrew2060.invasion.dimensions;

import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;

import net.kingdomsofarden.andrew2060.invasion.dimensions.DimensionManager.DimensionType;

public class Dimension {
    
    private DimensionType type;
    private int tier;
    private UUID id; //Don't save the world in a field to reduce memory usage
    private Set<DimensionSpawner> spawners;
    
    public DimensionType getType() {
        return this.type;
    }
    
    public int getTier() {
        return this.tier;
    }
    
    public Set<DimensionSpawner> getSpawners() {
        return this.spawners;
    }
    
    public UUID getUniqueId() { //Same as the world ID
        return this.id;
    }
    
    public World getWorld() {
        return Bukkit.getServer().getWorld(this.id);
    }

}
