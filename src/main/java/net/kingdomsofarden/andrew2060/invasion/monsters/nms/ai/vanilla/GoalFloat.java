package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.minecraft.server.v1_7_R4.EntityInsentient;

import java.util.UUID;

public class GoalFloat extends Goal {

    private EntityInsentient entity;

    public GoalFloat(EntityInsentient entity) {
        this.entity = entity;
        entity.getNavigation().e(true);
    }

    @Override
    public GoalType getType() {
        return GoalType.FOUR;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("ff171e10-2456-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        return this.entity.M() || this.entity.P();
    }

    @Override
    public void tick() {
        if (this.entity.aI().nextFloat() < 0.8F) {
            this.entity.getControllerJump().a();
        }
    }
}
