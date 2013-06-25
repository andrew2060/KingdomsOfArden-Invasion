package net.swagserv.andrew2060.invasion;

import net.swagserv.andrew2060.invasion.entities.listeners.GiantListener;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class InvasionPlugin extends JavaPlugin {
    private MobManager mobmanager;

    public void onEnable() {
        this.mobmanager = new MobManager(this);
        
        //Register Invasion Monster Listeners
        Bukkit.getPluginManager().registerEvents(new GiantListener(mobmanager), this);
    }
}
