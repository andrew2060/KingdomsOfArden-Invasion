package net.kingdomsofarden.andrew2060.invasion.api.mobactions;

public interface MobAction {
    
    /**
     * Is fired prior to the action being performed, if false is returned, skips to the action
     * 
     * @return Whether the action can be performed
     */
    public abstract boolean checkUsable();
    
    /**
     * Is fired when the action itself is performed
     */
    public abstract void tick();
}
