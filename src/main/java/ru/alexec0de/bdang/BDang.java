package ru.alexec0de.bdang;

import lombok.RequiredArgsConstructor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import ru.alexec0de.Config;
import ru.alexec0de.storage.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@RequiredArgsConstructor
public class BDang implements Listener {

    private final Storage items;
    private final Storage shulkers;

    public void fillInventoryWithRandomLoot(Inventory inventory) {
        ConfigurationSection itemsSection = items.getConfig().getConfigurationSection("items");
        if (itemsSection != null) {
            List<Integer> availableSlots = new ArrayList<>();
            for (int i = 0; i < inventory.getSize(); i++)
                availableSlots.add(i);
            for (String itemId : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(itemId);
                ItemStack item = itemSection.getItemStack("item");
                item.setAmount(getRandomNumber(itemSection.getInt("minAmount"), itemSection.getInt("maxAmount")));
                double chance = itemSection.getDouble("chance");
                if (Math.random() * 100.0D < chance && !availableSlots.isEmpty()) {
                    int randomSlotIndex = new Random().nextInt(availableSlots.size());
                    int slot = availableSlots.get(randomSlotIndex);
                    inventory.setItem(slot, item);
                    availableSlots.remove(randomSlotIndex);
                }
            }
        } else {
            items.getConfig().createSection("items");
        }

    }


    public void addShulker(Location location) {
        if (!(location.getBlock().getState() instanceof ShulkerBox))
            return;
        ShulkerBox shulkerBox = (ShulkerBox) location.getBlock().getState();
        fillInventoryWithRandomLoot(shulkerBox.getInventory());
        String uuid = UUID.randomUUID().toString();
        addShulkerConfig(uuid, location, false);

    }

    public void addShulkerConfig(String id, Location location, boolean opened) {
        ConfigurationSection itemsSection = shulkers.getConfig().getConfigurationSection("locs");
        if (itemsSection == null) {
            shulkers.getConfig().createSection("locs");
            addShulkerConfig(id, location, opened);
        } else {
            itemsSection = itemsSection.createSection(String.valueOf(id));
            itemsSection.set("location", location);
            itemsSection.set("opened", opened);
            shulkers.save();
        }
    }

    public void addItem(String id, ItemStack item, int chance, int minAmount, int maxAmount) {
        ConfigurationSection itemsSection = items.getConfig().getConfigurationSection("items");
        if (itemsSection == null) {
            items.getConfig().createSection("items");
            addItem(id, item, chance, minAmount, maxAmount);
        } else {
            itemsSection = itemsSection.createSection(String.valueOf(id));
            itemsSection.set("item", item);
            itemsSection.set("chance", chance);
            itemsSection.set("minAmount", minAmount);
            itemsSection.set("maxAmount", maxAmount);
            items.save();
        }
    }
    public boolean isShulker(Block placedBlock) {
        if (placedBlock.getType() == Material.SHULKER_BOX || placedBlock.getType() == Material.BLACK_SHULKER_BOX | placedBlock.getType() == Material.WHITE_SHULKER_BOX || placedBlock.getType() == Material.BLUE_SHULKER_BOX || placedBlock.getType() == Material.CYAN_SHULKER_BOX || placedBlock.getType() == Material.BROWN_SHULKER_BOX || placedBlock.getType() == Material.YELLOW_SHULKER_BOX || placedBlock.getType() == Material.GREEN_SHULKER_BOX || placedBlock.getType() == Material.LIME_SHULKER_BOX || placedBlock.getType() == Material.RED_SHULKER_BOX || placedBlock.getType() == Material.LIGHT_BLUE_SHULKER_BOX || placedBlock.getType() == Material.LIGHT_GRAY_SHULKER_BOX || placedBlock.getType() == Material.MAGENTA_SHULKER_BOX || placedBlock.getType() == Material.PINK_SHULKER_BOX || placedBlock.getType() == Material.PURPLE_SHULKER_BOX || placedBlock.getType() == Material.ORANGE_SHULKER_BOX || placedBlock.getType() == Material.GRAY_SHULKER_BOX)
            return true;
        return false;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event){
        ConfigurationSection locsSection = shulkers.getConfig().getConfigurationSection("locs");
        if (locsSection == null)
            return;
        if (event.getClickedBlock() == null)
            return;
        if (isShulker(event.getClickedBlock())){
            for (String itemId : locsSection.getKeys(false)){
                ConfigurationSection shulker = locsSection.getConfigurationSection(itemId);
                Location shulkerLocation = shulker.getLocation("location");
                if (event.getClickedBlock().getLocation().getBlockX() == shulkerLocation.getBlockX() && event.getClickedBlock().getLocation().getBlockY() == shulkerLocation.getBlockY() && event.getClickedBlock().getLocation().getBlockZ() == shulkerLocation.getBlockZ() && event.getClickedBlock().getLocation().getWorld().getName().equals(shulkerLocation.getWorld().getName())){
                    if (!shulker.getBoolean("opened")){
                        if (event.getPlayer().getItemInHand() == null || event.getPlayer().getItemInHand().getItemMeta() == null) {
                            for (String s : Config.Messages.closedDung) {
                                event.getPlayer().sendMessage(s);
                            }
                            event.setCancelled(true);
                            return;
                        }
                        if (event.getPlayer().getItemInHand().getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString("key"), PersistentDataType.STRING)){
                            event.getPlayer().playSound(shulkerLocation, Sound.UI_TOAST_CHALLENGE_COMPLETE, 50, 1);
                            shulkerLocation.getWorld().spawnParticle(Particle.TOTEM, shulkerLocation.add(0D, 1D, 0D), 10, 0.5, 2, 0.5, 0.1  );
                            shulkerLocation.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, shulkerLocation.add(0D, 1D, 0D), 10, 0.5, 2, 0.5, 0.1);
                            shulker.set("opened", true);
                            shulkers.save();
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                for (String s : Config.Messages.openDung) {
                                    player.sendMessage(s.replace("{player}", event.getPlayer().getName()));
                                }
                            }
                            if (Math.random() < 85 / 100.0) {
                                event.getPlayer().getInventory().removeItem(Config.getKey());
                            } else {
                                event.getPlayer().sendMessage(Config.Messages.saveKey);
                            }
                            return;
                        } else {
                            for (String s : Config.Messages.closedDung) {
                                event.getPlayer().sendMessage(s);
                            }
                            event.setCancelled(true);
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onLoot(LootGenerateEvent e) {
        Random random = new Random();
        int result = random.nextInt(100);
        if (result < 1) {
            e.getLoot().add(Config.getKey());
        }
    }


    @EventHandler
    public void on(BlockBreakEvent e){
        if (isShulker(e.getBlock())) {
            ConfigurationSection locsSection = shulkers.getConfig().getConfigurationSection("locs");
            for (String itemId : locsSection.getKeys(false)) {
                ConfigurationSection shulker = locsSection.getConfigurationSection(itemId);
                Location shulkerLocation = shulker.getLocation("location");
                if (e.getBlock().getLocation().getBlockX() == shulkerLocation.getBlockX() && e.getBlock().getLocation().getBlockY() == shulkerLocation.getBlockY() && e.getBlock().getLocation().getBlockZ() == shulkerLocation.getBlockZ() && e.getBlock().getLocation().getWorld().getName().equals(shulkerLocation.getWorld().getName())) {
                    e.setCancelled(true);
                    return;
                }
            }
        }
    }


    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockFromTo(BlockFromToEvent e) {
        if (isShulker(e.getToBlock())) {
            e.setCancelled(true);
        }
    }


}
