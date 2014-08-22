package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.minecraft.server.v1_7_R4.EntityInsentient;

import java.util.UUID;

public class GoalRandomLookaround extends Goal {
    private EntityInsentient a;
    private double b;
    private double c;
    private int d;

    public GoalRandomLookaround(EntityInsentient entityinsentient) {
        this.a = entityinsentient;
    }

    @Override
    public GoalType getType() {
        return GoalType.THREE;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("e84cb720-24c0-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        return this.a.aI().nextFloat() < 0.02F;
    }

    @Override
    public boolean shouldContinue() {
        return this.d >= 0;
    }

    @Override
    public void start() {
        double d0 = 6.283185307179586D * this.a.aI().nextDouble();

        this.b = Math.cos(d0);
        this.c = Math.sin(d0);
        this.d = 20 + this.a.aI().nextInt(20);
    }

    @Override
    public void finish() {
        --this.d;
        this.a.getControllerLook().a(this.a.locX + this.b, this.a.locY + (double) this.a.getHeadHeight(), this.a.locZ + this.c, 10.0F, (float) this.a.x());
    }

    @Override
    public void tick() {

    }
}
