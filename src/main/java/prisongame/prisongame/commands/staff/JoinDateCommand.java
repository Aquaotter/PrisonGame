package prisongame.prisongame.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JoinDateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 0){
            commandSender.sendMessage(ChatColor.RED+"You need to provide a player!");
            return true;
        }
        Player p = Bukkit.getPlayer(strings[0]);
        if(p == null) {
            commandSender.sendMessage(ChatColor.RED+"Sorry, I cannot find that player!");
            return true;
        }
        if(p.hasPermission("pbb.joindate.bypasscheck")){
            commandSender.sendMessage(ChatColor.GRAY+"Sorry, This players have perms to Bypass checking there Joindate!");
            return true;
        }
        Date date = new Date(p.getFirstPlayed());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String firstPlayed = sdf.format(date);
        commandSender.sendMessage(ChatColor.GRAY+"\n" + p.getName() + " joined on " + firstPlayed + "!\n" );
        return true;
    }
}
