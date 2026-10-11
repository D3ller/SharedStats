package fr.korastrip.paperplugin.listener;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.model.SharedGroup;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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

            if (group.isGroupDying) {
                event.setCancelled(true);
                return;
            }

            if(!group.getIsPlaying()) return;
            if(!group.getPlayersId().contains(player.getUniqueId())) return;

            var finalDamage = event.getFinalDamage();
            event.setCancelled(true);
            var newHealth = group.getHealth() - finalDamage;

            Bukkit.getLogger().info(
                    "Playing: " + group.getIsPlaying()
                            + " | Group HP: " + group.getHealth()
                            + " | Damage: " + finalDamage
            );

            List<Player> players = group.getOnlinePlayers();

            if (newHealth <= 0) {
                group.isGroupDying = true;

                Bukkit.getScheduler().runTask(
                        plugin,
                        () -> {
                            players.forEach(Player::kill);
                            group.isGroupDying = false;
                        }
                );

                players.forEach(p -> {
                    p.sendRichMessage("<red>The player <playername> died. So every players got killed.</red>", Placeholder.component("playername", player.displayName()));
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
