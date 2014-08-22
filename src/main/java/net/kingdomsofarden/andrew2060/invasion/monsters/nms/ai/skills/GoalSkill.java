package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.skills;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;

public abstract class GoalSkill extends Goal {
    public abstract boolean offCooldown();
    public abstract double queueChance();
}
