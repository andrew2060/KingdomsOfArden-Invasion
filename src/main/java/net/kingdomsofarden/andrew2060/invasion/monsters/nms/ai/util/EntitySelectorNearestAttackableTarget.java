package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.util;

import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla.GoalNearestAttackableTarget;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.IEntitySelector;

public class EntitySelectorNearestAttackableTarget implements IEntitySelector {

    final IEntitySelector c;
    final GoalNearestAttackableTarget d;

    public EntitySelectorNearestAttackableTarget(GoalNearestAttackableTarget goal, IEntitySelector ientityselector) {
        this.d = goal;
        this.c = ientityselector;
    }

    public boolean a(Entity entity) {
        return !(entity instanceof EntityLiving) ? false : (this.c != null && !this.c.a(entity) ? false :
                this.d.a((EntityLiving) entity, false));
    }
}
