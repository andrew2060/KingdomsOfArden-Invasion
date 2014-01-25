package net.kingdomsofarden.andrew2060.invasion.monsters.nms.goals;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.entity.Creature;

import net.kingdomsofarden.andrew2060.invasion.api.mobskills.MobAction;
import net.kingdomsofarden.andrew2060.invasion.util.Config;
import net.minecraft.server.v1_7_R1.PathfinderGoal;

public class PathfinderGoalMobSkillSelector extends PathfinderGoal {
    
    private long lastSkillExecution;
    private ArrayList<MobAction> actions;
    private Random skillSelector;
    private int selectedSkill;
    private Creature mob;
    public PathfinderGoalMobSkillSelector(Creature mob, ArrayList<MobAction> actions) {
        this.mob = mob;
        this.lastSkillExecution = System.currentTimeMillis();
        this.actions = actions;
        this.skillSelector = new Random();
        this.selectedSkill = 0;
    }
    
    @Override
    public boolean a() {  
        if(System.currentTimeMillis() - lastSkillExecution < Config.bossSkillCooldown) {
            return false;
        } else if (mob.getTarget() == null) {
            return false;
        } else {
            this.selectedSkill = this.skillSelector.nextInt(actions.size());
            return mob.isValid() && actions.get(this.selectedSkill).checkUsable(mob);
        }
    }
    
    @Override
    public void e() {
        this.actions.get(this.selectedSkill).tick(mob);
        this.lastSkillExecution = System.currentTimeMillis();
    }

    public ArrayList<MobAction> getActions() {
        return this.actions;
    }
}
