package net.kingdomsofarden.andrew2060.invasion.dimensions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

import net.kingdomsofarden.andrew2060.invasion.InvasionPlugin;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.massivecraft.factions.FFlag;
import com.massivecraft.factions.FPerm;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.entity.Board;
import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.FactionColls;
import com.massivecraft.mcore.ps.PS;

public class DimensionPortal {

    private UUID id;
    private final int difficulty;
    private Location nexusCenter;


    public static DimensionPortal fromId(UUID id) {
        InvasionPlugin plugin = InvasionPlugin.instance;
        File storageFolder = plugin.getDataFolder();
        storageFolder.mkdir();
        File file = new File(storageFolder.getPath() + "/" + id.toString());
        if (!file.exists()) {
            return null;
        }
        try {
            BufferedReader read = new BufferedReader(new FileReader(file));
            String line = read.readLine();
            read.close();
            String[] parsed = line.split("::");
            String[] locParse = parsed[0].split(":");
            Location portalFrom = new Location(plugin.getServer().getWorld(locParse[0]), Double.valueOf(locParse[0]), Double.valueOf(locParse[1]), Double.valueOf(locParse[2]));
            Location portalTo = null;
            if (!parsed[1].equals("null")) {
                locParse = parsed[1].split(":");
                portalTo = new Location(plugin.getServer().getWorld(locParse[0]), Double.valueOf(locParse[0]), Double.valueOf(locParse[1]), Double.valueOf(locParse[2]));
            }
            int health = Integer.valueOf(parsed[2]);
            int difficulty = Integer.valueOf(parsed[3]);
            return new DimensionPortal(portalFrom, portalTo, health, difficulty, id);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }        
    }
    
    /**
     * Constructs a new invasion portal and/or loads it from file using coordinates 
     * based on the location of its nexuses (beacon blocks)
     */
    public DimensionPortal(Location from, Location to, int health, int difficulty, UUID id) {
        Location chunkCenter = from.getChunk().getBlock(7, 0, 7).getLocation();
        chunkCenter = chunkCenter.getWorld().getHighestBlockAt(chunkCenter).getLocation();
        this.id = id;
        World world = from.getWorld();
        boolean match = false;
        while (!match) { //TODO: Potentially Dangerous
            switch (world.getBlockAt(chunkCenter).getType()) {

            case STONE:
            case GRASS:
            case DIRT:
            case ICE:
            case SNOW_BLOCK:
            case NETHERRACK:
            case NETHER_BRICK:
            case ENDER_STONE:
            case OBSIDIAN:
            case WATER:
            case LAVA:
                match = true;
            default:
                world.getBlockAt(chunkCenter).setType(Material.AIR);
                chunkCenter.subtract(0, 1, 0);
            }
        }
        this.nexusCenter = chunkCenter;
        this.difficulty = difficulty;
        
    }

    public void constructTower() {
        
    }
    
    public void registerFactionClaim(boolean createNew) {
        FactionColl forWorld = FactionColls.get().getForWorld(this.nexusCenter.getWorld().getName());
        Faction fact = null;
        if (!forWorld.containsId(this.id)) {
            fact = forWorld.create();
            fact.setName("Dominion Rift");
            fact.setDescription("Tier " + difficulty + " Dominion Invasion Nexus");
            fact.setFlag(FFlag.PERMANENT, true);
            fact.setPowerBoost((double) 999999);
            fact.setOpen(false);   
            
        } else {
            fact = FactionColls.get().getForWorld(this.nexusCenter.getWorld().getName()).get(this.id);
        }
        
        BoardColls.get().setFactionAt(PS.valueOf(this.nexusCenter),fact);
    }

    public void write() {
        
    }
    
}
