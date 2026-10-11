package fr.korastrip.paperplugin.listener;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.model.SharedGroup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Objects;
import java.util.UUID;

public class PlayerQuitListener implements Listener {

    private final SharedStats plugin;

    public PlayerQuitListener(SharedStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        UUID playerId = event.getPlayer().getUniqueId();
        SharedGroup group = this.plugin.getGroup();

        if (group.getPlayersId().contains(playerId)) {
            if (Objects.equals(group.getOwnerUuid(), playerId)) {
                group.getPlayersId()
                        .forEach(uuid -> {
                            Player player = Bukkit.getPlayer(uuid);
                            if (player != null) {
                                player.sendRichMessage("<grey>The group owner has leave, so the group has been disband </grey>");
                            }
                        });

                group.resetGroup();
                return;
            }
            group.removePlayer(playerId);
        }
    }
}
