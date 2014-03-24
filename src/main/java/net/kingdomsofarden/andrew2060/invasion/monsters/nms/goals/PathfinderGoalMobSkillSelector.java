package net.kingdomsofarden.andrew2060.invasion.monsters.nms.goals;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.Random;

import org.bukkit.entity.Creature;

import net.kingdomsofarden.andrew2060.invasion.api.mobskills.MobAction;
import net.kingdomsofarden.andrew2060.invasion.util.InvasionSettings;

public class PathfinderGoalMobSkillSelector extends PathfinderGoalDeobfuscated {
    
    private long lastSkillExecution;
    private ArrayList<MobAction> allActions;
    private Iterator<MobAction> actionIterator;
    private Creature mob;
    private Deque<MobAction> selectedActions;
    private MobAction perform;
    private long bossSkillCooldown;
    private Random randGen;
    
    public PathfinderGoalMobSkillSelector(Creature mob, ArrayList<MobAction> actions) {
        this.mob = mob;
        this.lastSkillExecution = 0;
        this.allActions = actions;
        this.actionIterator = this.allActions.iterator();
        this.selectedActions = new ArrayDeque<MobAction>();
        this.perform = null;
        this.bossSkillCooldown = InvasionSettings.get().getBossSkillCooldown();
        this.randGen = new Random();
    }
    
    @Override
    public boolean canAddGoalToQueue() {  
        if(System.currentTimeMillis() - lastSkillExecution < bossSkillCooldown) {
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
        if(!this.mob.isValid()) { //Cleanup
            this.selectedActions.clear();
            this.selectedActions = null;
            this.perform = null;
            this.allActions = null;
            return false;
        }
        if(System.currentTimeMillis() < this.lastSkillExecution + this.bossSkillCooldown) {
            return false;
        }
        this.perform = this.selectedActions.poll();
        while(perform != null) {
            if(perform.checkUsable(this.mob)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }
    
    @Override
    public void tickGoal() {
        this.perform.tick(mob);
        this.lastSkillExecution = System.currentTimeMillis();
    }

    public ArrayList<MobAction> getActions() {
        return this.allActions;
    }

    @Override
    public void setupGoal() {
        if(this.selectedActions.size() >= 10) { //Only queue up to a maximum of 10 actions
            return; 
        }
        int toQueue = 10 - this.selectedActions.size();
        int fullIteration = this.allActions.size();
        while(toQueue > 0 && fullIteration > 0) {
            if(!this.actionIterator.hasNext()) {
                this.actionIterator = allActions.iterator();
            } 
            MobAction next = this.actionIterator.next();
            fullIteration--;
            if(this.randGen.nextDouble() < next.getQueueChance()) {
                if(next.addToQueue(this.mob)) {
                    toQueue--;
                    this.selectedActions.add(next);
                }
            }
        }
        
    }
}
