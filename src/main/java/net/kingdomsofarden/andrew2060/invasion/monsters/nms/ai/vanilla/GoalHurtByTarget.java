package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla;

import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.minecraft.server.v1_7_R4.AxisAlignedBB;
import net.minecraft.server.v1_7_R4.EntityCreature;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R4.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class GoalHurtByTarget extends GoalTarget {

    boolean a;
    private int b;

    public GoalHurtByTarget(EntityCreature entitycreature, boolean flag) {
        super(entitycreature, false);
        this.a = flag;
    }

    @Override
    public GoalType getType() {
        return GoalType.ONE;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("a044d440-250f-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        int i = this.c.aK();

        return i != this.b && this.a(this.c.getLastDamager(), false);
    }

    @Override
    public void start() {
        this.c.setGoalTarget(this.c.getLastDamager());
        this.b = this.c.aK();
        if (this.a) {
            double d0 = this.f();
            List list = this.c.world.a(this.c.getClass(), AxisAlignedBB.a(this.c.locX, this.c.locY, this.c.locZ, this.c.locX + 1.0D, this.c.locY + 1.0D, this.c.locZ + 1.0D).grow(d0, 10.0D, d0));
            Iterator iterator = list.iterator();

            while (iterator.hasNext()) {
                EntityCreature entitycreature = (EntityCreature) iterator.next();

                if (this.c != entitycreature && entitycreature.getGoalTarget() == null && !entitycreature.c(this.c.getLastDamager())) {
                    // CraftBukkit start - call EntityTargetEvent
                    EntityTargetLivingEntityEvent event = CraftEventFactory.callEntityTargetLivingEvent(entitycreature, this.c.getLastDamager(), org.bukkit.event.entity.EntityTargetEvent.TargetReason.TARGET_ATTACKED_NEARBY_ENTITY);
                    if (event.isCancelled()) {
                        continue;
                    }
                    entitycreature.setGoalTarget(event.getTarget() == null ? null : ((CraftLivingEntity) event.getTarget()).getHandle());
                    // CraftBukkit end
                }
            }
        }

        super.start();
    }

    @Override
    public void tick() {

    }
}
