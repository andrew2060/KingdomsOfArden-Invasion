package net.kingdomsofarden.andrew2060.invasion.tracker;

import java.util.LinkedList;

import net.kingdomsofarden.andrew2060.invasion.InvasionPlugin;

public class TrackerValue extends LinkedList<DamageInfo> {

    private static final long serialVersionUID = 70339981842053371L;

    public TrackerValue(InvasionPlugin plugin) {
        super();
        plugin.getServer().getScheduler().runTaskTimer(plugin, new Runnable() {

            @Override
            public void run() {
                DamageInfo val = peekFirst();
                if(val != null && System.currentTimeMillis() - val.getApplyTime() > 30000) { // Newest entry > 30 seconds old
                    //Purge
                    clear();
                }
            }

        }, 200, 200);
    }

    public double getTotalDamage() {
        this.clean();
        double damage = 0;
        for(DamageInfo info : this) {
            damage += info.getDamage();
        }
        return damage;
    }

    private void clean() {
        if(this.isEmpty()) {
            return;
        } else {
            DamageInfo val = peekFirst();
            if(System.currentTimeMillis() - val.getApplyTime() > 30000) {
                this.clear();
            }
        }
    }

}
