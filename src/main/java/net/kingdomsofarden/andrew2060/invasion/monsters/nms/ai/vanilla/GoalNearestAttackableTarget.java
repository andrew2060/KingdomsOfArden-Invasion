package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla;


import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.util.EntitySelectorNearestAttackableTarget;
import net.minecraft.server.v1_7_R4.DistanceComparator;
import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.IEntitySelector;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class GoalNearestAttackableTarget extends GoalTarget {
    private final Class a;
    private final int b;
    private final DistanceComparator e;
    private final IEntitySelector f;
    private EntityLiving g;

    public GoalNearestAttackableTarget(EntityCreature entitycreature, Class oclass, int i, boolean flag) {
        this(entitycreature, oclass, i, flag, false);
    }

    public GoalNearestAttackableTarget(EntityCreature entitycreature, Class oclass, int i, boolean flag, boolean flag1) {
        this(entitycreature, oclass, i, flag, flag1, (IEntitySelector) null);
    }

    public GoalNearestAttackableTarget(EntityCreature entitycreature, Class oclass, int i, boolean flag, boolean flag1, IEntitySelector ientityselector) {
        super(entitycreature, flag, flag1);
        this.a = oclass;
        this.b = i;
        this.e = new DistanceComparator(entitycreature);
        this.f = new EntitySelectorNearestAttackableTarget(this, ientityselector);
    }

    @Override
    public GoalType getType() {
        return GoalType.ONE;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("78690b70-25a1-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        if (this.b > 0 && this.c.aI().nextInt(this.b) != 0) {
            return false;
        } else {
            double d0 = this.f();
            List list = this.c.world.a(this.a, this.c.boundingBox.grow(d0, 4.0D, d0), this.f);

            Collections.sort(list, this.e);
            if (list.isEmpty()) {
                return false;
            } else {
                this.g = (EntityLiving) list.get(0);
                return true;
            }
        }
    }

    public void start() {
        this.c.setGoalTarget(this.g);
        super.start();
    }

    @Override
    public void tick() {

    }
}
