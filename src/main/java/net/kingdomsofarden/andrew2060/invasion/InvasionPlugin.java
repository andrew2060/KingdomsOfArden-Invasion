package net.kingdomsofarden.andrew2060.invasion;

import net.kingdomsofarden.andrew2060.invasion.monsters.listeners.CreatureSpawnListener;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.goals.MobGoalManager;

import org.bukkit.plugin.java.JavaPlugin;

public class InvasionPlugin extends JavaPlugin {
    
    private MobManager mobManager;
    private MobGoalManager mobGoalManager;
    public static InvasionPlugin instance;
    
    public void onEnable() {
        instance = this;
        //Initialize the mob managers
        this.setMobGoalManager(new MobGoalManager(this));
        this.mobManager = new MobManager(this, mobGoalManager);
        //Register Listeners
        this.getServer().getPluginManager().registerEvents(new CreatureSpawnListener(mobManager), this);
    }

    public MobGoalManager getMobmanager() {
        return mobGoalManager;
    }

    public void setMobGoalManager(MobGoalManager mobmanager) {
        this.mobGoalManager = mobmanager;
    }
}
