package net.kingdomsofarden.andrew2060.invasion.monsters.nmsentities.goals;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_6_R2.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import net.kingdomsofarden.andrew2060.invasion.monsters.nmsentities.EntityInvasionGiantZombie;
import net.minecraft.server.v1_6_R2.Entity;
import net.minecraft.server.v1_6_R2.EntityAnimal;
import net.minecraft.server.v1_6_R2.EntityLiving;
import net.minecraft.server.v1_6_R2.EntityPlayer;
import net.minecraft.server.v1_6_R2.PathfinderGoal;

public class PathfinderGoalGiantShockwave extends PathfinderGoal {

    private EntityInvasionGiantZombie giant;
    private List<LivingEntity> validTargets;
    private Random rand;
    private long lastTick;
    
    public PathfinderGoalGiantShockwave(EntityInvasionGiantZombie giant) {
        this.giant = giant;
        this.rand = new Random();
        this.lastTick = System.currentTimeMillis();
    }

    //Get nearby entities->if valid return true
    @Override
    public boolean a() {
        if(validTargets != null) {
            validTargets = null;
        }
        if(lastTick + 5000 > System.currentTimeMillis()) {
            return false;
        }
        @SuppressWarnings("unchecked")
        List<Entity> notchEntityList = giant.world.getEntities(giant, giant.boundingBox.grow(16, 5, 16));
        this.validTargets = new LinkedList<LivingEntity>();
        for(Entity e : notchEntityList) {
            if(e instanceof EntityLiving) {
                if(e instanceof EntityPlayer) {
                    validTargets.add((CraftPlayer)e.getBukkitEntity());
                    continue;
                }
                if(e instanceof EntityAnimal) {
                    validTargets.add((CraftLivingEntity)e.getBukkitEntity());
                    continue;
                }
            }
        }
        this.lastTick = System.currentTimeMillis();
        return validTargets.size() > 0;
    }
    
    @Override
    public void e() {
        if(rand.nextInt(10) < 7) {
            this.validTargets = null;
            return;
        }
        for(LivingEntity lE : validTargets) {
            if(lE.getLocation().distanceSquared(giant.getBukkitEntity().getLocation()) <= 64) {
                Vector v =lE.getLocation().add(0, 2, 0).toVector().subtract(giant.getBukkitEntity().getLocation().toVector()).normalize();
                v = v.multiply(4);
                v = v.setY(0);
                lE.setVelocity(v);
                lE.getWorld().playEffect(lE.getLocation(), Effect.ZOMBIE_CHEW_WOODEN_DOOR, 1);
            }
        }
    }

}
