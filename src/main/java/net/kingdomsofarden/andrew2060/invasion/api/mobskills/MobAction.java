package net.kingdomsofarden.andrew2060.invasion.api.mobskills;

import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;

public abstract class MobAction {
    
    private EntityType[] applyToTypes;
    
    public MobAction(EntityType... types) {
        this.applyToTypes = types;
    }
    /**
     * Gets the array of valid EntityTypes to which this action pertains  
     */
    public final EntityType[] getMobTypes() {
        return this.applyToTypes;
    }
    /**
     * Is fired prior to the action being performed, if false is returned, skips to the action
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
