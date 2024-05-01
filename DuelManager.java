package it.coralmc;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class DuelManager {
    private static DuelManager instance;
    private Map<Player, Player> pendingDuels = new HashMap<>();
    private Map<Player, ItemStack[]> playerInventories = new HashMap<>();
    private KitManager kitManager;
    private RewardManager rewardManager;
    private PlayerStatsManager statsManager;
    private MySQLManager mysqlManager;
    private World duelWorld;

    private DuelManager() {
        kitManager = new KitManager();
        rewardManager = new RewardManager();
        statsManager = new PlayerStatsManager();
        mysqlManager = new MySQLManager(Config.stats_mysql);


        this.duelWorld = getOrCreateDuelWorld(Config.duel_world);
    }

    public static DuelManager getInstance() {
        if (instance == null) {
            instance = new DuelManager();
        }
        return instance;
    }

    public void requestDuel(Player sender, Player target, String kitName) {
        pendingDuels.put(target, sender);
        sender.sendMessage("Richiesta di duello mandata a " + target.getName());
        target.sendMessage(sender.getName() + " per accettare il duello usa /duel accept");
    }

    public void acceptDuel(Player player) {
        Player sender = pendingDuels.get(player);
        if (sender != null) {
            startDuel(sender, player);
            pendingDuels.remove(player);
        } else {
            player.sendMessage("Non hai richieste in sospeso!");
        }
    }

    private void startDuel(Player player1, Player player2) {
        // Save inventories
        playerInventories.put(player1, player1.getInventory().getContents());
        playerInventories.put(player2, player2.getInventory().getContents());


        for (Player player : new Player[]{player1, player2}) {
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
        }


        String kitName = "default";
        ItemStack[] kitItems = kitManager.getKit(kitName);
        if (kitItems != null) {
            player1.getInventory().setContents(kitItems);
            player2.getInventory().setContents(kitItems);
        } else {
            Bukkit.getLogger().warning("Kit '" + kitName + "' not found!");
        }


        Location spawnLocation = duelWorld.getSpawnLocation();
        player1.teleport(spawnLocation);
        player2.teleport(spawnLocation);


    }

    public void endDuel(Player winner, Player loser) {

        ItemStack[] winnerInventory = playerInventories.get(winner);
        ItemStack[] loserInventory = playerInventories.get(loser);
        if (winnerInventory != null && loserInventory != null) {
            winner.getInventory().setContents(winnerInventory);
            loser.getInventory().setContents(loserInventory);
        } else {
            Bukkit.getLogger().warning("Player inventories not found!");
        }


        rewardManager.giveMoney(winner, Config.kits.get("default").getReward().getMoney());


        statsManager.incrementDuelCount(winner);
        statsManager.incrementWinCount(winner);
        statsManager.incrementDuelCount(loser);
        statsManager.incrementLossCount(loser);


        mysqlManager.updatePlayerStats(winner);
        mysqlManager.updatePlayerStats(loser);


    }

    private World getOrCreateDuelWorld(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            world = Bukkit.createWorld(WorldCreator.name(worldName));
        }
        return world;
    }
}
