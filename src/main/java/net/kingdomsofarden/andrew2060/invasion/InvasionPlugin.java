package net.kingdomsofarden.andrew2060.invasion;

import net.kingdomsofarden.andrew2060.api.entity.EntityUtil;
import net.kingdomsofarden.andrew2060.invasion.listeners.*;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.entities.monsters.*;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.entities.projectiles.EntityGiantHomingWitherSkull;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.entities.projectiles.EntityGiantWitherSkull;

import net.kingdomsofarden.andrew2060.invasion.rewards.DropHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class InvasionPlugin extends JavaPlugin {

    private static InvasionPlugin instance;
    private DropHandler dropHandler;

    public static final InvasionPlugin i() {
        return instance;
    }

    @Override
    public void onLoad() {
        // Entities
        EntityUtil.registerCustomEntity(EntityInvasionSkeleton.class, "InvasionSkeleton", 51, true);
        EntityUtil.registerCustomEntity(EntityInvasionSpider.class, "InvasionSpider", 52, true);
        EntityUtil.registerCustomEntity(EntityInvasionGiant.class, "InvasionGiant", 53, true);
        EntityUtil.registerCustomEntity(EntityInvasionZombie.class, "InvasionZombie", 54, true);
        EntityUtil.registerCustomEntity(EntityInvasionWitherBoss.class, "InvasionWither", 64, true);

        // Projectiles and Misc
        EntityUtil.registerCustomEntity(EntityGiantHomingWitherSkull.class, "InvasionHomingSkull", 19, false);
        EntityUtil.registerCustomEntity(EntityGiantWitherSkull.class, "InvasionGiantHomingSkull", 19, false);
    }

    @Override
    public void onEnable() {
        instance = this;
        //Register Drops
        this.dropHandler = new DropHandler(this);
        //Register Listeners
        this.getServer().getPluginManager().registerEvents(new DebugListener(), this);
        this.getServer().getPluginManager().registerEvents(new CreatureScaleListener(this), this);
        this.getServer().getPluginManager().registerEvents(new CreatureDensityListener(this), this);
        this.getServer().getPluginManager().registerEvents(new RewardListener(this), this);
        this.getServer().getPluginManager().registerEvents(new TrackerListener(this), this);
    }


    public DropHandler getDropHandler() {
        return dropHandler;
    }
}
