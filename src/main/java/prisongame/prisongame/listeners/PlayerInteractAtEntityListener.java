package prisongame.prisongame.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.Inventory;
import prisongame.prisongame.PrisonGame;

public class PlayerInteractAtEntityListener implements Listener {
    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getType().equals(EntityType.WANDERING_TRADER)) {
            event.setCancelled(true);
        }
        if (event.getRightClicked().getType().equals(EntityType.VILLAGER)) {
            event.setCancelled(true);
        }
        if (event.getRightClicked().getName().equals("bertrude (real settings)")) {
            event.getPlayer().sendMessage("hello i am bertrude");
            event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
            Inventory inv = Bukkit.createInventory(null, 9, "bertrude");
            inv.addItem(PrisonGame.createGuiItem(Material.PLAYER_HEAD, ChatColor.BLUE + "old tab", ChatColor.GRAY + "sets tab to the default minecraft one, if you're boring."));
            inv.addItem(PrisonGame.createGuiItem(Material.POTION, ChatColor.LIGHT_PURPLE + "epic bertude night vision", ChatColor.GRAY + "gives you night vision i think"));
            inv.addItem(PrisonGame.createGuiItem(Material.NETHERITE_SWORD, ChatColor.LIGHT_PURPLE + "-1 dollar", ChatColor.GRAY + "this is a robbery"));
            inv.addItem(PrisonGame.createGuiItem(Material.GOLD_NUGGET, ChatColor.GOLD + "ping noises", ChatColor.GRAY + "toggles ping noises"));
            inv.addItem(PrisonGame.createGuiItem(Material.SHULKER_BOX, ChatColor.DARK_PURPLE + "buy shulker box", ChatColor.GRAY + "buy a shulker box to expand your ender chest", ChatColor.GRAY + "costs " + ChatColor.GREEN + "4000$"));
            event.getPlayer().openInventory(inv);
        }
    }
}
