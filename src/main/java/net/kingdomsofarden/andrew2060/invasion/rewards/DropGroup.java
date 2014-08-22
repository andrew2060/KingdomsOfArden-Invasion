package net.kingdomsofarden.andrew2060.invasion.rewards;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DropGroup {

    private final DropHandler config;
    private LinkedList<DropItem> drops;

    public DropGroup(DropHandler config, ConfigurationSection groupSection) {
        this.config = config;
        this.drops = new LinkedList<DropItem>();
        for (String entry : groupSection.getStringList("vanilla"))  {
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
                drops.add(new DropItem(lower, diff, chance, eliteOnly, minTier, maxTier, new Item(mat, null, null, null, null)));
            } catch (Exception e) {
                continue;
            }
        }
        for (String entry : groupSection.getStringList("items"))  {
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
                    drops.add(new DropItem(lower, diff, chance, eliteOnly, minTier, maxTier, item));
                } else {
                    throw new IllegalArgumentException("No config item named " + entry + " exists or error loading" +
                            ": check items.yml!");
                }
            } catch (Exception e) {
                continue;
            }
        }
    }

    public List<DropItem> getDrops() {
        return this.drops;
    }

}
