package net.kingdomsofarden.andrew2060.invasion.api.mobactions.bundled;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import net.kingdomsofarden.andrew2060.invasion.api.mobactions.MobAction;

public class MobActionGiantSpawnMinions extends MobAction {

    public MobActionGiantSpawnMinions() {
        super(new EntityType[] {EntityType.GIANT});
    }

    @Override
    public boolean checkUsable(LivingEntity executor) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void tick(LivingEntity executor) {
        // TODO Auto-generated method stub
        
    }

}
