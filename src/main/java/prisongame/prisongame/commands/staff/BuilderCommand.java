package prisongame.prisongame.commands.staff;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import prisongame.prisongame.PrisonGame;

import static prisongame.prisongame.config.ConfigKt.getConfig;

public class BuilderCommand implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player) || (!player.hasPermission("minecraft.command.gamemode") && player.getGameMode() != GameMode.CREATIVE)) {
            sender.sendMessage("You aren't a builder!");
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("toggle")) {
            var enabled = !PrisonGame.builder.getOrDefault(player, false);
            PrisonGame.builder.put(player, enabled);
            player.sendMessage(PrisonGame.mm.deserialize(
                    "<gray><verb> builder mode.",
                    Placeholder.component("verb", Component.text(enabled ? "Enabled" : "Disabled"))
            ));
            return true;
        }

        Inventory inv = Bukkit.createInventory(null, 9 * 6, "BUILDER ZONE");

        var prisons = getConfig().getPrisons();
        for (var key : prisons.keySet()) {
            var prison = prisons.get(key);
            inv.addItem(PrisonGame.createGuiItem(
                    prison.getMaterial(),
                    ChatColor.YELLOW + key + " [CUSTOM]",
                    "Teleports you to " + ChatColor.translateAlternateColorCodes('&', prison.getDisplayName())
            ));
        }

        inv.addItem(PrisonGame.createGuiItem(Material.PLAYER_HEAD, ChatColor.YELLOW + "Teleport All Creative [CMD]", "tp @a[gamemode=creative] @s"));
        inv.addItem(PrisonGame.createGuiItem(Material.PLAYER_HEAD, ChatColor.YELLOW + "Get Coords [CUSTOM]", "gets you the coords required for me to put in the plugin"));

        player.openInventory(inv);
        return true;
    }
}

