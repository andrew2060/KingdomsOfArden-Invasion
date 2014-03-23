package net.kingdomsofarden.andrew2060.invasion.monsters.nms.goals;

import net.minecraft.server.v1_7_R1.PathfinderGoal;
/**
 * Convenience class that shows deobfuscated mappings for pathfindergoal logic 
 * 
 * @author Andrew
 *
 */
public abstract class PathfinderGoalDeobfuscated extends PathfinderGoal {

    @Override
    public final boolean a() {
        return canAddGoalToQueue();
    }
    
    @Override
    public final boolean b() {
        return canTickGoal();
    }
    
    @Override
    public final void c() {
        setupGoal();
    }
    
    @Override
    public final void d() {
        tickGoal();
    }
    
    @Override
    public final void e() {
        doMobMovement();
    }
    
    public abstract boolean canAddGoalToQueue();
    public abstract boolean canTickGoal();
    public abstract void setupGoal();
    public abstract void tickGoal();
    public void doMobMovement() {}
    
    
}
