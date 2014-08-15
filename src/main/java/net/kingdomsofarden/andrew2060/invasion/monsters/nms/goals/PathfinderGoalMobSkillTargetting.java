package net.kingdomsofarden.andrew2060.invasion.monsters.nms.goals;

import java.util.ArrayList;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftCreature;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;

import net.kingdomsofarden.andrew2060.invasion.api.mobskills.MobTargetSelectorAction;
import net.minecraft.server.v1_7_R4.EntityCreature;

public class PathfinderGoalMobSkillTargetting extends PathfinderGoalTargetDeobfuscated {

    private Creature creature;
    private ArrayList<MobTargetSelectorAction> targettingActions;

    public PathfinderGoalMobSkillTargetting(Creature creature, ArrayList<MobTargetSelectorAction> targettingActions) {
        super((EntityCreature) ((CraftCreature)creature).getHandle(), false);
        this.targettingActions = targettingActions;
        this.creature = creature;
    }
    
    @Override
    public void setupGoal() {
        for (MobTargetSelectorAction targettingAction : targettingActions) {
            if (targettingAction.canDetermineTarget(creature)) {
                LivingEntity target = targettingAction.determineTarget(creature);
                if (target != null) {
                    creature.setTarget(target);
                    break;
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
        super.c();
    }

    @Override
    public boolean canAddGoalToQueue() {
        return true; //Highest priority
    }

}
