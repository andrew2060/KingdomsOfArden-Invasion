package net.kingdomsofarden.andrew2060.invasion.monsters.nms.goals;

import java.util.ArrayList;

import org.bukkit.craftbukkit.v1_6_R3.entity.CraftCreature;
import org.bukkit.entity.Creature;
import org.bukkit.entity.LivingEntity;

import net.kingdomsofarden.andrew2060.invasion.api.mobskills.MobTargetSelectorAction;
import net.minecraft.server.v1_6_R3.EntityCreature;
import net.minecraft.server.v1_6_R3.PathfinderGoalTarget;

public class PathfinderGoalMobSkillTargetting extends PathfinderGoalTarget {

    private Creature creature;
    private ArrayList<MobTargetSelectorAction> targettingActions;

    public PathfinderGoalMobSkillTargetting(Creature creature, ArrayList<MobTargetSelectorAction> targettingActions) {
        super((EntityCreature) ((CraftCreature)creature).getHandle(), false);
        this.targettingActions = targettingActions;
        this.creature = creature;
    }

    @Override
    public boolean a() {
        return true;
    }
    
    @Override
    public void c() {
        for(MobTargetSelectorAction targettingAction : targettingActions) {
            if(targettingAction.canDetermineTarget(creature)) {
                LivingEntity target = targettingAction.determineTarget(creature);
                if(target != null) {
                    creature.setTarget(target);
                    break;
                } else {
                    continue;
                }
            } else {
                continue;
            }
        }
    }

}
