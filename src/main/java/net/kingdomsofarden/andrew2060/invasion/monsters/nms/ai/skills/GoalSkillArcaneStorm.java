package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.skills;

import com.herocraftonline.heroes.characters.skill.Skill;
import net.kingdomsofarden.andrew2060.invasion.InvasionPlugin;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.minecraft.server.v1_7_R4.EntityInsentient;
import net.minecraft.server.v1_7_R4.EntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Golem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.NPC;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GoalSkillArcaneStorm extends GoalSkill {
    private final long cooldown;
    private final double queueChance;
    private final double damage;
    private final int duration;
    private int period;
    private final EntityInsentient entity;
    private int ticks;
    private long lastActivation;

    public GoalSkillArcaneStorm(EntityInsentient e, int period, int duration, long cooldown, double queueChance,
                                double damage) {
        this.entity = e;
        this.period = period;
        this.duration = duration;
        this.cooldown = cooldown;
        this.queueChance = queueChance;
        this.damage = damage;
    }

    @Override
    public boolean offCooldown() {
        return this.lastActivation + this.cooldown <= System.currentTimeMillis();
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
        return UUID.fromString("37342e80-2832-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        this.ticks = 0;
        return true;
    }

    public boolean shouldContinue() {
        return !(this.ticks % this.duration == 0 && this.ticks != 0);
    }

    @Override
    public void finish() {
        this.lastActivation = System.currentTimeMillis();
        this.ticks = 0;
    }

    @Override
    public boolean isContinuous() {
        return true;
    }

    @Override
    public void tick() {
        if (this.ticks % 10 == 0) {
            Location eLoc = this.entity.getBukkitEntity().getLocation();
            eLoc.getWorld().spigot().strikeLightningEffect(eLoc, true);
        }
        if (this.ticks % this.period == 0) {
            EntityLiving target = this.entity.getGoalTarget();
            if (target != null) {
                final Location loc = target.getBukkitEntity().getLocation();
                List<Location> circleLoc = circle(loc, 2, 1, false, false, 1);
                for (Location effectLoc : circleLoc) {
                    effectLoc.getWorld().playEffect(effectLoc, Effect.ENDER_SIGNAL, 1);
                }
                loc.getWorld().playSound(loc, Sound.ENDERMAN_TELEPORT, 5, 1);
                loc.getWorld().playEffect(loc, Effect.ENDER_SIGNAL, 1);
                Bukkit.getScheduler().runTaskLater(InvasionPlugin.i(), new Runnable() {

                    @Override
                    public void run() {
                        loc.getWorld().spigot().strikeLightningEffect(loc, false);
                        Arrow a = (Arrow) loc.getWorld().spawnEntity(loc, EntityType.ARROW);
                        for (Entity e : a.getNearbyEntities(3, 3, 3)) {
                            if (e instanceof HumanEntity || e instanceof Animals || e instanceof Golem
                                    || e instanceof NPC) {
                                Skill.damageEntity((LivingEntity)e, (LivingEntity) entity.getBukkitEntity(), damage,
                                        DamageCause.MAGIC, true);
                            }
                        }
                    }
                }, 60);
            }
        }
        this.ticks++;
    }

    private List<Location> circle(Location loc, Integer r, Integer h, boolean hollow, boolean sphere, int plus_y) {
        List<Location> circleblocks = new ArrayList<Location>();
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        for (int x = cx - r; x <= cx +r; x++)
            for (int z = cz - r; z <= cz +r; z++)
                for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r*r && !(hollow && dist < (r-1)*(r-1))) {
                        Location l = new Location(loc.getWorld(), x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }

        return circleblocks;
    }
}
