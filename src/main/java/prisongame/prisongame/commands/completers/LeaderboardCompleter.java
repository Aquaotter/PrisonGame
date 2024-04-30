package prisongame.prisongame.commands.completers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.commands.misc.LeaderboardCommand;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            List<String> leaderboards = new ArrayList<String>();
            for(LeaderboardCommand.Action action : LeaderboardCommand.Action.values())
                    leaderboards.add(action.name());
            return leaderboards;
    }
}
