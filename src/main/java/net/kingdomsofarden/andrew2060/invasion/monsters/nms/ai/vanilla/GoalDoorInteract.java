package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.minecraft.server.v1_7_R4.Block;
import net.minecraft.server.v1_7_R4.BlockDoor;
import net.minecraft.server.v1_7_R4.Blocks;
import net.minecraft.server.v1_7_R4.EntityInsentient;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.Navigation;
import net.minecraft.server.v1_7_R4.PathEntity;
import net.minecraft.server.v1_7_R4.PathPoint;

public abstract class GoalDoorInteract extends Goal {
    protected EntityInsentient a;
    protected int b;
    protected int c;
    protected int d;
    protected BlockDoor e;
    boolean f;
    float g;
    float h;

    public GoalDoorInteract(EntityInsentient entityinsentient) {
        this.a = entityinsentient;
    }

    @Override
    public boolean shouldStart() {
        if (!this.a.positionChanged) {
            return false;
        } else {
            Navigation navigation = this.a.getNavigation();
            PathEntity pathentity = navigation.e();

            if (pathentity != null && !pathentity.b() && navigation.c()) {
                for (int i = 0; i < Math.min(pathentity.e() + 2, pathentity.d()); ++i) {
                    PathPoint pathpoint = pathentity.a(i);

                    this.b = pathpoint.a;
                    this.c = pathpoint.b + 1;
                    this.d = pathpoint.c;
                    if (this.a.e((double) this.b, this.a.locY, (double) this.d) <= 2.25D) {
                        this.e = this.a(this.b, this.c, this.d);
                        if (this.e != null) {
                            return true;
                        }
                    }
                }

                this.b = MathHelper.floor(this.a.locX);
                this.c = MathHelper.floor(this.a.locY + 1.0D);
                this.d = MathHelper.floor(this.a.locZ);
                this.e = this.a(this.b, this.c, this.d);
                return this.e != null;
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        return !this.f;
    }

    @Override
    public void start() {
        this.f = false;
        this.g = (float) ((double) ((float) this.b + 0.5F) - this.a.locX);
        this.h = (float) ((double) ((float) this.d + 0.5F) - this.a.locZ);
    }

    @Override
    public void tick() {
        float f = (float) ((double) ((float) this.b + 0.5F) - this.a.locX);
        float f1 = (float) ((double) ((float) this.d + 0.5F) - this.a.locZ);
        float f2 = this.g * f + this.h * f1;

        if (f2 < 0.0F) {
            this.f = true;
        }
    }

    private BlockDoor a(int i, int j, int k) {
        Block block = this.a.world.getType(i, j, k);

        return block != Blocks.WOODEN_DOOR ? null : (BlockDoor) block;
    }
}
