package net.swagserv.andrew2060.invasion;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import net.swagserv.andrew2060.invasion.entities.InvasionGiant;

public class MobManager {

    private InvasionPlugin plugin;
    HashMap<UUID,InvasionGiant> temp;
    List<InvasionGiant> giants;
    public MobManager(InvasionPlugin plugin) {
        this.plugin = plugin;
        this.giants = new LinkedList<InvasionGiant>();
        //Register Timers for Giants
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {

            @Override
            public void run() {
                for(InvasionGiant giant : giants) {
                    giant.tick(MobManager.this);
                }
                
            }
            
        }, 0, 20);
        
    }
    public void addGiant(InvasionGiant giant) {
        giants.add(giant);
    }
    public void removeGiant(InvasionGiant giant) {
        giants.remove(giant);
    }
    public List<InvasionGiant> getGiants() {
        return giants;
    }
    
}
