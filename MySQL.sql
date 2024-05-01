package it.coralmc;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class LeaderboardCommand implements CommandExecutor {
    private PlayerStatsManager statsManager;

    public LeaderboardCommand(PlayerStatsManager statsManager) {
        this.statsManager = statsManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("duels.leaderboard")) {
                List<PlayerStats> leaderboard = statsManager.getLeaderboard(10); // Retrieve top 10 players
                player.sendMessage("===== Leaderboard =====");
                for (int i = 0; i < leaderboard.size(); i++) {
                    PlayerStats stats = leaderboard.get(i);
                    player.sendMessage((i + 1) + ". " + stats.getPlayerUUID() + " - Duels: " + stats.getDuels() + ", Wins: " + stats.getWins() + ", Losses: " + stats.getLosses());
                }
                return true;
            } else {
                player.sendMessage("Non hai il permesso di visualizzare la learderbord");
            }
        } else {
            sender.sendMessage("Solo i player posso eseguire questo comando!");
        }
        return false;
    }
}
