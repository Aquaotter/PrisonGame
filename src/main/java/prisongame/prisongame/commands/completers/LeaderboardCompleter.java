package prisongame.prisongame.commands.completers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
            List<String> leaderboards = new ArrayList<String>();
            leaderboards.add("playtime");
            leaderboards.add("money");
            leaderboards.add("bells");
            leaderboards.add("kills");
            leaderboards.add("deaths");
            leaderboards.add("kdr");
            leaderboards.add("allwardentime");
            leaderboards.add("mining");
            leaderboards.add("shoveling");
            leaderboards.add("plumber");
            leaderboards.add("codcooker");
            leaderboards.add("lumberjack");
            leaderboards.add("bounty");
            /* PLAYTIME,
        MONEY,
        BELLS,
        KILLS,
        DEATHS,
        KDR,
        ALLWARDENTIME,
        SHOVELING,
        MINING,
        BOUNTY,
        LUMBERJACK,
        PLUMBER,
        CODCOOKER*/
            return leaderboards;
    }
}
