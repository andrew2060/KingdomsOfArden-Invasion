package net.kingdomsofarden.andrew2060.invasion.monsters.nms.goals;

import net.minecraft.server.v1_7_R4.PathfinderGoal;

/**
 * Convenience class that shows deobfuscated mappings for pathfindergoal logic 
 * 
 * @author Andrew
 *
 */
public abstract class PathfinderGoalDeobfuscated extends PathfinderGoal {

    @Override
    public final boolean a() {
        return shouldStart();
    }
    
    @Override
    public final boolean b() {
        return shouldContinue();
    }
    
    @Override
    public final void c() {
        start();
    }
    
    @Override
    public final void d() {
        finish();
    }
    
    @Override
    public final void e() {
        tick();
    }
    
    public abstract boolean shouldStart();
    public abstract boolean shouldContinue();
    public abstract void start();
    public abstract void finish();
    public void tick() {}
    
    
}
