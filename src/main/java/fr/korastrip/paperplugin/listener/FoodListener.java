package fr.korastrip.paperplugin.listener;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.model.SharedGroup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodListener implements Listener {

    private final SharedStats plugin;

    public FoodListener(SharedStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (e.getEntity() instanceof Player player) {
            SharedGroup group = this.plugin.getGroup();

            if(!this.plugin.getSynchronizationConfig().isEnabled("food")) return;
            if(!group.getPlayersId().contains(player.getUniqueId())) return;
            if(!group.getIsPlaying()) return;

            if (e.getFoodLevel() == group.getFood()) {
                e.setCancelled(true);
                return;
            }

            if (player.getFoodLevel() == group.getFood()) {
                int newFood = Math.clamp(e.getFoodLevel(), 0, 20);

                group.setFood(newFood);
                group.syncFood();

                e.setCancelled(true);
            } else {
                group.syncFood();
                e.setCancelled(true);
            }
        }
    }

}
