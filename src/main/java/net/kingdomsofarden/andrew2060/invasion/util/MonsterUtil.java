package net.kingdomsofarden.andrew2060.invasion.util;

import net.kingdomsofarden.andrew2060.invasion.api.IInvasionMob;
import net.minecraft.server.v1_7_R4.EntityLiving;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public class MonsterUtil {
    public static IInvasionMob getInvasionMob(LivingEntity e) {
        EntityLiving eL = ((CraftLivingEntity)e).getHandle();
        if (eL instanceof IInvasionMob) {
            return (IInvasionMob) eL;
        } else {
            return null;
        }
    }
}
