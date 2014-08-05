package net.kingdomsofarden.andrew2060.invasion.tracker;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrackerStorage extends HashMap<String,TrackerValue> {

    private static final long serialVersionUID = -2318286668284908440L;


    public double getTotalDamage() {
        double total = 0;
        double totalPlayer = 0;
        for (Map.Entry<String, TrackerValue> entry : this.entrySet()) {
            double add = entry.getValue().getTotalDamage();
            total += add;
            if (!entry.getKey().regionMatches(0, "env", 0, 3)) {
                 totalPlayer += add;
            }
        }
        return totalPlayer/total > 0.7 ? totalPlayer : total;
    }
    
    public HashMap<UUID,Double> getContributingPlayers() {
        HashMap<UUID,Double> map = new HashMap<UUID,Double>();
        for (String key : this.keySet()) {
            if (key.regionMatches(0, "env", 0, 3)) {
                continue;
            } else {
                try {
                    UUID id = UUID.fromString(key);
                    double damage = this.get(key).getTotalDamage();
                    if(damage > 0) {
                        map.put(id, damage);
                    }
                } catch (IllegalArgumentException e) {}
            }
        }
        return map;
    }
    
}
