package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityInsentient;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.IRangedEntity;
import net.minecraft.server.v1_7_R4.MathHelper;
import org.bukkit.craftbukkit.v1_7_R4.event.CraftEventFactory;

import java.util.UUID;

import org.bukkit.event.entity.EntityTargetEvent.*;

public class GoalArrowAttack extends Goal {
    private final EntityInsentient a;
    private final IRangedEntity b;
    private EntityLiving c;
    private int d;
    private double e;
    private int f;
    private int g;
    private int h;
    private float i;
    private float j;

    public GoalArrowAttack(IRangedEntity irangedentity, double d0, int i, float f) {
        this(irangedentity, d0, i, i, f);
    }

    public GoalArrowAttack(IRangedEntity irangedentity, double d0, int i, int j, float f) {
        this.d = -1;
        if (!(irangedentity instanceof EntityLiving)) {
            throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
        } else {
            this.b = irangedentity;
            this.a = (EntityInsentient) irangedentity;
            this.e = d0;
            this.g = i;
            this.h = j;
            this.i = f;
            this.j = f * f;
        }
    }

    @Override
    public GoalType getType() {
        return GoalType.THREE;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("d81fd250-256b-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        EntityLiving entityliving = this.a.getGoalTarget();

        if (entityliving == null) {
            return false;
        } else {
            this.c = entityliving;
            return true;
        }
    }

    @Override
    public boolean shouldContinue() {
        return this.shouldStart() || !this.a.getNavigation().g();
    }

    @Override
    public void finish() {
        // CraftBukkit start
        TargetReason reason = this.c.isAlive() ? TargetReason.FORGOT_TARGET : TargetReason.TARGET_DIED;
        CraftEventFactory.callEntityTargetEvent((Entity) b, null, reason);
        // CraftBukkit end
        this.c = null;
        this.f = 0;
        this.d = -1;
    }

    @Override
    public void tick() {
        double d0 = this.a.e(this.c.locX, this.c.boundingBox.b, this.c.locZ);
        boolean flag = this.a.getEntitySenses().canSee(this.c);

        if (flag) {
            ++this.f;
        } else {
            this.f = 0;
        }

        if (d0 <= (double) this.j && this.f >= 20) {
            this.a.getNavigation().h();
        } else {
            this.a.getNavigation().a((Entity) this.c, this.e);
        }

        this.a.getControllerLook().a(this.c, 30.0F, 30.0F);
        float f;

        if (--this.d == 0) {
            if (d0 > (double) this.j || !flag) {
                return;
            }

            f = MathHelper.sqrt(d0) / this.i;
            float f1 = f;

            if (f < 0.1F) {
                f1 = 0.1F;
            }

            if (f1 > 1.0F) {
                f1 = 1.0F;
            }

            this.b.a(this.c, f1);
            this.d = MathHelper.d(f * (float) (this.h - this.g) + (float) this.g);
        } else if (this.d < 0) {
            f = MathHelper.sqrt(d0) / this.i;
            this.d = MathHelper.d(f * (float) (this.h - this.g) + (float) this.g);
        }
    }
}
