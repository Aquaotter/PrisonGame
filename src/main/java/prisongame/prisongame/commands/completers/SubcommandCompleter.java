package prisongame.prisongame.commands.completers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import prisongame.prisongame.PrisonGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class SubcommandCompleter implements TabCompleter {
    private String name;
    private String[] commands;

    public SubcommandCompleter(String name, String[] commands) {
        this.name = name;
        this.commands = commands;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1 || !command.getName().equalsIgnoreCase(name))
            return null;

        var completions = new ArrayList<String>();
        StringUtil.copyPartialMatches(args[0], List.of(commands), completions);
        Collections.sort(completions);
        return completions;
    }
}
