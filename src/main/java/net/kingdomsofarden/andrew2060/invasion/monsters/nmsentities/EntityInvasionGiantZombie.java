package net.kingdomsofarden.andrew2060.invasion.monsters.nmsentities;

import net.minecraft.server.v1_6_R2.Entity;
import net.minecraft.server.v1_6_R2.EntityGiantZombie;
import net.minecraft.server.v1_6_R2.EntityHuman;
import net.minecraft.server.v1_6_R2.World;

public class EntityInvasionGiantZombie extends EntityGiantZombie {

    public EntityInvasionGiantZombie(World world) {
        super(world);
    }
    
    //Increase the giant's aggro range
    protected Entity findTarget() {
        if(super.findTarget() != null) {
            return super.findTarget();
        } else {
            EntityHuman entityhuman = this.world.findNearbyVulnerablePlayer(this, 32.0D);
            return entityhuman != null && this.o(entityhuman) ? entityhuman : null;
        }
    }

}
