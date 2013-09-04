package net.kingdomsofarden.andrew2060.invasion.api.mobskills.bundledActions;

import java.util.Random;

import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.WitherSkull;

import net.kingdomsofarden.andrew2060.invasion.api.mobskills.MobAction;
import net.kingdomsofarden.andrew2060.invasion.util.TargettingUtil;

public class MobActionGiantFireWitherSkulls extends MobAction {
    
    Random rand;
    public MobActionGiantFireWitherSkulls() {
        super(EntityType.GIANT);
        rand = new Random();
    }

    @Override
    public boolean checkUsable(Creature giant) {
        if(!(giant instanceof Giant)) {
            return false;
        }
        Giant g = (Giant)giant;
        if(!(g.getTarget() != null)) {
            
        }
        return false;
    }

    @Override
    public void tick(Creature giant) {
        LivingEntity target = ((Giant)giant).getTarget();
        for(int i = 0; i < rand.nextInt(5) + 1; i++) {
            TargettingUtil.faceEntity(giant, target);
            WitherSkull WitherSkull = giant.launchProjectile(WitherSkull.class);
            WitherSkull.setShooter(giant);
            WitherSkull.setVelocity(target.getLocation().add(rand.nextInt(5)-1,rand.nextInt(5)-7,rand.nextInt(5)-1).subtract(giant.getEyeLocation()).toVector().normalize().multiply(1));
        }
    }

    
    
    
}
