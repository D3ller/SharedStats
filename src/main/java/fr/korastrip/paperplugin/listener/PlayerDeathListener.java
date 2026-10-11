package fr.korastrip.paperplugin.listener;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.model.SharedGroup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    private final SharedStats plugin;

    public PlayerDeathListener(SharedStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        SharedGroup group = plugin.getGroup();
        Player player = event.getPlayer();

        if (!group.getIsPlaying()) return;
        if (!group.getPlayersId().contains(player.getUniqueId())) return;

        Bukkit.getScheduler().runTaskLater(
                plugin,
                () -> {
                    if (player.isDead()) {
                        player.spigot().respawn();
                    }
                },
                20L
        );
    }
}