package it.coralmc;

import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.Map;

public class Reward {
    private RewardType type;
    private int value;
    private Map<String, Integer> items = new HashMap<>();

    public Reward(RewardType type, int value) {
        this.type = type;
        this.value = value;
    }

    public Reward(RewardType type, Map<String, Integer> items) {
        this.type = type;
        this.items = items;
    }

    public Reward(ConfigurationSection rewardConfig) {
        String rewardType = rewardConfig.getString("type", "money");
        if (rewardType.equalsIgnoreCase("money")) {
            this.type = RewardType.MONEY;
            this.value = rewardConfig.getInt("value", 0);
        } else if (rewardType.equalsIgnoreCase("items")) {
            this.type = RewardType.ITEMS;
            ConfigurationSection itemsConfig = rewardConfig.getConfigurationSection("items");
            if (itemsConfig != null) {
                for (String itemName : itemsConfig.getKeys(false)) {
                    this.items.put(itemName, itemsConfig.getInt(itemName));
                }
            }
        }
    }

    public RewardType getType() {
        return type;
    }

    public int getValue() {
        return value;
    }

    public Map<String, Integer> getItems() {
        return items;
    }
}
