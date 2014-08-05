package net.kingdomsofarden.andrew2060.invasion.tracker;

public class DamageInfo {
    
    private long time;
    private double damage;
    
    public DamageInfo(double damage) {
        this.damage = damage;
        this.time = System.currentTimeMillis();
    }

    public long getApplyTime() {
        return this.time;
    }

    public double getDamage() {
        return this.damage;
    }
    
}
