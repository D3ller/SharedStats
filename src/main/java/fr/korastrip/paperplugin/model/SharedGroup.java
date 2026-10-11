package fr.korastrip.paperplugin.model;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class SharedGroup {

    private Set<UUID> playersId = new HashSet<UUID>();
    public boolean isGroupDying = false;
    public Set<UUID> getPlayersId() {
        return playersId;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean getIsPlaying() {
        return isPlaying;
    }

    public void setOwnerUuid(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    public UUID getOwnerUuid() {
        return ownerUuid;
    }

    private UUID ownerUuid;
    private boolean isPlaying = false;
    private int deadCount = 0;
    private double groupHealth = 20.0;
    private double groupFood = 20.0;

    public void addPlayer(UUID uuid) throws RuntimeException {
        if (playersId.contains(uuid)) {
            throw new RuntimeException();
        }

        playersId.add(uuid);
    }

    public boolean removePlayer(UUID uuid) {
        return playersId.remove(uuid);
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void startGame() {
        if(isPlaying) {
            throw new RuntimeException();
        }

        isPlaying = true;
    }

    public void setHealth(double health) {
        groupHealth = health;
    }

    public double getHealth() {
        return groupHealth;
    }

    public void incrementsDeadCount() {
        if(isPlaying) deadCount++;
    }

    public void resetGroup() {
        playersId.clear();
        deadCount = 0;
        ownerUuid = null;
        isPlaying = false;
    }

    public List<Player> getOnlinePlayers() {
        return playersId.stream()
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
