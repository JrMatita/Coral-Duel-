import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RewardManager {
    private List<Reward> rewards;

    public RewardManager() {
        rewards = new ArrayList<>();
    }

    public void addReward(Reward reward) {
        rewards.add(reward);
    }

    public void removeReward(Reward reward) {
        rewards.remove(reward);
    }

    public Reward getRandomReward() {
        if (rewards.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return rewards.get(random.nextInt(rewards.size()));
    }

    public void giveReward(Player player, Reward reward) {
        switch (reward.getType()) {
            case MONEY:
                
                int money = reward.getValue();
                //EconomyManager.giveMoney(player, money);
                break;
            case ITEMS:
                
                List<ItemStack> items = reward.getItems();
                InventoryManager.giveItems(player, items);
                break;
            case EXPERIENCE:
                
                int experience = reward.getValue();
                //ExperienceManager.giveExperience(player, experience);
                break;
            default:
                break;
        }
    }
}
