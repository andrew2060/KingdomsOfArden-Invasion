package net.kingdomsofarden.andrew2060.invasion.rewards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import net.kingdomsofarden.andrew2060.invasion.InvasionPlugin;
import net.kingdomsofarden.andrew2060.invasion.dimensions.DimensionManager.DimensionType;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class DropHandler {

    private static DropHandler instance;

    private final InvasionPlugin plugin;

    private ConfigurationSection dropConfig;
    private ConfigurationSection groupConfig;
    private ConfigurationSection itemConfig;

    private HashMap<EntityType, DropTable> drops;
    private HashMap<String, Item> items;
    private HashMap<String, DropGroup> groups;

    public static DropHandler getInstance() {
        return instance;
    }

    public DropHandler(InvasionPlugin plugin) {
        this.plugin = plugin;
        this.drops = new HashMap<EntityType, DropTable>();
        this.items = new HashMap<String, Item>();
        this.groups = new HashMap<String, DropGroup>();
        this.loadConfigs();
    }

    private void loadConfigs() {
        this.dropConfig = this.loadConfig(new File(this.plugin.getDataFolder(), "drops.yml"));
        this.groupConfig = this.loadConfig(new File(this.plugin.getDataFolder(), "groups.yml"));
        this.itemConfig = this.loadConfig(new File(this.plugin.getDataFolder(), "items.yml"));
        this.loadItems();
        this.loadGroups();
        this.loadDrops();
    }

    private void loadItems() {
        for (String itemName : this.itemConfig.getKeys(false)) {
            try {
                ConfigurationSection itemSection = this.itemConfig.getConfigurationSection(itemName);
                Material type = Material.valueOf(itemSection.getString("material"));
                if (type == null) {
                    this.plugin.getLogger().log(Level.WARNING, "Could not load item " + itemName
                            + " as it does not have a valid type!");
                }
                String name = itemSection.getString("name", null);
                Map<Enchantment, Integer> enchs = null;
                if (itemSection.isConfigurationSection("enchants")) {
                    enchs = new HashMap<>();
                    for (String key : itemSection.getConfigurationSection("enchants").getKeys(false)) {
                        Enchantment e = Enchantment.getByName(key);
                        int level = itemSection.getInt("enchants." + key);
                        enchs.put(e, level);
                    }
                }
                List<String> lore = itemSection.getStringList("lore");
                if (lore.isEmpty()) {
                    lore = null;
                }
                if (lore != null && itemSection.isConfigurationSection("modules")) {
                    this.plugin.getLogger().log(Level.WARNING, "There are both lore and modules defined, lore will get" +
                            "overridden for item " + itemName);
                }
                ConfigurationSection moduleSection = itemSection.getConfigurationSection("modules");
                Map<String,String> modules = null;
                if (moduleSection != null) {
                    modules = new HashMap<String,String>();
                    for (String module : moduleSection.getKeys(false)) {
                        String data = moduleSection.getString(module);
                        modules.put(module, data);
                    }
                }
                Item item = new Item(type, name, enchs, lore, modules);
                this.items.put(itemName.toLowerCase(), item);
            } catch (Exception e) {
                this.plugin.getLogger().log(Level.WARNING, "Error loading " + itemName);
                e.printStackTrace();
                continue;
            }
        }
    }

    private void loadGroups() {
        for (String groupName : this.groupConfig.getKeys(false)) {
            try {
                DropGroup group = new DropGroup(this, this.groupConfig.getConfigurationSection(groupName));
                this.groups.put(groupName.toLowerCase(), group);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    private void loadDrops() {
        for (String key : this.dropConfig.getKeys(false)) {
            @SuppressWarnings("deprecation")
            EntityType type = EntityType.fromName(key);
            if (type == null) {
                this.plugin.getLogger().log(Level.WARNING, "Could not load drops for " + key
                        + " as this is not a valid entity type!");
                continue;
            } else {
                try {
                    DropTable drops = new DropTable(this, this.dropConfig.getConfigurationSection(key));
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
    }


    private ConfigurationSection loadConfig(File config) {
        if (!config.exists()) {
            try {
                this.plugin.getLogger().log(Level.WARNING, "File " + config.getName() + " not found - generating defaults.");
                config.getParentFile().mkdir();
                config.createNewFile();
                final OutputStream output = new FileOutputStream(config, false);
                final InputStream input = DropHandler.class.getResourceAsStream(config.getName());
                final byte[] buf = new byte[8192];
                int length = input.read(buf);
                while (length >= 0) {
                    output.write(buf, 0, length);
                    length = input.read(buf);
                }
                input.close();
                output.close();
            } catch (final Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return YamlConfiguration.loadConfiguration(config);
    }

    public Item getItem(String name) {
        return this.items.get(name.toLowerCase());
    }

    public DropGroup getGroup(String name) {
        return this.groups.get(name.toLowerCase());
    }

    public Collection<ItemStack> getMonsterDrops(EntityType type, String world, int tier) {
        return null;
    }

    public Collection<ItemStack> getBossDrops(EntityType type, String world, int tier) {
        return null;
    }

    public Collection<ItemStack> getDimensionalStabilizerDrops(DimensionType type, int tier) {
        return null;
    }

    public Collection<ItemStack> getDimensionDrops(DimensionType type, int tier) {
        return null;
    }



}
