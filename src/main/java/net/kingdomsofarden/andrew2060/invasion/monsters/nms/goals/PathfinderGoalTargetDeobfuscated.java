package net.kingdomsofarden.andrew2060.invasion.monsters.nms.goals;

import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.PathfinderGoalTarget;

public abstract class PathfinderGoalTargetDeobfuscated extends PathfinderGoalTarget {

    public PathfinderGoalTargetDeobfuscated(EntityCreature entitycreature, boolean flag) {
        super(entitycreature, flag);
    }

    @Override
    public final boolean a() {
        return canAddGoalToQueue();
    }
    
    @Override
    public final void c() {
        setupGoal();
    }

    public abstract boolean canAddGoalToQueue();

    public void setupGoal() {
        
    }

}
