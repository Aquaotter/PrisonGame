package prisongame.prisongame.commands.staff.debug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import prisongame.prisongame.PrisonGame;

public class HelpCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(PrisonGame.mm.deserialize("""
                <dark_gray>-=-=-=-=-=-=-=-<red>
                /debug pdc get (player) <dark_gray>- <gray>Read a player's container.</dark_gray>
                /debug pdc get (player) (key) <dark_gray>- <gray>Get the value of a player's container.</dark_gray>
                /debug pdc set (player) (key) (value) <dark_gray>- <gray>Set the value of a player's container.</dark_gray>
                /debug force (player) (role) <dark_gray>- <gray>Force a player to be a role.</dark_gray>
                </red>-=-=-=-=-=-=-=-</dark_gray>
                """));

        return true;
    }
}
