package ru.alexec0de.listener;

import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import ru.alexec0de.bdang.BDang;
import ru.alexec0de.dung.DungActions;
import ru.alexec0de.storage.Storage;

@AllArgsConstructor
public class BukkitListener implements Listener {
    private DungActions dungActions;
    private final Storage shulkers;
    private final BDang bDang;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent e) {
        ConfigurationSection locsSection = shulkers.getConfig().getConfigurationSection("locs");
        for (Block block : e.getBlocks()) {
            if (bDang.isShulker(block)) {
                for (String itemId : locsSection.getKeys(false)) {
                    ConfigurationSection shulker = locsSection.getConfigurationSection(itemId);
                    Location shulkerLocation = shulker.getLocation("location");
                    if (block.getLocation().getBlockX() == shulkerLocation.getBlockX() && Math.abs(block.getLocation().getBlockY() - shulkerLocation.getBlockY()) <= 2 && block.getLocation().getBlockZ() == shulkerLocation.getBlockZ() && block.getLocation().getWorld().getName().equals(shulkerLocation.getWorld().getName())) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }
}
