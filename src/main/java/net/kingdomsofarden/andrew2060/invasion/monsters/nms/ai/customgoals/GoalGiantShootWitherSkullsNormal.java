package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.customgoals;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.entities.monsters.EntityInvasionGiant;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.entities.projectiles.EntityGiantWitherSkull;
import net.minecraft.server.v1_7_R4.EntityLiving;
import org.bukkit.util.Vector;

import java.util.Random;
import java.util.UUID;

public class GoalGiantShootWitherSkullsNormal extends Goal {

    private final EntityInvasionGiant giant;

    private int amount;
    private int ticks;
    private EntityLiving target;
    private Random rand;

    public GoalGiantShootWitherSkullsNormal(EntityInvasionGiant giant) {
        this.giant = giant;
        this.ticks = 0;
        this.amount = this.giant.getElite() ? 3 : 1;
        this.rand = new Random();
    }

    @Override
    public GoalType getType() {
        return GoalType.ZERO;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("12ee2e50-259d-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        this.ticks++;
        if (this.ticks % 20 != 0)  {
            return false;
        }
        this.ticks = 0;
        this.target = this.giant.getGoalTarget();
        if (this.target == null) {
            return false;
        } else if (!this.target.isAlive()) {
            this.target = null;
            return false;
        } else if (!this.target.world.equals(this.giant.world)) {
            this.target = null;
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void tick() {
        Vector v = new Vector(this.target.locX, this.target.locY, this.target.locZ)
                .subtract(new Vector(this.giant.locX, this.giant.locY + this.giant.getHeadHeight(), this.giant.locZ));
        EntityGiantWitherSkull skull = new EntityGiantWitherSkull(this.giant.world, this.giant, v.getX(),
                v.getY(), v.getZ(), this.giant.getTier());
        skull.setPosition(this.giant.locX, this.giant.locX + this.giant.getHeadHeight(), this.giant.locZ);
        this.giant.world.addEntity(skull);
    }
}
