package fr.korastrip.paperplugin.listener;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.model.SharedGroup;
import fr.korastrip.paperplugin.services.TotemService;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.List;

public class DamageListener implements Listener {

    private final SharedStats plugin;

    public DamageListener(SharedStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageReceive(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {

            SharedGroup group = this.plugin.getGroup();

            if (!group.getIsPlaying()) return;
            if (!group.getPlayersId().contains(player.getUniqueId())) return;

            if (group.isGroupDying) {
                event.setCancelled(true);
                return;
            }

            var finalDamage = event.getFinalDamage();
            event.setCancelled(true);
            var newHealth = group.getHealth() - finalDamage;

            List<Player> players = group.getOnlinePlayers();

            if (newHealth <= 0) {

                TotemService totemService = new TotemService(plugin);

                if(totemService.check()) {
                    totemService.applyEffect();
                    return;
                }

                group.isGroupDying = true;

                Bukkit.getScheduler().runTask(
                        plugin,
                        () -> {
                            if (group.getIsPlaying()) {
                                players.forEach(Player::kill);
                            }
                        }
                );

                final Component mainTitle = player.displayName()
                        .color(NamedTextColor.RED);
                final Component subtitle = Component.text("is dead", NamedTextColor.WHITE);
                final Title title = Title.title(mainTitle, subtitle);

                group.addPlayerDeath(player);

                players.forEach(p -> {
                    p.showTitle(title);

                    p.playSound(
                            p.getLocation(),
                            Sound.ENTITY_WITHER_SPAWN,
                            1.0f,
                            1.0f
                    );
                });
                group.incrementsDeadCount();

                return;
            }

            group.setHealth(newHealth);
            group.syncHealth();
            player.playHurtAnimation(0);

            player.playSound(
                    player.getLocation(),
                    Sound.ENTITY_PLAYER_HURT,
                    1.0f,
                    1.0f
            );
        }
    }

}
