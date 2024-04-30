package it.coralmc;

import java.util.UUID;

public class PlayerStats {
    private UUID playerUUID;
    private int duels;
    private int wins;
    private int losses;

    public PlayerStats(UUID playerUUID, int duels, int wins, int losses) {
        this.playerUUID = playerUUID;
        this.duels = duels;
        this.wins = wins;
        this.losses = losses;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public int getDuels() {
        return duels;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }
}
