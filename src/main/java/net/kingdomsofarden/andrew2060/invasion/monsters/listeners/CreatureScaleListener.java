package net.kingdomsofarden.andrew2060.invasion.monsters.listeners;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.SecureRandom;
import java.util.Random;

import net.kingdomsofarden.andrew2060.invasion.InvasionPlugin;
import net.kingdomsofarden.andrew2060.invasion.util.Config;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
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
import com.herocraftonline.heroes.api.events.WeaponDamageEvent;
import com.herocraftonline.heroes.characters.CharacterDamageManager;
import com.herocraftonline.heroes.characters.Hero;
import com.herocraftonline.heroes.characters.classes.HeroClass.ExperienceType;

public class CreatureScaleListener implements Listener {

    private Random rand;
    private InvasionPlugin plugin;

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
        this.rand = new SecureRandom();
        this.plugin = plugin;
        try {
            this.getEntityHealth = CharacterDamageManager.class.getDeclaredMethod("getEntityMaxHealth", new Class[] {LivingEntity.class});
        } catch (NoSuchMethodException | SecurityException e) {
            e.printStackTrace();
        }
        this.getEntityHealth.setAccessible(true);
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled = true) 
    public void antiArmorListener(WeaponDamageEvent event) {
        if (event.getEntity() instanceof Monster) {
            Monster m = (Monster)event.getEntity();
            int armorSum = 0;
            for (ItemStack item : m.getEquipment().getArmorContents()) {

                switch (item.getType()) {
                case DIAMOND_HELMET:
                    armorSum += 2;
                    break; 
                case DIAMOND_CHESTPLATE:
                    armorSum += 5;
                    break;
                case DIAMOND_LEGGINGS:
                    armorSum += 4;
                    break;
                case DIAMOND_BOOTS:
                    armorSum += 1;
                    break;
                case CHAINMAIL_HELMET: 
                    armorSum += 2;
                    break;
                case CHAINMAIL_CHESTPLATE:
                    armorSum += 5;
                    break;
                case CHAINMAIL_LEGGINGS:
                    armorSum += 4;
                    break;
                case CHAINMAIL_BOOTS:
                    armorSum += 1;
                    break;
                case IRON_HELMET:
                    armorSum += 2;
                    break;
                case IRON_CHESTPLATE:
                    armorSum += 6;
                    break;
                case IRON_LEGGINGS:
                    armorSum += 5;
                    break;
                case IRON_BOOTS: 
                    armorSum += 2;
                    break;
                case GOLD_HELMET:
                    armorSum += 2;
                    break;
                case GOLD_CHESTPLATE:
                    armorSum += 5;
                    break;
                case GOLD_LEGGINGS: 
                    armorSum += 3;
                case GOLD_BOOTS: 
                    armorSum += 1;
                case LEATHER_HELMET:
                    armorSum += 1;
                    break;
                case LEATHER_CHESTPLATE: 
                    armorSum += 3;
                    break;
                case LEATHER_LEGGINGS: 
                    armorSum += 2;
                    break;
                case LEATHER_BOOTS: 
                    armorSum += 1;
                    break;
                default:
                    break;
                }
            }
            double damage = event.getDamage();
            double preArmor = 25D/(25 - armorSum) * damage;
            event.setDamage(preArmor);
        }
    }

    @EventHandler(priority=EventPriority.HIGHEST, ignoreCancelled = true) 
    public void onMobDamage(WeaponDamageEvent event) {
        if (!(event.getDamager() instanceof Hero)) {
            LivingEntity ent = event.getDamager().getEntity();
            if (!ent.hasMetadata("mobscaling.exponent")) {
                if (!(ent instanceof Monster || ent instanceof Ghast || ent instanceof Slime || ent instanceof Golem || ent instanceof EnderDragon)) {
                    return;
                }
                double exponent = (Math.log10(ent.getMaxHealth()/getDefaultMaxHealth(ent))/Math.log10(Config.GROWTH_RATE_HEALTH));
                int tier = Math.round(Math.round(exponent));
                if (tier == 0) {
                    return;
                }
                ent.setMetadata("mobscaling.tier", new FixedMetadataValue(plugin, tier));
                ent.setMetadata("mobscaling.exponent", new FixedMetadataValue(plugin, exponent));
                ent.setMetadata("mobscaling.elite", new FixedMetadataValue(plugin, false));
            }

            double exponent = ent.getMetadata("mobscaling.exponent").get(0).asDouble();
            event.setDamage(event.getDamage() * Math.pow(Config.GROWTH_RATE_DMG , exponent));
        }
    }

    @EventHandler(priority=EventPriority.MONITOR, ignoreCancelled = true)
    public void onMonsterSpawn(CreatureSpawnEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Monster || entity instanceof Ghast || entity instanceof Slime || entity instanceof Golem || entity instanceof EnderDragon)) {
            return;
        }
        if (entity.hasMetadata("mobscaling.ignore")) {
            return;
        }
        EntityType type = entity.getType();
        Location spawnLoc = entity.getLocation();
        Location origin = new Location(spawnLoc.getWorld(), 0, 64, 0);
        double modifier = spawnLoc.distanceSquared(origin);
        double exponent = 0.00;
        if (spawnLoc.getWorld().getEnvironment().equals(Environment.NETHER)) {
            exponent = Math.sqrt(modifier/Config.GROWTH_PER_SQUARED_NETHER);
            exponent += 0.5;
        } else {
            exponent = Math.sqrt(modifier/Config.GROWTH_PER_SQUARED_NORMAL);
        }
        boolean elite = rand.nextDouble() <= Config.ELITE_MOB_CHANCE;
        int tier = (int) Math.round(exponent);
        if (entity.getCustomName() == null && (entity instanceof Monster || entity instanceof Ghast)) {
            entity.setCustomName(getCustomName(type,tier,elite));  
            if (elite) {
                entity.setCustomNameVisible(true);
            }
        }
        if (exponent <= 1 && !elite) {
            return;
        }
        if (exponent < 1) {
            exponent = 1;
        }
        if (elite) {
            exponent += 0.5;
            tier += 1;
        }
        double multiplicand = Math.pow(Config.GROWTH_RATE_HEALTH, exponent);
        double health = entity.getMaxHealth() * multiplicand;
        entity.setMaxHealth(health);
        entity.setHealth(entity.getMaxHealth());
        if (type == EntityType.ZOMBIE || type == EntityType.PIG_ZOMBIE) {
            switch (tier) {

            case 0: {
                if (rand.nextDouble() < 0.3) {
                    entity.getEquipment().setArmorContents(leather);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.WOOD_SWORD));
                }
                break;
            }
            case 1: {
                if (rand.nextDouble() < 0.3) {
                    entity.getEquipment().setArmorContents(leather);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.WOOD_SWORD));
                }
                break;
            }
            case 2: {
                if (rand.nextDouble() < 0.3) {
                    entity.getEquipment().setArmorContents(gold);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.STONE_SWORD));
                } else {
                    entity.getEquipment().setArmorContents(leather);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.STONE_AXE));
                }
                break;
            }
            case 3: {
                if (rand.nextDouble() < 0.3) {
                    entity.getEquipment().setArmorContents(iron);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.IRON_SWORD));
                } else {
                    entity.getEquipment().setArmorContents(gold);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.IRON_AXE));
                }
                break;
            }
            case 4: {
                if (rand.nextDouble() < 0.3) {
                    entity.getEquipment().setArmorContents(diamond);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                } else {
                    entity.getEquipment().setArmorContents(iron);
                    entity.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_AXE));
                }
                break;
            }
            case 5: {
                if (rand.nextDouble() < 0.3) {
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
            default: {
                entity.getEquipment().setArmorContents(diamond);
                entity.getEquipment().setItemInHand(new ItemStack(Material.DIAMOND_SWORD));
                break;
            }

            }
        }
        if (type == EntityType.SKELETON) {
            switch (tier) {

            case 0: {
                if (rand.nextDouble() < 0.1) {
                    entity.getEquipment().setArmorContents(leather);
                }
                break;
            }
            case 1: {
                if (rand.nextDouble() < 0.3) {
                    entity.getEquipment().setArmorContents(leather);
                }
                break;
            }
            case 2: {
                if (rand.nextDouble() < 0.3) {
                    entity.getEquipment().setArmorContents(leather);
                }

                break;
            }
            case 3: {
                if (rand.nextDouble() < 0.3) {
                    entity.getEquipment().setArmorContents(leather);
                } else {
                    entity.getEquipment().setArmorContents(gold);
                }

                break;
            }
            case 4: {
                if (rand.nextDouble() < 0.3) {
                    entity.getEquipment().setArmorContents(gold);
                } else {
                    entity.getEquipment().setArmorContents(leather);
                }

                break;
            }
            case 5: {
                if (rand.nextDouble() < 0.3) {
                    entity.getEquipment().setArmorContents(gold);
                } else {
                    entity.getEquipment().setArmorContents(chainmail);
                }

                break;
            }
            case 6: {
                if (rand.nextDouble() < 0.3) {
                    entity.getEquipment().setArmorContents(chainmail);
                } else {
                    entity.getEquipment().setArmorContents(gold);
                }

                break;
            }
            default: {
                entity.getEquipment().setArmorContents(chainmail);
                break;
            }

            }
        }
        float dropchance = 0.01F;
        if (elite) {
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



    private String getCustomName(EntityType type, int tier, boolean crit) {

        StringBuilder name = new StringBuilder();
        if (crit) {
            name.append(ChatColor.DARK_RED + "Arch ");
        }

        if (tier <= Config.MOB_NAMES.length && tier > 0) {
            name.append(Config.MOB_NAMES[tier - 1]);
        } else if (tier > Config.MOB_NAMES.length) {
            name.append(Config.MOB_NAMES[Config.MOB_NAMES.length - 1]);
        } else {
            name.append(Config.MOB_NAMES[0]);
        }

        name.append(" ");

        switch (type) {

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
            name.append("Voidwalker");
            break;
        case GHAST:
            name.append("Wraith");
            break;
        case GIANT:
            name.append("Juggernaut");
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

    private double getDefaultMaxHealth(LivingEntity entity) {
        try {
            return (double) this.getEntityHealth.invoke(Heroes.getInstance().getDamageManager(), entity);
        } catch (IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
            return entity.getMaxHealth();
        }
    }




}
