package it.coralmc;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class KitConfig {
    private ItemStack[] items;
    private Reward reward;
    private String permission;

    public KitConfig(ItemStack[] items, Reward reward, String permission) {
        this.items = items;
        this.reward = reward;
        this.permission = permission;
    }

    public ItemStack[] getItems() {
        return items;
    }

    public Reward getReward() {
        return reward;
    }

    public String getPermission() {
        return permission;
    }

    public static Map<String, KitConfig> loadFromConfig(FileConfiguration config) {
        Map<String, KitConfig> kitConfigs = new HashMap<>();
        ConfigurationSection kitsSection = config.getConfigurationSection("kits");
        if (kitsSection != null) {
            for (String kitName : kitsSection.getKeys(false)) {
                ConfigurationSection kitSection = kitsSection.getConfigurationSection(kitName);
                if (kitSection != null) {
                    ItemStack[] items = kitSection.get("items");
                    Reward reward = new Reward(kitSection.getConfigurationSection("reward"));
                    String permission = kitSection.getString("permission", "");
                    kitConfigs.put(kitName, new KitConfig(items, reward, permission));
                }
            }
        }
        return kitConfigs;
    }
}
