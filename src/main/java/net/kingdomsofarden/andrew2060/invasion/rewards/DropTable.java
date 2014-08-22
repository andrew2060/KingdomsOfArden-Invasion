package net.kingdomsofarden.andrew2060.invasion.rewards;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class DropTable {

    private final DropHandler config;
    private final Random rand;
    private boolean replace;
    private HashMap<UUID,List<DropItem>> drops;
    private HashMap<UUID,List<DropGroup>> groups;


    public DropTable(DropHandler config, ConfigurationSection dropConfig) {
        this.config = config;
        this.drops = new HashMap<UUID,List<DropItem>>();
        this.groups = new HashMap<UUID,List<DropGroup>>();
        this.rand = new SecureRandom();
        for (World w : Bukkit.getWorlds()) {
            this.drops.put(w.getUID(), new LinkedList<DropItem>());
            this.groups.put(w.getUID(), new LinkedList<DropGroup>());
        }
        this.replace = dropConfig.getBoolean("replace");
        for (String key : dropConfig.getConfigurationSection("worlds").getKeys(false)) {
            if (key.equalsIgnoreCase("all")) {
                List<DropItem> items = this.loadItems(dropConfig.getConfigurationSection("worlds.all"));
                for (UUID id : this.drops.keySet()) {
                    this.drops.get(id).addAll(items);
                }
                List<DropGroup> group = this.loadGroups(dropConfig.getConfigurationSection("worlds.all"));
                for (UUID id : this.groups.keySet()) {
                    this.groups.get(id).addAll(group);
                }
            } else {
                World w = Bukkit.getWorld(key);
                if (w == null) {
                    try {
                        throw new IllegalArgumentException("Cannot load world " + key + "as it does not exist!");
                    } catch (Exception e) {
                        continue;
                    }
                } else {
                    List<DropItem> items = this.loadItems(dropConfig.getConfigurationSection("worlds." + key));
                    this.drops.get(w.getUID()).addAll(items);
                    List<DropGroup> group = this.loadGroups(dropConfig.getConfigurationSection("worlds." + key));
                    this.groups.get(w.getUID()).addAll(group);
                }
            }
        }
    }

    private List<DropGroup> loadGroups(ConfigurationSection worldSection) {
        List<DropGroup> add = new LinkedList<DropGroup>();
        for (String key : worldSection.getStringList("groups"))  {
            DropGroup group = this.config.getGroup(key);
            if (group == null) {
                throw new IllegalArgumentException("Group " + key + " not found!");
            } else {
                add.add(group);
            }
        }
        return add;
    }

    private List<DropItem> loadItems(ConfigurationSection worldSection) {
        List<DropItem> add = new LinkedList<DropItem>();
        for (String entry : worldSection.getStringList("vanilla"))  {
            try {
                String[] parse = entry.split(" ");
                Material mat = Material.valueOf(parse[0]);
                if (mat == null) {
                    throw new IllegalArgumentException("Item of type " + parse[0] + " does not exist!");
                }
                int lower;
                int diff = 0;
                if (parse[1].contains("-")) {
                    lower = Integer.valueOf(parse[1].split("-")[0]);
                    diff = Integer.valueOf(parse[1].split("-")[1]) - lower;
                } else {
                    lower = Integer.valueOf(parse[1]);
                }
                double chance = Double.valueOf(parse[2]);
                int minTier = -1;
                int maxTier = 999999;
                if (parse.length >= 4) {
                    if (parse[3].contains("-")) {
                        minTier = Integer.valueOf(parse[3].split("-")[0]);
                        maxTier = Integer.valueOf(parse[3].split("-")[1]) - lower;
                    } else if (parse[3].contains("+")) {
                        minTier = Integer.valueOf(parse[3].replace("+",""));
                        maxTier = 999999;
                    } else {
                        minTier = Integer.valueOf(parse[3]);
                        maxTier = minTier;
                    }
                }
                boolean eliteOnly = false;
                if (parse.length >= 5) {
                    eliteOnly = Boolean.valueOf(parse[4]);
                }
                add.add(new DropItem(lower, diff, chance, eliteOnly, minTier, maxTier, new Item(mat, null, null, null, null)));
            } catch (Exception e) {
                continue;
            }
        }
        for (String entry : worldSection.getStringList("items"))  {
            try {
                String[] parse = entry.split(" ");
                Item item = this.config.getItem(parse[0]);
                if (item != null) {
                    int lower;
                    int diff = 0;
                    if (parse[1].contains("-")) {
                        lower = Integer.valueOf(parse[1].split("-")[0]);
                        diff = Integer.valueOf(parse[1].split("-")[1]) - lower;
                    } else {
                        lower = Integer.valueOf(parse[1]);
                    }
                    double chance = Double.valueOf(parse[2]);
                    int minTier = -1;
                    int maxTier = 999999;
                    if (parse.length >= 4) {
                        if (parse[3].contains("-")) {
                            minTier = Integer.valueOf(parse[3].split("-")[0]);
                            maxTier = Integer.valueOf(parse[3].split("-")[1]) - lower;
                        } else if (parse[3].contains("+")) {
                            minTier = Integer.valueOf(parse[3].replace("+",""));
                            maxTier = 999999;
                        } else {
                            minTier = Integer.valueOf(parse[3]);
                            maxTier = minTier;
                        }
                    }
                    boolean eliteOnly = false;
                    if (parse.length >= 5) {
                        eliteOnly = Boolean.valueOf(parse[4]);
                    }
                    add.add(new DropItem(lower, diff, chance, eliteOnly, minTier, maxTier, item));
                } else {
                    throw new IllegalArgumentException("No config item named " + entry + " exists or error loading" +
                            ": check items.yml!");
                }
            } catch (Exception e) {
                continue;
            }
        }
        return add;
    }

    public Collection<ItemStack> getDrops(UUID world, int tier, boolean elite) {
        List<ItemStack> drops = new LinkedList<>();
        for (DropItem item : this.drops.get(world)) {
            if (!item.isEliteOnly() || elite) {
                if (tier >= item.getLevelReqMin() && tier <= item.getLevelReqMax()) {
                    if (this.rand.nextDouble() < item.getChance()) {
                        drops.add(item.getDrop(this.rand.nextDouble()));
                    }
                }
            }
        }
        for (DropGroup group : this.groups.get(world)) {
            for (DropItem item : group.getDrops()) {
                if (!item.isEliteOnly() || elite) {
                    if (tier >= item.getLevelReqMin() && tier <= item.getLevelReqMax()) {
                        if (this.rand.nextDouble() < item.getChance()) {
                            drops.add(item.getDrop(this.rand.nextDouble()));
                        }
                    }
                }
            }
        }
        return drops;
    }

    public void print() {
        for (UUID id : this.drops.keySet()) {
            System.out.println("    Drops for world " + Bukkit.getServer().getWorld(id).getName());
            for (DropItem item : this.drops.get(id)) {
                System.out.println("      Type: " + item.getItem().toItemStack(1).getType() + " Amount: "
                        + item.getLower() + "-" + (item.getDiff() + item.getLower()));
            }
            for (DropGroup group : this.groups.get(id)) {
                System.out.println("    For Group");
                for (DropItem item : group.getDrops()) {
                    System.out.println("          Type: " + item.getItem().toItemStack(1).getType() + " Amount: "
                            + item.getLower() + "-" + (item.getDiff() + item.getLower()));
                }
            }
        }
    }

    public boolean replace() {
        return this.replace;
    }
}
