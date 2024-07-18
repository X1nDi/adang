package ru.alexec0de.comands;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.alexec0de.Config;
import ru.alexec0de.bdang.BDang;
import ru.alexec0de.dung.DungActions;

import java.util.UUID;

@AllArgsConstructor
public class BDangCommands implements CommandExecutor {

    private final BDang dang;
    private final DungActions dungActions;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            if (commandSender.hasPermission("bdang.admin")) {
                final Player player = (Player) commandSender;
                if (strings[0].equals("additem")) {
                    ItemStack hand = player.getItemInHand();
                    if (hand == null || hand.getType() == Material.AIR) {
                        player.sendMessage("§сВ руке пусто!!!");
                        return false;
                    } else {
                        int chance = Integer.parseInt(strings[1]);
                        int min = Integer.parseInt(strings[2]);
                        int max = Integer.parseInt(strings[3]);
                        String uuid = UUID.randomUUID().toString();
                        dang.addItem(uuid, hand, chance, min, max);
                        player.sendMessage("§aУспешно");
                        return true;
                    }
                } else if (strings[0].equals("spawn")) {
                    if (strings.length == 2) {
                        int amount = Integer.parseInt(strings[1]);

                        for (int i = 0; i < amount; i++) {
                            final Location loc = randomSecurityLocation(player.getWorld());
                            player.sendMessage("§aДанж заспавнен на " + loc.getBlockX() + " " + loc.getBlockY() + " " + loc.getBlockZ());
                            dungActions.spawn(loc);
                        }
                    } else {
                        dungActions.spawn(player.getLocation());
                    }

                    return true;
                } else if (strings[0].equals("givekrystal")) {
                    player.getInventory().addItem(Config.getKrystal());
                    return true;
                }
            }
        }
        if (strings[0].equals("givekey")) {
            final Player player = Bukkit.getPlayer(strings[1]);
            if (player == null) return true;
            player.getInventory().addItem(Config.getKey());
            return true;
        }
        return true;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public Location randomSecurityLocation(World worldGruz){
        Location locTest = null;
        for (int i = 1; i <= 20; i++){
            int x = getRandomNumber(2000, 100);
            int z = getRandomNumber(2000, 100);
            Location loc = new Location(worldGruz, x, 0, z);
            int y = worldGruz.getHighestBlockYAt(loc);
            locTest = new Location(worldGruz, x, y, z);
            if (locTest.getBlock().getType() == Material.WATER
                    || locTest.getBlock().getType() == Material.LAVA
                    || locTest.getBlock().getType() == Material.AIR){
                locTest = null;
            } else {
                break;
            }
        }
        return locTest;


    }
}
