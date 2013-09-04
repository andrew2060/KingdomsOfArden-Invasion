package net.kingdomsofarden.andrew2060.invasion.api.mobactions.bundled;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;

import net.kingdomsofarden.andrew2060.invasion.api.mobactions.MobAction;

public class MobActionGiantFireWitherSkulls extends MobAction {
    
    public MobActionGiantFireWitherSkulls() {
        super(new EntityType[] {EntityType.GIANT});
    }

    @Override
    public boolean checkUsable(LivingEntity giant) {
        if(!(giant instanceof Giant)) {
            return false;
        }
        Giant g = (Giant)giant;
        if(!(g.getTarget() != null)) {
            
        }
        return false;
    }

    @Override
    public void tick(LivingEntity giant) {
        
    }

    
    
    
}
