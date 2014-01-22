package net.kingdomsofarden.andrew2060.invasion.monsters.listeners;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.kingdomsofarden.andrew2060.invasion.InvasionPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Golem;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.herocraftonline.heroes.Heroes;
import com.herocraftonline.heroes.api.events.ExperienceChangeEvent;
import com.herocraftonline.heroes.api.events.HeroKillCharacterEvent;
import com.herocraftonline.heroes.api.events.WeaponDamageEvent;
import com.herocraftonline.heroes.characters.CharacterDamageManager;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.classes.HeroClass.ExperienceType;

public class CreatureScaleListener implements Listener {
    
    private Random rand;
    private InvasionPlugin plugin;
    private Heroes heroes;    
    
    private Method getEntityHealth;
    
    private static ItemStack[] diamond;
    private static ItemStack[] chainmail;
    private static ItemStack[] iron;
    private static ItemStack[] gold;
    private static ItemStack[] leather;

    static {
        diamond = new ItemStack[] {new ItemStack(Material.DIAMOND_HELMET), new ItemStack(Material.DIAMOND_CHESTPLATE), new ItemStack(Material.DIAMOND_LEGGINGS), new ItemStack(Material.DIAMOND_BOOTS)};
        chainmail = new ItemStack[] {new ItemStack(Material.CHAINMAIL_HELMET), new ItemStack(Material.CHAINMAIL_CHESTPLATE), new ItemStack(Material.CHAINMAIL_LEGGINGS), new ItemStack(Material.CHAINMAIL_BOOTS)};
        iron = new ItemStack[] {new ItemStack(Material.IRON_HELMET), new ItemStack(Material.IRON_CHESTPLATE), new ItemStack(Material.IRON_LEGGINGS), new ItemStack(Material.IRON_BOOTS)};
        gold = new ItemStack[] {new ItemStack(Material.GOLD_HELMET), new ItemStack(Material.GOLD_CHESTPLATE), new ItemStack(Material.GOLD_LEGGINGS), new ItemStack(Material.GOLD_BOOTS)};
        leather = new ItemStack[] {new ItemStack(Material.LEATHER_HELMET), new ItemStack(Material.LEATHER_CHESTPLATE), new ItemStack(Material.LEATHER_LEGGINGS), new ItemStack(Material.LEATHER_BOOTS)};
    }
    
    public CreatureScaleListener(InvasionPlugin plugin) {
        this.rand = new Random();
        this.plugin = plugin;
        try {
            this.getEntityHealth = CharacterDamageManager.class.getDeclaredMethod("getEntityMaxHealth", new Class[] {LivingEntity.class});
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        this.getEntityHealth.setAccessible(true);
        this.heroes = (Heroes)plugin.getServer().getPluginManager().getPlugin("Heroes");
    }
    
    //Handle Damage Scaling
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled = true) 
    public void onMobDamage(WeaponDamageEvent event) {
        if(!(event.getDamager() instanceof Hero)) {
            LivingEntity ent = event.getDamager().getEntity();
            if(!ent.hasMetadata("mobscaling.exponent")) {
                if(!(ent instanceof Monster || ent instanceof Ghast || ent instanceof Slime || ent instanceof Golem || ent instanceof EnderDragon)) {
                    return;
                }
                double exponent = (Math.log10(ent.getMaxHealth()/getDefaultMaxHealth(ent))/Math.log10(1.33));
                int tier = Math.round(Math.round(exponent));
                if(tier == 0) {
                    return;
                }
                ent.setMetadata("mobscaling.tier", new FixedMetadataValue(plugin, tier));
                ent.setMetadata("mobscaling.exponent", new FixedMetadataValue(plugin, exponent));
                ent.setMetadata("mobscaling.elite", new FixedMetadataValue(plugin, false));
            }
            
            double exponent = ent.getMetadata("mobscaling.exponent").get(0).asDouble();
            event.setDamage(event.getDamage() * Math.pow(1.2 , exponent));
        }
    }
    
