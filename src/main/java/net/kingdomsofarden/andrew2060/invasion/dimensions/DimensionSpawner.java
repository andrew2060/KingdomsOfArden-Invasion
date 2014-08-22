package net.kingdomsofarden.andrew2060.invasion.dimensions;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

import net.kingdomsofarden.andrew2060.invasion.InvasionPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class DimensionSpawner {
       
    private Location location;
    private Random random;
    public byte health;
    public BukkitTask tickTask;
    
    private Creature boss;
    private Deque<SpawnEntry> spawnQueue;
    
    public DimensionSpawner(Location spawnerLoc) {
        this.spawnQueue = new LinkedList<SpawnEntry>();
        this.location = spawnerLoc;
        this.random = new Random();
        this.health = 100;
        this.tickTask = Bukkit.getServer().getScheduler().runTaskTimer(InvasionPlugin.i(), new Runnable() {

            @Override
            public void run() {
                tickHealth();
            }
            
        }, 0, 120);
        
    }
    
    public void queueSpawn(EntityType type, int amount) {
        this.spawnQueue.add(new SpawnEntry(type,amount));
    }
    
    public void tickSpawn() {
        SpawnEntry next = this.spawnQueue.poll();
        for (int i = 0; i < next.amount; i++) {
            Location spawnLoc = this.location.clone().add(random.nextInt(16), random.nextInt(16), random.nextInt(16));
            spawnLoc.getWorld().spawnEntity(spawnLoc.getWorld().getHighestBlockAt(spawnLoc).getLocation().add(0,1,0), next.type);
        }
    }
    
    public void tickHealth() {
        boolean close = false;
        for (Player p : this.location.getWorld().getPlayers()) {
            if (p.getLocation().distanceSquared(this.location) <= 256) {
                close = true;
                break;
            }
        }
        if (close) {
            if (this.health != 1 && this.health != -1) {
                this.health -= 1;
            } else if (this.health == -1) {
                if (this.boss != null && this.boss.isDead()) {
                    this.location.getBlock().setType(Material.AIR);
                    this.location.getWorld().createExplosion(this.location, 0.0F, false);
                }
            } else {
                this.health = -1;
                
            }
        } else {
            if (this.health == 1) {
                return;
            } else {
                this.health += 2;
                if (this.health > 1) {
                    this.health = 1;
                }
            }
            
        }
        
    }
    
    private class SpawnEntry {
        private EntityType type;
        private int amount;

        public SpawnEntry(EntityType type, int amount) {
            this.type = type;
            this.amount = amount;
        }
    }
    
}
