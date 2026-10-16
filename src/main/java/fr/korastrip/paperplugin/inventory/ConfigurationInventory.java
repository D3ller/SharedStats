package fr.korastrip.paperplugin.inventory;

import fr.korastrip.paperplugin.SharedStats;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfigurationInventory implements InventoryHolder {

    private final Inventory inventory;
    private final SharedStats plugin;

    public ConfigurationInventory(SharedStats plugin) {
        this.inventory = plugin.getServer().createInventory(this, 9 * 5, Component.text("Configuration"));
        this.plugin = plugin;

        ItemStack whiteGlassPane = this.empty(new ItemStack(Material.WHITE_STAINED_GLASS_PANE));
        ItemStack blackGlassPane = this.empty(new ItemStack(Material.BLACK_STAINED_GLASS_PANE));

        ItemStack toggleFoodConfiguration = this.renameItemStack(new ItemStack(Material.COOKED_BEEF), this.getDisplayName("food"));
        ItemStack toggleHealthConfiguration = this.renameItemStack(new ItemStack(Material.GLISTERING_MELON_SLICE), this.getDisplayName("health"));
        ItemStack togglePotionConfiguration = this.renameItemStack(new ItemStack(Material.POTION), this.getDisplayName("potions"));

        togglePotionConfiguration.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay().addHiddenComponents(DataComponentTypes.POTION_CONTENTS).build());

        this.inventory.setItem(20, toggleHealthConfiguration);
        this.inventory.setItem(22, toggleFoodConfiguration);
        this.inventory.setItem(24, togglePotionConfiguration);

        for (int i = 0; i < 45; i++) {
            if(i >= 9 && i < 36) continue;
            this.inventory.setItem(i, whiteGlassPane);
        }

        for(int i = 9; i < 36; i++) {
            if(i == 20 || i == 22 || i == 24) continue;
            this.inventory.setItem(i, blackGlassPane);
        }
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    private ItemStack renameItemStack(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.displayName(
                MiniMessage.miniMessage().deserialize(name)
        );
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack empty(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.displayName(Component.empty());
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private String isEnabled(String type) {
        return this.plugin.getSynchronizationConfig().isEnabled(type) ? "<green>Enabled</green>" : "<red>Disabled</red>";
    }

    private String getDisplayName(String stat) {
        String label = switch (stat) {
            case "health" -> "Health";
            case "food" -> "Food";
            case "potions" -> "Potions";
            default -> stat;
        };

        return "<white>" + label + ":</white> " + this.isEnabled(stat);
    }

    public void refresh(String stat) {

        int slot = switch (stat) {
            case "health" -> 20;
            case "food" -> 22;
            case "potions" -> 24;
            default -> -1;
        };

        if (slot == -1) return;
        ItemStack item = this.inventory.getItem(slot);
        if (item == null) return;


        item.editMeta(meta -> {
            meta.displayName(
                    MiniMessage.miniMessage().deserialize(this.getDisplayName(stat))
            );
        });

        this.inventory.setItem(slot, item);
    }

}
