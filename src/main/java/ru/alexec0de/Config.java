package ru.alexec0de;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import ru.alexec0de.data.DangData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Config {

    private static FileConfiguration configuration;

    private static List<DangData> dangsDataList = new ArrayList<>();
    @Getter
    private static ItemStack key;
    @Getter
    private static ItemStack krystal;

    public static void load(FileConfiguration c) {
        configuration = c;
        dangsDataList = parseDangData();
        key = parseKey();
        krystal = parseKrystal();
        parseMessage();
    }

    private static ItemStack parseKey(){
        final ConfigurationSection section = configuration.getConfigurationSection("item.key");
        final ItemStack itemStack = new ItemStack(Material.valueOf(section.getString("material")));
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(section.getString("name"));
        itemMeta.setLore(section.getStringList("lore"));
        itemMeta.addEnchant(Enchantment.LUCK, 1, true);
        itemMeta.getPersistentDataContainer().set(NamespacedKey.fromString("key"), PersistentDataType.STRING, "holyworld");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private static ItemStack parseKrystal(){
        final ConfigurationSection section = configuration.getConfigurationSection("item.krystal");
        final ItemStack itemStack = new ItemStack(Material.valueOf(section.getString("material")));
        final ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(section.getString("name"));
        itemMeta.setLore(section.getStringList("lore"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
    private static List<DangData> parseDangData() {
        final List<DangData> dangDataList = new ArrayList<>();
        final ConfigurationSection dangsSection = configuration.getConfigurationSection("dang");
        for (String key : dangsSection.getKeys(false)) {
            final ConfigurationSection dangSection = dangsSection.getConfigurationSection(key);
            List<Biome> biomes = new ArrayList<>();
            String[] strings = dangSection.getString("biome").split(";");
            for (String s : strings) {
                biomes.add(Biome.valueOf(s));
            }
            dangDataList.add(new DangData(dangSection.getString("fileName"), dangSection.getString("world"), biomes));
        }
        return dangDataList;
    }

    public static List<DangData> getDangsDataList() {
        return Collections.unmodifiableList(dangsDataList);
    }

    private static void parseMessage() {
        final ConfigurationSection messagesSection = configuration.getConfigurationSection("messages");
        Messages.openDung = messagesSection.getStringList("openDung");
        Messages.saveKey = messagesSection.getString("saveKey");
        Messages.closedDung = messagesSection.getStringList("closedDung");

    }




    public static class Messages {
        public static List<String> openDung;
        public static String saveKey;
        public static List<String> closedDung;
    }

}
