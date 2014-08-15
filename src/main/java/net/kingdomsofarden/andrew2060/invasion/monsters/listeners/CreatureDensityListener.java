package net.kingdomsofarden.andrew2060.invasion.monsters.listeners;

import net.kingdomsofarden.andrew2060.invasion.InvasionPlugin;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.events.CharacterDamageEvent;
import com.herocraftonline.heroes.characters.Monster;

public class CreatureDensityListener implements Listener {
    
    private Heroes heroes;
    public CreatureDensityListener(InvasionPlugin plugin) {
        this.heroes = (Heroes) plugin.getServer().getPluginManager().getPlugin("Heroes");
    }

    //Remove if more than 10 spawner mobs w/in a 5x5x5 cube
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onMonsterDamage(CharacterDamageEvent event) {
        Monster m = null;
        if (event.getEntity() instanceof org.bukkit.entity.Monster) {
            m = heroes.getCharacterManager().getMonster((LivingEntity) event.getEntity());
            if (m.getSpawnReason() != SpawnReason.SPAWNER) {
                return;
            }
            
        } else {
            return;
        }
        int limit = 10;
        for (Entity e : event.getEntity().getNearbyEntities(2, 2, 2)) {
            if (e.getType() == m.getEntity().getType()) {
                limit--;
                if (limit < 0) {
                    limit = 0;
                    e.remove();
                }
            }
        }
    }
    
    //Limits the maximum number of domesticated animals that can be present at any given time within a chunk
    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {
        Chunk c = event.getChunk();
        int max = 10;
        for (Entity e : c.getEntities()) {
            switch (e.getType()) {
            
            case COW:
            case PIG:
            case CHICKEN:
            case SHEEP: {
                max--;
                if (max < 0) {
                    max = 0;
                    e.remove();
                }
            }
            default: {
                continue;
            }
            
            }
        }
    }
    
}
