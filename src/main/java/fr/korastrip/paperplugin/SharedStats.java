package fr.korastrip.paperplugin;

import fr.korastrip.paperplugin.commands.group.GroupCommand;
import fr.korastrip.paperplugin.listener.*;
import fr.korastrip.paperplugin.model.SharedGroup;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class SharedStats extends JavaPlugin implements Listener {

    private final SharedGroup group = new SharedGroup();
    GroupCommand groupCommand = new GroupCommand(this);
    private SynchronizationConfig synchronizationConfig;

    public SharedGroup getGroup() {
        return group;
    }
    public SynchronizationConfig getSynchronizationConfig() {
        return synchronizationConfig;
    }

    @Override
    public void onEnable() {
        synchronizationConfig = new SynchronizationConfig(this);
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(new DamageListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerDeathListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        this.getServer().getPluginManager().registerEvents(new RegenerationListener(this), this);
        this.getServer().getPluginManager().registerEvents(new PlayerRespawnListener(this), this);
        this.getServer().getPluginManager().registerEvents(new EntityResurrectListener(this), this);
        this.getServer().getPluginManager().registerEvents(new FoodListener(this), this);
        this.getServer().getPluginManager().registerEvents(new InventoryClickListener(this), this);
        this.getServer().getPluginManager().registerEvents(new InventoryDragListener(), this);

        this.getLifecycleManager().registerEventHandler(
                LifecycleEvents.COMMANDS,
                event -> event.registrar().register(groupCommand.create())
        );
    }
}
