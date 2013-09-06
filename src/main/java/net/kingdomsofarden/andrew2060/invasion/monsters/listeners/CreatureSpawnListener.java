package net.kingdomsofarden.andrew2060.invasion.monsters.listeners;

import net.kingdomsofarden.andrew2060.invasion.MobManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Giant;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEvent;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.characters.Monster;


public class CreatureSpawnListener implements Listener {
    private MobManager mobmanager;
    private Heroes heroes;
    public CreatureSpawnListener(MobManager mobmanager) {
        this.mobmanager = mobmanager;
        this.heroes = (Heroes)Bukkit.getPluginManager().getPlugin("Heroes");
    }
    @EventHandler(ignoreCancelled = true)
    public void spawnGiant(PlayerInteractEvent event) {
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if(event.getPlayer().getItemInHand().getType().equals(Material.FIRE)) {
            Location loc = event.getClickedBlock().getLocation().add(0,1,0);
            Giant toSpawn = loc.getWorld().spawn(loc, Giant.class);
            CreatureSpawnEvent spawnEvent = new CreatureSpawnEvent(toSpawn, SpawnReason.NATURAL);
            Bukkit.getPluginManager().callEvent(spawnEvent);
            if(spawnEvent.isCancelled()) {
                toSpawn.remove();
            }
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onCreatureSpawnRegisterCharacter(CreatureSpawnEvent event) {
        if(event.getEntity() instanceof Creature) {
            mobmanager.registerCharacter((Creature) event.getEntity());
        }
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onCreatureSpawnModifyHealthDamage(CreatureSpawnEvent event) {
        if(event.getEntity() instanceof HumanEntity) {
            return;
        }
        Location spawnLoc = event.getLocation();
        double distanceSquared = event.getLocation().getWorld().getSpawnLocation().distanceSquared(spawnLoc);
        double multiplier = Math.pow(1.5,(distanceSquared/100000000));
        LivingEntity lE = event.getEntity();
        lE.setMaxHealth(lE.getMaxHealth()*multiplier);
        lE.setHealth(lE.getMaxHealth());
        Monster m = heroes.getCharacterManager().getMonster(lE);
        m.setDamage(0.5*multiplier > 1 ? m.getDamage()*0.5*multiplier : m.getDamage());
        m.setExperience((int) Math.floor(m.getExperience()*multiplier));
    }
}

