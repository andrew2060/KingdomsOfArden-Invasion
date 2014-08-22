package net.kingdomsofarden.andrew2060.invasion.monsters.nms.entities.monsters;

import net.kingdomsofarden.andrew2060.invasion.api.IInvasionMob;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalSelector;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.skills.GoalSkillArcaneStorm;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.skills.GoalSkillHomingSkull;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.skills.GoalSkillSelector;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla.*;
import net.minecraft.server.v1_7_R4.*;

public class EntityInvasionWitherBoss extends EntityWither implements IInvasionMob {

    protected GoalSelector goalSelector;
    protected GoalSelector targetSelector;
    protected GoalSkillSelector skillSelector;
    private int tier;
    private boolean elite;

    public EntityInvasionWitherBoss(World world) {
        super(world);
        if (this.goalSelector == null) {
            this.goalSelector = new GoalSelector();
        }
        this.targetSelector = new GoalSelector();
        this.skillSelector = new GoalSkillSelector(this);
        this.getAttributeInstance(GenericAttributes.b).setValue(32.0);
        this.goalSelector.addGoal(new GoalFloat(this), 0);
        this.goalSelector.addGoal(this.skillSelector, 1);
        this.goalSelector.addGoal(new GoalArrowAttack(this, 1.0D, 40, 20.0F), 2);
        this.goalSelector.addGoal(new GoalRandomStroll(this, 1.0D), 5);
        this.goalSelector.addGoal(new GoalLookAtPlayer(this, EntityHuman.class, 8.0F), 6);
        this.goalSelector.addGoal(new GoalRandomLookaround(this), 7);
        this.targetSelector.addGoal(new GoalHurtByTarget(this, false), 1);
        this.targetSelector.addGoal(new GoalNearestAttackableTarget(this, EntityHuman.class, 0, false), 2);
        this.targetSelector.addGoal(new GoalNearestAttackableTarget(this, EntityVillager.class, 0, false), 2);
        this.targetSelector.addGoal(new GoalNearestAttackableTarget(this, EntityIronGolem.class, 0, false), 2);
        this.skillSelector.purge();
        this.skillSelector.addSkill(new GoalSkillHomingSkull(this, 30000, 0.3));
        this.skillSelector.addSkill(new GoalSkillArcaneStorm(this, 20, 600, 300000, 1.0, 40 * Math.pow(1.1,
                Math.sqrt(tier))));
    }

    // Plugin load
    public EntityInvasionWitherBoss(World world, boolean elite, int tier) {
        this(world);
        this.elite = elite;
        this.tier = tier;
        this.update();
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
    public void setElite(boolean elite) {
        this.elite = elite;
    }

    @Override
    public void update() {
        this.skillSelector.purge();
        this.skillSelector.addSkill(new GoalSkillHomingSkull(this, 30000, 0.3 * Math.sqrt(this.tier)));
        this.skillSelector.addSkill(new GoalSkillArcaneStorm(this, 20, 600, 300000, 1.0, 40 * Math.pow(1.1,
                Math.sqrt(tier))));
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
