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
import prisongame.prisongame.commands.GangChatCommand;
import prisongame.prisongame.lib.OfflineEnderChest;

public class EnderChestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2)
            return false;

        var subcommand = args[0];
        var target = args[1];

        if(subcommand.equals("inspect")){inspect(sender, target); return true;}
        if(subcommand.equals("clear")){clear(sender, target); return true;}
        return false;
    }

    private void inspect(CommandSender sender, String target) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Only players can inspect ender chests."));
            return;
        }

        var targetp = Bukkit.getPlayer(target);
        if(targetp==null){
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Sorry, Can't find that player!"));
            return;
        }
        player.openInventory(targetp.getEnderChest());
    }

    private void clear(CommandSender sender, String player) {
        var target = Bukkit.getPlayer(player);

        if (target == null) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>The target isn't online."));
            return;
        }

        target.getEnderChest().clear();
        sender.sendMessage(PrisonGame.mm.deserialize("\n<gray>Cleared ender chest of <white>" + target.getName() + "</white>.\n"));
    }
}
