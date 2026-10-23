package fr.korastrip.paperplugin.listener;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.model.SharedGroup;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final SharedStats plugin;

    public PlayerJoinListener(SharedStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        SharedGroup group = this.plugin.getGroup();

        if(!group.getIsPlaying()) return;
        if (group.getPlayersId().contains(player.getUniqueId())) {

            Bukkit.getScheduler().runTask(plugin, () -> {
                if (!player.isOnline()) return;
                if(player.isDead()) return;

                if(this.plugin.getSynchronizationConfig().isEnabled("health")) player.setHealth(group.getHealth());
                if(this.plugin.getSynchronizationConfig().isEnabled("food")) player.setFoodLevel(group.getFood());
            });
        }
    }


}
