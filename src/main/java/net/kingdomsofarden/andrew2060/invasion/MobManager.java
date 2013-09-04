package net.kingdomsofarden.andrew2060.invasion;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Creature;

import net.kingdomsofarden.andrew2060.invasion.monsters.nms.goals.MobGoalManager;

public class MobManager {

    HashMap<UUID,Creature> characters;
    private MobGoalManager goalManager;
    public MobManager(InvasionPlugin plugin, MobGoalManager goalManager) {
        this.characters = new HashMap<UUID,Creature>(); 
        this.goalManager = goalManager;
    }

    public void registerCharacter(Creature creature) {
        if(characters.containsKey(creature.getUniqueId())) {
            Creature entity = characters.get(creature);
            if(!entity.equals(creature)) {
                entity.remove();
                if(goalManager.registerGoals(creature)) {
                    characters.put(creature.getUniqueId(), creature);
                }
            } else {
            }
        } else {
            if(goalManager.registerGoals(creature)) {
                characters.put(creature.getUniqueId(), creature);
            }
        }
    }
}
