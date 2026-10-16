package fr.korastrip.paperplugin.listener;

import fr.korastrip.paperplugin.inventory.ConfigurationInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;

public class InventoryDragListener implements Listener {

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        Inventory inventory = e.getInventory();

        if (!(inventory.getHolder(false) instanceof ConfigurationInventory)) {
            return;
        }

        e.setCancelled(true);
    }

}
