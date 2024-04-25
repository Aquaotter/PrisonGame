package prisongame.prisongame.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import prisongame.prisongame.PrisonGame;

public class PBSettingsCommand implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("hello i am bertrude");
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.playSound(p, Sound.BLOCK_NOTE_BLOCK_BASEDRUM, 1, 1);
            Inventory inv = Bukkit.createInventory(null, 9, "bertrude");
            inv.addItem(PrisonGame.createGuiItem(Material.PLAYER_HEAD, ChatColor.BLUE + "old tab", ChatColor.GRAY + "sets tab to the default minecraft one, if you're boring."));
            inv.addItem(PrisonGame.createGuiItem(Material.POTION, ChatColor.LIGHT_PURPLE + "epic bertude night vision", ChatColor.GRAY + "gives you night vision i think"));
            inv.addItem(PrisonGame.createGuiItem(Material.GOLD_NUGGET, ChatColor.GOLD + "ping noises", ChatColor.GRAY + "toggles ping noises"));
            p.openInventory(inv);
        }
        return true;
    }
}