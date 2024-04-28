package prisongame.prisongame.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;

import java.util.Arrays;

public class StaffChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        String msg = "";
        for(String substring : strings){
            msg += " "+substring;
        }
        for(Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage(PrisonGame.mm.deserialize("<gray>[<red>STAFF CHAT<gray>] <red>"+commandSender.getName()+"<gray>:<red>"+msg));
        }
        return true;
    }
}
