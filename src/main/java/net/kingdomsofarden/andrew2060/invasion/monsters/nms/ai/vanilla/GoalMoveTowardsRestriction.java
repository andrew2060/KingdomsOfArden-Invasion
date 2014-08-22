package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.minecraft.server.v1_7_R4.ChunkCoordinates;
import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.RandomPositionGenerator;
import net.minecraft.server.v1_7_R4.Vec3D;

import java.util.UUID;

public class GoalMoveTowardsRestriction extends Goal {
    private EntityCreature a;
    private double b;
    private double c;
    private double d;
    private double e;

    public GoalMoveTowardsRestriction(EntityCreature entitycreature, double d0) {
        this.a = entitycreature;
        this.e = d0;
    }

    @Override
    public GoalType getType() {
        return GoalType.ONE;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("cdd86e01-2459-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        if (this.a.bU()) {
            return false;
        } else {
            ChunkCoordinates chunkcoordinates = this.a.bV();
            Vec3D vec3d = RandomPositionGenerator.a(this.a, 16, 7,
                    Vec3D.a((double) chunkcoordinates.x, (double) chunkcoordinates.y, (double) chunkcoordinates.z));

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
}
