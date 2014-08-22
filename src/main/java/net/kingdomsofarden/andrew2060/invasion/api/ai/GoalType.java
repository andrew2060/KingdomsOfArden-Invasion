package net.kingdomsofarden.andrew2060.invasion.api.ai;

public enum GoalType {
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN;

    public boolean isCompatibleWith(GoalType type) {
        return (this.ordinal() & type.ordinal()) == 0;
    }
}