    //Handle EXP Scaling
    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMobExpDrop(ExperienceChangeEvent event) {
        if(event.getSource() != ExperienceType.KILLING){
            return;
        }
        if(event.getHero().getPlayer().hasMetadata("mobscaling.expreward.exponent")) {
            double exponent = event.getHero().getPlayer().getMetadata("mobscaling.expreward.exponent").get(0).asDouble();
            event.setExpGain(event.getExpChange() * Math.pow(1.1 , exponent));
            event.getHero().getPlayer().removeMetadata("mobscaling.expreward.exponent",plugin);
        }
        return;
    }
    
    
    //Handle Spawn Scaling
    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
    public void onMonsterSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        if(!(entity instanceof Monster || entity instanceof Ghast || entity instanceof Slime || entity instanceof Golem || entity instanceof EnderDragon)) {
            return;
        }
        if(entity.hasMetadata("mobscaling.ignore")) {
            return;
        }
        EntityType type = entity.getType();
        Location spawnLoc = entity.getLocation();
        Location origin = new Location(spawnLoc.getWorld(), 0, 64, 0);
        double modifier = spawnLoc.distanceSquared(origin);
        double exponent = 0.00;
        if(spawnLoc.getWorld().getEnvironment().equals(Environment.NETHER)) {
            exponent = Math.sqrt(modifier/1562500D);
            exponent += 0.5;
        } else {
            exponent = Math.sqrt(modifier/100000000D);
        }
        boolean elite = rand.nextInt(500) <= 1;
        int tier = Math.round(Math.round(exponent));
        if(entity.getCustomName() == null && (entity instanceof Monster || entity instanceof Ghast)) {
            entity.setCustomName(getCustomName(type,tier,elite));  
            if(elite) {
                entity.setCustomNameVisible(true);
            }
        }
        if(exponent <= 1 && !elite) {
            return;
        }
        if(exponent < 1) {
            exponent = 1;
        }
        if(elite) {
            exponent += 0.5;
            tier += 1;
        }
        double multiplicand = Math.pow(1.33, exponent);
        double health = entity.getMaxHealth() * multiplicand;
        entity.setMaxHealth(health);
        entity.setHealth(entity.getMaxHealth());
        if(type == EntityType.ZOMBIE || type == EntityType.PIG_ZOMBIE) {
            switch(tier) {
            
            case 0: {
                if(rand.nextInt(100) < 30) {
                    entity.getEquipment().setArmorContents(leather);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.WOOD_SWORD));
                }
                break;
            }
            case 1: {
                int random = rand.nextInt(100);
                if(random < 70) {
                    entity.getEquipment().setArmorContents(leather);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.WOOD_SWORD));
                }
                break;
            }
            case 2: {
                if(rand.nextInt(100) < 70) {
                    entity.getEquipment().setArmorContents(gold);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.STONE_SWORD));
                } else {
                    entity.getEquipment().setArmorContents(leather);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.STONE_AXE));
                }
                break;
            }
            case 3: {
                if(rand.nextInt(100) < 70) {
                    entity.getEquipment().setArmorContents(iron);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.IRON_SWORD));
                } else {
                    entity.getEquipment().setArmorContents(gold);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.IRON_AXE));
                }
                break;
            }
            case 4: {
                if(rand.nextInt(100) < 30) {
                    entity.getEquipment().setArmorContents(diamond);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                } else {
                    entity.getEquipment().setArmorContents(iron);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_AXE));
                }
                break;
            }
            case 5: {
                if(rand.nextInt(100) < 50) {
                    entity.getEquipment().setArmorContents(diamond);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                } else {
                    entity.getEquipment().setArmorContents(iron);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_AXE));
                }
                break;
            }
            case 6: {
                entity.getEquipment().setArmorContents(diamond);
                entity.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                break;
            }
            case 7: {
                entity.getEquipment().setArmorContents(diamond);
                entity.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                break;
            }
            case 8:
            case 9:
            case 10:
            case 11: 
            case 12: 
            case 13: {
                entity.getEquipment().setArmorContents(diamond);
                entity.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                break;
            }
            default: {
                
                break;
            }
            
            }
        }
        if(type == EntityType.SKELETON) {
            switch(tier) {
            
            case 0: {
                if(rand.nextInt(100) < 10) {
                    entity.getEquipment().setArmorContents(leather);
                }
                break;
            }
            case 1: {
                int random = rand.nextInt(100);
                if(random < 30) {
                    entity.getEquipment().setArmorContents(leather);
                }
                break;
            }
            case 2: {
                if(rand.nextInt(100) < 70) {
                    entity.getEquipment().setArmorContents(leather);
                }
                
                break;
            }
            case 3: {
                if(rand.nextInt(100) < 70) {
                    entity.getEquipment().setArmorContents(leather);
                } else {
                    entity.getEquipment().setArmorContents(gold);
                }
                
                break;
            }
            case 4: {
                if(rand.nextInt(100) < 70) {
                    entity.getEquipment().setArmorContents(gold);
                } else {
                    entity.getEquipment().setArmorContents(leather);
                }
                
                break;
            }
            case 5: {
                if(rand.nextInt(100) < 70) {
                    entity.getEquipment().setArmorContents(gold);
                } else {
                    entity.getEquipment().setArmorContents(chainmail);
                }
                
                break;
            }
            case 6: {
                if(rand.nextInt(100) < 70) {
                    entity.getEquipment().setArmorContents(chainmail);
                } else {
                    entity.getEquipment().setArmorContents(gold);
                }
                               
                break;
            }
            case 7:
            case 8:
            case 9:
            case 10:
            case 11: 
            case 12: 
            case 13: {
                entity.getEquipment().setArmorContents(chainmail);
                break;
            }
            default: {
                
                break;
            }
            
            }
        }
        float dropchance = 0.01F;
        if(elite) {
            dropchance += 0.5;
        }
        entity.getEquipment().setHelmetDropChance(dropchance);
        entity.getEquipment().setChestplateDropChance(dropchance);
        entity.getEquipment().setLeggingsDropChance(dropchance);
        entity.getEquipment().setBootsDropChance(dropchance);
        entity.getEquipment().setItemInHandDropChance(dropchance);
        entity.setMetadata("mobscaling.exponent", new FixedMetadataValue(this.plugin, exponent));
        entity.setMetadata("mobscaling.tier", new FixedMetadataValue(this.plugin, tier));
        entity.setMetadata("mobscaling.elite", new FixedMetadataValue(this.plugin, elite));
    }
    
    //Handle Rewards and Player Tagging
    @EventHandler(priority=EventPriority.HIGHEST) 
    public void onHeroKillCharacterReward(HeroKillCharacterEvent event) {
        LivingEntity defender = event.getDefender().getEntity();
        if(defender.hasMetadata("mobscaling.exponent")) {
            double exponent = defender.getMetadata("mobscaling.exponent").get(0).asDouble();
            int tier = defender.getMetadata("mobscaling.tier").get(0).asInt();
            processDrops(defender, tier);
            event.getAttacker().getPlayer().setMetadata("mobscaling.expreward.exponent", new FixedMetadataValue(this.plugin, exponent));
            event.getAttacker().getPlayer().setMetadata("mobscaling.loot.exponent", new FixedMetadataValue(this.plugin, exponent));
        } else {
            if(!(defender instanceof Monster || defender instanceof Ghast || defender instanceof Slime || defender instanceof Golem || defender instanceof EnderDragon)) {
                return;
            }
            double exponent = (Math.log10(defender.getMaxHealth()/getDefaultMaxHealth(defender))/Math.log10(1.33));
            int tier = Math.round(Math.round(exponent));
            if(tier == 0) {
                return;
            }
            defender.setMetadata("mobscaling.tier", new FixedMetadataValue(plugin, tier));
            defender.setMetadata("mobscaling.exponent", new FixedMetadataValue(plugin, exponent));
            defender.setMetadata("mobscaling.elite", new FixedMetadataValue(plugin, false));
            processDrops(defender, tier);
            event.getAttacker().getPlayer().setMetadata("mobscaling.expreward.exponent", new FixedMetadataValue(this.plugin, exponent));
            event.getAttacker().getPlayer().setMetadata("mobscaling.loot.exponent", new FixedMetadataValue(this.plugin, exponent));
        }
        if(defender instanceof Monster || defender instanceof Ghast) {
            if(defender.hasMetadata("mobscaling.exponent")) {
                
            }
        }
        return;
    }

    //Mob Name Generator
    private String getCustomName(EntityType type, int tier, boolean crit) {
        
        StringBuilder name = new StringBuilder();
        if(crit) {
            name.append(ChatColor.DARK_RED + "Elite ");
        }
        switch(tier) {
        
        case 0: {
            name.append("Zealot ");
            break;
        }
        case 1: {
            name.append("Herald ");
            break;
        }
        case 2: {
            name.append("Reaver ");
            break;
        }
        case 3: {
            name.append("Revenant ");
            break;
        }
        case 4: {
            name.append("Tyrannius ");
            break;
        }
        case 5: {
            name.append("Corpior ");
            break;
        }
        case 6: {
            name.append("Archon ");
            break;
        }
        case 7: {
            name.append("Imperatus ");
            break;
        }
        case 8: {
            name.append("Cherubis ");
            break;
        }
        case 9: {
            name.append("Nephilis ");
            break;
        }
        case 10: {
            name.append("Seraphis ");
            break;  
        }
        case 11: {
            name.append("Harbinger ");
            break;
        }
        case 12: 
        case 13: {
            name.append("Dominion ");
            break;
        }
        default: {
            break;
        }
        
        }
        
        switch(type) {
        
        case BLAZE:
            name.append("Jotunn");
            break;
        case CAVE_SPIDER:
            name.append("Carapace");
            break;
        case CREEPER:
            name.append("Garuda");
            break;
        case ENDERMAN:
            name.append("Leviathan");
            break;
        case GHAST:
            name.append("Wraith");
            break;
        case GIANT:
            name.append("Naglfar");
            break;
        case PIG_ZOMBIE:
            name.append("Lich");
            break;
        case SKELETON:
            name.append("Draugr");
            break;
        case SPIDER:
            name.append("Arachnen");
            break;
        case WITCH:
            name.append("Harpy");
            break;
        case WITHER:
            name.append("Naga");
            break;
        case ZOMBIE:
            name.append("Ghoul");
            break;
        default:
            return null;
            
        }
        
        return name.toString();
    }
    
    //Get heroes specific default max health
    private double getDefaultMaxHealth(LivingEntity entity) {
        try {
            return (double) this.getEntityHealth.invoke(heroes.getDamageManager(), entity);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
            return entity.getMaxHealth();
        }
    }
    
    //Process Drops
    private void processDrops(LivingEntity defender, int tier) {
        Location deathLoc = defender.getLocation();
        World world = deathLoc.getWorld();
        List<ItemStack> loot = new ArrayList<ItemStack>(10);
        //TODO: Add Loot Drops
        for(ItemStack lootItem : loot) {
            world.dropItemNaturally(deathLoc, lootItem);
        }
    }
    
}
