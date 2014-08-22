package net.kingdomsofarden.andrew2060.invasion.monsters.nms.entities.monsters;


import net.kingdomsofarden.andrew2060.invasion.api.IInvasionMob;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalSelector;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.customgoals.GoalGiantEarthquake;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.customgoals.GoalGiantShootWitherSkullsNormal;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.skills.GoalSkillHomingSkull;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.skills.GoalSkillSelector;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.skills.GoalSkillSummonMinions;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla.*;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.entity.EntityType;

public class EntityInvasionGiant extends EntityGiantZombie implements IInvasionMob {

    protected GoalSelector goalSelector;
    protected GoalSelector targetSelector;
    protected GoalSkillSelector skillSelector;
    private int tier;
    private boolean elite;

    public EntityInvasionGiant(World world) {
        super(world);
        if (this.goalSelector == null) {
            this.goalSelector = new GoalSelector();
        }
        this.targetSelector = new GoalSelector();
        this.skillSelector = new GoalSkillSelector(this);
        this.getAttributeInstance(GenericAttributes.b).setValue(32.0);
        this.goalSelector.addGoal(new GoalFloat(this), 1);
        this.goalSelector.addGoal(new GoalGiantShootWitherSkullsNormal(this), 1);
        this.goalSelector.addGoal(new GoalGiantEarthquake(this), 1);
        this.goalSelector.addGoal(this.skillSelector, 1);
        this.goalSelector.addGoal(new GoalRandomStroll(this, 1.0D), 5);
        this.goalSelector.addGoal(new GoalLookAtPlayer(this, EntityHuman.class, 16.0F), 6);
        this.goalSelector.addGoal(new GoalRandomLookaround(this), 6);
        this.targetSelector.addGoal(new GoalHurtByTarget(this, false), 1);
        this.targetSelector.addGoal(new GoalNearestAttackableTarget(this, EntityHuman.class, 0, true), 2);
        this.targetSelector.addGoal(new GoalNearestAttackableTarget(this, EntityVillager.class, 0, false), 2);
        this.targetSelector.addGoal(new GoalNearestAttackableTarget(this, EntityIronGolem.class, 0, false), 2);
        this.skillSelector.addSkill(new GoalSkillHomingSkull(this));
        this.skillSelector.addSkill(new GoalSkillSummonMinions(this, EntityType.ZOMBIE, 20));
        this.skillSelector.addSkill(new GoalSkillSummonMinions(this, EntityType.SKELETON, 10));
        this.skillSelector.addSkill(new GoalSkillSummonMinions(this, EntityType.SPIDER, 5));
    }

    // Plugin load
    public EntityInvasionGiant(World world, boolean elite, int tier) {
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

    @Override
    public void a(NBTTagCompound nbtTagCompound) {
        super.a(nbtTagCompound);
        if (nbtTagCompound.hasKey("Invasion_Elite")) {
            this.elite = nbtTagCompound.getBoolean("Invasion_Elite");
        } else {
            this.elite = false;
        }
        if (nbtTagCompound.hasKey("Invasion_Tier")) {
            this.tier = nbtTagCompound.getInt("Invasion_Tier");
        } else {
            this.tier = 0;
        }
        this.update();
    }

    public void b(NBTTagCompound nbttagcompound) {
        super.b(nbttagcompound);
        nbttagcompound.setBoolean("Invasion_Elite", this.elite);
        nbttagcompound.setInt("Invasion_Tier", this.tier);
    }

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
}
