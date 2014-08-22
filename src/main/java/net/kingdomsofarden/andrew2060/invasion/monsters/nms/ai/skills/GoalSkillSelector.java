package net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.skills;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalType;
import net.minecraft.server.v1_7_R4.EntityCreature;

import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;

public class GoalSkillSelector extends Goal {

    private static long SKILL_COOLDOWN = 5000;

    private EntityCreature entity;
    private GoalSkill activeGoal;
    private LinkedList<GoalSkill> goals;
    private Random rand;
    private long lastSkillTime;

    public GoalSkillSelector(EntityCreature entity) {
        this.entity = entity;
        this.goals = new LinkedList<GoalSkill>();
        this.activeGoal = null;
        this.rand = new Random();
        this.lastSkillTime = 0;
    }

    @Override
    public GoalType getType() {
        return GoalType.ZERO;
    }

    @Override
    public UUID getGoalUID() {
        return UUID.fromString("c6d1e990-25c8-11e4-8c21-0800200c9a66");
    }

    @Override
    public boolean shouldStart() {
        if (System.currentTimeMillis() < this.lastSkillTime + SKILL_COOLDOWN) {
            return false;
        }
        this.activeGoal = this.goals.peekLast();
        if (this.activeGoal == null) {
            return false;
        }

        this.activeGoal = this.goals.pollLast();
        this.goals.push(this.activeGoal);
        if (this.rand.nextDouble() > this.activeGoal.queueChance()) {
            return false;
        }
        if (!this.activeGoal.offCooldown()) {
            return false;
        }
        if (!this.activeGoal.shouldStart()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void start() {
        this.activeGoal.start();
    }
    @Override
    public boolean shouldContinue() {
        return this.activeGoal.shouldContinue();
    }

    @Override
    public void finish() {
        this.activeGoal.finish();
        this.activeGoal = null;
        this.lastSkillTime = System.currentTimeMillis();
    }

    @Override
    public void tick() {
        this.activeGoal.tick();
    }

    public void addSkill(GoalSkill goal) {
        this.goals.add(goal);
    }

    public void purge() {
        this.goals.clear();
    }
}
