package net.kingdomsofarden.andrew2060.invasion.tracker;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.projectiles.ProjectileSource;

public class TrackerKey {

    public static final String ENV_CACTUS = "env.cactus";
    public static final String ENV_DISPENSER = "env.dispenser";
    public static final String ENV_DROWNING = "env.drowning";
    public static final String ENV_EXPLOSION = "env.explosion";
    public static final String ENV_FALL = "env.fall";
    public static final String ENV_FALL_BLOCK = "env.fallblock";
    public static final String ENV_FIRE = "env.fire";
    public static final String ENV_LAVA = "env.lava";
    public static final String ENV_LIGHTNING = "env.lightning";
    public static final String ENV_MAGIC = "env.magic";
    public static final String ENV_POISON = "env.poison";
    public static final String ENV_SUFFOCATE = "env.suffocate";
    public static final String ENV_SUICIDE = "env.suicide";
    public static final String ENV_UNKNOWN = "env.unknown";
    public static final String ENV_WITHER = "env.wither";

    public static String getDamageTrackerKey(Entity entity) {
        return getDamageTrackerKey(null,entity);
    }

    public static String getDamageTrackerKey(DamageCause cause) {
        return getDamageTrackerKey(cause,null);
    }

    @SuppressWarnings("deprecation")
    public static String getDamageTrackerKey(DamageCause cause, Entity entity) {
        if (entity != null) {
            if (entity instanceof Player) { // Always attribute player damage to player
                return ((Player)entity).getUniqueId().toString();
            } else if (entity instanceof LivingEntity) {
                return ((LivingEntity)entity).getType().getName();
            } else if (entity instanceof Projectile) {
                if (((Projectile)entity).getShooter() != null) {
                    ProjectileSource source = ((Projectile)entity).getShooter();
                    if (source instanceof LivingEntity) {
                        if (source instanceof Player) {
                            return ((Player)source).getUniqueId().toString();
                        } else if (source instanceof LivingEntity) {
                            return ((LivingEntity)source).getType().getName();
                        } else {
                            return ENV_UNKNOWN;
                        }
                    } else {
                        return ENV_DISPENSER;
                    }
                } else {
                    return ENV_DISPENSER;
                }
            } else {
                return ENV_UNKNOWN;
            }
        } else {
            if (cause == null) {
                return ENV_UNKNOWN;
            } else {
                switch (cause) {

                case BLOCK_EXPLOSION:
                    return ENV_EXPLOSION;
                case CONTACT:
                    return ENV_CACTUS;
                case CUSTOM:
                    return ENV_UNKNOWN;
                case DROWNING:
                    return ENV_DROWNING;
                case ENTITY_ATTACK:
                    return ENV_UNKNOWN;
                case ENTITY_EXPLOSION:
                    return ENV_EXPLOSION;
                case FALL:
                    return ENV_FALL;
                case FALLING_BLOCK:
                    return ENV_FALL_BLOCK;
                case FIRE:
                    return ENV_FIRE;
                case FIRE_TICK:
                    return ENV_FIRE;
                case LAVA:
                    return ENV_LAVA;
                case LIGHTNING:
                    return ENV_LIGHTNING;
                case MAGIC:
                    return ENV_MAGIC;
                case MELTING:
                    return ENV_UNKNOWN;
                case POISON:
                    return ENV_POISON;
                case PROJECTILE:
                    return ENV_DISPENSER;
                case STARVATION:
                    return ENV_UNKNOWN;
                case SUFFOCATION:
                    return ENV_SUFFOCATE;
                case SUICIDE:
                    return ENV_SUICIDE;
                case THORNS:
                    return ENV_MAGIC;
                case VOID:
                    return ENV_UNKNOWN;
                case WITHER:
                    return ENV_WITHER;
                default:
                    return ENV_UNKNOWN;

                }    
            }
        }
    }
}
