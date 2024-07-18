package ru.alexec0de;

import org.bukkit.plugin.java.JavaPlugin;
import ru.alexec0de.addshulkers.AddShulkers;
import ru.alexec0de.bdang.BDang;
import ru.alexec0de.comands.BDangCommands;
import ru.alexec0de.dung.DungActions;
import ru.alexec0de.listener.BukkitListener;
import ru.alexec0de.schem.SchemAction;
import ru.alexec0de.shulker.BDangShulker;
import ru.alexec0de.shulker.HolyDangShulker;
import ru.alexec0de.shulker.ShulkerActions;
import ru.alexec0de.storage.Storage;

public final class ADang extends JavaPlugin {


    private ShulkerActions shulkerActions;
    private SchemAction schemAction;
    private DungActions dungActions;
    private BDang bDang;
    private Storage shulkers;
    private Storage items;
    private AddShulkers addShulkers;
    @Override
    public void onEnable() {
        saveDefaultConfig();
        Config.load(getConfig());
        shulkers = new Storage("shulkers.yml", this);
        items = new Storage("items.yml", this);
        bDang = new BDang(items, shulkers);
        shulkerActions = new BDangShulker(bDang);
        addShulkers = new AddShulkers(shulkerActions);
        schemAction = new SchemAction(this);
        dungActions = new DungActions(schemAction, addShulkers);


        getServer().getPluginManager().registerEvents(bDang, this);
        getServer().getPluginManager().registerEvents(new BukkitListener(dungActions, shulkers, bDang), this);
        getCommand("adang").setExecutor(new BDangCommands(bDang, dungActions));


    }
}
