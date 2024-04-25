package prisongame.prisongame.commands.danger.staff;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;
import prisongame.prisongame.keys.Keys;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class SeasonCommand implements CommandExecutor {
    private static final File SEASON = new File(PrisonGame.instance.getDataFolder().getAbsolutePath() + "/season");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0 || !args[0].equalsIgnoreCase("confirm")) {
            sender.sendMessage(PrisonGame.mm.deserialize("""
                    <red>Are you sure you want to do this?</red>
                    <dark_red>This will reset the money of all players!</dark_red>
                    <gray>If you're absolutely certain, please run <white>/season confirm</white>.
                    """));
            return true;
        }

        try {
            var season = getCurrentSeason();
            season++;
            writeNewSeason(season);

            for (var player : Bukkit.getOnlinePlayers()) {
                var pdc = player.getPersistentDataContainer();
                var money = Keys.MONEY.get(player);
                Keys.PREVIOUS_MONEY.set(player, money);
                Keys.MONEY.set(player, 0.0);
                Keys.SEASON.set(player, season);
                player.sendMessage(PrisonGame.mm.deserialize("\n<red>Your money has been reset due to the start of a new season!\n"));
            }
        } catch (IOException exception) {
            sender.sendMessage(PrisonGame.mm.deserialize(
                    "<red>An error occurred starting the new season.\n<dark_red><exception>",
                    Placeholder.component("exception", PrisonGame.mm.deserialize(exception.getMessage()))
            ));
        }

        sender.sendMessage(PrisonGame.mm.deserialize("\n<green>Started a new season.\n"));

        return true;
    }

    public static int getCurrentSeason() throws IOException {
        if (!SEASON.exists())
            return 0;

        return Integer.parseInt(Files.readString(SEASON.toPath()));
    }

    private static void writeNewSeason(int season) throws IOException {
        if (!SEASON.exists())
            SEASON.createNewFile();

        Files.writeString(SEASON.toPath(), "" + season);
    }
}
