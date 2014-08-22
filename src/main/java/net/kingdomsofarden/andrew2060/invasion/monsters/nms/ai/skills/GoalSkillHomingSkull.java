package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.skills;

import net.kingdomsofarden.andrew2060.invasion.api.IInvasionMob;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.entities.projectiles.EntityGiantHomingWitherSkull;
import net.minecraft.server.v1_7_R4.EntityCreature;
import net.minecraft.server.v1_7_R4.EntityLiving;
import org.bukkit.util.Vector;

import java.util.UUID;

public class GoalSkillHomingSkull extends GoalSkill {

    private final EntityCreature entity;
    private final double queueChance;
    private final long cooldown;
    private long lastUse;

    public GoalSkillHomingSkull(EntityCreature creature) {
        this(creature, 60000);
    }

    public GoalSkillHomingSkull(EntityCreature creature, long cooldown) {
        this(creature, cooldown, 0.30);
    }

    public GoalSkillHomingSkull(EntityCreature creature, long cooldown, double queueChance) {
        this.entity = creature;
        this.cooldown = cooldown;
        this.queueChance = queueChance;
        this.lastUse = 0;
    }

    @Override
    public boolean offCooldown() {
        return System.currentTimeMillis() > this.lastUse + this.cooldown;
    }

    @Override
    public double queueChance() {
        return this.queueChance;
    }

    @Override
    public GoalType getType() {
        return GoalType.ZERO;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("58a8d3b0-263c-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        EntityLiving target = this.entity.getGoalTarget();
        return target != null && target.world == this.entity.world &&
                new Vector(this.entity.locX, this.entity.locY, this.entity.locZ).distanceSquared(new Vector(target.locX,
                        target.locY, target.locZ)) <= 2304;
    }

    public boolean shouldContinue() {
        return false;
    }

    @Override
    public void start() {
        EntityLiving target = this.entity.getGoalTarget();
        Vector v = new Vector(target.locX, target.locY, target.locZ)
                .subtract(new Vector(this.entity.locX, this.entity.locY + this.entity.getHeadHeight(), this.entity.locZ)
                ).normalize().multiply(0.5);
        EntityGiantHomingWitherSkull skull = new EntityGiantHomingWitherSkull(this.entity.world, this.entity, v.getX(),
                v.getY(), v.getZ(), (((IInvasionMob)this.entity).getTier()), target);
        skull.setPosition(this.entity.locX, this.entity.locX + this.entity.getHeadHeight(), this.entity.locZ);
        this.entity.world.addEntity(skull);
        skull.motX = v.getX();
        skull.motY = v.getY();
        skull.motZ = v.getZ();
        skull.velocityChanged = true;
    }

    @Override
    public void finish() {
        this.lastUse = System.currentTimeMillis();
    }

    @Override
    public void tick() {

    }
}
