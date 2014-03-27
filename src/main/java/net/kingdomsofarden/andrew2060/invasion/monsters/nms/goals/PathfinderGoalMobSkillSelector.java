package net.kingdomsofarden.andrew2060.invasion.monsters.nms.goals;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Creature;

import net.minecraft.util.com.google.common.cache.CacheBuilder;
import net.minecraft.util.com.google.common.cache.CacheLoader;
import net.minecraft.util.com.google.common.cache.LoadingCache;
import net.kingdomsofarden.andrew2060.invasion.api.mobskills.MobAction;
import net.kingdomsofarden.andrew2060.invasion.util.InvasionSettings;

public class PathfinderGoalMobSkillSelector extends PathfinderGoalDeobfuscated {
    
    private long lastSkillExecution;
    private Deque<MobAction> allActions;
    private Creature mob;
    private MobAction perform;
    private long bossSkillCooldown;
    private Random randGen;
    private LoadingCache<MobAction,Long> failedLaunchTimeout;
    
    public PathfinderGoalMobSkillSelector(Creature mob, ArrayList<MobAction> actions) {
        this.mob = mob;
        this.lastSkillExecution = 0;
        this.allActions = new ArrayDeque<MobAction>(actions);
        this.perform = null;
        this.bossSkillCooldown = InvasionSettings.get().getBossSkillCooldown();
        this.randGen = new Random();
        this.failedLaunchTimeout = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build(new CacheLoader<MobAction,Long>() {

                    @Override
                    public Long load(MobAction action) {
                        return System.currentTimeMillis() + action.getFailChecksCooldown();
                    }
                    
                });
    }
    
    @Override
    public boolean canAddGoalToQueue() {  
        if(System.currentTimeMillis() - this.lastSkillExecution < this.bossSkillCooldown) {
            return false;
        } else if (mob.getTarget() == null) {
            return false;
        } else if (allActions.size() <= 0) {
            return false;
        } else {
            return true;
        }
        
    }
    
    //Selects an action to execute
    @Override
    public boolean canTickGoal() {
        if(this.perform == null) {
            return false;
        } else if(!this.perform.checkUsable(this.mob)) {
            this.failedLaunchTimeout.put(this.perform, System.currentTimeMillis());
            return false;
        }
        return true;
    }
    
    @Override
    public void tickGoal() {
        this.perform.tick(mob);
        this.lastSkillExecution = System.currentTimeMillis();
    }

    public Deque<MobAction> getActions() {
        return this.allActions;
    }

    @Override
    public void setupGoal() {
        this.perform = this.allActions.poll();
        this.allActions.add(this.perform);
        if(this.failedLaunchTimeout.getIfPresent(this.perform) != null) {
            if(System.currentTimeMillis() <= this.failedLaunchTimeout.getUnchecked(this.perform)) {
                return;
            }
        } 
        if(randGen.nextDouble() <= perform.getQueueChance()) {
            return;
        } else {
            this.perform = null;
            return;
        }
    }
}
