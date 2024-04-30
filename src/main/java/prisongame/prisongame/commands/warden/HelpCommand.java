package prisongame.prisongame.commands.warden;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;

import static prisongame.prisongame.config.ConfigKt.getConfig;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-");

        for (var entry : getConfig().getWarden().getHelp().entrySet()) {
            var key = entry.getKey();
            var value = entry.getValue();

            p.sendMessage(PrisonGame.mm.deserialize(String.format(
                    "<blue>/warden %s %s<dark_gray>- <white>%s",
                    key,
                    !value.getArgs().isEmpty() && !value.getArgs().get(0).isEmpty() ? "[" + String.join("] [", value.getArgs()) + "] " : "",
                    value.getDescription()
            )));
        }
//        p.sendMessage(ChatColor.BLUE + "/warden help" + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + "Shows you this menu.");
//        p.sendMessage(ChatColor.BLUE + "/warden solitary [name]" + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + "Sends a player to solitary, later to torture them. " + ChatColor.RED + "[PLAYER MUST BE DEAD]");
//        p.sendMessage(ChatColor.BLUE + "/warden target [name]" + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + "Turns a player in a target.");
//        p.sendMessage(ChatColor.BLUE + "/warden prefix [prefix]" + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + "Adds a prefix to your name.");
//        p.sendMessage(ChatColor.BLUE + "/warden fire [guard name]" + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + "Fires a guard from their job");
//        p.sendMessage(ChatColor.BLUE + "/warden resign" + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + "Resigns you from your job.");
//        p.sendMessage(ChatColor.BLUE + "/warden pass [name]" + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + "Gives a player warden.");
//        p.sendMessage(ChatColor.BLUE + "/warden guard [name]" + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + "Makes another player a guard.");
//        p.sendMessage(ChatColor.BLUE + "/warden swat [name]" + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + "Makes another player a SWAT guard." + ChatColor.RED + " [REQUIRES 'SWAT GUARDS' UPGRADE!]");
//        p.sendMessage(ChatColor.BLUE + "/warden nurse [name]" + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + "Makes another player a nurse.");
//        p.sendMessage(ChatColor.BLUE + "/warden intercom [msg]" + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + "Plays a loud message to all players.");
//        p.sendMessage(ChatColor.BLUE + "/warden grammar" + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + "Makes all guards type with proper grammar.");
//        p.sendMessage(ChatColor.BLUE + "/warden mutechat" + ChatColor.DARK_GRAY + " - " + ChatColor.WHITE + "Forces all players to use teamchat.");
        p.sendMessage(ChatColor.DARK_GRAY + "-=-=-=-=-=-=-=-");

        return true;
    }
}