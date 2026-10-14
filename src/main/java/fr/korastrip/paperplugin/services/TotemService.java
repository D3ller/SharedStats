package fr.korastrip.paperplugin.services;

import fr.korastrip.paperplugin.SharedStats;
import fr.korastrip.paperplugin.Utils;
import fr.korastrip.paperplugin.model.SharedGroup;
import org.bukkit.EntityEffect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.logging.Level;

public class TotemService {

    private final SharedStats plugin;

    public TotemService(SharedStats plugin) {
        this.plugin = plugin;
    }

    public boolean check() {
        SharedGroup group = this.plugin.getGroup();

        for (Player player : group.getOnlinePlayers()) {
            if (this.removeTotemFromUser(player)) {
                return true;
            }
        }
        return false;
    }

    public void applyEffect() {
        SharedGroup group = this.plugin.getGroup();

        group.setHealth(1);
        group.syncHealth();

        group.getOnlinePlayers().forEach(p -> {

            //Apply the same effect as vanilla
            p.clearActivePotionEffects();

            p.playSound(p.getLocation(), Sound.ITEM_TOTEM_USE, 1f, 1f);
            p.playEffect(EntityEffect.PROTECTED_FROM_DEATH);
            p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Utils.toTick(45), 1));
            p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Utils.toTick(40), 0));
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, Utils.toTick(5), 1));
        });
    }

    private boolean removeTotemFromUser(Player player) {

        PlayerInventory playerInventory = player.getInventory();
        int slotID = -1;
        for (int i = 0; i < playerInventory.getSize(); i++) {
            ItemStack item = playerInventory.getItem(i);
            if (item != null && item.getType() == Material.TOTEM_OF_UNDYING) {
                slotID = i;
                break;
            }
        }

        if (slotID == -1) {
            this.plugin.getLogger().log(Level.FINE, "No totem find on this player");
            return false;
        }


        ItemStack item = playerInventory.getItem(slotID);
        if (item == null) return false;

        int itemAmount = item.getAmount();
        if (itemAmount < 1) return false;

        if (itemAmount == 1) {
            playerInventory.setItem(slotID, null);
        } else {
            item.setAmount(itemAmount - 1);
            playerInventory.setItem(slotID, item);
        }


        return true;
    }

}
