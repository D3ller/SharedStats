package fr.korastrip.paperplugin.model;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class SharedGroup {

    private final Set<UUID> playersId = new HashSet<>();

    public Set<UUID> getPlayersId() {
        return Collections.unmodifiableSet(this.playersId);
    }

    private final HashMap<UUID, Integer> deathLeaderBoard = new HashMap<>();

    private UUID ownerUuid;

    public void setOwnerUuid(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public UUID getOwnerUuid() {
        return this.ownerUuid;
    }

    private boolean isPlaying = false;

    public boolean getIsPlaying() {
        return this.isPlaying;
    }


    private double groupHealth = 20.0;

    public void setHealth(double health) {
        this.groupHealth = health;
    }

    public double getHealth() {
        return this.groupHealth;
    }


    private double groupFood = 20.0;
    private int deadCount = 0;

    public int getDeadCount() {
        return this.deadCount;
    }

    private final Set<UUID> respawnedPlayers = new HashSet<>();

    public void addRespawnedPlayer(UUID playerId) {
        this.respawnedPlayers.add(playerId);
    }

    public Set<UUID> getRespawnedPlayers() {
        return Collections.unmodifiableSet(this.respawnedPlayers);
    }

    public void clearRespawnedPlayers() {
        this.respawnedPlayers.clear();
    }

    public boolean isGroupDying = false;

    public void addPlayer(UUID uuid) {
        if (this.playersId.contains(uuid)) {
            throw new IllegalArgumentException();
        }

        this.playersId.add(uuid);
    }

    public void removePlayer(UUID uuid) {
        this.playersId.remove(uuid);
        this.deathLeaderBoard.remove(uuid);
    }

    public void startGame() {
        if (this.isPlaying) {
            throw new IllegalStateException();
        }

        this.isPlaying = true;
    }

    public void incrementsDeadCount() {
        if (this.isPlaying) this.deadCount++;
    }

    public void resetGroup() {
        this.resetStats();
        this.playersId.clear();
        this.ownerUuid = null;
    }

    public void resetStats() {
        this.deadCount = 0;
        this.isGroupDying = false;
        this.isPlaying = false;
        this.respawnedPlayers.clear();
        this.deathLeaderBoard.clear();
    }

    public List<Player> getOnlinePlayers() {
        return this.playersId.stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .toList();
    }

    public void syncHealth() {
        this.getOnlinePlayers().forEach(p -> {
            p.setHealth(this.getHealth());
        });
    }

    public void addPlayerDeath(Player player) {
        UUID playerUUID = player.getUniqueId();
        deathLeaderBoard.merge(playerUUID, 1, Integer::sum);
    }

    public int getPlayerDeaths(Player player) {
        return deathLeaderBoard.getOrDefault(player.getUniqueId(), 0);
    }

}
