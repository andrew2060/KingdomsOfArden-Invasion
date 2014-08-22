package net.kingdomsofarden.andrew2060.invasion.monsters.nms.entities.monsters;

import net.kingdomsofarden.andrew2060.invasion.api.IInvasionMob;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalSelector;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.skills.GoalSkillSelector;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla.*;
import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.vanilla.virtual.GoalSpiderTargetting;
import net.minecraft.server.v1_7_R4.*;

public class EntityInvasionSpider extends EntitySpider implements IInvasionMob {
    protected GoalSelector goalSelector;
    protected GoalSelector targetSelector;
    protected GoalSkillSelector skillSelector;
    private int tier;
    private boolean elite;

    public EntityInvasionSpider(World world) {
        super(world);
        if (this.goalSelector == null) {
            this.goalSelector = new GoalSelector();
        }
        this.targetSelector = new GoalSelector();
        this.skillSelector = new GoalSkillSelector(this);
        this.getAttributeInstance(GenericAttributes.b).setValue(32.0);
        this.goalSelector.addGoal(new GoalFloat(this), 0);
        this.goalSelector.addGoal(new GoalMeleeAttack(this, EntityHuman.class, 1.0D, false), 1);
        this.goalSelector.addGoal(this.skillSelector, 1);
        this.goalSelector.addGoal(new GoalRandomStroll(this, 1.0D), 5);
        this.goalSelector.addGoal(new GoalLookAtPlayer(this, EntityHuman.class, 16.0F), 6);
        this.goalSelector.addGoal(new GoalRandomLookaround(this), 6);
        this.targetSelector.addGoal(new GoalHurtByTarget(this, false), 1);
        this.targetSelector.addGoal(new GoalSpiderTargetting(this), 2);
    }

    // Plugin load
    public EntityInvasionSpider(World world, boolean elite, int tier) {
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
