package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.minecraft.server.v1_7_R4.EntityCreature;

import java.util.UUID;

public class GoalRestrictSun extends Goal {
    private EntityCreature a;

    public GoalRestrictSun(EntityCreature entitycreature) {
        this.a = entitycreature;
    }

    @Override
    public GoalType getType() {
        return GoalType.ZERO;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("4ed89990-258a-11e4-8c21-0800200c9a6");
    }

    @Override
    public boolean shouldStart() {
        return this.a.world.w();
    }

    @Override
    public void start() {
        this.a.getNavigation().d(true);
    }

    @Override
    public void finish() {
        this.a.getNavigation().d(false);
    }

    @Override
    public void tick() {

    }
}
