
package fr.korastrip.paperplugin.listener;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.model.SharedGroup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class PotionSynchronizationListener implements Listener {
    private final SharedStats plugin;
    private final Set<UUID> synchronizing = new HashSet<>();

    public PotionSynchronizationListener(SharedStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPotionEffectChange(EntityPotionEffectEvent event) {
        if (event.isCancelled()
                || !(event.getEntity() instanceof Player source)
                || synchronizing.contains(source.getUniqueId())
                || !plugin.getSynchronizationConfig().isEnabled("potions")) {
            return;
        }

        SharedGroup group = plugin.getGroup();

        if (!group.getIsPlaying()
                || !group.getPlayersId().contains(source.getUniqueId())) {
            return;
        }

        UUID sourceId = source.getUniqueId();

        plugin.getServer().getScheduler().runTask(plugin, () -> {
            if (!group.getIsPlaying()
                    || !group.getPlayersId().contains(sourceId)
                    || !plugin.getSynchronizationConfig().isEnabled("potions")) {
                return;
            }

            Set<PotionEffectType> sourceTypes = new HashSet<>();
            for (PotionEffect effect : source.getActivePotionEffects()) {
                sourceTypes.add(effect.getType());
            }

            for (Player target : group.getOnlinePlayers()) {
                if (target.equals(source)) {
                    continue;
                }

                UUID targetId = target.getUniqueId();
                synchronizing.add(targetId);
                try {
                    // Remove effects that the source no longer has.
                    for (PotionEffect existing : target.getActivePotionEffects()) {
                        if (!sourceTypes.contains(existing.getType())) {
                            target.removePotionEffect(existing.getType());
                        }
                    }

                    // Add missing effects or update effects whose strength differs.
                    for (PotionEffect effect : source.getActivePotionEffects()) {
                        PotionEffect existing =
                                target.getPotionEffect(effect.getType());

                        boolean needsUpdate = existing == null
                                || existing.getAmplifier() != effect.getAmplifier()
                                || existing.isAmbient() != effect.isAmbient()
                                || existing.hasParticles() != effect.hasParticles()
                                || existing.hasIcon() != effect.hasIcon();

                        if (needsUpdate) {
                            target.addPotionEffect(effect, true);
                        }
                    }
                } finally {
                    synchronizing.remove(targetId);
                }
            }
        });
    }
}
