package net.kingdomsofarden.andrew2060.invasion.api.mobskills.bundledActions;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.ItemStack;

import net.kingdomsofarden.andrew2060.invasion.api.MobMinionManager;
import net.kingdomsofarden.andrew2060.invasion.api.mobskills.MobAction;
import net.kingdomsofarden.andrew2060.toolhandler.util.ImprovementUtil;

public class MobActionGiantSpawnMinions extends MobAction {

    Random rand;
    private int tier;
    private ItemStack warriorHelmet;
    private ItemStack warriorChest;
    private ItemStack warriorLeggings;
    private ItemStack warriorBoots;
    private ItemStack archerHelmet;
    private ItemStack archerChest;
    private ItemStack archerLeggings;
    private ItemStack archerBoots;
    private ItemStack sword;
    private ItemStack bow;
    
    public MobActionGiantSpawnMinions() {
        super(EntityType.GIANT);
        this.rand = new Random();
        this.tier = rand.nextInt(5);
        this.warriorHelmet = new ItemStack(Material.DIAMOND_HELMET);
        this.warriorChest = new ItemStack(Material.DIAMOND_CHESTPLATE);
        this.warriorLeggings = new ItemStack(Material.DIAMOND_LEGGINGS);
        this.warriorBoots = new ItemStack(Material.DIAMOND_BOOTS);
        this.archerHelmet = new ItemStack(Material.CHAINMAIL_HELMET);
        this.archerChest = new ItemStack(Material.CHAINMAIL_CHESTPLATE);
        this.archerLeggings = new ItemStack(Material.CHAINMAIL_LEGGINGS);
        this.archerBoots = new ItemStack(Material.CHAINMAIL_BOOTS);
        this.sword = new ItemStack(Material.DIAMOND_SWORD);
        this.bow = new ItemStack(Material.BOW);
    }

    @Override
    public boolean checkUsable(Creature executor) {
        return MobMinionManager.instance.getMinions(executor).size() <= 30;
    }

