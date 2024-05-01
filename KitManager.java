
package it.coralmc;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class KitManager {
    private Map<String, Kit> kits = new HashMap<>();
    private JavaPlugin plugin;

    public KitManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadKitsFromConfig();
    }

    private void loadKitsFromConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        if (config.isConfigurationSection("kits")) {
            for (String kitName : config.getConfigurationSection("kits").getKeys(false)) {
                ItemStack[] items = null; 
                kits.put(kitName, new Kit(kitName, items));
            }
        }
    }

    public Kit getKit(String name) {
        return kits.get(name);
    }

    public void addKit(String name, ItemStack[] items) {
        kits.put(name, new Kit(name, items));
    }

    public void saveKitsToConfig() {
        File configFile = new File(plugin.getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        for (Map.Entry<String, Kit> entry : kits.entrySet()) {
            String kitName = entry.getKey();
            Kit kit = entry.getValue();

        }

        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
