package prisongame.prisongame.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;

public class InvCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 2)
            return false;

        var subcommand = args[0];
        var target = Bukkit.getPlayer(args[1]);
        if(target==null){
            sender.sendMessage(ChatColor.RED+"Sorry, Cannot Find that player!");
            return true;
        }
        if(subcommand.equals("inspect")){inspect(sender, target); return true;}
        if(subcommand.equals("clear")){clear(sender, target); return true;}
        return false;
    }

    private void inspect(CommandSender sender, Player target) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(PrisonGame.mm.deserialize("<red>Only players can inspect ender chests."));
            return;
        }
        player.openInventory(target.getInventory());
    }

    private void clear(CommandSender sender, Player target) {
        target.getInventory().clear();
        sender.sendMessage(PrisonGame.mm.deserialize("\n<gray>Cleared Inventory of <white>" + target.getName() + "</white>.\n"));
    }
}
