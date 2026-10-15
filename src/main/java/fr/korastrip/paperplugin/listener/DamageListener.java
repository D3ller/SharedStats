package fr.korastrip.paperplugin.listener;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.model.SharedGroup;
import fr.korastrip.paperplugin.services.SharedHealthService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class DamageListener implements Listener {

    private final SharedStats plugin;

    public DamageListener(SharedStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamageReceive(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {

            SharedGroup group = this.plugin.getGroup();

            if (!group.getIsPlaying()) return;
            if (!group.getPlayersId().contains(player.getUniqueId())) return;

            if (group.isGroupDying) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamageApplied(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {

            SharedGroup group = this.plugin.getGroup();
            if (!group.getIsPlaying()) return;
            if (!group.getPlayersId().contains(player.getUniqueId())) return;

            Bukkit.getScheduler().runTask(plugin,
                    () -> {
                        SharedHealthService sharedHealthService = new SharedHealthService(this.plugin);
                        sharedHealthService.shareLoss();
                    }
            );
        }
    }

}
