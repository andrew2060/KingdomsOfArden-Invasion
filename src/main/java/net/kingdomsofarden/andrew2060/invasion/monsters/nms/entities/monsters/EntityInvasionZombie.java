package net.kingdomsofarden.andrew2060.invasion.monsters.nms.entities.monsters;

import net.kingdomsofarden.andrew2060.invasion.api.IInvasionMob;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalSelector;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla.*;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntityIronGolem;
import net.minecraft.server.v1_7_R4.EntityVillager;
import net.minecraft.server.v1_7_R4.EntityZombie;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.World;

public class EntityInvasionZombie extends EntityZombie implements IInvasionMob {

    private final GoalBreakDoor bs = new GoalBreakDoor(this);
    private int tier;
    private boolean elite = false;
    private boolean bu = false;
    protected GoalSelector goalSelector;
    protected GoalSelector targetSelector;

    // World load
    public EntityInvasionZombie(World world) {
        super(world);
        this.goalSelector = new GoalSelector();
        this.targetSelector = new GoalSelector();
        this.goalSelector.addGoal(new GoalFloat(this), 0);
        this.goalSelector.addGoal(new GoalMeleeAttack(this, EntityHuman.class, 1.0D, false), 2);
        this.goalSelector.addGoal(new GoalMeleeAttack(this, EntityIronGolem.class, 1.0D, true), 2);
        this.goalSelector.addGoal(new GoalMeleeAttack(this, EntityVillager.class, 1.0D, true), 2);
        this.goalSelector.addGoal(new GoalMoveTowardsRestriction(this, 1.0D), 5);
        this.goalSelector.addGoal(new GoalMoveThroughVillage(this, 1.0D, false), 6);
        this.goalSelector.addGoal(new GoalRandomStroll(this, 1.0D), 7);
        this.goalSelector.addGoal(new GoalLookAtPlayer(this, EntityHuman.class, 8.0f), 8);
        this.goalSelector.addGoal(new GoalRandomLookaround(this), 8);
        this.targetSelector.addGoal(new GoalHurtByTarget(this, true), 1);
        this.targetSelector.addGoal(new GoalNearestAttackableTarget(this, EntityHuman.class, 0, true), 2);
        this.targetSelector.addGoal(new GoalNearestAttackableTarget(this, EntityVillager.class, 0, false), 2);
        this.targetSelector.addGoal(new GoalNearestAttackableTarget(this, EntityIronGolem.class, 0, false), 2);
        this.a(0.6F, 1.8F);
    }

    // Plugin load
    public EntityInvasionZombie(World world, boolean elite, int tier) {
        this(world);
        this.elite = elite;
        this.tier = tier;
    }

    @Override
    public GoalSelector getGoalSelector() {
        return this.goalSelector;
    }

    @Override
    public GoalSelector getTargetSelector() {
        return this.targetSelector;
    }

    @Override
    public boolean getElite() {
        return this.elite;
    }

    @Override
    public int getTier() {
        return this.tier;
    }

    @Override
    public void setTier(int tier) {
        this.tier = tier;
    }

    @Override
    public void setElite(boolean val) {
        this.elite = val;
    }

    @Override
    public void update() {}

    // EntityInsentient
    @Override
    public boolean bk() {
        return true;
    }

    @Override
    protected void bn() {
        ++this.aU;
        this.world.methodProfiler.a("checkDespawn");
        this.w();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("sensing");
        this.getEntitySenses().a();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("targetSelector");
        this.targetSelector.updateGoals();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("goalSelector");
        this.goalSelector.updateGoals();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("navigation");
        this.getNavigation().f();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("move");
        this.getControllerMove().c();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("look");
        this.getControllerLook().a();
        this.world.methodProfiler.b();
        this.world.methodProfiler.a("jump");
        this.getControllerJump().b();
        this.world.methodProfiler.b();
    }

    // EntityZombie

    @Override
    public void a(boolean flag) {
        if (this.goalSelector == null) { // Custom
            this.goalSelector = new GoalSelector();
        }
        if (this.bu != flag) {
            this.bu = flag;
            if (flag) {
                this.goalSelector.addGoal(this.bs, 1);
            } else {
                this.goalSelector.removeGoal(this.bs);
            }
        }
    }

    @Override
    public boolean bZ() {
        return this.bu;
    }

    // NBT Read/Write

    @Override
    public void a(NBTTagCompound nbttagcompound) {
        super.a(nbttagcompound);
        if (nbttagcompound.hasKey("Invasion_Elite")) {
            this.elite = nbttagcompound.getBoolean("Invasion_Elite");
        } else {
            this.elite = false;
        }
        if (nbttagcompound.hasKey("Invasion_Tier")) {
            this.tier = nbttagcompound.getInt("Invasion_Tier");
        } else {
            this.tier = 1;
        }
        this.update();
    }

    @Override
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("CanBreakDoors", this.bZ());
        nbttagcompound.setBoolean("Invasion_Elite", this.elite);
        nbttagcompound.setInt("Invasion_Tier", this.tier);
    }

}
