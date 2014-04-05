package net.kingdomsofarden.andrew2060.invasion.dimensions;

import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;

public class DimensionCreatureManager {
   
    public Cache<Location,DimensionSpawner> spawnPoints;
    
    public DimensionCreatureManager() {
        this.spawnPoints = CacheBuilder
                .newBuilder()
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .build(new CacheLoader<Location,DimensionSpawner>() {

                    @Override
                    public DimensionSpawner load(Location loc) {
                        return new DimensionSpawner(loc);
                    }
                    
                });
    }

}
