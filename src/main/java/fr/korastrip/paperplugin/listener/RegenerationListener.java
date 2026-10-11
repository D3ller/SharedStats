package fr.korastrip.paperplugin.listener;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.model.SharedGroup;
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

        if (event.getEntity() instanceof Player player) {
            if(group.getPlayersId().contains(player.getUniqueId()) && group.getIsPlaying()) {

                event.setCancelled(true);

                double healthAmount = event.getAmount();

                double currentHealth = group.getHealth();
                double health = currentHealth + healthAmount;

                group.setHealth(Math.min(health, 20.0));
                group.syncHealth();

            }
        }
    }

}
