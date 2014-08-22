package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla;

import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.minecraft.server.v1_7_R4.EntityIronGolem;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.Village;

import java.util.UUID;

public class GoalDefendVillage extends GoalTarget {
    EntityIronGolem a;
    EntityLiving b;

    public GoalDefendVillage(EntityIronGolem entityirongolem) {
        super(entityirongolem, false, true);
        this.a = entityirongolem;
    }

    @Override
    public GoalType getType() {
        return GoalType.ONE;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("1bd97110-251a-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        Village village = this.a.bZ();

        if (village == null) {
            return false;
        } else {
            this.b = village.b((EntityLiving) this.a);
            if (!this.a(this.b, false)) {
                if (this.c.aI().nextInt(20) == 0) {
                    this.b = village.c(this.a);
                    return this.a(this.b, false);
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    @Override
    public void start() {
        this.a.setGoalTarget(this.b);
        super.start();
    }

    @Override
    public void tick() {

    }
}
