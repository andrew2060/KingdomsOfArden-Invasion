package net.kingdomsofarden.andrew2060.invasion.api.ai;

public class GoalSelectorItem {
    private Goal goal;
    private int priority;

    public GoalSelectorItem(int priority, Goal goal) {
        this.goal = goal;
        this.priority = priority;
    }

    public Goal getGoal() {
        return this.goal;
    }

    public int getPriority() {
        return this.priority;
    }
}
