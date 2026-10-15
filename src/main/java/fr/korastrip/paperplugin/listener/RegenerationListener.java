package fr.korastrip.paperplugin.listener;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.model.SharedGroup;
import fr.korastrip.paperplugin.services.SharedHealthService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class RegenerationListener implements Listener {

    private final SharedStats plugin;

    public RegenerationListener(SharedStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onRegeneration(EntityRegainHealthEvent event) {
        SharedGroup group = this.plugin.getGroup();

        if (!plugin.getSynchronizationConfig().isEnabled("health")) return;

        if (event.getEntity() instanceof Player player) {
            if(group.getPlayersId().contains(player.getUniqueId()) && group.getIsPlaying()) {

                event.setCancelled(true);
                SharedHealthService sharedHealthService = new SharedHealthService(this.plugin);
                sharedHealthService.shareLoss();

                if(group.isGroupDying) return;

                double healthAmount = event.getAmount();

                double currentHealth = group.getHealth();
                double health = currentHealth + healthAmount;

                group.setHealth(Math.min(health, 20.0));
                group.syncHealth();

            }
        }
    }

}
