package fr.korastrip.paperplugin.listener;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.model.SharedGroup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodSynchronizationListener implements Listener {
    private final SharedStats plugin;
    private boolean synchronizing = false;

    public FoodSynchronizationListener(SharedStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (synchronizing || event.isCancelled()) {
            return;
        }

        if (!(event.getEntity() instanceof Player source)) {
            return;
        }

        if (!plugin.getSynchronizationConfig().isEnabled("food")) {
            return;
        }

        SharedGroup group = plugin.getGroup();

        if (!group.getIsPlaying()
                || !group.getPlayersId().contains(source.getUniqueId())) {
            return;
        }

        // Run after Minecraft applies the food change.
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (!group.getIsPlaying()
                    || !group.getPlayersId().contains(source.getUniqueId())
                    || !plugin.getSynchronizationConfig().isEnabled("food")) {
                return;
            }

            int foodLevel = source.getFoodLevel();
            float saturation = source.getSaturation();

            synchronizing = true;
            try {
                for (Player target : group.getOnlinePlayers()) {
                    if (target.equals(source)) {
                        continue;
                    }

                    target.setFoodLevel(foodLevel);
                    target.setSaturation(Math.min(saturation, foodLevel));
                }
            } finally {
                synchronizing = false;
            }
        });
    }
}