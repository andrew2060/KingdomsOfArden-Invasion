package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityInsentient;

import java.util.UUID;

public class GoalLookAtPlayer extends Goal {
    private EntityInsentient b;
    protected Entity a;
    private float c;
    private int d;
    private float e;
    private Class f;

    public GoalLookAtPlayer(EntityInsentient entityinsentient, Class oclass, float f) {
        this.b = entityinsentient;
        this.f = oclass;
        this.c = f;
        this.e = 0.02F;
    }

    public GoalLookAtPlayer(EntityInsentient entityinsentient, Class oclass, float f, float f1) {
        this.b = entityinsentient;
        this.f = oclass;
        this.c = f;
        this.e = f1;
    }

    @Override
    public boolean shouldStart() {
        if (this.b.aI().nextFloat() >= this.e) {
            return false;
        } else {
            if (this.b.getGoalTarget() != null) {
                this.a = this.b.getGoalTarget();
            }

            if (this.f == EntityHuman.class) {
                this.a = this.b.world.findNearbyPlayer(this.b, (double) this.c);
            } else {
                this.a = this.b.world.a(this.f, this.b.boundingBox.grow((double) this.c, 3.0D, (double) this.c), (Entity) this.b);
            }

            return this.a != null;
        }
    }

    @Override
    public boolean shouldContinue() {
        return !this.a.isAlive() ? false : (this.b.f(this.a) > (double) (this.c * this.c) ? false : this.d > 0);
    }

    @Override
    public void start() {
        this.d = 40 + this.b.aI().nextInt(40);
    }

    @Override
    public void finish() {
        this.a = null;
    }

    @Override
    public void tick() {
        this.b.getControllerLook().a(this.a.locX, this.a.locY + (double) this.a.getHeadHeight(), this.a.locZ, 10.0F, (float) this.b.x());
        --this.d;
    }

    @Override
    public GoalType getType() {
        return GoalType.TWO;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("39a36d90-24c0-11e4-8c21-0800200c9a66");
    }
}
