package fr.korastrip.paperplugin.listener;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.model.SharedGroup;
import fr.korastrip.paperplugin.services.SharedHealthService;
import fr.korastrip.paperplugin.services.TotemService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityResurrectEvent;

public class EntityResurrectListener implements Listener {

    private final SharedStats plugin;

    public EntityResurrectListener(SharedStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityResurrect(EntityResurrectEvent e) {
        if(!this.plugin.getSynchronizationConfig().isEnabled("health")) return;

        if (e.getEntity() instanceof Player player) {

            TotemService totemService = new TotemService(this.plugin);
            SharedGroup group = this.plugin.getGroup();

            if (!group.getIsPlaying()) return;
            if (!group.getPlayersId().contains(player.getUniqueId())) return;

            if (group.isGroupDying) {
                e.setCancelled(true);
                return;
            }

            if (!e.isCancelled()) {
                totemService.applyEffect();
            } else if (totemService.check()) {
                e.setCancelled(false);
                totemService.applyEffect();
            } else {
                SharedHealthService sharedHealthService = new SharedHealthService(this.plugin);
                sharedHealthService.groupDeath(player);
            }
        }
    }
}
