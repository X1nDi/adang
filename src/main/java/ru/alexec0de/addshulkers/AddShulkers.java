package ru.alexec0de.addshulkers;

import lombok.AllArgsConstructor;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.util.BlockIterator;
import ru.alexec0de.shulker.ShulkerActions;

@AllArgsConstructor
public class AddShulkers {

    private final ShulkerActions actions;

    public void addShulkersInLocation(Location location, int radius) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    // Получаем блок на текущих координатах
                    Block block = location.getWorld().getBlockAt(location.clone().add(x, y, z));

                    // Выполняем действие с блоком (например, меняем его на стекло)
                   if (isShulker(block.getType())) {
                       actions.addShulker(block.getLocation());
                   }
                }
            }
        }
    }
    private boolean isShulker(Material placedBlock) {
        if (placedBlock == Material.SHULKER_BOX || placedBlock == Material.BLACK_SHULKER_BOX || placedBlock == Material.WHITE_SHULKER_BOX || placedBlock == Material.BLUE_SHULKER_BOX || placedBlock == Material.CYAN_SHULKER_BOX || placedBlock == Material.BROWN_SHULKER_BOX || placedBlock == Material.YELLOW_SHULKER_BOX || placedBlock == Material.GREEN_SHULKER_BOX || placedBlock == Material.LIME_SHULKER_BOX || placedBlock == Material.RED_SHULKER_BOX || placedBlock == Material.LIGHT_BLUE_SHULKER_BOX || placedBlock == Material.LIGHT_GRAY_SHULKER_BOX || placedBlock == Material.MAGENTA_SHULKER_BOX || placedBlock == Material.PINK_SHULKER_BOX || placedBlock == Material.PURPLE_SHULKER_BOX || placedBlock == Material.ORANGE_SHULKER_BOX || placedBlock == Material.GRAY_SHULKER_BOX)
            return true;
        return false;
    }

}
