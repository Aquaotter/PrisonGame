package prisongame.prisongame.commands.completers;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static prisongame.prisongame.config.ConfigKt.getConfig;
import static prisongame.prisongame.listeners.InventoryClickListener.switchMap;

public class ForceMapCompleter implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return switch (args.length) {
            case 1 -> createMapsArray();
            default -> Arrays.stream(new String[0]).toList();
        };
    }

    private static List<String> createMapsArray() {
        List<String> maps = new ArrayList<>();
        for (var prison : getConfig().getPrisons().values()) {
            maps.add(prison.getName());
        }
        return maps;
    }

}
