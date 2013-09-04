package net.kingdomsofarden.andrew2060.invasion.api.mobactions.bundled;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;

import net.kingdomsofarden.andrew2060.invasion.api.mobactions.MobAction;
import net.kingdomsofarden.andrew2060.toolhandler.util.ImprovementUtil;

public class MobActionGiantSpawnMinions extends MobAction {
    
    List<Monster> minions;
    Random rand;
    private int tier;
    public MobActionGiantSpawnMinions() {
        super(new EntityType[] {EntityType.GIANT});
        this.minions = new LinkedList<Monster>();
        this.rand = new Random();
        this.tier = rand.nextInt(5);
    }

    @Override
    public boolean checkUsable(LivingEntity executor) {
        return minions.size() <= 30;
    }

    @Override
    public void tick(LivingEntity executor) {
        Location loc = executor.getLocation();
        //Get Items
        ItemStack warriorHelmet = new ItemStack(Material.DIAMOND_HELMET);
        ItemStack warriorChest = new ItemStack(Material.DIAMOND_CHESTPLATE);
        ItemStack warriorLeggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        ItemStack warriorBoots = new ItemStack(Material.DIAMOND_BOOTS);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        ItemStack bow = new ItemStack(Material.BOW);
        
        warriorChest.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, rand.nextInt(3)+3);
        warriorChest.addUnsafeEnchantment(Enchantment.THORNS, rand.nextInt(4) + 2);
        warriorBoots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, rand.nextInt(1) + 4);
        sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, rand.nextInt(4)+2);
        sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, rand.nextInt(1) + 2);
        if(rand.nextDouble() < 0.10 * tier) {
            bow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
        }
        bow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, rand.nextInt(1) + tier);
        
        //Spawn Accompaniment
        
        Zombie zomb  = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
        if(zomb == null) {
            
        } else {
            ImprovementUtil.setQuality(warriorHelmet, rand.nextInt(21) + tier * 20);
            ImprovementUtil.setQuality(warriorChest, rand.nextInt(21) + tier * 20);
            ImprovementUtil.setQuality(warriorLeggings, rand.nextInt(21) + tier * 20);
            ImprovementUtil.setQuality(warriorBoots, rand.nextInt(21) + tier * 20);
            ImprovementUtil.applyEnchantmentLevel(warriorHelmet, Enchantment.PROTECTION_ENVIRONMENTAL);
            ImprovementUtil.applyEnchantmentLevel(warriorChest, Enchantment.PROTECTION_ENVIRONMENTAL);
            ImprovementUtil.applyEnchantmentLevel(warriorLeggings, Enchantment.PROTECTION_ENVIRONMENTAL);
            ImprovementUtil.applyEnchantmentLevel(warriorBoots, Enchantment.PROTECTION_ENVIRONMENTAL);
            
            zomb.getEquipment().setArmorContents(new ItemStack[] {
                    warriorHelmet.clone(),
                    warriorChest.clone(),
                    warriorLeggings.clone(),
                    warriorBoots.clone()
            });
            
            ImprovementUtil.setQuality(sword, rand.nextInt(21) + tier * 20);
            ImprovementUtil.applyEnchantmentLevel(sword, Enchantment.DAMAGE_ALL);

            zomb.getEquipment().setItemInHand(sword.clone());
            zomb.setCustomName("Undead Warrior");
            zomb.setCustomNameVisible(true);
            zomb.setRemoveWhenFarAway(false);
            minions.add(zomb);
        }
    
        Skeleton skele = (Skeleton) loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
        if(skele == null) {
        
        } else {
            
            ImprovementUtil.setQuality(warriorHelmet, rand.nextInt(21) + tier * 20);
            ImprovementUtil.setQuality(warriorChest, rand.nextInt(21) + tier * 20);
            ImprovementUtil.setQuality(warriorLeggings, rand.nextInt(21) + tier * 20);
            ImprovementUtil.setQuality(warriorBoots, rand.nextInt(21) + tier * 20);
            ImprovementUtil.applyEnchantmentLevel(warriorHelmet, Enchantment.PROTECTION_ENVIRONMENTAL);
            ImprovementUtil.applyEnchantmentLevel(warriorChest, Enchantment.PROTECTION_ENVIRONMENTAL);
            ImprovementUtil.applyEnchantmentLevel(warriorLeggings, Enchantment.PROTECTION_ENVIRONMENTAL);
            ImprovementUtil.applyEnchantmentLevel(warriorBoots, Enchantment.PROTECTION_ENVIRONMENTAL);

            skele.getEquipment().setArmorContents(new ItemStack[] {
                    warriorHelmet.clone(),
                    warriorChest.clone(),
                    warriorLeggings.clone(),
                    warriorBoots.clone()
            });
            ImprovementUtil.setQuality(bow, rand.nextInt(21) + tier * 20);
            ImprovementUtil.applyEnchantmentLevel(sword, Enchantment.ARROW_DAMAGE);
            
            skele.getEquipment().setItemInHand(bow);
            skele.setCustomName("Undead Archer");
            skele.setCustomNameVisible(true);
            skele.setRemoveWhenFarAway(false);
            minions.add(skele);
        }
        
    }

}
