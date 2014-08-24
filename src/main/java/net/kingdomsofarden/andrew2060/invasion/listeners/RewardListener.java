package net.kingdomsofarden.andrew2060.invasion.listeners;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.*;

import com.herocraftonline.heroes.listeners.HEntityListener;
import net.kingdomsofarden.andrew2060.invasion.InvasionPlugin;
import net.kingdomsofarden.andrew2060.invasion.api.IInvasionMob;
import net.kingdomsofarden.andrew2060.invasion.tracker.TrackerStorage;
import net.kingdomsofarden.andrew2060.invasion.util.Config;
import net.kingdomsofarden.andrew2060.invasion.util.Constants;

import net.kingdomsofarden.andrew2060.invasion.util.MonsterUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.events.HeroKillCharacterEvent;
import com.herocraftonline.heroes.characters.CharacterDamageManager;
import com.herocraftonline.heroes.characters.CharacterManager;
import com.herocraftonline.heroes.characters.CharacterTemplate;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.Monster;
import com.herocraftonline.heroes.characters.classes.HeroClass;
import com.herocraftonline.heroes.characters.classes.HeroClass.ExperienceType;
import com.herocraftonline.heroes.characters.effects.CombatEffect.LeaveCombatReason;
import com.herocraftonline.heroes.characters.effects.Effect;
import com.herocraftonline.heroes.characters.effects.common.CombustEffect;
import com.herocraftonline.heroes.characters.effects.common.SummonEffect;
import com.herocraftonline.heroes.util.Properties;
import com.herocraftonline.heroes.util.Util;
import org.bukkit.plugin.RegisteredListener;

public class RewardListener implements Listener {
  
    private Method getEntityHealth;
    private InvasionPlugin plugin;
    private Heroes heroesPlugin;
    private Properties heroesConf;
    private UUID arenaWorld;
    private final double log_conv; // Used to convert between log bases when metadata isn't set

