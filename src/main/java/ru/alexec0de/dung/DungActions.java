package ru.alexec0de.dung;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.jetbrains.annotations.NotNull;
import ru.alexec0de.Config;
import ru.alexec0de.addshulkers.AddShulkers;
import ru.alexec0de.data.DangData;
import ru.alexec0de.schem.SchemAction;

import java.util.List;
import java.util.Random;


@AllArgsConstructor
public class DungActions {
    private final SchemAction schemAction;
    private final AddShulkers addShulkers;


    public void spawn(@NotNull Location loc) {

        final String world = loc.getWorld().getName();
        final List<DangData> dangDataList = Config.getDangsDataList();

        for (int i = 0; i <= 19; i++){
            DangData dangData = dangDataList.get(new Random().nextInt(dangDataList.size()));
            for (Biome b : dangData.getBiome()) {
                if (dangData.getWorld().equals(world) && b == loc.getWorld().getBiome(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())) {
                    schemAction.spawnSchem(loc, dangData.getFileName());
                    addShulkers.addShulkersInLocation(loc, 35);
                    createRegion(loc.getBlockX(), loc.getBlockZ(), loc.getWorld());
                    return;
                }
            }
        }


    }


    private void createRegion(int x, int z, World worldBukkit) {
        final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        final int radius = 12; // Размер региона в блоках
        if (container != null) {
            final RegionManager regionManager = container.get(BukkitAdapter.adapt(worldBukkit));

            final BlockVector3 minPoint = BlockVector3.at(x - radius, 0, z - radius);
            final BlockVector3 maxPoint = BlockVector3.at(x + radius, 255, z + radius);

            final String nameRegion = "dung_x" + x + "_z" + z;
            final ProtectedCuboidRegion region = new ProtectedCuboidRegion(nameRegion, minPoint, maxPoint);

            region.setFlag(Flags.USE, StateFlag.State.ALLOW);
            region.setFlag(Flags.BLOCK_BREAK, StateFlag.State.DENY);
            region.setFlag(Flags.USE, StateFlag.State.DENY);
            region.setFlag(Flags.BUILD, StateFlag.State.DENY);
            region.setFlag(Flags.MOB_SPAWNING, StateFlag.State.DENY);
            region.setFlag(Flags.PLACE_VEHICLE, StateFlag.State.DENY);
            region.setFlag(Flags.PVP, StateFlag.State.ALLOW);
            region.setFlag(Flags.TNT, StateFlag.State.DENY);
            region.setFlag(Flags.CHEST_ACCESS, StateFlag.State.ALLOW);
            region.setFlag(Flags.CREEPER_EXPLOSION, StateFlag.State.DENY);
            region.setFlag(Flags.DAMAGE_ANIMALS, StateFlag.State.DENY);
            region.setFlag(Flags.GHAST_FIREBALL, StateFlag.State.DENY);
            region.setFlag(Flags.RESPAWN_ANCHORS, StateFlag.State.DENY);
            region.setFlag(Flags.DESTROY_VEHICLE, StateFlag.State.DENY);
            region.setFlag(Flags.ENDERDRAGON_BLOCK_DAMAGE, StateFlag.State.DENY);
            region.setFlag(Flags.MOB_DAMAGE, StateFlag.State.DENY);

            regionManager.addRegion(region);
        }
    }


}
