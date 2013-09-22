package net.kingdomsofarden.andrew2060.invasion.api.mobskills.bundledTargetting;

import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import net.kingdomsofarden.andrew2060.invasion.api.MobMinionManager;
import net.kingdomsofarden.andrew2060.invasion.api.mobskills.MobTargetSelectorAction;
import net.minecraft.server.v1_6_R3.PathfinderGoalHurtByTarget;

public class MobTargetSelectorActionRetaliateNoUndeadFriendlyFire extends MobTargetSelectorAction {

    public MobTargetSelectorActionRetaliateNoUndeadFriendlyFire() {
        super(EntityType.GIANT, EntityType.SKELETON, EntityType.ZOMBIE);
        this.setReplaces(PathfinderGoalHurtByTarget.class);
    }

    @Override
    public boolean canDetermineTarget(Creature creature) {
        return !(creature.isValid() && creature.hasLineOfSight(creature.getTarget()) && creature.getTarget().getLocation().distanceSquared(creature.getLocation()) < 1024 && !MobMinionManager.instance.getMinions(creature).contains(creature.getTarget()));
    }

    @Override
    public LivingEntity determineTarget(Creature creature) {
        if(creature.getLastDamageCause().getCause() == DamageCause.ENTITY_ATTACK) {
            Entity damager = ((EntityDamageByEntityEvent)creature.getLastDamageCause()).getDamager();
            if(!(damager instanceof LivingEntity)) {
                return null;
            }
            LivingEntity lE = (LivingEntity)damager;
            switch(lE.getType()) {
            
            case ZOMBIE:
            case SKELETON:
            case SPIDER:
            case PIG_ZOMBIE:
            case ENDERMAN:
            case CREEPER:
                return null;
                
            default:
                return lE;
            
            }
        }
        return null;
    }

}
