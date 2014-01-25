package net.kingdomsofarden.andrew2060.invasion.api.mobskills;

import net.kingdomsofarden.andrew2060.invasion.InvasionPlugin;

import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;

public abstract class MobAction {
    
    private EntityType[] applyToTypes;
    public InvasionPlugin plugin;
    
    public MobAction(InvasionPlugin plugin) {
        this.applyToTypes = this.setMobTypes();
        this.plugin = plugin;
    }
    
    /** 
     * Used to set types to apply this MobAction to
     * @return The array of valid EntityTypes to which this action pertains  
     */
    public abstract EntityType[] setMobTypes();
    
    /**
     * Gets the types this MobAction applies to
     * @return an array of EntityTypes using this mob action
     */
    public final EntityType[] getMobTypes() {
        return this.applyToTypes;
    }
    
    /**
     * Is fired prior to the action being performed, if false is returned, skips to the next action
     * 
     * @param executor The mob performing the action
     * @return Whether the action can be performed
     */
    public abstract boolean checkUsable(Creature executor);
    
    /**
     * Is fired when the action itself is performed
     * @param executor The mob performing the action
     */
    public abstract void tick(Creature executor);
    
    
}
