package net.kingdomsofarden.andrew2060.invasion.monsters.nms.entities.projectiles;

import com.herocraftonline.heroes.characters.skill.Skill;
import net.minecraft.server.v1_7_R4.DamageSource;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.EntityWitherSkull;
import net.minecraft.server.v1_7_R4.EnumDifficulty;
import net.minecraft.server.v1_7_R4.MathHelper;
import net.minecraft.server.v1_7_R4.MobEffect;
import net.minecraft.server.v1_7_R4.MobEffectList;
import net.minecraft.server.v1_7_R4.MovingObjectPosition;
import net.minecraft.server.v1_7_R4.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.entity.ExplosionPrimeEvent;

public class EntityGiantWitherSkull extends EntityWitherSkull {
    private float heal;
    private float damage;

    public EntityGiantWitherSkull(World world, EntityLiving entityliving, double d0, double d1, double d2, int tier) {
        super(world, entityliving, d0, d1, d2);
        this.damage = (float) (20 * MathHelper.sqrt(tier));
        this.heal = this.damage * 0.3F < 10 ? 10 : this.damage * 0.5F;
    }

    public EntityGiantWitherSkull(World world) {
        super(world);
        this.die();
    }

    protected void a(MovingObjectPosition movingobjectposition) {
        if (!this.world.isStatic) {
            if (movingobjectposition.entity != null) {
                if (this.shooter != null) {
                    if (movingobjectposition.entity instanceof EntityLiving) {
                        if (Skill.damageEntity((LivingEntity)movingobjectposition.entity.getBukkitEntity(),
                                (LivingEntity)this.shooter.getBukkitEntity(),
                                this.damage, EntityDamageEvent.DamageCause.ENTITY_ATTACK, false)) {
                            this.shooter.heal(this.heal, RegainReason.WITHER); // CraftBukkit
                        }
                    } else {
                        movingobjectposition.entity.damageEntity(DamageSource.mobAttack(this.shooter), this.damage);
                    }
                } else {
                    movingobjectposition.entity.damageEntity(DamageSource.MAGIC, this.damage);
                }

                if (movingobjectposition.entity instanceof EntityLiving) {
                    ((EntityLiving) movingobjectposition.entity)
                            .addEffect(new MobEffect(MobEffectList.WITHER.id, 800, 1));
                }
            }

            // CraftBukkit start
            ExplosionPrimeEvent event = new ExplosionPrimeEvent(this.getBukkitEntity(), 1.0F, false);
            this.world.getServer().getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.world.createExplosion(this, this.locX, this.locY, this.locZ, event.getRadius(), event.getFire(),
                        this.world.getGameRules().getBoolean("mobGriefing"));
            }
            // CraftBukkit end

            this.die();
        }
    }
}
