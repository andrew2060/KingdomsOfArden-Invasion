package net.kingdomsofarden.andrew2060.invasion.monsters;

import net.minecraft.server.v1_6_R2.EntityInsentient;

import org.bukkit.entity.EntityType;

public class NMSEntityManager {
    
    private String name;
    private int id;
    private EntityType entityType;
    private Class<? extends EntityInsentient> nmsClass;
    private Class<? extends EntityInsentient> customClass;

    private NMSEntityManager(String name, int id, EntityType entityType, Class<? extends EntityInsentient> nmsClass, Class<? extends EntityInsentient> customClass){
        this.name = name;
        this.id = id;
        this.entityType = entityType;
        this.nmsClass = nmsClass;
        this.customClass = customClass;
    }

    public String getName(){
        return this.name;
    }

    public int getID(){
        return this.id;
    }

    public EntityType getEntityType(){
        return this.entityType;
    }

    public Class<? extends EntityInsentient> getNMSClass(){
        return this.nmsClass;
    }

    public Class<? extends EntityInsentient> getCustomClass(){
        return this.customClass;
    }
}
