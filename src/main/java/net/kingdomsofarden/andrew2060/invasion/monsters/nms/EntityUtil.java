package net.kingdomsofarden.andrew2060.invasion.monsters.nms;

import net.kingdomsofarden.andrew2060.invasion.api.IInvasionMob;
import net.minecraft.server.v1_7_R4.EntityLiving;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public class EntityUtil {
    public static IInvasionMob getInvasionMob(LivingEntity e) {
        EntityLiving nmsEntity = ((CraftLivingEntity)e).getHandle();
        if (!(nmsEntity instanceof IInvasionMob)) {
            return null;
        } else {
            return ((IInvasionMob)nmsEntity);
        }
    }
}
