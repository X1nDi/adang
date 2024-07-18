package ru.alexec0de.schem;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockState;
import lombok.AllArgsConstructor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import ru.alexec0de.ADang;
import ru.alexec0de.shulker.ShulkerActions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@AllArgsConstructor
public class SchemAction {

    private final ADang plugin;

    public void spawnSchem(@NotNull Location location, @NotNull String fileName){
        File file = new File(plugin.getDataFolder() + "/schem/" + fileName);
        ClipboardFormat format = ClipboardFormats.findByFile(file);

        try (ClipboardReader reader = format.getReader(new FileInputStream(file))) {
            final Clipboard clipboard = reader.read();
            final BlockVector3 cord = BlockVector3.at(location.getX(), location.getY(), location.getZ());



            final EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(location.getWorld()));
            final Operation operation = new ClipboardHolder(clipboard).createPaste(editSession)
                    .to(cord).ignoreAirBlocks(false).build();
            Operations.complete(operation);

            editSession.close();

        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }



}
