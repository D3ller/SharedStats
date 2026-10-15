package fr.korastrip.paperplugin.listener;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.model.SharedGroup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawnListener implements Listener {

    private final SharedStats plugin;

    public PlayerRespawnListener(SharedStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        Player player = event.getPlayer();
        SharedGroup group = this.plugin.getGroup();

        if (group.getPlayersId().contains(player.getUniqueId()) && group.isGroupDying) {            group.addRespawnedPlayer(player.getUniqueId());

            Bukkit.getScheduler().runTask(
                    plugin,
                    () -> {
                        if (group.getOnlinePlayers().size() == group.getRespawnedPlayers().size()) {
                            if (plugin.getSynchronizationConfig().isEnabled("health")) {
                                group.setHealth(20);
                                group.syncHealth();
                            }
                            group.clearRespawnedPlayers();
                            group.isGroupDying = false;
                        }
                    }
            );
        }
    }
}
