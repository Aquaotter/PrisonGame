package prisongame.prisongame.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R1.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.lib.OfflineEnderChest;

public class EnderChestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2)
            return false;

        var subcommand = args[0];
        var target = Bukkit.getOfflinePlayer(args[1]);

        if(subcommand.equals("inspect")){inspect(sender, target); return true;}
        if(subcommand.equals("clear")){clear(sender, target); return true;}
        return false;
    }

    private void inspect(CommandSender sender, OfflinePlayer offlineTarget) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Only players can inspect ender chests."));
            return;
        }

        var target = offlineTarget.getPlayer();

        if (target == null) {
            player.openInventory(new CraftInventory(new OfflineEnderChest(offlineTarget)));
            return;
        }

        player.openInventory(target.getEnderChest());
    }

    private void clear(CommandSender sender, OfflinePlayer offlineTarget) {
        var target = offlineTarget.getPlayer();

        if (target == null) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The target isn't online."));
            return;
        }

        target.getEnderChest().clear();
        sender.sendMessage(PrisonGame.mm.deserialize("\n<gray>Cleared ender chest of <white>" + target.getName() + "</white>.\n"));
    }
}
