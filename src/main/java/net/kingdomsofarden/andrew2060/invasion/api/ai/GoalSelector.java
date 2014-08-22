package net.kingdomsofarden.andrew2060.invasion.api.ai;

import net.kingdomsofarden.andrew2060.invasion.monsters.nms.ai.skills.GoalSkillSelector;

import java.util.*;

public class GoalSelector {
    private Map<UUID, GoalSelectorItem> goalMap = new HashMap<UUID, GoalSelectorItem>();
    private ArrayList<GoalSelectorItem> goals = new ArrayList<GoalSelectorItem>();
    private ArrayList<GoalSelectorItem> activeGoals = new ArrayList<GoalSelectorItem>();
    private int delay = 0;

    public GoalSelector() {
    }

    public void addGoal(Goal goal, int priority) {
        this.addGoal(goal.getGoalUID(), goal, priority);
    }

    public void addGoal(UUID key, Goal goal, int priority) {
        GoalSelectorItem goalItem = new GoalSelectorItem(priority, goal);
        if (this.goalMap.containsKey(key)) {
            return;
        }
        this.goalMap.put(key, goalItem);
        this.goals.add(goalItem);
    }

    public void addAndReplaceGoal(UUID key, Goal goal, int priority) {
        if (this.goalMap.containsKey(key)) {
            this.removeGoal(key);
        }
        this.addGoal(key, goal, priority);
    }

    public void removeGoal(Goal goal) {
        Iterator<GoalSelectorItem> iterator = this.goals.iterator();

        while (iterator.hasNext()) {
            GoalSelectorItem goalItem = iterator.next();
            Goal goal1 = goalItem.getGoal();

            if (goal1 == goal) {
                if (this.activeGoals.contains(goalItem)) {
                    goal1.finish();
                    this.activeGoals.remove(goalItem);
                }

                iterator.remove();
            }
        }
    }

    public void removeGoal(UUID key) {
        Iterator<Map.Entry<UUID, GoalSelectorItem>> iterator = this.goalMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, GoalSelectorItem> entry = iterator.next();
            GoalSelectorItem goalItem = entry.getValue();
            Goal goal1 = goalItem.getGoal();

            if (key.equals(entry.getKey())) {
                if (this.activeGoals.contains(goalItem)) {
                    goal1.finish();
                    this.activeGoals.remove(goalItem);
                }
                if (this.goals.contains(goalItem)) {
                    this.goals.remove(goalItem);
                }

                iterator.remove();
            }
        }
    }

    public void clearGoals() {
        this.goalMap.clear();
        this.goals.clear();

        Iterator<GoalSelectorItem> iterator = this.activeGoals.iterator();

        while (iterator.hasNext()) {
            iterator.next().getGoal().finish();
        }
        this.activeGoals.clear();
    }

    public Goal getGoal(UUID key) {
        Iterator<Map.Entry<UUID, GoalSelectorItem>> iterator = this.goalMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, GoalSelectorItem> entry = iterator.next();
            GoalSelectorItem goalItem = entry.getValue();
            Goal goal = goalItem.getGoal();

            if (key.equals(entry.getKey())) {
                return goal;
            }
        }
        return null;
    }

    public void updateGoals() {
        Iterator<GoalSelectorItem> iterator;
        if (this.delay++ % 3 == 0) {

            iterator = this.goals.iterator();

            while (iterator.hasNext()) {
                GoalSelectorItem goalItem = iterator.next();

                if (this.activeGoals.contains(goalItem)) {
                    if (this.canUse(goalItem) && goalItem.getGoal().shouldContinue()) {
                        continue;
                    }

                    goalItem.getGoal().finish();
                    this.activeGoals.remove(goalItem);
                } else {
                    if (this.canUse(goalItem) && goalItem.getGoal().shouldStart()) {

                        goalItem.getGoal().start();
                        this.activeGoals.add(goalItem);
                    }
                }

            }

            this.delay = 0;
        } else {
            iterator = this.activeGoals.iterator();

            while (iterator.hasNext()) {
                GoalSelectorItem goalItem = iterator.next();
                if (!goalItem.getGoal().shouldContinue()) {
                    goalItem.getGoal().finish();
                    iterator.remove();
                }
            }
        }

        iterator = this.activeGoals.iterator();

        while (iterator.hasNext()) {
            GoalSelectorItem goalItem = iterator.next();
            goalItem.getGoal().tick();
        }
    }

    private boolean canUse(GoalSelectorItem goalItem) {
        Iterator<GoalSelectorItem> iterator = this.goals.iterator();

        while (iterator.hasNext()) {
            GoalSelectorItem goalItem1 = iterator.next();
            if (goalItem1 != goalItem) {
                if (goalItem.getPriority() > goalItem1.getPriority()) {
                    if (!this.areCompatible(goalItem, goalItem1) && this.activeGoals.contains(goalItem1)) {
                        return false;
                    }
                    //goal.i() -> isContinuous
                } else if (!goalItem1.getGoal().isContinuous() && this.activeGoals.contains(goalItem1)) {
                    return false;
                }
            }
        }
        return true;
    }

    protected boolean areCompatible(GoalSelectorItem goalItem, GoalSelectorItem goalItem1) {
        return goalItem.getGoal().getType().isCompatibleWith(goalItem1.getGoal().getType());
    }
}
