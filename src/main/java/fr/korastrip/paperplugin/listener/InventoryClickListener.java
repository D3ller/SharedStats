package fr.korastrip.paperplugin.listener;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.enums.ErrorMessage;
import fr.korastrip.paperplugin.inventory.ConfigurationInventory;
import fr.korastrip.paperplugin.model.SharedGroup;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryClickListener implements Listener {

    private final SharedStats plugin;

    public InventoryClickListener(SharedStats plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClickListener(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();
        SharedGroup group = this.plugin.getGroup();

        if (!(inventory.getHolder(false) instanceof ConfigurationInventory configuration)) {
            return;
        }

        e.setCancelled(true);

        if (group.getIsPlaying()) {
            return;
        }

        ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null) return;

        int slot = e.getRawSlot();

        String stats;

        switch (slot) {
            case 20 -> {
                stats = "health";
            }
            case 22 -> {
                stats = "food";
            }
            case 24 -> {
                stats = "potions";
            }
            default -> {
                stats = null;
            }
        }

        if (stats == null) return;

        if (!this.plugin.getSynchronizationConfig().toggle(stats)) {
            e.getWhoClicked().sendRichMessage("<red>" + ErrorMessage.CONFIG_SAVE_FAILED.getMessage() + "</red>");
            return;
        }

        configuration.refresh(stats);
    }
}
