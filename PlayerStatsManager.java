package it.coralmc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerStatsManager {
    private MySQLManager mysqlManager;

    public PlayerStatsManager(MySQLManager mysqlManager) {
        this.mysqlManager = mysqlManager;
        createTable();
    }

    private void createTable() {
        try (Connection connection = mysqlManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "CREATE TABLE IF NOT EXISTS player_stats (" +
                             "id INT AUTO_INCREMENT PRIMARY KEY," +
                             "player_uuid VARCHAR(36) NOT NULL," +
                             "duels INT DEFAULT 0," +
                             "wins INT DEFAULT 0," +
                             "losses INT DEFAULT 0," +
                             "UNIQUE(player_uuid)" +
                             ")"
             )) {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementDuelCount(UUID playerUUID) {
        updateStat(playerUUID, "duels", 1);
    }

    public void incrementWinCount(UUID playerUUID) {
        updateStat(playerUUID, "wins", 1);
    }

    public void incrementLossCount(UUID playerUUID) {
        updateStat(playerUUID, "losses", 1);
    }

    private void updateStat(UUID playerUUID, String columnName, int value) {
        try (Connection connection = mysqlManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO player_stats (player_uuid, " + columnName + ") VALUES (?, ?) " +
                             "ON DUPLICATE KEY UPDATE " + columnName + "=" + columnName + "+?"
             )) {
            statement.setString(1, playerUUID.toString());
            statement.setInt(2, value);
            statement.setInt(3, value);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public PlayerStats getPlayerStats(UUID playerUUID) {
        try (Connection connection = mysqlManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT duels, wins, losses FROM player_stats WHERE player_uuid=?"
             )) {
            statement.setString(1, playerUUID.toString());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int duels = resultSet.getInt("duels");
                    int wins = resultSet.getInt("wins");
                    int losses = resultSet.getInt("losses");
                    return new PlayerStats(playerUUID, duels, wins, losses);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PlayerStats> getLeaderboard(int limit) {
        List<PlayerStats> leaderboard = new ArrayList<>();
        try (Connection connection = mysqlManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT player_uuid, duels, wins, losses FROM player_stats " +
                             "ORDER BY wins DESC LIMIT ?"
             )) {
            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    UUID uuid = UUID.fromString(resultSet.getString("player_uuid"));
                    int duels = resultSet.getInt("duels");
                    int wins = resultSet.getInt("wins");
                    int losses = resultSet.getInt("losses");
                    leaderboard.add(new PlayerStats(uuid, duels, wins, losses));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaderboard;
    }
}
