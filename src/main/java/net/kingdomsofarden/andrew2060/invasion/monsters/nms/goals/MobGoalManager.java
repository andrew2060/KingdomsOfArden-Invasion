package net.kingdomsofarden.andrew2060.invasion.monsters.nms.goals;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import net.kingdomsofarden.andrew2060.invasion.api.mobactions.MobAction;
import net.minecraft.server.v1_6_R2.EntityInsentient;
import net.minecraft.server.v1_6_R2.PathfinderGoalSelector;

import org.bukkit.craftbukkit.v1_6_R2.entity.CraftLivingEntity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class MobGoalManager {

    HashMap<EntityType,ArrayList<MobAction>> entityActionsMap; 
    public MobGoalManager() {
        this.entityActionsMap = new HashMap<EntityType,ArrayList<MobAction>>();
        try {
            Class<?>[] mobActions = getClasses("net.kingdomsofarden.andrew2060.invasion.api.mobactions.bundled");
            for(Class<?> actionClass: mobActions) {
                try {
                    MobAction toAdd = (MobAction) actionClass.getConstructor(new Class<?>[] {}).newInstance(new Object[] {});
                    for(EntityType type : toAdd.getMobTypes()) {
                        addMobAction(type, toAdd);
                    }
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return;
        }
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
    public boolean registerGoals(EntityType mobType, LivingEntity mob) {
        ArrayList<MobAction> actions = this.entityActionsMap.get(mobType);
        if(actions == null || !(actions.size() > 0)) {
            return false;
        }
        try {
            Field goalSelectorField = EntityInsentient.class.getField("goalSelector");
            goalSelectorField.setAccessible(true);
            PathfinderGoalSelector goalSelector = (PathfinderGoalSelector) goalSelectorField.get(((CraftLivingEntity)mob).getHandle());
            goalSelector.a(1, new PathfinderGoalMobSkillSelector(mob, actions));
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException | ClassCastException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    /**
     * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
     *
     * @param packageName The base package
     * @return The classes
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private static Class<?>[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     * @throws ClassNotFoundException
     */
    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
