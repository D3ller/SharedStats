package fr.korastrip.paperplugin.services;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.model.SharedGroup;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;

public class SharedHealthService {

    private final SharedStats plugin;

    public SharedHealthService(SharedStats plugin) {
        this.plugin = plugin;
    }


    public void groupDeath(Player deathPlayer) {
        SharedGroup group = this.plugin.getGroup();
        group.isGroupDying = true;

        var players = group.getOnlinePlayers();

        Bukkit.getScheduler().runTask(
                plugin,
                () -> {
                    if (group.getIsPlaying()) {
                        group.getOnlinePlayers().stream().filter(p -> !p.isDead()).forEach(Damageable::kill);
                    }
                }
        );

        final Component mainTitle = deathPlayer.displayName()
                .color(NamedTextColor.RED);
        final Component subtitle = Component.text("is dead", NamedTextColor.WHITE);
        final Title title = Title.title(mainTitle, subtitle);

        group.addPlayerDeath(deathPlayer);

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
    }

    public void shareLoss() {
        SharedGroup group = this.plugin.getGroup();

        if (!group.getIsPlaying() || group.isGroupDying) return;

        double totalLost = 0;
        Player mostHurtPlayer = null;
        double currentDamage = 0;
        for (Player member : group.getOnlinePlayers()) {
            if (member.isDead()) continue;

            // Players store health as a float: compare at the same precision to avoid fake losses.
            double playerLost = (float) group.getHealth() - member.getHealth();
            if (playerLost > 0) {
                totalLost += playerLost;

                if(playerLost > currentDamage) {
                    currentDamage = playerLost;
                    mostHurtPlayer = member;
                }

            }
        }

        if (totalLost == 0) return;

        var newHealth = group.getHealth() - totalLost;

        if (newHealth <= 0) {
            TotemService totemService = new TotemService(this.plugin);
            if (totemService.check()) {
                totemService.applyEffect();
            } else {
                this.groupDeath(mostHurtPlayer);
            }
            return;
        }

        group.setHealth(newHealth);
        group.syncHealth();
    }
}
