package net.kingdomsofarden.andrew2060.invasion.monsters.listeners;

import java.util.Random;

import net.kingdomsofarden.andrew2060.invasion.MobManager;
import net.kingdomsofarden.andrew2060.invasion.monsters.InvasionGiant;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;

import com.herocraftonline.heroes.api.events.WeaponDamageEvent;
import com.herocraftonline.heroes.characters.Hero;


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
        if(!(rand.nextInt(20000) < 1)) {
            return;
        }
        Location loc = event.getEntity().getLocation();
        Location toSpawn = loc.getWorld().getHighestBlockAt(loc).getLocation().add(0,1,0);
        InvasionGiant giant = new InvasionGiant(toSpawn);
        mobmanager.addGiant(giant);
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST) 
    public void onWeaponDamage(WeaponDamageEvent event) {
        if(!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        LivingEntity lE = (LivingEntity)event.getEntity();
        if(lE.getCustomName() == null) {
            return;
        }
        if(event.getDamager() instanceof Player) {
            return;
        }
        //Verify that our mob manager has this giant if it is a giant
        if(lE instanceof Giant) {
            Giant g = (Giant)lE;
            //If not, destroy the giant, and make a new one!
            if(!mobmanager.getGiants().containsKey(g.getUniqueId())) {
                Location loc = g.getLocation();
                g.remove();
                InvasionGiant newGiant = new InvasionGiant(loc);
                mobmanager.getGiants().put(newGiant.getGiant().getUniqueId(), newGiant);
            }
        }
        if(lE instanceof Player) {
            return;
        }
        //Verify that its something that was spawned by us
        if(!lE.getCustomName().contains("Undead")) {
            return;
        }
        //Verify non-projectile
        if(event.getEntity() instanceof Projectile) {
            return;
        }
        
        if(!(event.getDamager() instanceof Hero)) {
            event.setCancelled(true);
        }
    }
    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST) 
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        LivingEntity lE = (LivingEntity)event.getEntity();
        if(lE.getCustomName() == null) {
            return;
        }
        //Verify that our mob manager has this giant if it is a giant
        if(lE instanceof Giant) {
            Giant g = (Giant)lE;
            //If not, destroy the giant, and make a new one!
            if(!mobmanager.getGiants().containsKey(g.getUniqueId())) {
                Location loc = g.getLocation();
                g.remove();
                InvasionGiant newGiant = new InvasionGiant(loc);
                mobmanager.getGiants().put(newGiant.getGiant().getUniqueId(), newGiant);
            }
        }
        if(lE instanceof Player) {
            return;
        }
        switch(event.getCause()) {
        
        case BLOCK_EXPLOSION:
        case CUSTOM:
        case ENTITY_ATTACK:
        case ENTITY_EXPLOSION:
        case PROJECTILE:
        case MAGIC:
            return;  
        default:
            break;
            
        }
        //Verify that its something that was spawned by us
        if(!lE.getCustomName().contains("Undead")) {
            return;
        }
        //Cancel all damage that's not from a player
        event.setCancelled(true);
    }
    @EventHandler(ignoreCancelled = true)
    public void spawnGiant(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if(event.getPlayer().getItemInHand().getType().equals(Material.FIRE)) {
            InvasionGiant giant = new InvasionGiant(event.getPlayer().getLocation());
            mobmanager.addGiant(giant);
        }
    }
    @EventHandler(ignoreCancelled = true)
    public void onPluginDisable(PluginDisableEvent event) {
        if(event.getPlugin().getName().equals("KingdomsOfArden-Invasion")) {
            for(InvasionGiant g : mobmanager.getGiants().values()) {
                g.kill();
            }
        }
    }
}

