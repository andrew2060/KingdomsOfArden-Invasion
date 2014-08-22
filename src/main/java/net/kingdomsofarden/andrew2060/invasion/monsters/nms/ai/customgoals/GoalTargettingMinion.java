package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.customgoals;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.minecraft.server.v1_7_R4.EntityInsentient;

import java.util.UUID;

public class GoalTargettingMinion extends Goal {

    private EntityInsentient entity;
    private EntityInsentient eL;

    public GoalTargettingMinion(EntityInsentient owner, EntityInsentient entity) {
        this.eL = owner;
        this.entity = entity;
    }

    @Override
    public GoalType getType() {
        return GoalType.ZERO;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("7a9ca980-263f-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        if (!(this.eL.isAlive() && this.eL.valid)) {
            this.entity.die();
            return false;
        }
        return true;
    }

    @Override
    public void tick() {
        if (this.entity.getGoalTarget() != this.eL.getGoalTarget()) {
            this.entity.setGoalTarget(this.eL.getGoalTarget());
        }
    }
}
