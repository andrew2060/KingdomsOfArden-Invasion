package net.kingdomsofarden.andrew2060.invasion.util;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import net.kingdomsofarden.andrew2060.invasion.InvasionPlugin;

public class InvasionSettings {
    
    private static Random randGen;
    private static InvasionSettings instance;
    
    private InvasionPlugin plugin;
    
    private long bossSkillCooldown;
    
    private double healthMultiplier;
    private double damageMultiplier;
    private double expMultiplier;
    private HashMap<World,Long> blocksPerScaleMultiplier;
    
    public InvasionSettings(InvasionPlugin plugin) {
        instance = this;
        randGen = new SecureRandom();
        this.plugin = plugin;
        loadConfig();
    }
    
    private void loadConfig() {
        FileConfiguration config = this.plugin.getConfig();
        config.options().copyDefaults(true);
        
        this.setHealthMultiplier(config.getDouble("mobs.healthMultiplier", 1.33));
        this.setDamageMultiplier(config.getDouble("mobs.damageMultiplier", 1.25));
        this.setExpMultiplier(config.getDouble("mobs.expMultiplier", 1.2));
        this.setBossSkillCooldown(config.getLong("mobs.skillCooldown",5000));
        
        this.blocksPerScaleMultiplier = new HashMap<World,Long>();
        
        for(String s : config.getConfigurationSection("mobs.scalePerXBlocks").getValues(false).keySet()) {
            World w = Bukkit.getServer().getWorld(s);
            if(w == null) {
                this.plugin.getLogger().log(Level.SEVERE, "Configuration Error: World " + s + " not found!");
                continue;
            } else {
                blocksPerScaleMultiplier.put(w,config.getLong("mobs.scalePerXBlocks." + s));
            }
        }
        
        this.plugin.saveConfig();
    }
    
    public static InvasionSettings get() {
        return instance;
    }
    
    public static Random rand() {
        return randGen;
    }
    
    public static boolean randChance(double d) {
        return randGen.nextDouble() <= d;
    }

    public long getBossSkillCooldown() {
        return bossSkillCooldown;
    }

    public void setBossSkillCooldown(long bossSkillCooldown) {
        this.bossSkillCooldown = bossSkillCooldown;
    }

    public double getHealthMultiplier() {
        return healthMultiplier;
    }

    public void setHealthMultiplier(double healthMultiplier) {
        this.healthMultiplier = healthMultiplier;
    }

    public double getDamageMultiplier() {
        return damageMultiplier;
    }

    public void setDamageMultiplier(double damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }

    public double getExpMultiplier() {
        return expMultiplier;
    }

    public void setExpMultiplier(double expMultiplier) {
        this.expMultiplier = expMultiplier;
    }
    
    
}
