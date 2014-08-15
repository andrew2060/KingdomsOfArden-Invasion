package net.kingdomsofarden.andrew2060.invasion.util;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;

public class NMSInterface {
    public static double getPostArmorDamage(LivingEntity defender, double damage) {
        int i = 25 - ((CraftLivingEntity) defender).getHandle().aV();
        float f1 = (float) damage * (float) i;
        return f1 / 25;
    }
}
