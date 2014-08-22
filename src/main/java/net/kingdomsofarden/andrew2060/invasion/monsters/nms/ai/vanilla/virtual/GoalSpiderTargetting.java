package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla.virtual;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.entities.monsters.EntityInvasionSpider;
import net.minecraft.server.v1_7_R4.EntityLiving;
import org.bukkit.util.Vector;

import java.util.UUID;

public class GoalSpiderTargetting extends Goal {

    private final EntityInvasionSpider e;

    public GoalSpiderTargetting(EntityInvasionSpider e) {
        this.e = e;
    }

    @Override
    public GoalType getType() {
        return GoalType.ZERO;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("bece8cd0-26ae-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        EntityLiving target = this.e.getGoalTarget();
        return target == null || new Vector(target.locX, target.locY, target.locZ).distanceSquared(new Vector(
                this.e.locX, this.e.locY, this.e.locZ )) > 324;
    }

    @Override
    public void tick() {
        float f = this.e.d(1.0F);

        if (f < 0.5F) {
            double d0 = 16.0D;

            this.e.setGoalTarget(this.e.world.findNearbyVulnerablePlayer(this.e, d0));
        } else {
            this.e.setGoalTarget(null);
        }
    }
}
