package net.kingdomsofarden.andrew2060.invasion.listeners;


import net.kingdomsofarden.andrew2060.invasion.InvasionPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Giant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;


public class DebugListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void spawnItems(PlayerInteractEvent event) {
        if (!event.getPlayer().isOp()) {
            return;
        }
        if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (event.getPlayer().getItemInHand().getType().equals(Material.FIRE)) {
            event.setCancelled(true);

            event.getPlayer().getLocation().getWorld().dropItem(event.getPlayer().getLocation(),
                    InvasionPlugin.i().getDropHandler().getItem("empyrean_dragonslayer").toItemStack(1));
            event.getPlayer().getLocation().getWorld().dropItem(event.getPlayer().getLocation(),
                    InvasionPlugin.i().getDropHandler().getItem("infernal_soulripper").toItemStack(1));

        }
    }

}

