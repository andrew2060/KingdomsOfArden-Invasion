package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.minecraft.server.v1_7_R4.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class GoalMoveThroughVillage extends Goal {
    private EntityCreature a;
    private double b;
    private PathEntity c;
    private VillageDoor d;
    private boolean e;
    private List f = new ArrayList();

    public GoalMoveThroughVillage(EntityCreature entitycreature, double d0, boolean flag) {
        this.a = entitycreature;
        this.b = d0;
        this.e = flag;
    }

    @Override
    public boolean shouldStart() {
        this.f();
        if (this.e && this.a.world.w()) {
            return false;
        } else {
            Village village = this.a.world.villages.getClosestVillage(MathHelper.floor(this.a.locX), MathHelper.floor(this.a.locY), MathHelper.floor(this.a.locZ), 0);

            if (village == null) {
                return false;
            } else {
                this.d = this.a(village);
                if (this.d == null) {
                    return false;
                } else {
                    boolean flag = this.a.getNavigation().c();

                    this.a.getNavigation().b(false);
                    this.c = this.a.getNavigation().a((double) this.d.locX, (double) this.d.locY, (double) this.d.locZ);
                    this.a.getNavigation().b(flag);
                    if (this.c != null) {
                        return true;
                    } else {
                        Vec3D vec3d = RandomPositionGenerator.a(this.a, 10, 7, Vec3D.a((double) this.d.locX, (double) this.d.locY, (double) this.d.locZ));

                        if (vec3d == null) {
                            return false;
                        } else {
                            this.a.getNavigation().b(false);
                            this.c = this.a.getNavigation().a(vec3d.a, vec3d.b, vec3d.c);
                            this.a.getNavigation().b(flag);
                            return this.c != null;
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean shouldContinue() {
        if (this.a.getNavigation().g()) {
            return false;
        } else {
            float f = this.a.width + 4.0F;

            return this.a.e((double) this.d.locX, (double) this.d.locY, (double) this.d.locZ) > (double) (f * f);
        }
    }

    @Override
    public void start() {
        this.a.getNavigation().a(this.c, this.b);
    }

    @Override
    public void finish() {
        if (this.a.getNavigation().g() || this.a.e((double) this.d.locX, (double) this.d.locY, (double) this.d.locZ) < 16.0D) {
            this.f.add(this.d);
        }
    }

    private VillageDoor a(Village village) {
        VillageDoor villagedoor = null;
        int i = Integer.MAX_VALUE;
        List list = village.getDoors();
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            VillageDoor villagedoor1 = (VillageDoor) iterator.next();
            int j = villagedoor1.b(MathHelper.floor(this.a.locX), MathHelper.floor(this.a.locY), MathHelper.floor(this.a.locZ));

            if (j < i && !this.a(villagedoor1)) {
                villagedoor = villagedoor1;
                i = j;
            }
        }

        return villagedoor;
    }

    private boolean a(VillageDoor villagedoor) {
        Iterator iterator = this.f.iterator();

        VillageDoor villagedoor1;

        do {
            if (!iterator.hasNext()) {
                return false;
            }

            villagedoor1 = (VillageDoor) iterator.next();
        } while (villagedoor.locX != villagedoor1.locX || villagedoor.locY != villagedoor1.locY || villagedoor.locZ != villagedoor1.locZ);

        return true;
    }

    private void f() {
        if (this.f.size() > 15) {
            this.f.remove(0);
        }
    }

    @Override
    public GoalType getType() {
        return GoalType.ONE;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("b00d5150-24b9-11e4-8c21-0800200c9a66");
    }


    @Override
    public void tick() {
        return;
    }
}