    @Override
    public void tick(Creature executor) {
        Location loc = executor.getLocation();
        for(int i = 0; i < rand.nextInt(tier) + 1; i++) {
            //Spawn Accompaniment

            Zombie zomb  = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
            if(zomb == null) {

            } else {
                ItemStack warriorHelmet = this.warriorHelmet.clone();
                ItemStack warriorChest = this.warriorChest.clone();
                ItemStack warriorLeggings = this.warriorLeggings.clone();
                ItemStack warriorBoots = this.warriorBoots.clone();
                ItemStack sword = this.sword.clone();
                //Get Items
                warriorChest.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, rand.nextInt(tier)+2);
                warriorChest.addUnsafeEnchantment(Enchantment.THORNS, rand.nextInt(tier) + 2);
                warriorLeggings.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, rand.nextInt(tier)+2);
                warriorBoots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, rand.nextInt(tier) + 2);
                sword.addUnsafeEnchantment(Enchantment.FIRE_ASPECT, rand.nextInt(tier)+2);
                sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, rand.nextInt(1) + 2);
                ImprovementUtil.setQuality(warriorHelmet, rand.nextInt(21) + tier * 20);
                ImprovementUtil.setQuality(warriorChest, rand.nextInt(21) + tier * 20);
                ImprovementUtil.setQuality(warriorLeggings, rand.nextInt(21) + tier * 20);
                ImprovementUtil.setQuality(warriorBoots, rand.nextInt(21) + tier * 20);
                ImprovementUtil.applyEnchantmentLevel(warriorHelmet, Enchantment.PROTECTION_ENVIRONMENTAL);
                ImprovementUtil.applyEnchantmentLevel(warriorChest, Enchantment.PROTECTION_ENVIRONMENTAL);
                ImprovementUtil.applyEnchantmentLevel(warriorLeggings, Enchantment.PROTECTION_ENVIRONMENTAL);
                ImprovementUtil.applyEnchantmentLevel(warriorBoots, Enchantment.PROTECTION_ENVIRONMENTAL);

                zomb.getEquipment().setArmorContents(new ItemStack[] {
                        warriorHelmet,
                        warriorChest,
                        warriorLeggings,
                        warriorBoots
                });

                ImprovementUtil.setQuality(sword, rand.nextInt(21) + tier * 20);
                ImprovementUtil.applyEnchantmentLevel(sword, Enchantment.DAMAGE_ALL);

                zomb.getEquipment().setItemInHand(sword);
                zomb.setCustomName("Undead Warrior");
                zomb.setCustomNameVisible(true);
                zomb.setRemoveWhenFarAway(false);
                MobMinionManager.instance.addMinion(executor,zomb);
            }

            Skeleton skele = (Skeleton) loc.getWorld().spawnEntity(loc, EntityType.SKELETON);
            if(skele == null) {

            } else {
                ItemStack archerHelmet = this.archerHelmet.clone();
                ItemStack archerChest = this.archerChest.clone();
                ItemStack archerLeggings = this.archerLeggings.clone();
                ItemStack archerBoots = this.archerBoots.clone();
                ItemStack bow = this.bow.clone();
                ImprovementUtil.setQuality(archerHelmet, rand.nextInt(21) + tier * 20);
                ImprovementUtil.setQuality(archerChest, rand.nextInt(21) + tier * 20);
                ImprovementUtil.setQuality(archerLeggings, rand.nextInt(21) + tier * 20);
                ImprovementUtil.setQuality(archerBoots, rand.nextInt(21) + tier * 20);
                ImprovementUtil.applyEnchantmentLevel(archerHelmet, Enchantment.PROTECTION_ENVIRONMENTAL);
                ImprovementUtil.applyEnchantmentLevel(archerChest, Enchantment.PROTECTION_ENVIRONMENTAL);
                ImprovementUtil.applyEnchantmentLevel(archerLeggings, Enchantment.PROTECTION_ENVIRONMENTAL);
                ImprovementUtil.applyEnchantmentLevel(archerBoots, Enchantment.PROTECTION_ENVIRONMENTAL);
                archerChest.addUnsafeEnchantment(Enchantment.PROTECTION_PROJECTILE, rand.nextInt(tier)+2);
                archerChest.addUnsafeEnchantment(Enchantment.THORNS, rand.nextInt(tier) + 2);
                archerLeggings.addUnsafeEnchantment(Enchantment.PROTECTION_FIRE, rand.nextInt(tier)+2);
                archerBoots.addUnsafeEnchantment(Enchantment.PROTECTION_FALL, rand.nextInt(tier) + 2);
                if(rand.nextDouble() < 0.10 * tier) {
                    bow.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 1);
                }
                bow.addUnsafeEnchantment(Enchantment.ARROW_KNOCKBACK, rand.nextInt(1) + tier);
                bow.addUnsafeEnchantment(Enchantment.ARROW_FIRE, rand.nextInt(1) + tier);

                skele.getEquipment().setArmorContents(new ItemStack[] {
                        archerHelmet,
                        archerChest,
                        archerLeggings,
                        archerBoots
                });
                ImprovementUtil.setQuality(bow, rand.nextInt(21) + tier * 20);
                ImprovementUtil.applyEnchantmentLevel(sword, Enchantment.ARROW_DAMAGE);
                skele.getEquipment().setItemInHand(bow);
                if(rand.nextInt(tier) >= 2) {
                    skele.setSkeletonType(SkeletonType.WITHER);
                    skele.setCustomName("Undead Veteran Archer");
                } else {
                    skele.setCustomName("Undead Archer");
                }
                skele.setCustomNameVisible(true);
                skele.setRemoveWhenFarAway(false);
                MobMinionManager.instance.addMinion(executor,skele);
            }
        }

    }

}
