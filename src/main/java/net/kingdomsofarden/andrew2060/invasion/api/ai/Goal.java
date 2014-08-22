package net.kingdomsofarden.andrew2060.invasion.api.ai;

import java.util.UUID;

public abstract class Goal {

    public abstract GoalType getType();

    public abstract UUID getGoalUID();

    public abstract boolean shouldStart(); //a

    public boolean shouldContinue() { //b
        return shouldStart();
    }

    public void start() { //c
    }

    public void finish() { //d
    }

    public boolean isContinuous() {
        return true;
    }

    public abstract void tick(); //e
}
