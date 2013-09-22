package net.kingdomsofarden.andrew2060.invasion.monsters.nms.goals;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.kingdomsofarden.andrew2060.invasion.api.MobMinionManager;
import net.kingdomsofarden.andrew2060.invasion.api.mobskills.MobAction;
import net.kingdomsofarden.andrew2060.invasion.api.mobskills.MobTargetSelectorAction;
import net.minecraft.server.v1_6_R3.EntityCreature;
import net.minecraft.server.v1_6_R3.EntityInsentient;
import net.minecraft.server.v1_6_R3.PathfinderGoal;
import net.minecraft.server.v1_6_R3.PathfinderGoalSelector;

import org.bukkit.craftbukkit.v1_6_R3.entity.CraftCreature;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;

public class MobGoalManager {

    private HashMap<EntityType,ArrayList<MobAction>> entityActionsMap; 
    private HashMap<EntityType,ArrayList<MobTargetSelectorAction>> entityTargettingMap;
    private Field pathfinderGoalField;
    public static MobMinionManager minionManager;
    public MobGoalManager() {
        minionManager = new MobMinionManager();
        this.entityActionsMap = new HashMap<EntityType,ArrayList<MobAction>>();
        this.entityTargettingMap = new HashMap<EntityType,ArrayList<MobTargetSelectorAction>>();
        this.pathfinderGoalField = null;
        try {
            Class<?> goalSelectorItemClass = Class.forName("net.minecraft.server.v1_6_R2.PathfinderGoalSelectorItem");
            pathfinderGoalField = goalSelectorItemClass.getField("a");
            pathfinderGoalField.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException | ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
        loadActions();
    }
    public boolean addMobAction(EntityType type, MobAction action) {
        if(this.entityActionsMap.containsKey(type)) {
            this.entityActionsMap.get(type).add(action);
        } else {
            this.entityActionsMap.put(type, new ArrayList<MobAction>());
            this.entityActionsMap.get(type).add(action);
        }
        return true;
    }
    public boolean addMobTargettingSelectorAction(EntityType type, MobTargetSelectorAction toAdd) {
        if(this.entityTargettingMap.containsKey(type)) {
            this.entityTargettingMap.get(type).add(toAdd);
        } else {
            this.entityTargettingMap.put(type, new ArrayList<MobTargetSelectorAction>());
            this.entityTargettingMap.get(type).add(toAdd);
        }
        return true;
    }
    @SuppressWarnings("rawtypes")
    public boolean registerGoals(Creature mob) {
        EntityType mobType = mob.getType();
        ArrayList<MobAction> actions = this.entityActionsMap.get(mobType);
        ArrayList<MobTargetSelectorAction> targettingActions = this.entityTargettingMap.get(mobType);
        boolean flag = false;
        if(actions != null && actions.size() > 0) {
            flag = true;
            try {
                EntityCreature nmsEntity = ((CraftCreature)mob).getHandle();
                Field goalSelectorField = EntityInsentient.class.getField("goalSelector");
                goalSelectorField.setAccessible(true);
                PathfinderGoalSelector goalSelector = (PathfinderGoalSelector) goalSelectorField.get(nmsEntity);
                goalSelector.a(1, new PathfinderGoalMobSkillSelector(mob, actions));
                goalSelectorField.set(nmsEntity, goalSelector);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | ClassCastException e) {
                e.printStackTrace();
                return false;
            }            
        }
        
        if(targettingActions != null && targettingActions.size() > 0) {
            flag = true;
            try {
                EntityCreature nmsEntity = ((CraftCreature)mob).getHandle();
                Field targetSelectorField = EntityInsentient.class.getField("targetSelector");
                targetSelectorField.setAccessible(true);
                PathfinderGoalSelector targetSelector = (PathfinderGoalSelector) targetSelectorField.get(nmsEntity);
                Field goalListField = PathfinderGoalSelector.class.getField("a");
                goalListField.setAccessible(true);
                List goalList = (List) goalListField.get(targetSelector);
                Set<Object> toRemove = new HashSet<Object>();
                for(MobTargetSelectorAction targettingAction : targettingActions) {
                    for(Class<? extends PathfinderGoal> clazz : targettingAction.getReplaces()) {
                        for(Object selectorItem : goalList) {
                            PathfinderGoal goal = (PathfinderGoal) pathfinderGoalField.get(selectorItem);
                            if(clazz.isInstance(goal)) {
                                toRemove.add(selectorItem);
                                break;
                            }
                        }
                    }
                }
                for(Object obj : toRemove) {
                    goalList.remove(obj);
                }
                goalListField.set(targetSelector, goalList);
                targetSelector.a(1,new PathfinderGoalMobSkillTargetting(mob,targettingActions));
                targetSelectorField.set(nmsEntity, targetSelector);
            } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
                return false;
            }
        }
        return flag;
    }
    
    public ArrayList<MobAction> getMobActions(Creature creature) {
        try {
            EntityCreature nmsEntity = ((CraftCreature)creature).getHandle();
            Field goalSelectorField = EntityInsentient.class.getField("goalSelector");
            goalSelectorField.setAccessible(true);
            PathfinderGoalSelector goalSelector = (PathfinderGoalSelector) goalSelectorField.get(nmsEntity);
            Field goalListField = PathfinderGoalSelector.class.getField("a");
            goalListField.setAccessible(true);
            List<?> goalList = (List<?>) goalListField.get(goalSelector);
            for(int i = 0; i < goalList.size(); i++) {
                Object selectorItem = goalList.get(i);
                PathfinderGoal goal = (PathfinderGoal) pathfinderGoalField.get(selectorItem);
                if(goal instanceof PathfinderGoalMobSkillSelector) {
                    return ((PathfinderGoalMobSkillSelector)goal).getActions();
                }
            }
        } catch(NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
        
    }
    
    private void loadActions() {
        
    }
}
