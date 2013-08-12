package net.kingdomsofarden.andrew2060.invasion;

import net.kingdomsofarden.andrew2060.invasion.monsters.listeners.GiantListener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class InvasionPlugin extends JavaPlugin {
    private MobManager mobmanager;
    public static InvasionPlugin instance;
    
    public void onEnable() {
        this.mobmanager = new MobManager(this);
        instance = this;
        //Register Invasion Monster Listeners
        Bukkit.getPluginManager().registerEvents(new GiantListener(mobmanager), this);
    }
}
