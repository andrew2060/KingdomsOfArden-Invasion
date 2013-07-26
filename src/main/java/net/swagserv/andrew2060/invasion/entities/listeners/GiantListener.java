package net.swagserv.andrew2060.invasion.entities.listeners;

import java.util.Random;

import net.swagserv.andrew2060.invasion.MobManager;
import net.swagserv.andrew2060.invasion.entities.InvasionGiant;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;


public class GiantListener implements Listener {
    private Random rand;
    private MobManager mobmanager;
    public GiantListener(MobManager mobmanager) {
        this.rand = new Random();
        this.mobmanager = mobmanager;
    }
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void replaceGiantSpawn(CreatureSpawnEvent event) {
        //Can't cancel original event due to heroes character manager listening to lowest priority
        if(!(event.getEntityType() == EntityType.ZOMBIE)) {
            return;
        }
        if(event.getSpawnReason() == SpawnReason.SPAWNER) {
            return;
        }
        if(!(rand.nextInt(10000) < 1)) {
            return;
        }
        Location loc = event.getEntity().getLocation();
        Location toSpawn = loc.getWorld().getHighestBlockAt(loc).getLocation().add(0,1,0);
        InvasionGiant giant = new InvasionGiant(toSpawn);
        mobmanager.addGiant(giant);
    }
    //TODO: DEBUG
    @EventHandler(ignoreCancelled = true)
    public void spawnGiant(PlayerInteractEvent event) {
        if(event.getPlayer().getItemInHand().getType().equals(Material.FIRE)) {
            InvasionGiant giant = new InvasionGiant(event.getPlayer().getLocation());
            mobmanager.addGiant(giant);
        }
    }
    @EventHandler(ignoreCancelled = true)
    public void onPluginDisable(PluginDisableEvent event) {
        if(event.getPlugin().getName().equals("Swagserv-Invasion")) {
            for(InvasionGiant g : mobmanager.getGiants()) {
                g.kill();
            }
        }
    }
}

