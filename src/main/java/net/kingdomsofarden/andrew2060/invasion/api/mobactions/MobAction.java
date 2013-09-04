package net.kingdomsofarden.andrew2060.invasion.api.mobactions;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public abstract class MobAction {
    
    private EntityType[] applyToTypes;
    
    public MobAction(EntityType[] types) {
        this.applyToTypes = types;
    }
    /**
     * Gets the array of valid  
     * @param type Bukkit Class of the mob type that can perform this action, must be insentinent and extend LivingEntity
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
    public abstract boolean checkUsable(LivingEntity executor);
    
    /**
     * Is fired when the action itself is performed
     * @param executor The mob performing the action
     */
    public abstract void tick(LivingEntity executor);
    
    
}
