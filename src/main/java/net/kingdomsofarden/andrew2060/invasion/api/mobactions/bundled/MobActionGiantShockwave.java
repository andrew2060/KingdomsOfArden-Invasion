package net.kingdomsofarden.andrew2060.invasion.api.mobactions.bundled;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.kingdomsofarden.andrew2060.invasion.api.mobactions.MobAction;

import org.bukkit.Effect;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class MobActionGiantShockwave implements MobAction {

    private Giant giant;
    private List<LivingEntity> validTargets;
    private Random rand;
    private long lastTick;
    
    public MobActionGiantShockwave(Giant giant) {
        this.giant = giant;
        this.rand = new Random();
        this.lastTick = System.currentTimeMillis();
    }

    @Override
    public boolean checkUsable() {
        if(validTargets != null) {
            validTargets = null;
        }
        if(lastTick + 5000 > System.currentTimeMillis()) {
            return false;
        }
        this.validTargets = new LinkedList<LivingEntity>();
        for(Entity e : giant.getNearbyEntities(16, 5, 16)) {
            if(e instanceof LivingEntity) {
                if(e instanceof Player) {
                    validTargets.add((Player)e);
                    continue;
                }
                if(e instanceof Animals) {
                    validTargets.add((Animals)e);
                    continue;
                }
            }
        }
        this.lastTick = System.currentTimeMillis();
        return validTargets.size() > 0;
    }

    @Override
    public void tick() {
        if(rand.nextInt(10) < 7) {
            this.validTargets = null;
            return;
        }
        for(LivingEntity lE : validTargets) {
            if(lE.getLocation().distanceSquared(giant.getLocation()) <= 64) {
                Vector v =lE.getLocation().add(0, 2, 0).toVector().subtract(giant.getLocation().toVector()).normalize();
                v = v.multiply(4);
                v = v.setY(0);
                lE.setVelocity(v);
                lE.getWorld().playEffect(lE.getLocation(), Effect.ZOMBIE_CHEW_WOODEN_DOOR, 1);
            }
        }
    }

}
