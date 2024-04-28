package prisongame.prisongame.commands.staff;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static prisongame.prisongame.config.ConfigKt.reloadConfig;
import static prisongame.prisongame.config.ConfigKt.setConfig;

public class PlayTimeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length == 0){
            commandSender.sendMessage(ChatColor.RED+"You need to provide a player!");
            return true;
        }
        OfflinePlayer p = Bukkit.getOfflinePlayer(strings[0]);
        if(p == null) {
            commandSender.sendMessage(ChatColor.RED+"Sorry, I cannot find that player!");
            return true;
        }
        int timeinticks = p.getStatistic(Statistic.PLAY_ONE_MINUTE);
        int seconds = timeinticks/20;
        int min = seconds/60;
        int hours = min/60;
        int days = hours/24;
        commandSender.sendMessage(ChatColor.GRAY+"\n" + p.getName() + " has a Total Playtime of: \nTOTAL TICKS: " + timeinticks+"\nTOTAL SECONDS: " + seconds+"\nTOTAL MINUTES: " + min+"\nTOTAL HOURS: " + hours+"\nTOTAL DAYS: " + days+"\n ");
        return true;
    }
}
