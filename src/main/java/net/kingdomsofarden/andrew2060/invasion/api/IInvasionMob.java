package net.kingdomsofarden.andrew2060.invasion.api;

import net.kingdomsofarden.andrew2060.invasion.api.ai.Goal;
import net.kingdomsofarden.andrew2060.invasion.api.ai.GoalSelector;

import java.util.UUID;

public interface IInvasionMob {

    public abstract GoalSelector getGoalSelector();
    public abstract GoalSelector getTargetSelector();

    public abstract int getTier();
    public abstract boolean getElite();

    public abstract void setTier(int tier);
    public abstract void setElite(boolean elite);

    public void update();

}
