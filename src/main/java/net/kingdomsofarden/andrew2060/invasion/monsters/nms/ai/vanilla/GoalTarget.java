package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla;

// CraftBukkit start
import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.minecraft.server.v1_7_R4.*;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_7_R4.event.CraftEventFactory;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
// CraftBukkit end

public abstract class GoalTarget extends Goal {

    protected EntityCreature c;
    protected boolean d;
    private boolean a;
    private int b;
    private int e;
    private int f;

    public GoalTarget(EntityCreature entitycreature, boolean flag) {
        this(entitycreature, flag, false);
    }

    public GoalTarget(EntityCreature entitycreature, boolean flag, boolean flag1) {
        this.c = entitycreature;
        this.d = flag;
        this.a = flag1;
    }

    @Override
    public boolean shouldContinue() {
        EntityLiving entityliving = this.c.getGoalTarget();

        if (entityliving == null) {
            return false;
        } else if (!entityliving.isAlive()) {
            return false;
        } else {
            double d0 = this.f();

            if (this.c.f(entityliving) > d0 * d0) {
                return false;
            } else {
                if (this.d) {
                    if (this.c.getEntitySenses().canSee(entityliving)) {
                        this.f = 0;
                    } else if (++this.f > 60) {
                        return false;
                    }
                }

                return !(entityliving instanceof EntityPlayer) || !((EntityPlayer) entityliving).playerInteractManager.isCreative();
            }
        }
    }

    protected double f() {
        AttributeInstance attributeinstance = this.c.getAttributeInstance(GenericAttributes.b);

        return attributeinstance == null ? 16.0D : attributeinstance.getValue();
    }

    @Override
    public void start() {
        this.b = 0;
        this.e = 0;
        this.f = 0;
    }

    public void d() {
        this.c.setGoalTarget((EntityLiving) null);
    }

    public boolean a(EntityLiving entityliving, boolean flag) {
        if (entityliving == null) {
            return false;
        } else if (entityliving == this.c) {
            return false;
        } else if (!entityliving.isAlive()) {
            return false;
        } else if (!this.c.a(entityliving.getClass())) {
            return false;
        } else { //TODO: Prevent undead friendly fire incidents
            if (this.c instanceof EntityOwnable && StringUtils.isNotEmpty(((EntityOwnable) this.c).getOwnerUUID())) {
                if (entityliving instanceof EntityOwnable && ((EntityOwnable) this.c).getOwnerUUID().equals(((EntityOwnable) entityliving).getOwnerUUID())) {
                    return false;
                }

                if (entityliving == ((EntityOwnable) this.c).getOwner()) {
                    return false;
                }
            } else if (entityliving instanceof EntityHuman && !flag && ((EntityHuman) entityliving).abilities.isInvulnerable) {
                return false;
            }

            if (!this.c.b(MathHelper.floor(entityliving.locX), MathHelper.floor(entityliving.locY), MathHelper.floor(entityliving.locZ))) {
                return false;
            } else if (this.d && !this.c.getEntitySenses().canSee(entityliving)) {
                return false;
            } else {
                if (this.a) {
                    if (--this.e <= 0) {
                        this.b = 0;
                    }

                    if (this.b == 0) {
                        this.b = this.a(entityliving) ? 1 : 2;
                    }

                    if (this.b == 2) {
                        return false;
                    }
                }

                // CraftBukkit start - Check all the different target goals for the reason, default to RANDOM_TARGET
                EntityTargetEvent.TargetReason reason = EntityTargetEvent.TargetReason.RANDOM_TARGET;

                if (this instanceof GoalDefendVillage) {
                    reason = EntityTargetEvent.TargetReason.DEFEND_VILLAGE;
                } else if (this instanceof GoalHurtByTarget) {
                    reason = EntityTargetEvent.TargetReason.TARGET_ATTACKED_ENTITY;
                } else if (this instanceof GoalNearestAttackableTarget) {
                    if (entityliving instanceof EntityHuman) {
                        reason = EntityTargetEvent.TargetReason.CLOSEST_PLAYER;
                    }
                }
                /*else if (this instanceof GoalOwnerHurtByTarget) {
                    reason = EntityTargetEvent.TargetReason.TARGET_ATTACKED_OWNER;
                } else if (this instanceof GoalOwnerHurtTarget) {
                    reason = EntityTargetEvent.TargetReason.OWNER_ATTACKED_TARGET;
                }*/

                EntityTargetLivingEntityEvent event = CraftEventFactory.callEntityTargetLivingEvent(this.c, entityliving, reason);
                if (event.isCancelled() || event.getTarget() == null) {
                    this.c.setGoalTarget(null);
                    return false;
                } else if (entityliving.getBukkitEntity() != event.getTarget()) {
                    this.c.setGoalTarget((EntityLiving) ((CraftEntity) event.getTarget()).getHandle());
                }
                if (this.c instanceof EntityCreature) {
                    ((EntityCreature) this.c).target = ((CraftEntity) event.getTarget()).getHandle();
                }
                // CraftBukkit end

                return true;
            }
        }
    }

    private boolean a(EntityLiving entityliving) {
        this.e = 10 + this.c.aI().nextInt(5);
        PathEntity pathentity = this.c.getNavigation().a(entityliving);

        if (pathentity == null) {
            return false;
        } else {
            PathPoint pathpoint = pathentity.c();

            if (pathpoint == null) {
                return false;
            } else {
                int i = pathpoint.a - MathHelper.floor(entityliving.locX);
                int j = pathpoint.c - MathHelper.floor(entityliving.locZ);

                return (double) (i * i + j * j) <= 2.25D;
            }
        }
    }
}
