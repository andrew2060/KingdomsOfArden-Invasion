package net.kingdomsofarden.andrew2060.invasion.monsters.nms.entities.monsters;

import net.kingdomsofarden.andrew2060.invasion.api.IInvasionMob;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalSelector;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla.*;
import net.minecraft.server.v1_7_R4.*;

public class EntityInvasionSkeleton extends EntitySkeleton implements IInvasionMob {

    private GoalArrowAttack bp = new GoalArrowAttack(this, 1.0D, 20, 60, 15.0F);
    private GoalMeleeAttack bq = new GoalMeleeAttack(this, EntityHuman.class, 1.2D, false);

    protected GoalSelector goalSelector;
    protected GoalSelector targetSelector;
    private int tier;
    private boolean elite;

    public EntityInvasionSkeleton(World world) {
        super(world);
        if (this.goalSelector == null) {
            this.goalSelector = new GoalSelector();
        }
        this.targetSelector = new GoalSelector();
        this.goalSelector.addGoal(new GoalFloat(this), 1);
        this.goalSelector.addGoal(new GoalRestrictSun(this), 2);
        this.goalSelector.addGoal(new GoalFleeSun(this, 1.0D), 3);
        this.goalSelector.addGoal(new GoalRandomStroll(this, 1.0D), 5);
        this.goalSelector.addGoal(new GoalLookAtPlayer(this, EntityHuman.class, 8.0F), 6);
        this.goalSelector.addGoal(new GoalRandomLookaround(this), 6);
        this.targetSelector.addGoal(new GoalHurtByTarget(this, false), 1);
        this.targetSelector.addGoal(new GoalNearestAttackableTarget(this, EntityHuman.class, 0, true), 2);
        this.targetSelector.addGoal(new GoalNearestAttackableTarget(this, EntityIronGolem.class, 0, false), 2);

        if (world != null && !world.isStatic) {
            this.bZ();
        }
    }

    public EntityInvasionSkeleton(World world, boolean elite, int tier) {
        this(world);
        this.tier = tier;
        this.elite = elite;
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
    public int getTier() {
        return this.tier;
    }

    @Override
    public boolean getElite() {
        return this.elite;
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

    // EntitySkeleton
    @Override
    public void bZ() {
        if (this.goalSelector == null) {
            this.goalSelector = new GoalSelector();
        }
        if (this.bp == null) {
            this.bp = new GoalArrowAttack(this, 1.0D, 20, 60, 15.0F);
        }
        if (this.bq == null) {
            this.bq = new GoalMeleeAttack(this, EntityHuman.class, 1.2D, false);
        }
        this.goalSelector.removeGoal(this.bq);
        this.goalSelector.removeGoal(this.bp);
        ItemStack itemstack = this.be();

        if (itemstack != null && itemstack.getItem() == Items.BOW) {
            this.goalSelector.addGoal(this.bp, 4);
        } else {
            this.goalSelector.addGoal(this.bq, 4);
        }
    }

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
    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("Invasion_Elite", this.elite);
        nbttagcompound.setInt("Invasion_Tier", this.tier);
    }
}
