package net.kingdomsofarden.andrew2060.invasion.tracker.listeners;

import com.herocraftonline.heroes.api.events.CharacterDamageEvent;
import com.herocraftonline.heroes.api.events.SkillDamageEvent;
import com.herocraftonline.heroes.api.events.WeaponDamageEvent;
import net.kingdomsofarden.andrew2060.invasion.InvasionPlugin;
import net.kingdomsofarden.andrew2060.invasion.tracker.DamageInfo;
import net.kingdomsofarden.andrew2060.invasion.tracker.TrackerKey;
import net.kingdomsofarden.andrew2060.invasion.tracker.TrackerStorage;
import net.kingdomsofarden.andrew2060.invasion.tracker.TrackerValue;
import net.kingdomsofarden.andrew2060.invasion.util.Constants;
import net.kingdomsofarden.andrew2060.invasion.util.NMSInterface;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.metadata.FixedMetadataValue;

public class TrackerListener {
    
    private InvasionPlugin instance;
        
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
    public void onWeaponDamage(WeaponDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            double damage = event.getDamage();
            if (event.getCause().equals(DamageCause.ENTITY_ATTACK)) {
                damage = NMSInterface.getPostArmorDamage((LivingEntity) event.getEntity(), damage);
            }
            applyDamageMetadata(event.getCause(), event.getDamager().getEntity(), event.getEntity(), damage);
        }
    }
    
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
    public void onSkillDamage(SkillDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            applyDamageMetadata(DamageCause.MAGIC, event.getDamager().getEntity(), event.getEntity(), event.getDamage());
        }
    }
    
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
    public void onCharacterDamage(CharacterDamageEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            if (event.getCause().equals(DamageCause.FIRE_TICK) || event.getCause().equals(DamageCause.FIRE)) {
                
            }
            applyDamageMetadata(event.getCause(), null, event.getEntity(), event.getDamage());
        }
    }

    public void applyDamageMetadata(DamageCause cause, Entity damager, Entity damaged, double damage) {
        if (!damaged.hasMetadata(Constants.DAMAGETRACKER_META)) { //Create new meta tracking
            TrackerStorage obj = new TrackerStorage();
            damaged.setMetadata(Constants.DAMAGETRACKER_META, new FixedMetadataValue(InvasionPlugin.instance, obj));
        }

        TrackerStorage val = (TrackerStorage) damaged.getMetadata(Constants.DAMAGETRACKER_META).get(0).value();
        String key = TrackerKey.getDamageTrackerKey(cause, damager);
        if (val.containsKey(key)) {
            val.get(key).push(new DamageInfo(damage));
            return;
        } else {
            val.put(key, new TrackerValue(instance));
            val.get(key).add(new DamageInfo(damage));
            return;
        }
        
    }
    
}
    