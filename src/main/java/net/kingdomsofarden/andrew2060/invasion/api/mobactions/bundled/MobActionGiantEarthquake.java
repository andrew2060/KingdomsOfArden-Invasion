package net.kingdomsofarden.andrew2060.invasion.api.mobactions.bundled;

import java.util.List;
import java.util.Random;

import net.kingdomsofarden.andrew2060.invasion.api.mobactions.MobAction;
import net.kingdomsofarden.andrew2060.toolhandler.ToolHandlerPlugin;

import org.bukkit.Effect;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;


public class MobActionGiantEarthquake implements MobAction {

    private Giant giant;
    private List<LivingEntity> validTargets;
    private Random rand;
    private long lastTick;
    
    public MobActionGiantEarthquake(Giant giant) {
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
        for(LivingEntity lE : validTargets) {
            Vector original = lE.getLocation().toVector();
            Vector to = lE.getLocation().add(0, 1, 0).toVector();
            Vector applied = to.subtract(original).normalize();
            if(lE instanceof Player) {       
                int roll = rand.nextInt(100);
                if(roll < 30) {
                    ToolHandlerPlugin.instance.getPotionEffectHandler().addPotionEffectStacking(PotionEffectType.CONFUSION.createEffect(400,10),lE,false);
                }
            }
            lE.setVelocity(applied);
            lE.getWorld().createExplosion(lE.getLocation().subtract(0, 6, 0), 0F);
            lE.damage(10,giant);
            ToolHandlerPlugin.instance.getPotionEffectHandler().addPotionEffectStacking(PotionEffectType.SLOW.createEffect(30, 1),lE,false);
            lE.getWorld().playEffect(lE.getLocation(), Effect.ZOMBIE_CHEW_WOODEN_DOOR, 1);
        }
    }
}
