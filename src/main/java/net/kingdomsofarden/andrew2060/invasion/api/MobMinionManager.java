package net.kingdomsofarden.andrew2060.invasion.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.LivingEntity;

public class MobMinionManager {
    
    public static MobMinionManager instance;
    
    private HashMap<UUID,ArrayList<LivingEntity>> minions;
    
    public MobMinionManager() {
        minions = new HashMap<UUID,ArrayList<LivingEntity>>();
        instance = this;
    }
    
    public ArrayList<LivingEntity> getMinions(LivingEntity entity) {
        return getMinions(entity.getUniqueId());
    }
    
    private ArrayList<LivingEntity> getMinions(UUID uuid) {
        return minions.get(uuid) == null ? new ArrayList<LivingEntity>() : minions.get(uuid);
    }
    
    public void addMinion(LivingEntity owner, LivingEntity minion) {
        addMinion(owner.getUniqueId(), minion);
    }
    
    private void addMinion(UUID uuid, LivingEntity minion) {
        if (minions.containsKey(uuid)) {
            minions.get(uuid).add(minion);
        } else {
            minions.put(uuid, new ArrayList<LivingEntity>());
            minions.get(uuid).add(minion);
        }
    }
    
}
