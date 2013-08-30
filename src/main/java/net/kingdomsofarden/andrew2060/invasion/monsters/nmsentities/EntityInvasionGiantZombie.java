package net.kingdomsofarden.andrew2060.invasion.monsters.nmsentities;

import java.util.Random;

import net.kingdomsofarden.andrew2060.invasion.monsters.nmsentities.goals.PathfinderGoalGiantEarthquake;
import net.kingdomsofarden.andrew2060.invasion.monsters.nmsentities.goals.PathfinderGoalGiantFireWitherskulls;
import net.kingdomsofarden.andrew2060.invasion.monsters.nmsentities.goals.PathfinderGoalGiantShockwave;
import net.minecraft.server.v1_6_R2.DamageSource;
import net.minecraft.server.v1_6_R2.Entity;
import net.minecraft.server.v1_6_R2.EntityAnimal;
import net.minecraft.server.v1_6_R2.EntityDamageSource;
import net.minecraft.server.v1_6_R2.EntityDamageSourceIndirect;
import net.minecraft.server.v1_6_R2.EntityGiantZombie;
import net.minecraft.server.v1_6_R2.EntityHuman;
import net.minecraft.server.v1_6_R2.EntityLiving;
import net.minecraft.server.v1_6_R2.EntityVillager;
import net.minecraft.server.v1_6_R2.EntityWitherSkull;
import net.minecraft.server.v1_6_R2.IRangedEntity;
import net.minecraft.server.v1_6_R2.MathHelper;
import net.minecraft.server.v1_6_R2.PathfinderGoalFloat;
import net.minecraft.server.v1_6_R2.PathfinderGoalHurtByTarget;
import net.minecraft.server.v1_6_R2.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_6_R2.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_6_R2.PathfinderGoalMoveThroughVillage;
import net.minecraft.server.v1_6_R2.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_6_R2.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_6_R2.PathfinderGoalRandomStroll;
import net.minecraft.server.v1_6_R2.World;

public class EntityInvasionGiantZombie extends EntityGiantZombie implements IRangedEntity {

    private int tier;

    public EntityInvasionGiantZombie(World world) {
        super(world);
        this.tier = new Random().nextInt(5);
        this.goalSelector.a(0, new PathfinderGoalFloat(this));
        this.goalSelector.a(1, new PathfinderGoalGiantEarthquake(this));
        this.goalSelector.a(2, new PathfinderGoalGiantFireWitherskulls(this, 1.0D, 40, 20.0F));
        this.goalSelector.a(3, new PathfinderGoalGiantShockwave(this));
        this.goalSelector.a(4, new PathfinderGoalMeleeAttack(this, EntityHuman.class, 0.23F, false));
        this.goalSelector.a(5, new PathfinderGoalMeleeAttack(this, EntityVillager.class, 0.23F, true));
        this.goalSelector.a(6, new PathfinderGoalMoveTowardsRestriction(this, 0.23F));
        this.goalSelector.a(7, new PathfinderGoalMoveThroughVillage(this, 0.23F, false));
        this.goalSelector.a(8, new PathfinderGoalRandomStroll(this, 1.0D));
        this.goalSelector.a(9, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0F));
        
        this.targetSelector.a(1, new PathfinderGoalHurtByTarget(this, false));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityHuman.class, 0, true));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityVillager.class, 0, false));
        this.targetSelector.a(2, new PathfinderGoalNearestAttackableTarget(this, EntityAnimal.class, 0, false));
    }
    
    /**
     * Increase the giant's default aggro search radius past 16 blocks 
     */
    
    @Override
    protected Entity findTarget() {
        EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, 32.0D);
        return entityhuman != null && this.o(entityhuman) ? entityhuman : null;
    }
    
    /**
     * Process Invulnerabilities prior to sending to DamageEntity
     */
    
    @Override
    public boolean damageEntity(DamageSource damageSource, float f) {
        if(!((damageSource instanceof EntityDamageSourceIndirect) || (damageSource instanceof EntityDamageSource && damageSource.n().equalsIgnoreCase("player")))) {
            return false;
        } else {
            return super.damageEntity(damageSource, f);
        }
    }
    
    //Copy Paste From EntityWither to implement 
    @Override
    public void a(EntityLiving entityLiving, float arg1) {
        // NMS Change Start: fire once for each tier
        for(int i = 0; i <= tier; i++) {
            this.a(0, entityLiving);
        }
        // NMS Change End
    }
    
    private void a(int i, EntityLiving entityliving) {
        this.a(i, entityliving.locX, entityliving.locY + (double) entityliving.getHeadHeight() * 0.5D, entityliving.locZ, i == 0 && this.random.nextFloat() < 0.001F);
    }
    
    private double r(int i) {
        if (i <= 0) {
            return this.locX;
        } else {
            float f = (this.aN + (float) (180 * (i - 1))) / 180.0F * 3.1415927F;
            float f1 = MathHelper.cos(f);

            return this.locX + (double) f1 * 1.3D;
        }
    }
    private double s(int i) {
        return i <= 0 ? this.locY + 3.0D : this.locY + 2.2D;
    }

    private double t(int i) {
        if (i <= 0) {
            return this.locZ;
        } else {
            float f = (this.aN + (float) (180 * (i - 1))) / 180.0F * 3.1415927F;
            float f1 = MathHelper.sin(f);

            return this.locZ + (double) f1 * 1.3D;
        }
    }
    private void a(int i, double d0, double d1, double d2, boolean flag) {
        this.world.a((EntityHuman) null, 1014, (int) this.locX, (int) this.locY, (int) this.locZ, 0);
        double d3 = this.r(i);
        double d4 = this.s(i);
        double d5 = this.t(i);
        double d6 = d0 - d3;
        double d7 = d1 - d4;
        double d8 = d2 - d5;
        EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.world, this, d6, d7, d8);

        if (flag) {
            entitywitherskull.a(true);
        }

        entitywitherskull.locY = d4;
        entitywitherskull.locX = d3;
        entitywitherskull.locZ = d5;
        this.world.addEntity(entitywitherskull);
    }
    
    public int getTier() {
        return tier;
    }
    
    @Override
    protected void dropDeathLoot(boolean flag, int i) {
        
    }
}
