package ru.alexec0de.shulker;

import lombok.AllArgsConstructor;
import org.bukkit.Location;
import ru.alexec0de.bdang.BDang;

@AllArgsConstructor
public class BDangShulker implements ShulkerActions {

    private final BDang bDang;

    @Override
    public void addShulker(Location location) {
        System.out.println("Adding shulker at " + location);
        bDang.addShulker(location);
    }
}
