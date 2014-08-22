package net.kingdomsofarden.andrew2060.invasion.util;

import net.kingdomsofarden.andrew2060.invasion.api.IInvasionMob;
import net.minecraft.server.v1_7_R4.Entity;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.EntityTypes;
import net.minecraft.server.v1_7_R4.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class MonsterUtil {

    private static HashMap<EntityType,Constructor<? extends Entity>> ctors;

    static {
        ctors = new HashMap<EntityType, Constructor<? extends Entity>>();
    }

    public static void init() {
        for (EntityType type : EntityType.values()) {
            if (isRegisteredMobType(type)) {
                Class<? extends Entity> spawnClazz = EntityTypes.a(type.getTypeId());
                try {
                    Constructor<? extends Entity> ctor = spawnClazz.getConstructor(World.class);
                    ctors.put(type, ctor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isRegisteredMobType(EntityType type) {
        switch (type) {

        case GIANT:
        case SKELETON:
        case SPIDER:
        case WITHER:
        case ZOMBIE:
            return true;
        default:
            return false;

        }
    }

    public static boolean isInvasionMob(LivingEntity e) {
        return ((CraftLivingEntity)e).getHandle() instanceof IInvasionMob;
    }

    public static IInvasionMob getInvasionMob(LivingEntity e) {
        EntityLiving eL = ((CraftLivingEntity)e).getHandle();
        if (eL instanceof IInvasionMob) {
            return (IInvasionMob) eL;
        } else {
            return null;
        }
    }

    public static IInvasionMob spawnMob(EntityType type, Location location, CreatureSpawnEvent.SpawnReason reason) {
        World world = ((CraftWorld)location.getWorld()).getHandle();
        if (isRegisteredMobType(type)) {
            try {
                Entity entity = ctors.get(type).newInstance(world);
                world.addEntity(entity, reason);
                return (IInvasionMob)entity;
            } catch(Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
