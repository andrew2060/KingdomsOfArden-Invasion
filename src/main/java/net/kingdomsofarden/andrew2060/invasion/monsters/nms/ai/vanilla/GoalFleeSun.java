package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.Vec3D;
import net.minecraft.server.v1_7_R4.World;

import java.util.Random;
import java.util.UUID;

public class GoalFleeSun extends Goal {
    private EntityCreature a;
    private double b;
    private double c;
    private double d;
    private double e;
    private World f;

    public GoalFleeSun(EntityCreature entitycreature, double d0) {
        this.a = entitycreature;
        this.e = d0;
        this.f = entitycreature.world;
    }

    @Override
    public GoalType getType() {
        return GoalType.ONE;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("847b4a90-2588-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        if (!this.f.w()) {
            return false;
        } else if (!this.a.isBurning()) {
            return false;
        } else if (!this.f.i(MathHelper.floor(this.a.locX), (int) this.a.boundingBox.b, MathHelper.floor(this.a.locZ))) {
            return false;
        } else {
            Vec3D vec3d = this.f();

            if (vec3d == null) {
                return false;
            } else {
                this.b = vec3d.a;
                this.c = vec3d.b;
                this.d = vec3d.c;
                return true;
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        return !this.a.getNavigation().g();
    }

    @Override
    public void start() {
        this.a.getNavigation().a(this.b, this.c, this.d, this.e);
    }

    @Override
    public void tick() {

    }

    private Vec3D f() {
        Random random = this.a.aI();

        for (int i = 0; i < 10; ++i) {
            int j = MathHelper.floor(this.a.locX + (double) random.nextInt(20) - 10.0D);
            int k = MathHelper.floor(this.a.boundingBox.b + (double) random.nextInt(6) - 3.0D);
            int l = MathHelper.floor(this.a.locZ + (double) random.nextInt(20) - 10.0D);

            if (!this.f.i(j, k, l) && this.a.a(j, k, l) < 0.0F) {
                return Vec3D.a((double) j, (double) k, (double) l);
            }
        }

        return null;
    }
}
