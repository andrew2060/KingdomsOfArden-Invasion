package net.kingdomsofarden.andrew2060.invasion;

import java.util.HashMap;
import java.util.UUID;

import net.kingdomsofarden.andrew2060.invasion.entities.InvasionGiant;

public class MobManager {

    private InvasionPlugin plugin;
    HashMap<UUID,InvasionGiant> giants;
    public MobManager(InvasionPlugin plugin) {
        this.plugin = plugin;
        this.giants = new HashMap<UUID,InvasionGiant>();
        //Register Timers for Giants
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
            
            @Override
            public void run() {
                for(InvasionGiant giant : giants.values()) {
                    giant.tick(MobManager.this);
                }
                
            }
            
        }, 0, 20);
        
    }
    public void addGiant(InvasionGiant giant) {
        giants.put(giant.getGiant().getUniqueId(),giant);
        
    }
    public void removeGiant(InvasionGiant giant) {
        giants.remove(giant.getGiant().getUniqueId());
    }
    public HashMap<UUID,InvasionGiant> getGiants() {
        return giants;
    }
    
}
