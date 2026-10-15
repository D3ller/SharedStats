package fr.korastrip.paperplugin;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

public class SynchronizationConfig {

    private final SharedStats plugin;
    private final File file;
    private YamlConfiguration config;

    public SynchronizationConfig(SharedStats plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "configuration.yml");

        if (!file.exists()) {
            plugin.saveResource("configuration.yml", false);
        }

        reload();
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);

        config.addDefault("synchronization.health", true);
        config.addDefault("synchronization.food", true);
        config.addDefault("synchronization.potions", true);
        config.options().copyDefaults(true);

        try {
            config.save(file);
        } catch (IOException exception) {
            plugin.getLogger().severe(
                    "Failed to save default configuration: " + exception.getMessage()
            );
        }
    }

    public boolean isEnabled(String stat) {
        String key = getKey(stat);
        return config.getBoolean(key);
    }

    public boolean setEnabled(String stat, boolean enabled) {
        String key = getKey(stat);
        boolean previousValue = config.getBoolean(key);

        config.set(key, enabled);

        try {
            config.save(file);
            return true;
        } catch (IOException exception) {
            config.set(key, previousValue);
            plugin.getLogger().severe(
                    "Failed to save configuration.yml: " + exception.getMessage()
            );
            return false;
        }
    }

    private String getKey(String stat) {
        String normalizedStat = stat.toLowerCase(Locale.ROOT);

        if (!normalizedStat.equals("health")
                && !normalizedStat.equals("food")
                && !normalizedStat.equals("potions")) {
            throw new IllegalArgumentException("Unknown synchronization stat: " + stat);
        }

        return "synchronization." + normalizedStat;
    }
}
