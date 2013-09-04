package net.kingdomsofarden.andrew2060.invasion.api.mobskills;

import java.util.LinkedList;
import net.minecraft.server.v1_6_R2.PathfinderGoal;

import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public abstract class MobTargetSelectorAction {
    
    private EntityType[] applyToTypes;
    private LinkedList<Class<? extends PathfinderGoal>> replaces;
    public MobTargetSelectorAction(EntityType... types) {
        this.applyToTypes = types;
        this.replaces = new LinkedList<Class<? extends PathfinderGoal>>();
    }
    /**
     * Gets the array of valid EntityTypes this action should be added to 
     * @return An array of EntityTypes
     */
    public final EntityType[] getMobTypes() {
        return this.applyToTypes;
    }
    
    /**
     * Checks for if conditions are met to execute target determination: i.e. checking any preexisting targets to see if overriding should occur
     * @param creature The creature doing the target determination
     * @return whether target determination will occur under this specific action
     */
    public abstract boolean canDetermineTarget(Creature creature);
    
    /**
     * The logic for determining a creature's target.
     * @param creature The creature doing the target determination
     * @return The determined target, or null;
     */
    public abstract LivingEntity determineTarget(Creature creature);
    
    @SafeVarargs
    public final void setReplaces(Class<? extends PathfinderGoal>... replaces) {
        for(Class<? extends PathfinderGoal> toAdd : replaces) {
            this.replaces.add(toAdd);
        }
    }
    
    public final LinkedList<Class<? extends PathfinderGoal>> getReplaces() {
        return this.replaces;
    }
    
}
