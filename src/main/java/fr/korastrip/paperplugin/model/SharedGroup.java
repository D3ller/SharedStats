package fr.korastrip.paperplugin.model;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class SharedGroup {

    private final Set<UUID> playersId = new HashSet<>();
    public Set<UUID> getPlayersId() {
        return this.playersId;
    }

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
        respawnedPlayers.add(playerId);
    }
    public Set<UUID> getRespawnedPlayers() {
        return respawnedPlayers;
    }
    public void clearRespawnedPlayers() {
        respawnedPlayers.clear();
    }

    public boolean isGroupDying = false;

    public void addPlayer(UUID uuid) throws RuntimeException {
        if (this.playersId.contains(uuid)) {
            throw new RuntimeException();
        }

        this.playersId.add(uuid);
    }

    public void removePlayer(UUID uuid) {
        this.playersId.remove(uuid);
    }

    public void startGame() {
        if(this.isPlaying) {
            throw new RuntimeException();
        }

        this.isPlaying = true;
    }

    public void incrementsDeadCount() {
        if(this.isPlaying) this.deadCount++;
    }

    public void resetGroup() {
        this.playersId.clear();
        this.setHealth(20);
        this.deadCount = 0;
        this.ownerUuid = null;
        this.isPlaying = false;
        this.isGroupDying = false;
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

}
