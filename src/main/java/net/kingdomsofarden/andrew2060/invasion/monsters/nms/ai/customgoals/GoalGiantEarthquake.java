package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.customgoals;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.entities.monsters.EntityInvasionGiant;
import net.minecraft.server.v1_7_R4.*;

import java.util.List;
import java.util.UUID;

public class GoalGiantEarthquake extends Goal {
    private EntityInvasionGiant giant;
    private int ticks;

    public GoalGiantEarthquake(EntityInvasionGiant giant) {
        this.giant = giant;
        this.ticks = 0;
    }


    @Override
    public GoalType getType() {
        return GoalType.ZERO;
    }

    @Override
    public UUID getGoalUID() {
        return null;
    }

    @Override
    public boolean shouldStart() {
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return true;
    }

    @Override
    public void tick() {
        if (this.ticks % 20 != 19) {
            this.ticks++;
            return;
        }
        this.ticks = 0;
        for (Entity e : (List<Entity>)this.giant.world.getEntities(this.giant, this.giant.boundingBox.grow(4, 4, 4))) {
            if (e instanceof EntityPlayer || e instanceof EntityHuman || e instanceof EntityIronGolem) {
                e.damageEntity(DamageSource.mobAttack(this.giant), 30F);
                e.motY = 0.5;
                e.velocityChanged = true;
            }
        }
    }
}
