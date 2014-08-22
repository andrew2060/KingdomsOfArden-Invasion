package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.skills;

import net.kingdomsofarden.andrew2060.invasion.api.IInvasionMob;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.customgoals.GoalTargettingMinion;
import net.minecraft.server.v1_7_R4.EntityInsentient;
import net.minecraft.server.v1_7_R4.EntityLiving;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_7_R4.event.CraftEventFactory;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.CreatureSpawnEvent.*;

import java.util.*;


public class GoalSkillSummonMinions extends GoalSkill {

    private final Random rand;
    private final EntityType type;
    private final int limit;
    private EntityInsentient entity;
    private List<EntityLiving> minions;
    private long lastActivation = 0;

    public GoalSkillSummonMinions(EntityInsentient entity, EntityType type, int limit) {
        this.minions = new LinkedList<EntityLiving>();
        this.entity = entity;
        this.rand = new Random();
        this.type = type;
        this.limit = limit;
    }

    @Override
    public boolean offCooldown() {
        return System.currentTimeMillis() > this.lastActivation + 1000;
    }

    @Override
    public double queueChance() {
        return 0.30;
    }

    @Override
    public GoalType getType() {
        return GoalType.ZERO;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("681f4310-263c-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        if (!(this.entity instanceof IInvasionMob)) {
            return false;
        }
        Iterator<EntityLiving> iterator = this.minions.iterator();
        while (iterator.hasNext()) {
            EntityLiving eL = iterator.next();
            if (!(eL.isAlive() && eL.valid)) {
                eL.dead = true; // Set in case of chunk unloads to prevent spam of 10000 mobs
                iterator.remove();
            }
        }
        return this.minions.size() < limit;
    }

    @Override
    public boolean shouldContinue() {
        return false;
    }

    @Override
    public void tick() {
        int size = ((IInvasionMob)this.entity).getTier() - this.minions.size();
        if (size > 10) {
            size = 10;
        }
        CraftWorld world = this.entity.world.getWorld();
        Location loc = new Location(this.entity.world.getWorld(), this.entity.locX, this.entity.locY, this.entity.locZ);
        for (int i = 0; i < size; i++) {
            Entity e = world.spawnEntity(loc.clone().add(1.0 * rand.nextGaussian(), 0, 1.0 * rand.nextGaussian()),
                    type);
            EntityLiving lE = ((CraftLivingEntity) e).getHandle();
            CraftEventFactory.callCreatureSpawnEvent(lE, SpawnReason.REINFORCEMENTS);
            if (lE instanceof IInvasionMob) {
                IInvasionMob iMob = ((IInvasionMob)lE);
                iMob.getTargetSelector().clearGoals();
                iMob.getTargetSelector().addGoal(new GoalTargettingMinion(this.entity, (EntityInsentient) lE), 1);
            }
        }
        this.lastActivation = System.currentTimeMillis();
    }
}