    public RewardListener(InvasionPlugin plugin) {
        this.plugin = plugin;
        this.heroesPlugin = Heroes.getInstance();
        this.heroesConf = Heroes.properties;
        if (Bukkit.getWorld("Arenas") != null) {
            this.arenaWorld = Bukkit.getWorld("Arenas").getUID();
        } else {
            this.arenaWorld = null;
        }

        try {
            this.getEntityHealth = CharacterDamageManager.class.getDeclaredMethod("getEntityMaxHealth", new Class[] {LivingEntity.class});
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        this.getEntityHealth.setAccessible(true);
        this.log_conv = Math.log10(Config.GROWTH_RATE_HEALTH);
        RegisteredListener temp = null;
        for (RegisteredListener l : EntityDeathEvent.getHandlerList().getRegisteredListeners()) {
            if (l.getListener().getClass().equals(HEntityListener.class)) {
                temp = l;
            }
        }
        if (temp != null) {
            EntityDeathEvent.getHandlerList().unregister(temp);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event) {
        
        // Set up constants
        final LivingEntity lE = event.getEntity();
        final boolean player = lE instanceof Player;
        final CharacterManager cM = this.heroesPlugin.getCharacterManager();
        final CharacterTemplate cT = cM.getCharacter(lE);
        
        // We don't want any exp orbs to drop
        event.setDroppedExp(0);
        
        // Determine whether killer was a player - if so fire appropriate event
        final Player attacker = getAttacker(lE.getLastDamageCause());
        if (attacker != null) {
            Bukkit.getPluginManager().callEvent(new HeroKillCharacterEvent(cT, cM.getHero(attacker)));
        }
        
        // Only process rewards for entity deaths with tracker meta keys
        if (lE.hasMetadata(Constants.DAMAGETRACKER_META)) { 
            TrackerStorage storage = (TrackerStorage) lE.getMetadata(Constants.DAMAGETRACKER_META).get(0).value();
            double totalDamage = storage.getTotalDamage();
            if (totalDamage > 0) {
                double baseExp = 0;
                boolean skip = false; // Skip rewards if marked as such or no exp value set
                if (!player) {
                    Monster m = (Monster)cT;
                    baseExp = m.getExperience();
                    if (baseExp == -1) {
                        if (heroesConf.creatureKillingExp.containsKey(lE.getType())) {
                            baseExp = heroesConf.creatureKillingExp.get(lE.getType());
                        } else {
                            skip = true;
                        }
                    }
                    
                    // Anti-spawner checks
                    if (heroesConf.noSpawnCamp && (m.getSpawnReason() == SpawnReason.SPAWNER)) {
                        baseExp *= heroesConf.spawnCampExpMult;
                    }                    
                    
                    // Multiply rewarded exp based on scaling
                    if (lE.hasMetadata("mobscaling.exponent")) {
                        baseExp *= Math.pow(Config.GROWTH_RATE_EXP, lE.getMetadata("mobscaling.exponent").get(0).asDouble());
                    } else {
                        double exponent = Math.log10(lE.getMaxHealth()/getDefaultMaxHealth(lE))/this.log_conv;
                        baseExp *= Math.pow(Config.GROWTH_RATE_EXP, exponent);
                    }
                }
                if (!skip) {
                    if (storage.isDroppable() && !player) {
                        int tier = 1;
                        boolean elite = false;
                        if (lE.hasMetadata("mobscaling.tier") && lE.hasMetadata("mobscaling.elite") ) {
                            tier = lE.getMetadata("mobscaling.tier").get(0).asInt();
                            elite = lE.getMetadata("mobscaling.elite").get(0).asBoolean();
                        } else if (MonsterUtil.getInvasionMob(lE) != null) {
                            IInvasionMob mob = MonsterUtil.getInvasionMob(lE);
                            tier = mob.getTier();
                            elite = mob.getElite();
                        } else {
                            tier = (int) Math.round(Math.log10(lE.getMaxHealth()
                                    /getDefaultMaxHealth(lE))/this.log_conv);
                        }
                        this.plugin.getDropHandler().setDrops(lE.getType(),
                                lE.getLocation().getWorld().getUID(), tier, elite, event.getDrops());
                    } else {
                        if (!player) {
                            event.getDrops().clear();
                        }
                    }
                    HashMap<UUID,Double> contrib = storage.getContributingPlayers();
                    for (UUID id : contrib.keySet()) {
                        Player p = plugin.getServer().getPlayer(id);
                        if (p != null) {
                            double proportion = contrib.get(id) / totalDamage;
                            Hero h = cM.getHero(p);
                            if (h != null) { // TODO: Handle adding EXP back for D/Ced players?
                                // Calculate base exp for PvP kills
                                if (player) {
                                    rewardPVPExp(h, cM.getHero((Player)lE), proportion);
                                    continue;
                                }
                                
                                // Check whether the dying entity is a valid source of exp for this player
                                if (h.isOwnedSummon(lE) || h.getPlayer().equals(lE)) {
                                    continue;
                                }
                                
                                // Calculate and award EXP
                                double amount = baseExp * proportion;
                                h.gainExp(amount, ExperienceType.KILLING, h.getPlayer().getLocation());
                            }
                        }
                    }
                }
            }
        } else {
            if (!player) {
                event.getDrops().clear();
            }
        }
        
        // Handle post-death tasks and cleanup
        if (player) {
            final Hero h = cM.getHero((Player) lE);
            Util.deaths.put(h.getName(), event.getEntity().getLocation());
            
            // Cancel any queued or active effects/skills
            h.cancelDelayedSkill();
            for (final Effect effect : h.getEffects()) {
                if (!effect.isPersistent()) {
                    h.removeEffect(effect);
                }
            }
            
            // Leave combat
            if (h.isInCombat()) {
                if ((lE.getLastDamageCause() != null) && (lE.getLastDamageCause().getCause() != DamageCause.SUICIDE)) {
                    h.leaveCombat(LeaveCombatReason.DEATH);
                } else {
                    h.leaveCombat(LeaveCombatReason.SUICIDE);
                }
            }
            
            // Handle EXP loss due to death
            double lossMult = Heroes.properties.expLoss; // Redundant if statement for some reason (Heroes API)
            if (attacker != null) {
                lossMult = Heroes.properties.pvpExpLossMultiplier;
            }
            h.loseExpFromDeath(lossMult, attacker != null);
  
        } else { // Not a player death, this is easy
            this.heroesPlugin.getCharacterManager().removeMonster(lE); // Mark for removal from manager
            cT.clearEffects();
        }
    }

    private void rewardPVPExp(Hero attacker, Hero defender, double proportion) {
        HeroClass aClass = attacker.getHeroClass();
        HeroClass dClass = defender.getHeroClass();
        
        int dLevel = defender.getLevel(dClass);       
        
        int aTierLevel = getTieredLevel(attacker, aClass); 
        int dTierLevel = getTieredLevel(defender, dClass);
        
        int diff = dTierLevel - aTierLevel;

        double exp = Config.PVP_EXP_BASE * (dClass.getTier() + 1);
        double mod = Config.PVP_EXP_PER_LEVEL_DIFF * diff;
        
        // Halve exp for arenas
        if (defender.getPlayer().getWorld().getUID().equals(this.arenaWorld)) {
            exp *= 0.5;
            mod *= 0.5;
            attacker.getPlayer().sendMessage(ChatColor.GRAY + "50% exp penalty was applied due to kill being"
                    + " in an arena");
        }
        
        if (exp + mod < 0) {
            mod = -exp;
        }
        
        String rank = "§8newbie§7";
        if (dLevel > 0 && dLevel <= 20) {
            rank = "§8newbie§7";
        } else if (dLevel > 20 && dLevel <= 30) {
            rank = "§9apprentice§7";
        } else if (dLevel > 30 && dLevel <= 40) {
            rank = "§3seasoned§7";
        } else if (dLevel > 40 && dLevel <= 50) {
            rank = "§2veteran§7";
        } else if (dLevel > 50 && dLevel < 65) {
            rank = "§6elite§7";
        } else if (dLevel >= 65 && dLevel <75) {
            rank = "§5legendary§7";
        } else if (dLevel == 75) {
            rank = "§4master§7";
        }
        DecimalFormat dF = new DecimalFormat("#0.##");
        DecimalFormat signed = new DecimalFormat("+#0.## ; -#0.##");
        attacker.getPlayer().sendMessage("§7You were awarded " + dF.format(exp * proportion)
                + "(" + signed.format(mod * proportion) + " due to level difference) for killing a " + rank
                + dClass.getName() + " (" + dF.format(proportion * 100) + "% of total damage)");
        attacker.addExp(exp + mod, aClass, defender.getPlayer().getLocation());
        return ;
    }

    private int getTieredLevel(Hero h, HeroClass hClass) {
        return getTieredLevel(h, hClass, 0);
    }

    private int getTieredLevel(Hero h, HeroClass hClass, int sum) {
        if (!hClass.hasNoParents()) {
            for (HeroClass hC : hClass.getParents()) {
                sum += getTieredLevel(h, hC, sum);
            }
        }
        return sum + h.getLevel(hClass);
    }
    
    private double getDefaultMaxHealth(LivingEntity entity) {
        try {
            return (double) this.getEntityHealth.invoke(Heroes.getInstance().getDamageManager(), entity);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
            return entity.getMaxHealth();
        }
    }

    private Player getAttacker(EntityDamageEvent event) {
        if (event == null) {
            return null;
        }
        if (event instanceof EntityDamageByEntityEvent) {
            final Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            if (damager instanceof Player) {
                return (Player) damager;
            } else if (damager instanceof Projectile) {
                final Projectile projectile = (Projectile) damager;
                if (projectile.getShooter() instanceof Player) {
                    return (Player) projectile.getShooter();
                } else if (projectile.getShooter() instanceof Skeleton) { 
                    final CharacterTemplate character = this.heroesPlugin.getCharacterManager().getCharacter((LivingEntity) projectile.getShooter());
                    if (character.hasEffect("Summon")) { // Attribute summoned mob damage to player
                        final SummonEffect summon = (SummonEffect) character.getEffect("Summon");
                        return summon.getSummoner().getPlayer();
                    }
                }
            } else if (damager instanceof LivingEntity) {
                if (damager instanceof Tameable) { // Pets
                    final Tameable attacker = (Tameable) damager;
                    if (attacker.isTamed() && (attacker.getOwner() instanceof Player)) {
                        return (Player) attacker.getOwner();
                    }
                }
                final CharacterTemplate character = this.heroesPlugin.getCharacterManager().getCharacter((LivingEntity) damager);
                if (character.hasEffect("Summon")) { // Attribute summoned mob damage to player
                    final SummonEffect summon = (SummonEffect) character.getEffect("Summon");
                    return summon.getSummoner().getPlayer();
                }
            }
        } else if ((event.getCause() == DamageCause.FIRE_TICK) && (event.getEntity() instanceof LivingEntity)) {
            final CharacterTemplate combusted = this.heroesPlugin.getCharacterManager().getCharacter((LivingEntity) event.getEntity());
            if (combusted.hasEffect("Combust")) { // Handle Combust Effect for kill credit
                return ((CombustEffect) combusted.getEffect("Combust")).getApplier();
            }
        } else if ((event.getCause() == DamageCause.POISON) && (event.getEntity() instanceof LivingEntity)) {
            final CharacterTemplate poisoned = this.heroesPlugin.getCharacterManager().getCharacter((LivingEntity) event.getEntity());
            if (poisoned.hasEffect("Poisoned")) {
                // TODO: Poison
            }
        }
        return null;
    }
}
